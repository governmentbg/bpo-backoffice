package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.RelationshipType;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.Objects;

public class PatentSpcUtils {

    public static LinkedHashMap<String, String> generateMainPatentOptionsForSpc(MessageSource messageSource) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(FileType.PATENT.code(), messageSource.getMessage("main.spc.patent.option.p", null, LocaleContextHolder.getLocale()));
        map.put(FileType.EU_PATENT.code(), messageSource.getMessage("main.spc.patent.option.t", null, LocaleContextHolder.getLocale()));
        return map;

    }

    public static boolean hasSpcObjects(CPatent patent){
        if (patent.getFile().getFileId().getFileType().equals(FileType.PATENT.code()) || patent.getFile().getFileId().getFileType().equals(FileType.EU_PATENT.code())){
            if (!Objects.isNull(patent.getFile().getRelationshipList()) && !CollectionUtils.isEmpty(patent.getFile().getRelationshipList())){
                CRelationship relationship= patent.getFile().getRelationshipList().stream().filter(r->r.getRelationshipType().equals(RelationshipType.SPC_MAIN_PATENT_TYPE)).findFirst().orElse(null);
                if (!Objects.isNull(relationship)){
                    return true;
                }
            }
        }
        return false;
    }


    public static String generateSpcMainPatentFullName(CFileId fileId, CStatus status){
        return  !Objects.isNull(fileId) && !Objects.isNull(status) ? fileId.getFileSeq() + "/" +
                fileId.getFileType() + "/" + fileId.getFileSeries() + "/" + fileId.getFileNbr() + " - "+status.getStatusName() : null;
    }
}
