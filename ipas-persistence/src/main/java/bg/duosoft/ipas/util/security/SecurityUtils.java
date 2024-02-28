package bg.duosoft.ipas.util.security;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessSimpleData;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.action.ActionService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.enums.security.SecurityRolePrefix;
import bg.duosoft.security.bpo.authorization.domain.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static bg.duosoft.ipas.enums.security.SecurityRolePrefix.EDIT_PREFIX;
import static bg.duosoft.ipas.enums.security.SecurityRolePrefix.OWN_PREFIX;

@Slf4j
public class SecurityUtils {
    private static final Integer DEFAULT_USER_ID = 4;//IPASPROD

    public static UserDetails getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(auth))
            return null;

        return (UserDetails) auth.getPrincipal();
    }

    public static String getLoggedUsername() {
        UserDetails loggedUser = getLoggedUser();
        if (Objects.isNull(loggedUser))
            return null;

        return loggedUser.getUsername();
    }

    public static String getLoggedUserFullName() {
        UserDetails loggedUser = getLoggedUser();
        if (Objects.isNull(loggedUser))
            return null;

        return loggedUser.getFullName();
    }

    public static Integer getLoggedUserId() {
        UserDetails loggedUser = getLoggedUser();
        if (Objects.isNull(loggedUser))
            return DEFAULT_USER_ID;

        return Math.toIntExact(loggedUser.getUserId());
    }
    public static boolean hasAuthorizedByUsers() {
        return !CollectionUtils.isEmpty(getLoggedUser().getAuthorizedByUsers());
    }
    public static List<Integer> getLoggedUserAndAuthorizedByUserIds() {
        Integer currentUserId = getLoggedUserId();
        UserDetails user = getLoggedUser();
        List<Integer> result = new ArrayList<>();
        result.add(currentUserId);
        if (hasAuthorizedByUsers()) {
            result.addAll(user.getAuthorizedByUsers().stream().map(r -> r.getUserId().intValue()).collect(Collectors.toList()));
        }
        return result;
    }
    public static List<String> getAuthorizedByUserNames() {
        return hasAuthorizedByUsers() ? getLoggedUser().getAuthorizedByUsers().stream().map(r -> StringUtils.isEmpty(r.getFullName()) ? r.getUsername() : r.getFullName()).collect(Collectors.toList()) : null;
    }

    public static boolean hasRights(SecurityRole securityRole) {
        return hasRights(securityRole.code());
    }

    public static boolean hasMarkIdentityPanelRights(CFileId fileId) {
        FileType fileType = FileType.selectByCode(fileId.getFileType());
        switch (fileType) {
            case ACP:
                return hasRights(SecurityRole.AcpIdentityData.code());
            default:
                return hasRights(SecurityRole.MarkIdentityData.code());
        }
    }

    public static boolean hasMarkProcessPanelRights(CFileId fileId) {
        FileType fileType = FileType.selectByCode(fileId.getFileType());
        switch (fileType) {
            case ACP:
                return hasRights(SecurityRole.AcpProcess.code());
            default:
                return hasRights(SecurityRole.MarkProcess.code());
        }
    }

    public static boolean hasMarkPersonsPanelRights(CFileId fileId) {
        FileType fileType = FileType.selectByCode(fileId.getFileType());
        switch (fileType) {
            case ACP:
                return hasRights(SecurityRole.AcpPersons.code());
            default:
                return hasRights(SecurityRole.MarkPersons.code());
        }
    }

    public static boolean hasRights(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth.getAuthorities() != null && auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(r -> Objects.equals(r, role)));
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return userDetails.hasRole(role);
    }

    public static boolean hasAnyRights(SecurityRole... securityRole) {
        return Arrays.stream(securityRole).anyMatch(SecurityUtils::hasRights);
    }

    public static boolean hasAnyRights(List<SecurityRole> securityRole) {
        return securityRole.stream().anyMatch(SecurityUtils::hasRights);
    }

    public static boolean isOffidocObjectEditEnabled(COffidoc cOffidoc,ProcessService processService,UserService userService) {
        if (Objects.nonNull(getLoggedUserId())){
            if (hasRights(SecurityRole.getSecurityRole(SecurityRolePrefix.IP_OBJECT_OFFIDOC_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + SecurityRolePrefix.ALL_PREFIX))){
                return true;
            }

            CUser responsibleUser = cOffidoc.getProcessSimpleData().getResponsibleUser();
            if (Objects.nonNull(responsibleUser) && Objects.nonNull(responsibleUser.getUserId())){
                if ( Objects.equals(getLoggedUserId(), responsibleUser.getUserId()) && hasRights(SecurityRole.getSecurityRole(SecurityRolePrefix.IP_OBJECT_OFFIDOC_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + OWN_PREFIX))){
                    return true;
                }

                if (checkIfSubProcessContainsLoggedUser(processService,cOffidoc.getProcessSimpleData()) || checkIfDepartmentAndAuthorizedByUserIdsContainsResponsibleUser(userService,cOffidoc.getProcessSimpleData())){
                    return true;
                }
            }

        }
        return false;
    }

    public static boolean isUserdocObjectEditEnabled(CUserdoc cUserdoc,ProcessService processService,UserService userService) {
        if (Objects.nonNull(getLoggedUserId())){
            if ((hasRights(SecurityRole.getSecurityRole(SecurityRolePrefix.IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + SecurityRolePrefix.ALL_PREFIX)))){
                return true;
            }
            CUser responsibleUser = cUserdoc.getProcessSimpleData().getResponsibleUser();
            if (Objects.nonNull(responsibleUser) && Objects.nonNull(responsibleUser.getUserId())){

                if ( Objects.equals(getLoggedUserId(), responsibleUser.getUserId()) && hasRights(SecurityRole.getSecurityRole(SecurityRolePrefix.IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + OWN_PREFIX))){
                    return true;
                }

                if (checkIfSubProcessContainsLoggedUser(processService,cUserdoc.getProcessSimpleData()) || checkIfDepartmentAndAuthorizedByUserIdsContainsResponsibleUser(userService,cUserdoc.getProcessSimpleData())){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isIntellectualPropertyObjectEditEnabled(String ipObjectPrefix, CFile file,ProcessService processService,UserService userService) {
        if (Objects.nonNull(getLoggedUserId())){
            if (hasRights(SecurityRole.getSecurityRole(ipObjectPrefix + "." + EDIT_PREFIX + "." + SecurityRolePrefix.ALL_PREFIX))){
                return true;
            }
            CUser responsibleUser = file.getProcessSimpleData().getResponsibleUser();
            if (Objects.nonNull(responsibleUser) && Objects.nonNull(responsibleUser.getUserId())){
                if ( Objects.equals(getLoggedUserId(), responsibleUser.getUserId()) && hasRights(SecurityRole.getSecurityRole(ipObjectPrefix + "." + EDIT_PREFIX + "." + OWN_PREFIX))){
                    return true;
                }
                if (checkIfSubProcessContainsLoggedUser(processService,file.getProcessSimpleData()) || checkIfDepartmentAndAuthorizedByUserIdsContainsResponsibleUser(userService,file.getProcessSimpleData())){
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkIfSubProcessContainsLoggedUser(ProcessService processService, CProcessSimpleData proc){
        List<Integer> subProcessesUserIds = processService.selectSubProcessesResponsibleUserIds(proc.getProcessId().getProcessType(), proc.getProcessId().getProcessNbr());
        if(!CollectionUtils.isEmpty(subProcessesUserIds)){
            Integer resultedUserId = subProcessesUserIds.stream().filter(r -> r.equals(getLoggedUserId())).findFirst().orElse(null);
            return Objects.nonNull(resultedUserId);
        }
       return false;
    }

    private static boolean checkIfDepartmentAndAuthorizedByUserIdsContainsResponsibleUser(UserService userService,CProcessSimpleData proc){
        List<Integer> departmentAndAuthorizedByUserIds = userService.getDepartmentAndAuthorizedByUserIds(getLoggedUserId());
        if (!CollectionUtils.isEmpty(departmentAndAuthorizedByUserIds)){
            Integer resultedUserId = departmentAndAuthorizedByUserIds.stream().filter(r -> r.equals(proc.getResponsibleUser().getUserId())).findFirst().orElse(null);
            return Objects.nonNull(resultedUserId);
        }
        return false;
    }










    public static boolean isManualSubProcessEditEnabled(CProcess process) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        CUser responsibleUser = process.getResponsibleUser();
        return (userDetails.getUserId() != null
                && responsibleUser != null
                && Objects.equals(userDetails.getUserId().intValue(), responsibleUser.getUserId())
                && hasRights(SecurityRole.getSecurityRole(SecurityRolePrefix.MANUAL_SUB_PROCESS_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + OWN_PREFIX)))
                || (hasRights(SecurityRole.getSecurityRole(SecurityRolePrefix.MANUAL_SUB_PROCESS_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + SecurityRolePrefix.ALL_PREFIX)));
    }

    public static boolean hasIpObjectPaymentsRights(CFileId fileId, ProcessService processService) {
        if (hasRights(SecurityRole.PaymentsExtendedRights)) {
            return true;
        }
        if (hasRights(SecurityRole.PaymentsEdit) && isLoggedUserResponsibleUser(fileId, processService)) {
            return true;
        }

        return false;
    }

    public static boolean hasUserdocPaymentsRights(CDocumentId documentId, ProcessService processService) {
        if (hasRights(SecurityRole.PaymentsExtendedRights)) {
            return true;
        }
        if (hasRights(SecurityRole.PaymentsEdit) && isLoggedUserResponsibleUser(documentId, processService)) {
            return true;
        }

        return false;
    }

    public static boolean isLoggedUserResponsibleUser(CFileId fileId, ProcessService processService) {
        Integer responsibleUser = processService.selectIpObjectResponsibleUser(fileId);
        if (Objects.isNull(responsibleUser))
            return false;

        return getLoggedUserAndAuthorizedByUserIds().contains(responsibleUser);
    }

    public static boolean isLoggedUserResponsibleUser(CDocumentId documentId, ProcessService processService) {
        Integer responsibleUser = processService.selectUserdocResponsibleUser(documentId);
        if (Objects.isNull(responsibleUser))
            return false;

        return getLoggedUserAndAuthorizedByUserIds().contains(responsibleUser);
    }

    public static boolean isLoggedUserResponsibleUser(COffidocId offidocId, ProcessService processService) {
        Integer responsibleUser = processService.selectOffidocResponsibleUser(offidocId);
        if (Objects.isNull(responsibleUser))
            return false;

        return getLoggedUserAndAuthorizedByUserIds().contains(responsibleUser);
    }

    public static boolean hasSecretDataRights(CUser responsibleUser, CFileId fileId, ActionService actionService, UserService userService){
        String fileType = fileId.getFileType();
        // Add checks for other file types if it is needed. Check hasPublications SQL if it is correct for other types.
        if(fileType.equals(FileType.EU_PATENT.code()) || fileType.equals(FileType.PLANTS_AND_BREEDS.code())){
            return true;
        }
        else if (fileType.equals(FileType.PATENT.code()) || fileType.equals(FileType.UTILITY_MODEL.code())){
            if (hasRights(SecurityRole.PatentSecretData) || actionService.hasPublications(fileId.getFileSeq(),fileId.getFileType(),fileId.getFileSeries(),fileId.getFileNbr())){
                return true;
            }
        }else{
            throw new RuntimeException("Unrecognized file type on hasSecretDataRights action!");
        }

        if (Objects.isNull(responsibleUser) || Objects.isNull(responsibleUser.getUserId())){
            return false;
        }

        List<Integer> departmentAndAuthorizedByUserIds = userService.getDepartmentAndAuthorizedByUserIds(getLoggedUserId());
        Integer resultedUserId = departmentAndAuthorizedByUserIds.stream().filter(r -> r.equals(responsibleUser.getUserId())).findFirst().orElse(null);
        return Objects.nonNull(resultedUserId);
    }

}
