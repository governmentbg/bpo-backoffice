package bg.duosoft.ipas.util.journal;

import bg.duosoft.ipas.core.model.action.CJournal;
import bg.duosoft.ipas.core.model.journal.CJournalElement;
import bg.duosoft.ipas.core.model.process.CActionProcessEvent;
import bg.duosoft.ipas.core.model.process.CProcessEvent;
import bg.duosoft.ipas.util.date.DateUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class JournalUtils {

    private static final String VIEW_JOURNAL_ELEMENT_URL = "/journal/view/element/{element}?node={node}";
    private static final Date NEW_JOURNAL_VERSION_DATE = DateUtils.convertToDate(LocalDate.of(2021,3,15));

    public static boolean hasJournalForEdit(CProcessEvent cProcessEvent) {
        CActionProcessEvent eventAction = cProcessEvent.getEventAction();
        if (Objects.nonNull(eventAction)) {
            Long journalPublicationWcode = eventAction.getActionType().getJournalPublicationWcode();
            if (Objects.nonNull(journalPublicationWcode)) {
                CJournal journal = eventAction.getJournal();
                return Objects.isNull(journal) || !journal.isJournalClosed();
            }
        }
        return false;
    }

    public static String selectJournalElementUrl(String journalUrl, CJournalElement journalElement) {
        if(Objects.isNull(journalElement)) {
            throw new RuntimeException("Journal element is empty - cannot find journal node and element!");
        }

        String fullJournalUrl = journalUrl + VIEW_JOURNAL_ELEMENT_URL;
        return fullJournalUrl.replace("{element}", String.valueOf(journalElement.getJournalNbr())).replace("{node}", String.valueOf(journalElement.getNodeNbr()));
    }

    public static boolean isBeforeNewJournalVersion(Date publicationDate) {
        return publicationDate.before(NEW_JOURNAL_VERSION_DATE);
    }

}
