package bg.duosoft.ipas.core.service.action;

import bg.duosoft.ipas.core.model.action.CJournal;

import java.util.List;
import java.util.Map;

public interface JournalService {

    CJournal selectJournal(String journalCode);

    Map<String, String> selectOpenJournals();

}