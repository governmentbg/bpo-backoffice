package bg.duosoft.ipas.core.service.search;

import bg.duosoft.ipas.util.search.IndexProgressMonitor;

import java.io.Serializable;

public interface IndexService {
    IndexProgressMonitor indexAll(boolean async);
    void deleteAll();

//    IndexProgressMonitor index(Class<?>... entityTypes);

    <ID extends Serializable, T> void index(ID idObj, Class<T> entityType);

    void delete(Class<?> entityType);

    <ID extends Serializable, T> void delete(ID idObj, Class<T> entityType);

    IndexProgressMonitor indexMarks();

    IndexProgressMonitor indexPatents();

    IndexProgressMonitor indexActions();

    IndexProgressMonitor indexProcesses();

    IndexProgressMonitor indexPersonAddresses();

    IndexProgressMonitor indexPatentSummary();

    IndexProgressMonitor indexIpcClasses();
    IndexProgressMonitor indexCpcClasses();

    IndexProgressMonitor indexPatentLocarnoClasses();

    IndexProgressMonitor indexFileRelationships();

    IndexProgressMonitor indexNiceClasses();

    IndexProgressMonitor indexViennaClasses();

    IndexProgressMonitor indexMarkAttachmentViennaClasses();

    IndexProgressMonitor indexDocs();

    IndexProgressMonitor indexUserDocs();

    IndexProgressMonitor indexUserDocPersons();

}
