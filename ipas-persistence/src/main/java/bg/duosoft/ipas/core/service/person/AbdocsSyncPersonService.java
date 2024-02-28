package bg.duosoft.ipas.core.service.person;

import bg.duosoft.ipas.core.model.person.CPersonAbdocsSync;

import java.util.List;

public interface AbdocsSyncPersonService {

    List<CPersonAbdocsSync> selectNotProcessed();

    CPersonAbdocsSync update(CPersonAbdocsSync record);

    CPersonAbdocsSync markAsProcessedAndSynchronized(CPersonAbdocsSync record);

    CPersonAbdocsSync markAsProcessedAndNotSynchronized(CPersonAbdocsSync record);
}
