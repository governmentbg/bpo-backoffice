package bg.duosoft.ipas.util.eupatent;

import bg.bpo.ebd.ebddpersistence.entity.EbdDPatent;
import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.core.model.action.CActionType;
import bg.duosoft.ipas.core.model.ebddownload.CEbdPatent;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.process.CActionProcessEvent;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessEvent;
import bg.duosoft.ipas.core.model.reception.CReceptionEuPatent;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.enums.EuPatentReceptionType;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.integration.ebddownload.service.EbdPatentService;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.date.DateUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import java.util.*;

import static bg.duosoft.ipas.core.service.nomenclature.ConfigParamService.EXT_CONFIG_PARAM_EPO_PATENT_VALIDATION_ACTION_TYPE;
import static bg.duosoft.ipas.core.service.nomenclature.ConfigParamService.EXT_CONFIG_PARAM_EPO_PATENT_VALIDATION_STATUS_CODE;

public class EuPatentUtils {

    public static final int EPO_PATENT_VALIDATION_STATUS_600 = 600;
    public static final int EPO_PATENT_VALIDATION_STATUS_601 = 601;

    public static CFileId generateEUPatentCFileId(EbdDPatent patent) {
        return generateEUPatentCFileId(Integer.valueOf(patent.getIdappli()), patent.getDtappli());
    }

    public static CFileId generateEUPatentCFileId(Integer fileNbr, Date filingDate) {
        CFileId cFileId = new CFileId();
        cFileId.setFileSeq(DefaultValue.BULGARIA_CODE);
        cFileId.setFileType(FileType.EU_PATENT.code());
        cFileId.setFileSeries(DateUtils.dateToLocalDate(filingDate).getYear());
        cFileId.setFileNbr(fileNbr);
        return cFileId;
    }

    public static CFileId generateEUPatentCFileId(CEbdPatent patent) {
        return generateEUPatentCFileId(Integer.valueOf(patent.getFilingNumber()), patent.getFilingDate());
    }

    public static boolean isEuPatentValidated(CProcess cProcess, ConfigParamService configParamService) {
        CConfigParam validatedActionTypeConfigParam = configParamService.selectExtConfigByCode(EXT_CONFIG_PARAM_EPO_PATENT_VALIDATION_ACTION_TYPE);
        if (Objects.isNull(validatedActionTypeConfigParam))
            throw new RuntimeException("Cannot find config param " + EXT_CONFIG_PARAM_EPO_PATENT_VALIDATION_ACTION_TYPE);

        CConfigParam validatedStatusConfigParam = configParamService.selectExtConfigByCode(EXT_CONFIG_PARAM_EPO_PATENT_VALIDATION_STATUS_CODE);
        if (Objects.isNull(validatedStatusConfigParam))
            throw new RuntimeException("Cannot find config param " + EXT_CONFIG_PARAM_EPO_PATENT_VALIDATION_STATUS_CODE);

        String statusCode = cProcess.getStatus().getStatusId().getStatusCode();
        if (statusCode.equals(validatedStatusConfigParam.getValue()))
            return true;

        List<CProcessEvent> processEventList = cProcess.getProcessEventList();
        CActionType actionTypeValidated = processEventList == null ? null : processEventList.stream()
                .filter(cProcessEvent -> Objects.nonNull(cProcessEvent.getEventAction()))
                .map(CProcessEvent::getEventAction)
                .map(CActionProcessEvent::getActionType)
                .filter(cActionType -> cActionType.getActionType().equals(validatedActionTypeConfigParam.getValue()))
                .findFirst()
                .orElse(null);

        return Objects.nonNull(actionTypeValidated);
    }

    public static CFile selectEuPatentIpasFile(FileService fileService, CEbdPatent cEbdPatent) {
        CFileId euPatentCFileId = EuPatentUtils.generateEUPatentCFileId(cEbdPatent);
        return fileService.findById(euPatentCFileId.getFileSeq(), euPatentCFileId.getFileType(), euPatentCFileId.getFileSeries(), euPatentCFileId.getFileNbr());
    }

    public static void fillEuPatentWarnings(CReception receptionForm, List<String> warnings, MessageSource messageSource,
                                            EbdPatentService ebdPatentService, FileService fileService, ProcessService processService, ConfigParamService configParamService) {
        CReceptionEuPatent euPatent = receptionForm.getEuPatent();
        if (!StringUtils.isEmpty(euPatent.getObjectNumber())) {
            CEbdPatent cEbdPatent = ebdPatentService.selectByFileNumber(euPatent.getObjectNumber());
            if (cEbdPatent.isWithdrawn())
                warnings.add(messageSource.getMessage("eupatent.withdrawn.warning", null, LocaleContextHolder.getLocale()));

            CFile euPatentIpasFile = EuPatentUtils.selectEuPatentIpasFile(fileService, cEbdPatent);
            if (Objects.nonNull(euPatentIpasFile)) {
                CProcess cProcess = processService.selectProcess(euPatentIpasFile.getProcessId(), true);
                boolean isEuPatentValidated = EuPatentUtils.isEuPatentValidated(cProcess, configParamService);
                if (!isEuPatentValidated && !euPatent.getUserdocType().equals(EuPatentReceptionType.VALIDATION_AFTER_MODIFICATION.code()))
                    warnings.add(messageSource.getMessage("eupatent.ipas.existing", null, LocaleContextHolder.getLocale()));
            }

        }
    }

    public static LinkedHashMap<String, String> generateEuPatentReceptionTypeSelectOptions(MessageSource messageSource) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(EuPatentReceptionType.VALIDATION.code(), messageSource.getMessage("reception.ebd.type.VALIDATION", null, LocaleContextHolder.getLocale()));
        map.put(EuPatentReceptionType.TEMPORARY_PROTECTION.code(), messageSource.getMessage("reception.ebd.type.TEMPORARY_PROTECTION", null, LocaleContextHolder.getLocale()));
        map.put(EuPatentReceptionType.VALIDATION_AFTER_MODIFICATION.code(), messageSource.getMessage("reception.ebd.type.VALIDATION_AFTER_MODIFICATION", null, LocaleContextHolder.getLocale()));
        return map;
    }
}
