package bg.duosoft.ipas.persistence.repository.entity.custom;

import bg.duosoft.ipas.persistence.model.nonentity.GenerateJournalData;

public interface JournalElementRepositoryCustom {
    GenerateJournalData selectJournalParamsByAction(String procTyp, Integer procNbr, Integer actionNbr);
}
