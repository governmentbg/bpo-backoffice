package bg.duosoft.ipas.util.patent;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.patent.CPatentAttachment;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.process.ProcessTypeUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PatentUtils {
    public static Integer generateAttachmentIdOnAdd(List<CPatentAttachment> attachmentsParam, Integer attachmentType){
        List<CPatentAttachment> patentAttachments = attachmentsParam.stream().filter(r->r.getAttachmentType().getId().equals(attachmentType)).collect(Collectors.toList());
        CPatentAttachment patentAttachmentWithMaxId = patentAttachments.stream()
                .max(Comparator.comparing(r -> r.getId())).orElse(null);
        if (Objects.isNull(patentAttachmentWithMaxId)){
            return DefaultValue.INCREMENT_VALUE;
        }else{
            return patentAttachmentWithMaxId.getId()+DefaultValue.INCREMENT_VALUE;
        }
    }

    public static boolean hasDetailDataAccess(CFile file, StatusService statusService){
        boolean patentInSecretStatus = ProcessTypeUtils.isPatentInSecretStatus(file, statusService);
        boolean hasRights = SecurityUtils.hasRights(SecurityRole.PatentSecretData);
        if (patentInSecretStatus && !hasRights){
            return false;
        }
        return true;
    }
}
