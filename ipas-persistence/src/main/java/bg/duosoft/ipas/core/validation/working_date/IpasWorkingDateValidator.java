package bg.duosoft.ipas.core.validation.working_date;

import bg.duosoft.ipas.core.service.dailylog.DailyLogService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class IpasWorkingDateValidator implements IpasValidator<Date> {

    @Autowired
    private DailyLogService dailyLogService;

    @Override
    public List<ValidationError> validate(Date workingDate, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();

        rejectIfEmpty(errors, workingDate, "receptionWorkingDate");
        rejectIfWorkingDateIsEqualToCurrentWorkingDate(workingDate, errors);
        rejectIfWorkingDateIsBeforeCurrentWorkingDate(workingDate, errors);
        rejectIfWorkingDateIsAfterCurrentDate(workingDate, errors);
        return errors;
    }

    private void rejectIfWorkingDateIsBeforeCurrentWorkingDate(Date workingDate, List<ValidationError> errors) {
        if (CollectionUtils.isEmpty(errors)) {
            Date currentWorkingDate = dailyLogService.getWorkingDate();
            if (Objects.nonNull(currentWorkingDate)) {
                rejectIfFirstDateIsBeforeSecond(errors, DateUtils.convertToLocalDate(workingDate), DateUtils.convertToLocalDate(currentWorkingDate), "receptionWorkingDate", "reception.workingDate.earlier");
            }
        }
    }

    private void rejectIfWorkingDateIsAfterCurrentDate(Date workingDate, List<ValidationError> errors) {
        if (CollectionUtils.isEmpty(errors)) {
            rejectIfFirstDateIsAfterSecond(errors, DateUtils.convertToLocalDate(workingDate), LocalDate.now(), "receptionWorkingDate", "reception.workingDate.later");
        }
    }

    private void rejectIfWorkingDateIsEqualToCurrentWorkingDate(Date workingDate, List<ValidationError> errors) {
        if (CollectionUtils.isEmpty(errors)) {
            Date currentWorkingDate = dailyLogService.getWorkingDate();
            if (Objects.nonNull(currentWorkingDate)) {
                rejectIfBothDatesAreEquals(errors, DateUtils.convertToLocalDate(workingDate), DateUtils.convertToLocalDate(currentWorkingDate), "receptionWorkingDate", "reception.workingDate.equals");
            }
        }
    }
}
