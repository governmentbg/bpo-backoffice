package bg.duosoft.ipas.core.service.impl.userdoc;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.service.action.InternationalMarkActionService;
import bg.duosoft.ipas.core.service.ext.ErrorLogService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.core.service.userdoc.UserdocIrregularityLetterService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserdocIrregularityLetterServiceImpl implements UserdocIrregularityLetterService {
    private final FileService fileService;
    private final UserdocService userdocService;
    private final MessageSource messageSource;
    private final ErrorLogService errorLogService;
    private final ReceptionService receptionService;
    private final InternationalMarkActionService internationalMarkActionService;

    public synchronized CReceptionResponse acceptWipoIrregularityLetters(String parentDocumentNumber, Integer registrationNumber, String registrationDup, CUserdoc userdoc, List<CAttachment> attachments) {
        CUserdoc parentUserdoc = UserdocUtils.findCorrectUserdocFromWipoRequest(parentDocumentNumber, userdocService);

        CReceptionResponse response;
        if (Objects.nonNull(parentUserdoc)) {
            response = receptionService.acceptInternationalMarkUserdoc(userdoc, attachments, null, false, parentUserdoc.getDocumentId());

            String actionExtParam = getActionExtParam(parentUserdoc.getUserdocType().getUserdocType());
            if (StringUtils.hasText(actionExtParam)) {
                internationalMarkActionService.insertUserdocNormalAction(parentUserdoc, actionExtParam);
            }
        } else if (Objects.nonNull(registrationNumber)) {
            CFileId existingFileId = selectFileIdByParentDocumentNumber(registrationNumber, registrationDup);

            if (Objects.nonNull(existingFileId)) {
                userdoc.getUserdocType().setUserdocType(UserdocType.MARK_INTERNATIONAL_IRREGULARITY_NOTICE);
                response = receptionService.acceptInternationalMarkUserdoc(userdoc, attachments, existingFileId, true, null);
            } else {
                String fullRegistrationNumber = registrationNumber + (StringUtils.hasText(registrationDup) ? registrationDup : "");
                saveMissingIpObjectErrorLog(fullRegistrationNumber, userdoc);
                throw new RuntimeException("Cannot find international mark with INTREGN = " + fullRegistrationNumber);
            }
        } else {
            saveMissingParentDocumentErrorLog(parentDocumentNumber, userdoc);
            throw new RuntimeException("No parentDocumentId or registrationNumber provided! OFFREF = " + parentDocumentNumber);
        }

        return response;
    }

    private void saveMissingIpObjectErrorLog(String registrationNumber, CUserdoc userdoc) {
        String errorMessage = "Main object with registration number (INTREGN) = " + registrationNumber + " couldn't be found in IPAS !";

        String actionTitle = messageSource.getMessage("error.action.irreg.register", null, LocaleContextHolder.getLocale());
        String customMessage = messageSource.getMessage("error.ipas.irreg.registration", null, LocaleContextHolder.getLocale());
        String instruction = messageSource.getMessage("instruction.ipas.irreg.file", new String[]{registrationNumber}, LocaleContextHolder.getLocale());
        errorLogService.createNewRecord(ErrorLogAbout.IPAS, actionTitle, errorMessage, customMessage, true, instruction, ErrorLogPriority.MEDIUM);
    }

    private void saveMissingParentDocumentErrorLog(String externalSystemId, CUserdoc userdoc) {
        String errorMessage = "Userdoc with external system id = " + externalSystemId + " doesn't exist in IPAS !";

        String actionTitle = messageSource.getMessage("error.action.irreg.register", null, LocaleContextHolder.getLocale());
        String customMessage = messageSource.getMessage("error.ipas.irreg.registration", null, LocaleContextHolder.getLocale());
        String instruction = messageSource.getMessage("instruction.ipas.irreg.parentDoc", new String[]{externalSystemId}, LocaleContextHolder.getLocale());


        errorLogService.createNewRecord(ErrorLogAbout.IPAS, actionTitle, errorMessage, customMessage, true, instruction, ErrorLogPriority.MEDIUM);
    }

    private CFileId selectFileIdByParentDocumentNumber(Integer registrationNumber, String registrationDup) {
        List<CFile> allInternationalFiles = fileService.findAllByRegistrationNbrAndDupAndFileType(registrationNumber, registrationDup, FileType.getInternationalMarkFileTypes());

        if (!CollectionUtils.isEmpty(allInternationalFiles)) {
            CFile cFile = allInternationalFiles.stream().max(Comparator.comparing(fileId -> fileId.getProcessSimpleData().getCreationDate())).orElse(null);

            if (Objects.nonNull(cFile)) {
                return cFile.getFileId();
            }
        }

        return null;
    }

    private String getActionExtParam(String userdocType) {
        switch (userdocType) {
            case UserdocType.MARK_INTERNATIONAL_SUBSEQUENT_DESIGNATION:
                return ConfigParamService.EXT_CONFIG_PARAM_ZTR_IRREGULARITY_LETTER_ACTION_TYPE;
            case UserdocType.MARK_INTERNATIONAL_REGISTRATION_REQUEST:
                return ConfigParamService.EXT_CONFIG_PARAM_ZMR_IRREGULARITY_LETTER_ACTION_TYPE;
            default:
                return null;
        }
    }
}
