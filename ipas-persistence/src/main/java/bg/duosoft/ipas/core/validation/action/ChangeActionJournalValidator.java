package bg.duosoft.ipas.core.validation.action;

import bg.duosoft.ipas.core.model.action.CAction;
import bg.duosoft.ipas.core.model.action.CJournal;
import bg.duosoft.ipas.core.service.action.JournalService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ChangeActionJournalValidator implements IpasValidator<CAction> {

    @Autowired
    private JournalService journalService;

    @Override
    public List<ValidationError> validate(CAction obj, Object... additionalArgs) {
        CJournal journal = obj.getJournal();

        List<ValidationError> errors = new ArrayList<>();
        checkEmptyJournalObject(journal, errors);
        if (Objects.nonNull(journal.getJournalCode())) {
            checkEmptyJournalCode(journal, errors);
            if (CollectionUtils.isEmpty(errors)) {
                CJournal originalJournal = journalService.selectJournal(journal.getJournalCode());
                checkExistingJournal(originalJournal, errors);
                checkIfClosed(originalJournal, errors);
                checkIfPublished(originalJournal, errors);
            }
        }
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }

    private void checkEmptyJournalObject(CJournal journal, List<ValidationError> errors) {
        if (CollectionUtils.isEmpty(errors) && Objects.isNull(journal))
            Objects.requireNonNull(errors).add(ValidationError.builder().pointer("journal.journalCode").messageCode("required.field").build());
    }

    private void checkEmptyJournalCode(CJournal cJournal, List<ValidationError> errors) {
        if (CollectionUtils.isEmpty(errors) && StringUtils.isEmpty(cJournal.getJournalCode()))
            errors.add(ValidationError.builder().pointer("journal.journalCode").messageCode("required.field").build());
    }

    private void checkExistingJournal(CJournal cJournal, List<ValidationError> errors) {
        if (CollectionUtils.isEmpty(errors) && Objects.isNull(cJournal))
            Objects.requireNonNull(errors).add(ValidationError.builder().pointer("journal.journalCode").messageCode("action.original.journal.not.exist").build());
    }

    private void checkIfPublished(CJournal cJournal, List<ValidationError> errors) {
        if (CollectionUtils.isEmpty(errors) && Objects.nonNull(cJournal.getPublicationDate()))
            errors.add(ValidationError.builder().pointer("journal.journalCode").messageCode("action.original.journal.published").build());
    }

    private void checkIfClosed(CJournal cJournal, List<ValidationError> errors) {
        if (CollectionUtils.isEmpty(errors) && cJournal.getIndClosed())
            errors.add(ValidationError.builder().pointer("journal.journalCode").messageCode("action.original.journal.closed").build());
    }
}
