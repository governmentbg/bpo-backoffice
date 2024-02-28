package bg.duosoft.ipas.core.validation.ipobject;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CPriorityData;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.date.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User: Georgi
 * Date: 5.12.2019 г.
 * Time: 15:03
 */
@Component
public class ExhibitionValidator implements IpasValidator<CFile> {
    @Override
    public List<ValidationError> validate(CFile obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        CPriorityData priorityData = obj.getPriorityData();
        if (Objects.nonNull(priorityData)) {
            boolean hasExhibitionData = priorityData.isHasExhibitionData();
            if (hasExhibitionData) {
                if (StringUtils.isEmpty(priorityData.getExhibitionNotes()))
                    errors.add(ValidationError.builder().pointer("exhibition.exhibitionNotes").messageCode("required.field").build());

                if (Objects.isNull(priorityData.getExhibitionDate())) {
                    errors.add(ValidationError.builder().pointer("exhibition.exhibitionDate").messageCode("required.field").build());
                } else if (obj.getFilingData().getFilingDate() != null && DateUtils.dateToLocalDate(obj.getFilingData().getFilingDate()).isBefore(DateUtils.dateToLocalDate(priorityData.getExhibitionDate()))) {
                    errors.add(ValidationError.builder().pointer("exhibition.exhibitionDate").messageCode("exhibition.exhibition.date.before.filing.date").build());
                }


            } else {
                if (!StringUtils.isEmpty(priorityData.getExhibitionNotes()))
                    errors.add(ValidationError.builder().pointer("exhibition.exhibitionNotes").messageCode("should.be.empty").build());
                if (!Objects.isNull(priorityData.getExhibitionDate()))
                    errors.add(ValidationError.builder().pointer("exhibition.exhibitionDate").messageCode("should.be.empty").build());
            }
        }

        return CollectionUtils.isEmpty(errors) ? null : errors;
    }
}
