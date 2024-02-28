package bg.duosoft.ipas.core.validation.ipobject;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.RelationshipDirection;
import bg.duosoft.ipas.enums.RelationshipType;
import bg.duosoft.ipas.persistence.repository.entity.file.IpFileRepository;
import bg.duosoft.ipas.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;

/**
 * User: Georgi
 * Date: 13.12.2019 г.
 * Time: 14:38
 */
@Component
public class RelationshipValidator implements IpasTwoArgsValidator<CRelationship, CFile> {
    @Autowired
    private IpoSearchService searchServiceUtils;

    @Autowired
    private IpFileRepository ipFileRepository;

    @Override
    public List<ValidationError> validate(CRelationship obj, CFile arg, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();

        if (obj == null || obj.getFileId() == null || obj.getFileId().getFileNbr() == null || obj.getFileId().getFileSeries() == null || StringUtils.isEmpty(obj.getFileId().getFileType()) || StringUtils.isEmpty(obj.getFileId().getFileSeq())) {
            return Collections.singletonList(ValidationError.builder().pointer(getPointer(obj.getRelationshipType())).messageCode("missing.relationship").build());
        }


        Date relationshipFilingDate = ipFileRepository.getFilingDate(obj.getFileId().getFileSeq(),obj.getFileId().getFileType(),
                obj.getFileId().getFileSeries(),obj.getFileId().getFileNbr());

        if (Objects.isNull(relationshipFilingDate)) {
            return Collections.singletonList(ValidationError.builder().pointer(getPointer(obj.getRelationshipType())).messageCode("missing.relationship").invalidValue(obj.getFileId().getFileSeq() + "/" + obj.getFileId().getFileType() + "/" + obj.getFileId().getFileSeries() + "/" + obj.getFileId().getFileNbr()).build());
        }

        if (!obj.getFileId().getFileType().equals(FileType.SINGLE_DESIGN.code()) && !obj.getFileId().getFileType().equals(FileType.INTERNATIONAL_SINGLE_DESIGN.code())){
            errors.addAll(validateRelationshipFilingDate(obj, arg, relationshipFilingDate));
        }
        return errors;
    }

    private List<ValidationError> validateRelationshipFilingDate(CRelationship obj, CFile file,Date relationshipFilingDate) {
        LocalDate filingDate = DateUtils.dateToLocalDate(file.getFilingData().getFilingDate());
        LocalDate relatedObjectFilingDate = DateUtils.dateToLocalDate(relationshipFilingDate);
        if (filingDate != null && relatedObjectFilingDate != null) {
            boolean wrongFilingDates;
            if (obj.getRelationshipRole().equals(RelationshipDirection.RELATIONSHIP_CONTAINS_DIVIDED_OBJECTS_ROLE)) {
                wrongFilingDates = relatedObjectFilingDate.isBefore(filingDate);
            } else {
                wrongFilingDates = relatedObjectFilingDate.isAfter(filingDate);
            }
            if (wrongFilingDates) {
                return Collections.singletonList(ValidationError.builder().pointer(getPointer(obj.getRelationshipType())).messageCode("wrong.relationship.filing.date").invalidValue(obj.getFileId().getFileSeq() + "/" + obj.getFileId().getFileType() + "/" + obj.getFileId().getFileSeries() + "/" + obj.getFileId().getFileNbr()).build());
            }
        }
        return new ArrayList<>();
    }

    private String getPointer(String relationshipType) {
        if (relationshipType == null) {
            return "relationship";
        }
        switch (relationshipType) {
            case RelationshipType.DIVISIONAL_EUROPEAN_PATENT_TYPE:
            case RelationshipType.DIVISIONAL_MARK_TYPE:
            case RelationshipType.DIVISIONAL_PATENT_TYPE:
            case RelationshipType.DIVISIONAL_UTILITY_MODEL_TYPE:
            case RelationshipType.DIVISIONAL_DESIGN_TYPE:
                return "divisional.app.autocomplete";
            case RelationshipType.TRANSFORMED_NATIONAL_PATENT_TYPE:
                return "national.patent.transformation.autocomplete";
            case RelationshipType.SPC_MAIN_PATENT_TYPE:
                return "main.patent.autocomplete";
            default:
                return "relationship";
        }
    }

}
