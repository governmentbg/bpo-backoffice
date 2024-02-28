package bg.duosoft.ipas.core.service.journal;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.journal.CJournalElement;

public interface JournalElementService {

    CJournalElement selectByAction(CActionId actionId, Boolean loadPdfContent);

    byte[] selectNotPublishedElementFile(String journalUrl, CActionId actionId);
}
