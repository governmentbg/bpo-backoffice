package bg.duosoft.ipas.core.service.ext;

import bg.duosoft.ipas.core.model.error.CIndexQueue;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IndexQueueService {
    void startIndexing();

    List<CIndexQueue> findAllNotIndexed();

    void addFailPatentSummaryToIndexQueue();

    void addFailIpcClassesToIndexQueue();

    void addFailLocarnoClassesToIndexQueue();

    void addFailFileRelationshipsToIndexQueue();

    void addFailNiceClassesToIndexQueue();

    void addFailViennaClassesToIndexQueue();

    void addFailPersonAddressesToIndexQueue();

    void addFailMarksToIndexQueue();

    void addFailPatentToIndexQueue();

    void addFailIpDocsToIndexQueue();

    void addFailIpUserdocsToIndexQueue();

    void addFailIpUserdocPersonToIndexQueue();

    void addFailIpProcToIndexQueue();

    void addFailIpActionToIndexQueue();

    /**
     * @return the current max id in the queue table
     */
    public Optional<Integer> getQueueMaxId();

    /**
     * @param id
     * deletes the records with id <= provided id
     */
    public void deleteQueueOldRecords(int id);

}
