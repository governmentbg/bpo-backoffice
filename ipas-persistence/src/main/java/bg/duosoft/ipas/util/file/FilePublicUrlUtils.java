package bg.duosoft.ipas.util.file;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.model.person.CAuthor;
import bg.duosoft.ipas.core.model.util.CPdfAttachmentBookmark;
import bg.duosoft.ipas.core.service.file.FileRelationshipsService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.RelationshipType;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FilePublicUrlUtils {


    private static String selectDesignPublicUrl(String publicRegisterUrl, CFileId fileId, FileRelationshipsService fileRelationshipsService) {
        List<CRelationship> relationships2ForFile = fileRelationshipsService.findRelationships2ForFile(fileId, RelationshipType.DESIGN_TYPE);
        if (CollectionUtils.isEmpty(relationships2ForFile)) {
            return publicRegisterUrl + "?key=" + fileId.getFileNbr() + "-0001";
        }
        Integer minFileNbr = relationships2ForFile.stream().map(relationship -> relationship.getFileId().getFileNbr()).min(Comparator.comparingInt(Integer::intValue)).orElse(null);

        String minFileNbrAsString = String.valueOf(minFileNbr);
        String mainDesignNbrAsString = String.valueOf(fileId.getFileNbr());

        if (minFileNbrAsString.contains(mainDesignNbrAsString)) {
            String singleDesignSeqNumber = minFileNbrAsString.replace(mainDesignNbrAsString, "");
            return publicRegisterUrl + "?key=" + mainDesignNbrAsString + "-0" + singleDesignSeqNumber;
        } else {
            String singleDesignNumberSecondPart =  minFileNbrAsString.substring(minFileNbrAsString.length() - 3);
            return publicRegisterUrl + "?key=" + mainDesignNbrAsString + "-0" + singleDesignNumberSecondPart;
        }
    }

    public static String selectPublicUrl(String publicRegisterUrl, CFileId fileId, FileRelationshipsService fileRelationshipsService) {
        if (Objects.isNull(fileId)) {
            throw new RuntimeException("Cannot select public URL - fileId is empty");
        }

        FileType fileType = FileType.selectByCode(fileId.getFileType());
        switch (fileType) {
            case EU_PATENT:
                return publicRegisterUrl + "?epo_key=EP" + String.format("%08d", fileId.getFileNbr());
            case DESIGN:
                return selectDesignPublicUrl(publicRegisterUrl, fileId, fileRelationshipsService);
            default:
                return publicRegisterUrl + "?key=" + fileId.getFileSeries() + String.format("%06d", fileId.getFileNbr()) + fileId.getFileType();
        }
    }

    public static boolean hasPublicRegisterObject(CFileId fileId) {
        if (Objects.isNull(fileId)) {
            throw new RuntimeException("FileId is empty");
        }

        String fileType = FileType.getFileTypesWithPublicUrl().stream().filter(f -> f.equals(fileId.getFileType())).findFirst().orElse(null);
        return Objects.nonNull(fileType);
    }

    public static boolean hasAllPublicRegisterObject(List<String> groupFileTypes) {
        return FileType.getFileTypesWithPublicUrl().containsAll(groupFileTypes);
    }
}
