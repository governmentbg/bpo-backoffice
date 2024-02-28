package bg.duosoft.ipas.core.service.impl.userdoc.config;

import bg.duosoft.ipas.core.mapper.userdoc.userdoc_type.config.UserdocTypeConfigMapper;
import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdoc;
import bg.duosoft.ipas.core.model.reception.CUserdocReceptionRelation;
import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.model.userdoc.config.CUserdocTypeConfig;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.UserdocReceptionRelationService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.core.service.userdoc.config.UserdocTypeConfigService;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.config.CfUserdocTypeConfigRepository;
import bg.duosoft.ipas.util.DefaultValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static bg.duosoft.ipas.core.service.nomenclature.ConfigParamService.EXCLUDED_RESPONSIBLE_USERS;
@Service
@Transactional
public class UserdocTypeConfigServiceImpl implements UserdocTypeConfigService {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserdocReceptionRelationService userdocReceptionRelationService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private CfUserdocTypeConfigRepository cfUserdocTypeConfigRepository;

    @Autowired
    private UserdocTypeConfigMapper userdocTypeConfigMapper;

    @Autowired
    private ConfigParamService configParamService;

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Override
    public CUserdocTypeConfig findById(String userdocType) {
        CUserdocTypeConfig cUserdocTypeConfig = userdocTypeConfigMapper.toCore(cfUserdocTypeConfigRepository.findById(userdocType).orElse(null));
        if(Objects.isNull(cUserdocTypeConfig)){
            throw new RuntimeException("Define configuration for userdoc typ:"+userdocType+"!");
        }
        return cUserdocTypeConfig;
    }

    @Override
    public void defineUpperProc(CReceptionUserdoc userdocReception, CFileId affectedId) {

        // userdocReception and affectedId have to be initialized!
        if (Objects.isNull(userdocReception) || Objects.isNull(affectedId)){
            throw new RuntimeException("Empty userdocReception or affectedId!");
        }

        //Get configuration for specific userdoc typ.
        CUserdocTypeConfig userdocTypeConfig = findById(userdocReception.getUserdocType());

        // define upper proc depending on configuration.
        if (userdocTypeConfig.getRegisterToProcess().equals(RegisterToProcessType.TOP_PROCESS.code())){
            userdocReception.setFileId(affectedId);
        }else if (userdocTypeConfig.getRegisterToProcess().equals(RegisterToProcessType.SUB_PROCESS.code())){
            defineUpperProcWhenRegisterProcTypeIsSubProcess(userdocReception,affectedId);
        }else if (userdocTypeConfig.getRegisterToProcess().equals(RegisterToProcessType.REGISTERED_OBJECT_SUB_PROCESS.code())){
            defineUpperProcWhenRegisterProcTypeIsSubProcessRegisteredObject(userdocReception,affectedId);
        } else{
            throw new RuntimeException("Unrecognized REGISTER_TO_PROCESS type!");
        }

    }

    @Override
    public void defineResponsibleUserOnReception(CDocumentId docId,Integer submissionType) {
      Integer parentResponsibleUser = processService.selectResponsibleUserOfUserdocParentProcess(docId.getDocOrigin(), docId.getDocLog(), docId.getDocSeries(), docId.getDocNbr());
      CProcess docProcess = processService.selectUserdocProcess(docId,false);
      if (processService.isUpperProcessUserdoc(docId)){
          if (!isExcludedResponsibleUser(parentResponsibleUser)){
              processService.updateResponsibleUser(parentResponsibleUser, docProcess.getProcessId().getProcessType(), docProcess.getProcessId().getProcessNbr());
          }
          return;
      }
      CUserdocType userdocType = userdocTypesService.selectUserdocTypeByDocId(docId);
      CUserdocTypeConfig userdocTypeConfig = findById(userdocType.getUserdocType());
       if(callResponsibleUserConfigurationOnReception(docProcess,parentResponsibleUser,userdocTypeConfig,submissionType)) {
           defineResponsibleUser(docProcess,parentResponsibleUser,userdocTypeConfig);
       }
    }

    @Override
    public void defineResponsibleUserOnRelocation(CDocumentId docId, CProcessId userdocProcessId, CProcessId newUpperProcessId) {
        if (Objects.nonNull(docId) && Objects.nonNull(userdocProcessId) && Objects.nonNull(newUpperProcessId)) {
            Integer parentResponsibleUser = processService.selectResponsibleUser(newUpperProcessId);
            CUserdocType userdocType = userdocTypesService.selectUserdocTypeByDocId(docId);
            UserdocGroup userdocGroup = userdocType.getUserdocGroup();
            if (Objects.nonNull(userdocGroup) && userdocGroup == UserdocGroup.CORRESP) {
                boolean isUserExcluded = isExcludedResponsibleUser(parentResponsibleUser);
                if (isUserExcluded || Objects.isNull(parentResponsibleUser)) {
                    processService.updateResponsibleUser(null, userdocProcessId.getProcessType(), userdocProcessId.getProcessNbr());
                } else {
                    processService.updateResponsibleUser(parentResponsibleUser, userdocProcessId.getProcessType(), userdocProcessId.getProcessNbr());
                }
            }
        }
    }


    private List<CProcess> getValidNotFinishedUserdocProcessesWhenRegisterProcTypeIsSubProcess(CReceptionUserdoc userdocReception, CFileId affectedId){
        CProcessId affectedIpObjectProcessId = processService.selectFileProcessId(affectedId);
        List<CProcess> notFinishedUserdocProcesses = processService.selectNotFinishedUserdocProcessesRelatedToIpObjectProcess(affectedIpObjectProcessId,false);
        List<CProcess> validNotFinishedProcesses = new ArrayList<>();

        if (!CollectionUtils.isEmpty(notFinishedUserdocProcesses)){
            notFinishedUserdocProcesses.stream().forEach(process->{
                CUserdocReceptionRelation cUserdocReceptionRelation = userdocReceptionRelationService.selectById(userdocReception.getUserdocType(), process.getProcessOriginData().getUserdocType());
                if (Objects.nonNull(cUserdocReceptionRelation)){
                    validNotFinishedProcesses.add(process);
                }
            });
        }
        return CollectionUtils.isEmpty(validNotFinishedProcesses)?null:validNotFinishedProcesses;
    }

    private void defineUpperProcWhenRegisterProcTypeIsSubProcess(CReceptionUserdoc userdocReception, CFileId affectedId){
        List<CProcess> validNotFinishedProcesses = getValidNotFinishedUserdocProcessesWhenRegisterProcTypeIsSubProcess(userdocReception,affectedId);
        if (!CollectionUtils.isEmpty(validNotFinishedProcesses) && validNotFinishedProcesses.size() == DefaultValue.ONE_RESULT_SIZE){
            CDocumentId upperDocumentId = validNotFinishedProcesses.get(DefaultValue.FIRST_RESULT).getProcessOriginData().getDocumentId();
            userdocReception.setDocumentId(upperDocumentId);
        }else{
            userdocReception.setFileId(affectedId);
        }
    }


    private void defineUpperProcWhenRegisterProcTypeIsSubProcessRegisteredObject(CReceptionUserdoc userdocReception, CFileId affectedId){
        if (isRegisteredObjectWhenRegisterProcTypeIsSubProcess(affectedId)){
            defineUpperProcWhenRegisterProcTypeIsSubProcess(userdocReception,affectedId);
        }else{
            userdocReception.setFileId(affectedId);
        }
    }

    private boolean isRegisteredObjectWhenRegisterProcTypeIsSubProcess(CFileId affectedId){
        CFile affectedObjectCFile = fileService.findById(affectedId);
        return Objects.nonNull(affectedObjectCFile.getRegistrationData()) && Objects.nonNull(affectedObjectCFile.getRegistrationData().getRegistrationId())
                && Objects.nonNull(affectedObjectCFile.getRegistrationData().getRegistrationId().getRegistrationNbr());
    }


    private String getResponsibleUserConfigDependingOnFileType(String fileTyp,CUserdocTypeConfig userdocTypeConfig){
        if (fileTyp.equalsIgnoreCase(FileType.MARK.code()) || fileTyp.equalsIgnoreCase(FileType.DIVISIONAL_MARK.code()) ||
                fileTyp.equalsIgnoreCase(FileType.GEOGRAPHICAL_INDICATIONS.code()) || fileTyp.equalsIgnoreCase(FileType.GEOGRAPHICAL_INDICATIONS_V.code())
          || fileTyp.equalsIgnoreCase(FileType.INTERNATIONAL_MARK_I.code()) || fileTyp.equalsIgnoreCase(FileType.INTERNATIONAL_MARK_R.code()) || fileTyp.equalsIgnoreCase(FileType.INTERNATIONAL_MARK_B.code())){
            return userdocTypeConfig.getMarkInheritResponsibleUser();
        }else{
           return  userdocTypeConfig.getInheritResponsibleUser();
        }
    }

    private void defineResponsibleUser(CProcess docProcess,Integer parentResponsibleUser,CUserdocTypeConfig userdocTypeConfig) {
        String inheritResponsibleUser = getResponsibleUserConfigDependingOnFileType(docProcess.getProcessOriginData().getUserdocFileId().getFileType(),userdocTypeConfig);
        if (!inheritResponsibleUser.equals(InheritResponsibleUserType.NONE.code())){
            if (inheritResponsibleUser.equals(InheritResponsibleUserType.ALL.code())){
                processService.updateResponsibleUser(parentResponsibleUser, docProcess.getProcessId().getProcessType(), docProcess.getProcessId().getProcessNbr());
            }else if(inheritResponsibleUser.equals(InheritResponsibleUserType.REGISTERED_IPOBJECTS.code())){
                updateResponsibleUserOnInheritTypeRegisteredObject(docProcess,parentResponsibleUser);
            }else if(inheritResponsibleUser.equals(InheritResponsibleUserType.UNREGISTERED_IPOBJECTS.code())){
                updateResponsibleUserOnInheritTypeUnRegisteredObject(docProcess,parentResponsibleUser);
            }else{
                throw new RuntimeException("Unrecognized INHERIT_RESPONSIBLE_USER type!");
            }
        }

    }

    private void updateResponsibleUserOnInheritTypeRegisteredObject(CProcess docProcess, Integer parentResponsibleUser){
        if(isRegisteredObjectWhenRegisterProcTypeIsSubProcess(processService.selectTopProcessFileId(docProcess.getProcessOriginData().getTopProcessId()))){
            processService.updateResponsibleUser(parentResponsibleUser, docProcess.getProcessId().getProcessType(), docProcess.getProcessId().getProcessNbr());
        }
    }

    private void updateResponsibleUserOnInheritTypeUnRegisteredObject(CProcess docProcess, Integer parentResponsibleUser){
        if(!isRegisteredObjectWhenRegisterProcTypeIsSubProcess(processService.selectTopProcessFileId(docProcess.getProcessOriginData().getTopProcessId()))){
            processService.updateResponsibleUser(parentResponsibleUser, docProcess.getProcessId().getProcessType(), docProcess.getProcessId().getProcessNbr());
        }
    }


    private boolean callResponsibleUserConfigurationOnReception(CProcess docProcess,Integer parentResponsibleUser,CUserdocTypeConfig userdocTypeConfig,Integer submissionType){
        if (isExcludedResponsibleUser(parentResponsibleUser)){
            return false;
        }

        if(submissionType.equals(SubmissionType.ELECTRONIC.code()) && (userdocTypeConfig.getRegisterToProcess().equals(RegisterToProcessType.SUB_PROCESS.code())
                || (userdocTypeConfig.getRegisterToProcess().equals(RegisterToProcessType.REGISTERED_OBJECT_SUB_PROCESS.code()) &&
                isRegisteredObjectWhenRegisterProcTypeIsSubProcess(processService.selectTopProcessFileId(docProcess.getProcessOriginData().getTopProcessId()))))){
            List<CProcess> notFinishedUserdocProcessesRelatedToMainObject = processService.selectNotFinishedUserdocProcessesRelatedToIpObjectProcess(docProcess.getProcessOriginData().getTopProcessId(),false);
            if (!CollectionUtils.isEmpty(notFinishedUserdocProcessesRelatedToMainObject)){
                return false;
            }

        }
        return true;
    }

    private boolean isExcludedResponsibleUser(Integer resposibleUser) {
        if (Objects.isNull(resposibleUser))
            return false;

        
        User userDb = userService.getUser(resposibleUser);
        if (Objects.isNull(userDb) || userDb.getIndInactive()){
            return true;
        }

        List<Integer> excludedResponsibleUsersOnInheritance = new ArrayList<>();
        CConfigParam cConfigParam = configParamService.selectExtConfigByCode(EXCLUDED_RESPONSIBLE_USERS);
        String[] exludedUsersArray = cConfigParam.getValue().split(", ");
        for (String excludedUser:exludedUsersArray) {
            excludedResponsibleUsersOnInheritance.add(Integer.valueOf(excludedUser));
        }
        Integer existingExcludedUser = excludedResponsibleUsersOnInheritance.stream()
                .filter(excludedUser -> excludedUser.equals(resposibleUser))
                .findFirst()
                .orElse(null);

        return Objects.nonNull(existingExcludedUser);
    }

}
