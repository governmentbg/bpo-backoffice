package bg.duosoft.ipas.core.validation.reception;

import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CSubmissionType;
import bg.duosoft.ipas.core.service.dailylog.DailyLogService;
import bg.duosoft.ipas.core.service.reception.SubmissionTypeService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.person.PersonValidator;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.person.RepresentativeUtils;
import bg.duosoft.ipas.util.validation.PersonValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

abstract class ReceptionBaseValidator implements IpasValidator<CReception> {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SubmissionTypeService submissionTypeService;
    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private DailyLogService dailyLogService;

    public List<ValidationError> validate(CReception receptionForm, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        rejectIfTrue(errors, receptionForm == null, "receptionForm", "empty.reception.form");

        CSubmissionType submissionType = receptionForm == null ? null : submissionTypeService.selectById(receptionForm.getSubmissionType());
        rejectIfTrue(errors, submissionType == null, "submissionType", "reception.wrong.submission.type");

        validateEntryDate(receptionForm, errors);

        rejectIfEmpty(errors, dailyLogService.getWorkingDate(), "receptionForm", "reception.no.daily.log.opened");
        validateRepresentativeLawyers(receptionForm, errors);
        return errors;
    }

    private void validateEntryDate(CReception receptionForm, List<ValidationError> errors) {
        Date entryDate = receptionForm.getEntryDate();
        rejectIfEmpty(errors, entryDate, "entryDate");
        if (CollectionUtils.isEmpty(errors)) {
            LocalDate currentDate = LocalDate.now();
            LocalDate entryDateAsLocalDate = DateUtils.convertToLocalDate(entryDate);
            rejectIfFirstDateIsAfterSecond(errors, entryDateAsLocalDate, currentDate, "entryDate", "reception.entryDate.future");
        }
    }

    private void validateRepresentativeLawyers(CReception receptionForm, List<ValidationError> errors) {
        if (containsRepresentatives(receptionForm)) {
            List<CRepresentative> representativeList = receptionForm.getRepresentationData().getRepresentativeList();
            rejectIfFalse(errors,RepresentativeUtils.areAllLawyersPhysicalPersons(representativeList),"receptionRepresentatives","lawyer.not.physical.person.error");
        }
    }

    protected boolean containsOwners(CReception receptionForm) {
        COwnershipData ownershipData = receptionForm.getOwnershipData();
        if (Objects.isNull(ownershipData))
            return false;

        List<COwner> ownerList = ownershipData.getOwnerList();
        return !CollectionUtils.isEmpty(ownerList);
    }

    protected boolean containsRepresentatives(CReception receptionForm) {
        CRepresentationData representationData = receptionForm.getRepresentationData();
        if (Objects.isNull(representationData))
            return false;

        List<CRepresentative> representativeList = representationData.getRepresentativeList();
        return !CollectionUtils.isEmpty(representativeList);
    }

    protected void validateOwnersData(List<ValidationError> errors, List<COwner> ownerList) {
        for (COwner cOwner : ownerList) {
            if (CollectionUtils.isEmpty(errors)) {
                CPerson person = cOwner.getPerson();
                if (Objects.nonNull(person)) {
                    List<ValidationError> personErrors = personValidator.validate(person, PersonValidationUtils.excludeFieldsOnReception());
                    if (!CollectionUtils.isEmpty(personErrors)) {
                        String message = messageSource.getMessage("reception.owners.wrong.data", new String[]{person.getPersonName()}, LocaleContextHolder.getLocale());
                        message += personErrors
                                .stream()
                                .map(r -> !StringUtils.isEmpty(r.getMessage()) ? r.getMessage() : (StringUtils.isEmpty(r.getMessageCode()) ? null : messageSource.getMessage(r.getMessageCode(), r.getInvalidValue() == null ? null : new Object[]{r.getInvalidValue()}, LocaleContextHolder.getLocale())))
                                .filter(Objects::nonNull)
                                .map(r -> " - " + r)
                                .collect(Collectors.joining("\n"));
                        errors.add(ValidationError.builder().pointer("receptionOwners").message(message).build());
                    }
                }
            }
        }
    }

}
