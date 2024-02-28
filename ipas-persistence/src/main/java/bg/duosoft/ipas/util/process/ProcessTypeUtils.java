package bg.duosoft.ipas.util.process;


import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.enums.FileType;

import java.util.Objects;

public class ProcessTypeUtils {

    public static final Integer IPOBJECT_TYPE_WCODE = 1;
    public static final Integer USERDOC_TYPE_WCODE = 2;
    public static final Integer OFFIDOC_TYPE_WCODE = 3;
    public static final Integer MANUAL_SUB_PROCESS_TYPE_WCODE = 4;

    public static boolean isManualSubProcess(CProcess process) {
        if (Objects.isNull(process))
            return false;

        Integer relatedToWorkCode = process.getProcessOriginData().getRelatedToWorkCode();
        if (Objects.isNull(relatedToWorkCode))
            return false;

        return relatedToWorkCode.equals(MANUAL_SUB_PROCESS_TYPE_WCODE);
    }

    public static boolean isUserdocProcess(CProcess process) {
        if (Objects.isNull(process))
            return false;

        Integer relatedToWorkCode = process.getProcessOriginData().getRelatedToWorkCode();
        if (Objects.isNull(relatedToWorkCode))
            return false;

        return relatedToWorkCode.equals(USERDOC_TYPE_WCODE);
    }

    public static boolean isIpObjectProcess(CProcess process) {
        if (Objects.isNull(process))
            return false;

        Integer relatedToWorkCode = process.getProcessOriginData().getRelatedToWorkCode();
        if (Objects.isNull(relatedToWorkCode))
            return false;

        return relatedToWorkCode.equals(IPOBJECT_TYPE_WCODE);
    }


    public static boolean isOffidocProcess(CProcess process) {
        if (Objects.isNull(process))
            return false;

        Integer relatedToWorkCode = process.getProcessOriginData().getRelatedToWorkCode();
        if (Objects.isNull(relatedToWorkCode))
            return false;

        return relatedToWorkCode.equals(OFFIDOC_TYPE_WCODE);
    }


    public static boolean isPatentInSecretStatus(CFile file, StatusService statusService) {
        boolean isPatentInSecretStatus = false;
        FileType fileType = FileType.selectByCode(file.getFileId().getFileType());
        switch (fileType){
            case PATENT:
            case UTILITY_MODEL:
                isPatentInSecretStatus = statusService.isPatentInSecretStatus(file.getProcessId().getProcessType(), file.getProcessSimpleData().getStatusCode());
                break;
        }
        return isPatentInSecretStatus;
    }
}
