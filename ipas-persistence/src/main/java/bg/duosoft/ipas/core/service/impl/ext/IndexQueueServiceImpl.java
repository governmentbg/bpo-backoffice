package bg.duosoft.ipas.core.service.impl.ext;

import bg.duosoft.ipas.core.mapper.ext.IndexQueueMapper;
import bg.duosoft.ipas.core.model.error.CIndexQueue;
import bg.duosoft.ipas.core.service.ext.IndexQueueService;
import bg.duosoft.ipas.core.service.impl.search.HibernateSearchService;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.action.IpActionPK;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.ext.IndexQueue;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPerson;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPersonPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationship;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationshipPK;
import bg.duosoft.ipas.persistence.model.entity.mark.*;
import bg.duosoft.ipas.persistence.model.entity.patent.*;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddressesPK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.persistence.repository.entity.action.IpActionRepository;
import bg.duosoft.ipas.persistence.repository.entity.doc.IpDocRepository;
import bg.duosoft.ipas.persistence.repository.entity.ext.IndexQueueRepository;
import bg.duosoft.ipas.persistence.repository.entity.file.IpFileRelationshipsRepository;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpLogoViennaClassesRepository;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkNiceClassesRepository;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkRepository;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentIpcClassesRepository;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentLocarnoClassesRepository;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentRepository;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentSummaryRepository;
import bg.duosoft.ipas.persistence.repository.entity.person.IpPersonAdressesRepository;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocPersonRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocRepository;
import bg.duosoft.ipas.util.search.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.HibernateException;
import org.hibernate.search.bridge.TwoWayFieldBridge;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IndexQueueServiceImpl implements IndexQueueService {

    private final static String IP_PATENT_TYPE = "1";
    private final static String IP_MARK_TYPE = "2";
    private final static String IP_PERSON_ADDRESS_TYPE = "3";
    private final static String IP_PROC_TYPE = "4";
    private final static String IP_ACTION_TYPE = "5";
    private final static String IP_LOGO_VIENNA_CLASSES_TYPE = "6";
    private final static String IP_MARK_NICE_TYPE = "7";
    private final static String IP_PATENT_IPC_CLASSES_TYPE = "8";
    private final static String IP_PATENT_SUMMARY_TYPE = "9";
    private final static String IP_DOC_TYPE = "10";
    private final static String IP_USERDOC_TYPE = "11";
    private final static String IP_USERDOC_PERSON_TYPE = "12";
    private final static String IP_PATENT_LOCARNO_CLASSES_TYPE = "13";
    private final static String IP_FILE_RELATIONSHIP_TYPE = "14";
    private final static String IP_MARK_ATTACHMENT_VIENNA_CLASSES_TYPE = "15";
    private final static String IP_PATENT_CPC_CLASSES_TYPE = "16";
    @Autowired
    private IndexLogger indexLogger;

    @Autowired
    private IndexQueueRepository indexQueueRepository;

    @Autowired
    private IndexService indexService;

    @Autowired
    private IndexQueueMapper indexQueueMapper;

    @Autowired
    private HibernateSearchService hibernateSearchService;

    @Autowired
    private IpPatentSummaryRepository patentSummaryRepository;

    @Autowired
    private IpPatentIpcClassesRepository ipcClassesRepository;

    @Autowired
    private IpPatentLocarnoClassesRepository locarnoClassesRepository;

    @Autowired
    private IpFileRelationshipsRepository fileRelationshipsRepository;

    @Autowired
    private IpMarkNiceClassesRepository niceClassesRepository;

    @Autowired
    private IpLogoViennaClassesRepository viennaClassesRepository;

    @Autowired
    private IpPersonAdressesRepository personAdressesRepository;

    @Autowired
    private IpMarkRepository markRepository;

    @Autowired
    private IpPatentRepository patentRepository;

    @Autowired
    private IpDocRepository docRepository;

    @Autowired
    private IpUserdocRepository ipUserdocRepository;

    @Autowired
    private IpUserdocPersonRepository userdocPersonRepository;

    @Autowired
    private IpProcRepository procRepository;

    @Autowired
    private IpActionRepository actionRepository;

    @Override
    public void startIndexing() {
        log.debug("Start index cron");

        List<IndexQueue> all = indexQueueRepository.findAllByCheckedFalseAndIndexedAtNullOrderByInsertedAtAscIdAsc();
        log.debug("There are {} rows for indexing.", all.size());

        all.stream().parallel()
                .forEach(indexQueue -> {
                    changeIndex(indexQueue);
                });
        log.debug("End index cron");
    }

    @Override
    public List<CIndexQueue> findAllNotIndexed() {
        List<IndexQueue> awaitingToBeIndexed = indexQueueRepository.findAllByCheckedFalseAndIndexedAtNullOrderByInsertedAtAscIdAsc();

        return indexQueueMapper.toCoreList(awaitingToBeIndexed);
    }

    @Override
    @Transactional
    public void addFailPatentSummaryToIndexQueue() {

        Util<IpPatentSummaryPK> util = new Util<>(
                IpPatentSummary.class,
                "pk",
                new IpPatentSummaryPKBridge(),
                docPK -> {

                    String[] split = docPK.split("/");
                    if (split.length != 5) {
                        throw new RuntimeException("index doesn't contain correct pk");
                    }

                    IpPatentSummaryPK ipPatentSummaryPK = new IpPatentSummaryPK(
                            split[0],
                            split[1],
                            Integer.parseInt(split[2]),
                            Integer.parseInt(split[3]),
                            split[4]);

                    IndexQueue indexQueue = new IndexQueue(ipPatentSummaryPK,
                            IP_PATENT_SUMMARY_TYPE,
                            "I",
                            new Date());
                    return indexQueue;
                },
                patentSummaryRepository.listPatentSummaryPK()
        );
        addFailRecordsToIndexQueue(util);
    }

    @Override
    @Transactional
    public void addFailIpcClassesToIndexQueue() {

        Util<IpPatentIpcClassesPK> util = new Util<>(
                IpPatentIpcClasses.class,
                "pk",
                new IpPatentIpcClassPKBridge(),
                docPK -> {

                    String[] split = docPK.split("/");
                    if ((split.length < 4 || split.length > 11)
                            || docPK.chars().filter(ch -> ch == '/').count() != 10 ) {
                        throw new RuntimeException("index doesn't contain correct pk");
                    }

                    IpPatentIpcClassesPK ipPatentIpcClassesPK = new IpPatentIpcClassesPK(
                            split[0],
                            split[1],
                            Integer.parseInt(split[2]),
                            Integer.parseInt(split[3]),
                            split[4],
                            split[5],
                            split[6],
                            split[7],
                            split[8],
                            split[9],
                            split[10]);

                    IndexQueue indexQueue = new IndexQueue(ipPatentIpcClassesPK,
                            IP_PATENT_IPC_CLASSES_TYPE,
                            "I",
                            new Date());
                    return indexQueue;
                },
                ipcClassesRepository.listPatentIpcClassesPK()
        );
        addFailRecordsToIndexQueue(util);
    }

    @Override
    @Transactional
    public void addFailLocarnoClassesToIndexQueue() {

        Util<IpPatentLocarnoClassesPK> util = new Util<>(
                IpPatentLocarnoClasses.class,
                "pk",
                new IpPatentLocarnoClassesPKBridge(),
                docPK -> {

                    String[] split = docPK.split("/");
                    if (split.length != 5) {
                        throw new RuntimeException("index doesn't contain correct pk");
                    }

                    IpPatentLocarnoClassesPK ipPatentLocarnoClassesPK = new IpPatentLocarnoClassesPK(
                            split[0],
                            split[1],
                            Integer.parseInt(split[2]),
                            Integer.parseInt(split[3]),
                            split[4]);

                    IndexQueue indexQueue = new IndexQueue(ipPatentLocarnoClassesPK,
                            IP_PATENT_LOCARNO_CLASSES_TYPE,
                            "I",
                            new Date());
                    return indexQueue;
                },
                locarnoClassesRepository.listLocarnoClassesPK()
        );
        addFailRecordsToIndexQueue(util);
    }

    @Override
    @Transactional
    public void addFailFileRelationshipsToIndexQueue() {

        Util<IpFileRelationshipPK> util = new Util<>(
                IpFileRelationship.class,
                "pk",
                new IpFileRelationshipPkBridge(),
                docPK -> {

                    String[] split = docPK.split("/");
                    if (split.length != 9) {
                        throw new RuntimeException("index doesn't contain correct pk");
                    }

                    IpFileRelationshipPK ipFileRelationshipPK = new IpFileRelationshipPK(
                            split[0],
                            split[1],
                            Integer.parseInt(split[2]),
                            Integer.parseInt(split[3]),
                            split[4],
                            split[5],
                            Integer.parseInt(split[6]),
                            Integer.parseInt(split[7]),
                            split[8]);

                    IndexQueue indexQueue = new IndexQueue(ipFileRelationshipPK,
                            IP_FILE_RELATIONSHIP_TYPE,
                            "I",
                            new Date());
                    return indexQueue;
                },
                fileRelationshipsRepository.listFileRelationshipPK()
        );
        addFailRecordsToIndexQueue(util);
    }

    @Override
    @Transactional
    public void addFailNiceClassesToIndexQueue() {

        Util<IpMarkNiceClassesPK> util = new Util<>(
                IpMarkNiceClasses.class,
                "pk",
                new IpMarkNiceClassesPkBridge(),
                docPK -> {

                    String[] split = docPK.split("/");
                    if (split.length != 6) {
                        throw new RuntimeException("index doesn't contain correct pk");
                    }

                    IpMarkNiceClassesPK ipMarkNiceClassesPK = new IpMarkNiceClassesPK(
                            split[0],
                            split[1],
                            Integer.parseInt(split[2]),
                            Integer.parseInt(split[3]),
                            Long.parseLong(split[4]),
                            split[5]);

                    IndexQueue indexQueue = new IndexQueue(ipMarkNiceClassesPK,
                            IP_MARK_NICE_TYPE,
                            "I",
                            new Date());
                    return indexQueue;
                },
                niceClassesRepository.listNiceClassesPK()
        );
        addFailRecordsToIndexQueue(util);
    }

    @Override
    @Transactional
    public void addFailViennaClassesToIndexQueue() {

        Util<IpLogoViennaClassesPK> util = new Util<>(
                IpLogoViennaClasses.class,
                "pk",
                new IpLogoViennaClassesPkBridge(),
                docPK -> {

                    String[] split = docPK.split("/");
                    if (split.length != 7) {
                        throw new RuntimeException("index doesn't contain correct pk");
                    }

                    IpLogoViennaClassesPK ipLogoViennaClassesPK = new IpLogoViennaClassesPK(
                            split[0],
                            split[1],
                            Integer.parseInt(split[2]),
                            Integer.parseInt(split[3]),
                            Long.parseLong(split[4]),
                            Long.parseLong(split[5]),
                            Long.parseLong(split[6]));

                    IndexQueue indexQueue = new IndexQueue(ipLogoViennaClassesPK,
                            IP_LOGO_VIENNA_CLASSES_TYPE,
                            "I",
                            new Date());
                    return indexQueue;
                },
                viennaClassesRepository.listLogoViennaClassesPK()
        );
        addFailRecordsToIndexQueue(util);
    }

    @Override
    @Transactional
    public void addFailPersonAddressesToIndexQueue() {

        Util<IpPersonAddressesPK> util = new Util<>(
                IpPersonAddresses.class,
                "pk",
                new IpPersonAddressPkBridge(),
                docPK -> {

                    String[] split = docPK.split("/");
                    if (split.length != 2) {
                        throw new RuntimeException("index doesn't contain correct pk");
                    }

                    IpPersonAddressesPK ipPersonAddressesPK = new IpPersonAddressesPK(
                            Integer.parseInt(split[0]),
                            Integer.parseInt(split[1]));

                    IndexQueue indexQueue = new IndexQueue(ipPersonAddressesPK,
                            IP_PERSON_ADDRESS_TYPE,
                            "I",
                            new Date());
                    return indexQueue;
                },
                personAdressesRepository.listIpPersonAddressPK()
        );
        addFailRecordsToIndexQueue(util);
    }

    @Override
    @Transactional
    public void addFailMarksToIndexQueue() {

        Util<IpFilePK> util = new Util<>(
                IpMark.class,
                "pk",
                new IpFileBridge(),
                docPK -> {

                    String[] split = docPK.split("/");
                    if (split.length != 4) {
                        throw new RuntimeException("index doesn't contain correct pk");
                    }

                    IpFilePK ipFilePK = new IpFilePK(
                            split[0],
                            split[1],
                            Integer.parseInt(split[2]),
                            Integer.parseInt(split[3]));

                    IndexQueue indexQueue = new IndexQueue(ipFilePK,
                            IP_MARK_TYPE,
                            "I",
                            new Date());
                    return indexQueue;
                },
                markRepository.listFilePK()
        );
        addFailRecordsToIndexQueue(util);
    }

    @Override
    @Transactional
    public void addFailPatentToIndexQueue() {

        Util<IpFilePK> util = new Util<>(
                IpPatent.class,
                "pk",
                new IpFileBridge(),
                docPK -> {

                    String[] split = docPK.split("/");
                    if (split.length != 4) {
                        throw new RuntimeException("index doesn't contain correct pk");
                    }

                    IpFilePK ipFilePK = new IpFilePK(
                            split[0],
                            split[1],
                            Integer.parseInt(split[2]),
                            Integer.parseInt(split[3]));

                    IndexQueue indexQueue = new IndexQueue(
                            ipFilePK,
                            IP_PATENT_TYPE,
                            "I",
                            new Date());
                    return indexQueue;
                },
                patentRepository.listFilePK()
        );
        addFailRecordsToIndexQueue(util);
    }

    @Override
    @Transactional
    public void addFailIpDocsToIndexQueue() {

        Util<IpDocPK> util = new Util<IpDocPK>(IpDoc.class,
                "pk",
                new IpDocPKBridge(),
                docPK -> {

                    String[] split = docPK.split("/");
                    if (split.length != 4) {
                        throw new RuntimeException("index doesn't contain correct pk");
                    }

                    IpDocPK ipDocPK = new IpDocPK(
                            split[0],
                            split[1],
                            Integer.parseInt(split[2]),
                            Integer.parseInt(split[3]));
                    IndexQueue indexQueue = new IndexQueue(
                            ipDocPK,
                            IP_DOC_TYPE,
                            "I",
                            new Date());
                    return indexQueue;
                },
                docRepository.listDocPK()
        );
        addFailRecordsToIndexQueue(util);
    }

    @Override
    @Transactional
    public void addFailIpUserdocsToIndexQueue() {

        Util<IpDocPK> util = new Util<IpDocPK>(IpUserdoc.class,
                "pk",
                new IpDocPKBridge(),
                docPK -> {

                    String[] split = docPK.split("/");
                    if (split.length != 4) {
                        throw new RuntimeException("index doesn't contain correct pk");
                    }

                    IpDocPK ipDocPK = new IpDocPK(
                            split[0],
                            split[1],
                            Integer.parseInt(split[2]),
                            Integer.parseInt(split[3]));
                    IndexQueue indexQueue = new IndexQueue(ipDocPK, IP_USERDOC_TYPE, "I", new Date());
                    return indexQueue;
                },
                ipUserdocRepository.listUserdocPk()
        );
        addFailRecordsToIndexQueue(util);
    }

    @Override
    @Transactional
    public void addFailIpUserdocPersonToIndexQueue() {

        Util<IpUserdocPersonPK> util = new Util<IpUserdocPersonPK>(
                IpUserdocPerson.class,
                "pk",
                new IpUserDocPersonPKBridge(),
                docPK -> {

                    String[] split = docPK.split("/");
                    if (split.length != 7) {
                        throw new RuntimeException("index doesn't contain correct pk");
                    }

                    IpUserdocPersonPK ipUserdocPersonPK = new IpUserdocPersonPK(
                            split[0],
                            split[1],
                            Integer.parseInt(split[2]),
                            Integer.parseInt(split[3]),
                            Integer.parseInt(split[4]),
                            Integer.parseInt(split[5]),
                            UserdocPersonRole.valueOf(split[6]));
                    IndexQueue indexQueue = new IndexQueue(
                            ipUserdocPersonPK,
                            IP_USERDOC_PERSON_TYPE,
                            "I",
                            new Date());
                    return indexQueue;
                },
                userdocPersonRepository.listUserdocPersonPK()
        );
        addFailRecordsToIndexQueue(util);
    }

    @Override
    @Transactional
    public void addFailIpProcToIndexQueue() {

        Util<IpProcPK> util = new Util<>(
                IpProc.class,
                "pk",
                new IpProcBridge(),
                docPK -> {

                    String[] split = docPK.split("/");
                    if (split.length != 2) {
                        throw new RuntimeException("index doesn't contain correct pk");
                    }

                    IpProcPK ipProcPK = new IpProcPK(
                            split[0],
                            Integer.parseInt(split[1]));
                    IndexQueue indexQueue = new IndexQueue(
                            ipProcPK,
                            IP_PROC_TYPE,
                            "I",
                            new Date());
                    return indexQueue;
                },
                procRepository.listIpProcPK()
        );
        addFailRecordsToIndexQueue(util);
    }

    @Override
    @Transactional
    public void addFailIpActionToIndexQueue() {

        Util<IpActionPK> util = new Util<>(
                IpAction.class,
                "pk",
                new IpActionBridge(),
                docPK -> {

                    String[] split = docPK.split("/");
                    if (split.length != 3) {
                        throw new RuntimeException("index doesn't contain correct pk");
                    }

                    IpActionPK ipActionPK = new IpActionPK(
                            split[0],
                            Integer.parseInt(split[1]),
                            Integer.parseInt(split[2]));

                    IndexQueue indexQueue = new IndexQueue(
                            ipActionPK,
                            IP_ACTION_TYPE,
                            "I",
                            new Date());
                    return indexQueue;
                },
                actionRepository.listIpActionPK()
        );
        addFailRecordsToIndexQueue(util);
    }

    @Transactional
    public void changeIndex(IndexQueue index) {
        if (index.getOperation().equalsIgnoreCase("D")) {
            log.debug("Delete a document from index " + index.getId());
            delIndexEntity(index);
        } else {
            log.debug("Insert or update a document into index " + index.getId());
            indexEntity(index);
        }
    }

    private void indexEntity(IndexQueue index) {
        switch (index.getType()) {
            case IP_PATENT_TYPE:
                try {
                    indexService.index(getIpFilePkFromIndexQueue(index), IpPatent.class);
                    index.setIndexedAt(new Date());

                    log.debug("Insert or update an IpPatent with id IpFilePK(fileSeq={}, fileTyp={}, fileSer={}, fileNbr={}) into index",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr());
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> allByFileSeqAndFileTypAndFileSerAndFileNbrAndOperation = indexQueueRepository.findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndOperationAndCheckedFalse(
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            "D");

                    if (allByFileSeqAndFileTypAndFileSerAndFileNbrAndOperation.size() > 0) {
                        log.warn("Unable to find IpPatent with id IpFilePK(fileSeq={}, fileTyp={}, fileSer={}, fileNbr={}). It is deleted!",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr());
                        index.setIndexedAt(new Date());
                    } else {
                        log.error("Unable to find IpPatent with id IpFilePK(fileSeq={}, fileTyp={}, fileSer={}, fileNbr={}). It is NOT deleted!",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr());
                        log.error(e.getMessage(), e);
                    }
                } catch (HibernateException e) {
                    log.error("Cannot index row with the identifier: IpFilePK(fileSeq={}, fileTyp={}, fileSer={}, fileNbr={})!",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr());
                    log.error(e.getMessage(), e);
                }
                break;
            case IP_MARK_TYPE:
                try {
                    indexService.index(getIpFilePkFromIndexQueue(index), IpMark.class);
                    index.setIndexedAt(new Date());

                    log.debug("Insert or update an IpMark with id IpFilePK(fileSeq={}, fileTyp={}, fileSer={}, fileNbr={}) into index",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr());
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> allByFileSeqAndFileTypAndFileSerAndFileNbrAndOperation = indexQueueRepository.findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndOperationAndCheckedFalse(
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            "D");

                    if (allByFileSeqAndFileTypAndFileSerAndFileNbrAndOperation.size() > 0) {
                        log.warn("Unable to find IpMark with id IpFilePK(fileSeq={}, fileTyp={}, fileSer={}, fileNbr={}). It is deleted!",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr());
                        index.setIndexedAt(new Date());
                    } else {
                        log.error("Unable to find IpMark with id IpFilePK(" +
                                        "fileSeq={}, " +
                                        "fileTyp={}, " +
                                        "fileSer={}, " +
                                        "fileNbr={}). " +
                                        "It is NOT deleted!",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr());
                        log.debug(e.getMessage());
                    }
                } catch (HibernateException e) {
                    log.error("Cannot index row with the identifier: " +
                                    "IpFilePK(" +
                                    "fileSeq={}, " +
                                    "fileTyp={}, " +
                                    "fileSer={}, " +
                                    "fileNbr={})!",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr());
                    log.error(e.getMessage(), e);
                }
                break;
            case IP_PERSON_ADDRESS_TYPE:
                try {
                    IpPersonAddressesPK ipPersonAddressesPK = new IpPersonAddressesPK(index.getPersonNbr(), index.getAddrNbr());
                    indexService.index(ipPersonAddressesPK, IpPersonAddresses.class);
                    index.setIndexedAt(new Date());

                    log.debug("Insert or update an IpPersonAddresses with personNbr={} and addrNbr={} into index",
                            index.getPersonNbr(),
                            index.getAddrNbr());
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> allByPersonNbrAndOperation = indexQueueRepository.findAllByPersonNbrAndAddrNbrAndOperationAndCheckedFalse(
                            index.getPersonNbr(),
                            index.getAddrNbr(),
                            "D");

                    if (allByPersonNbrAndOperation.size() > 0) {
                        log.warn("Unable to find IpPersonAddresses with personNbr={} and addrNbr={}. It is deleted!",
                                index.getPersonNbr(), index.getAddrNbr());
                        index.setIndexedAt(new Date());
                    } else {
                        log.error("Unable to find IpPersonAddresses with id={} and addrNbr={}. It is NOT deleted!",
                                index.getPersonNbr(), index.getAddrNbr());
                        log.debug(e.getMessage());
                    }
                } catch (HibernateException e) {
                    log.error("Cannot index row with the identifier: IpPersonAddressesPK( personNbr={} and addrNbr={})!",
                            index.getPersonNbr(),
                            index.getAddrNbr());
                    log.error(e.getMessage(), e);
                }
                break;
            case IP_PROC_TYPE:
                try {
                    indexService.index(getIpProcPKFromIndexQueue(index), IpProc.class);
                    index.setIndexedAt(new Date());

                    log.debug("Insert or update an IpProc with id IpFilePK(procTyp={}, procNbr={}) into index",
                            index.getProcTyp(),
                            index.getProcNbr());
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> allByProcTypAndProcNbrAndOperation = indexQueueRepository.findAllByProcTypAndProcNbrAndOperationAndCheckedFalse(
                            index.getProcTyp(),
                            index.getProcNbr(),
                            "D");
                    if (allByProcTypAndProcNbrAndOperation.size() > 0) {
                        log.warn("Unable to find a IpProc with id IpFilePK(procTyp={}, procNbr={}). It is deleted!",
                                index.getProcTyp(),
                                index.getProcNbr());
                        index.setIndexedAt(new Date());
                    } else {
                        log.error("Unable to find a IpProc with id IpFilePK(procTyp={}, procNbr={}). It is deleted!",
                                index.getProcTyp(),
                                index.getProcNbr());
                        log.debug(e.getMessage());
                    }
                } catch (HibernateException e) {
                    log.error("Cannot index row with the identifier: IpFilePK(procTyp={}, procNbr={})!",
                            index.getProcTyp(),
                            index.getProcNbr());
                    log.error(e.getMessage(), e);
                }
                break;
            case IP_ACTION_TYPE:
                try {
                    indexService.index(getIpActionPKFromIndexQueue(index), IpAction.class);
                    index.setIndexedAt(new Date());

                    log.debug("Insert or update an IpAction with id IpActionPK(procTyp={}, procNbr={}, actionNbr={}) into index",
                            index.getProcTyp(),
                            index.getProcNbr(),
                            index.getActionNbr());
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> allByProcTypAndProcNbrAndActionNbr = indexQueueRepository.findAllByProcTypAndProcNbrAndActionNbrAndOperationAndCheckedFalse(
                            index.getProcTyp(),
                            index.getProcNbr(),
                            index.getActionNbr(),
                            "D");

                    if (allByProcTypAndProcNbrAndActionNbr.size() > 0) {
                        log.warn("Unable to find an IpAction with id IpActionPK(procTyp={}, procNbr={}, actionNbr={}). It is deleted!",
                                index.getProcTyp(),
                                index.getProcNbr(),
                                index.getActionNbr());
                        index.setIndexedAt(new Date());
                    } else {
                        log.error("Unable to find IpAction with id IpActionPK(procTyp={}, procNbr={}, actionNbr={}). It is NOT deleted!",
                                index.getProcTyp(),
                                index.getProcNbr(),
                                index.getActionNbr());
                        log.debug("Stack trace :", e.fillInStackTrace());
                    }
                } catch (HibernateException e) {
                    log.error("Cannot index row with the identifier: IpActionPK(procTyp={}, procNbr={}, actionNbr={})!",
                            index.getProcTyp(),
                            index.getProcNbr(),
                            index.getActionNbr());
                    log.error(e.getMessage(), e);
                }
                break;
            case IP_LOGO_VIENNA_CLASSES_TYPE:
                try {
                    indexService.index(getIpLogoViennaClassesPKFromIndexQueue(index), IpLogoViennaClasses.class);
                    index.setIndexedAt(new Date());

                    log.debug("Insert or update an IpLogoViennaClasses with id IpLogoViennaClassesPK(" +
                                    "fileSeq={}, " +
                                    "fileTyp={}, " +
                                    "fileSer={}, " +
                                    "fileNbr={}, " +
                                    "viennaClassCode={}, " +
                                    "viennaGroupCode={}, " +
                                    "viennaElemCode={}) into index",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getViennaClassCode(),
                            index.getViennaGroupCode(),
                            index.getViennaElemCode());
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> all = indexQueueRepository.findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndViennaClassCodeAndViennaGroupCodeAndViennaElemCodeAndOperationAndCheckedFalse(
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getViennaClassCode(),
                            index.getViennaGroupCode(),
                            index.getViennaElemCode(),
                            "D");

                    if (all.size() > 0) {
                        log.warn("Unable to find an IpLogoViennaClasses with id IpLogoViennaClassesPK(" +
                                        "fileSeq={}, " +
                                        "fileTyp={}, " +
                                        "fileSer={}, " +
                                        "fileNbr={}, " +
                                        "viennaClassCode={}, " +
                                        "viennaGroupCode={}, " +
                                        "viennaElemCode={}). " +
                                        "It is deleted!",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr(),
                                index.getViennaClassCode(),
                                index.getViennaGroupCode(),
                                index.getViennaElemCode());
                        index.setIndexedAt(new Date());
                    } else {
                        log.error("Unable to find IpLogoViennaClasses with id IpLogoViennaClassesPK(" +
                                        "fileSeq={}, " +
                                        "fileTyp={}, " +
                                        "fileSer={}, " +
                                        "fileNbr={}, " +
                                        "viennaClassCode={}, " +
                                        "viennaGroupCode={}, " +
                                        "viennaElemCode={}). " +
                                        "It is NOT deleted!",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr(),
                                index.getViennaClassCode(),
                                index.getViennaGroupCode(),
                                index.getViennaElemCode());
                        log.debug(e.getMessage());
                    }
                } catch (HibernateException e) {
                    log.error("Cannot index row with the identifier: " +
                                    "IpLogoViennaClassesPK(" +
                                    "fileSeq={}, " +
                                    "fileTyp={}, " +
                                    "fileSer={}, " +
                                    "fileNbr={}, " +
                                    "viennaClassCode={}, " +
                                    "viennaGroupCode={}, " +
                                    "viennaElemCode={})!",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getViennaClassCode(),
                            index.getViennaGroupCode(),
                            index.getViennaElemCode());
                    log.error(e.getMessage(), e);
                }
                break;
            case IP_MARK_NICE_TYPE:
                try {
                    indexService.index(getIpMarkNiceClassesPKFromIndexQueue(index), IpMarkNiceClasses.class);
                    index.setIndexedAt(new Date());

                    log.debug("Insert or update an IpMarkNiceClasses with id IpLogoViennaClassesPK(" +
                                    "fileSeq={}, " +
                                    "fileTyp={}, " +
                                    "fileSer={}, " +
                                    "fileNbr={}, " +
                                    "niceClassCode={}, " +
                                    "niceClassStatusWcode={}) into index",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getNiceClassCode(),
                            index.getNiceClassStatusWcode());
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> all = indexQueueRepository.findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndNiceClassCodeAndNiceClassStatusWcodeAndOperationAndCheckedFalse(
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getNiceClassCode(),
                            index.getNiceClassStatusWcode(),
                            "D");

                    if (all.size() > 0) {
                        log.warn("Unable to find IpMarkNiceClasses with id IpLogoViennaClassesPK(" +
                                        "fileSeq={}, " +
                                        "fileTyp={}, " +
                                        "fileSer={}, " +
                                        "fileNbr={}, " +
                                        "niceClassCode={}, " +
                                        "niceClassStatusWcode={}). " +
                                        "It is deleted!",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr(),
                                index.getNiceClassCode(),
                                index.getNiceClassStatusWcode());
                        index.setIndexedAt(new Date());
                    } else {
                        log.error("Unable to find IpMarkNiceClasses with id IpLogoViennaClassesPK(" +
                                        "fileSeq={}, " +
                                        "fileTyp={}, " +
                                        "fileSer={}, " +
                                        "fileNbr={}, " +
                                        "niceClassCode={}, " +
                                        "niceClassStatusWcode={}). " +
                                        "It is NOT deleted!",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr(),
                                index.getNiceClassCode(),
                                index.getNiceClassStatusWcode());
                        log.debug(e.getMessage());
                    }
                } catch (HibernateException e) {
                    log.error("Cannot index row with the identifier: " +
                                    "IpLogoViennaClassesPK(" +
                                    "fileSeq={}, " +
                                    "fileTyp={}, " +
                                    "fileSer={}, " +
                                    "fileNbr={}, " +
                                    "niceClassCode={}, " +
                                    "niceClassStatusWcode={}).",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getNiceClassCode(),
                            index.getNiceClassStatusWcode());
                    log.error(e.getMessage(), e);
                }
                break;
            case IP_PATENT_IPC_CLASSES_TYPE:
                try {
                    indexService.index(getIpPatentIpcClassesPKFromIndexQueue(index), IpPatentIpcClasses.class);
                    index.setIndexedAt(new Date());

                    log.debug("Insert or update an IpPatentIpcClasses with id IpLogoViennaClassesPK("  +
                                    "fileSeq={}, " +
                                    "fileTyp={}, " +
                                    "fileSer={}, " +
                                    "fileNbr={}, " +
                                    "ipcEditionCode={}, " +
                                    "ipcSectionCode={}, " +
                                    "ipcClassCode={}, " +
                                    "ipcSubclassCode={}, " +
                                    "ipcGroupCode={}, " +
                                    "ipcSubgroupCode={}, " +
                                    "ipcQualificationCode={}) into index",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getIpcEditionCode(),
                            index.getIpcSectionCode(),
                            index.getIpcClassCode(),
                            index.getIpcSubclassCode(),
                            index.getIpcGroupCode(),
                            index.getIpcSubgroupCode(),
                            index.getIpcQualificationCode());
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> all = indexQueueRepository.findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndIpcEditionCodeAndIpcSectionCodeAndIpcClassCodeAndIpcSubclassCodeAndIpcGroupCodeAndIpcSubgroupCodeAndIpcQualificationCodeAndOperationAndCheckedFalse(
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getIpcEditionCode(),
                            index.getIpcSectionCode(),
                            index.getIpcClassCode(),
                            index.getIpcSubclassCode(),
                            index.getIpcGroupCode(),
                            index.getIpcSubgroupCode(),
                            index.getIpcQualificationCode(),
                            "D");

                    if (all.size() > 0) {
                        log.warn("Unable to find IpPatentIpcClasses with id IpLogoViennaClassesPK(" +
                                        "fileSeq={}, " +
                                        "fileTyp={}, " +
                                        "fileSer={}, " +
                                        "fileNbr={}, " +
                                        "ipcEditionCode={}, " +
                                        "ipcSectionCode={}, " +
                                        "ipcClassCode={}, " +
                                        "ipcSubclassCode={}, " +
                                        "ipcGroupCode={}, " +
                                        "ipcSubgroupCode={}, " +
                                        "ipcQualificationCode={}). " +
                                        "It is deleted!",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr(),
                                index.getIpcEditionCode(),
                                index.getIpcSectionCode(),
                                index.getIpcClassCode(),
                                index.getIpcSubclassCode(),
                                index.getIpcGroupCode(),
                                index.getIpcSubgroupCode(),
                                index.getIpcQualificationCode());
                        index.setIndexedAt(new Date());
                    } else {
                        log.error("Unable to find IpPatentIpcClasses with id IpLogoViennaClassesPK(" +
                                        "fileSeq={}, " +
                                        "fileTyp={}, " +
                                        "fileSer={}, " +
                                        "fileNbr={}, " +
                                        "ipcEditionCode={}, " +
                                        "ipcSectionCode={}, " +
                                        "ipcClassCode={}, " +
                                        "ipcSubclassCode={}, " +
                                        "ipcGroupCode={}, " +
                                        "ipcSubgroupCode={}, " +
                                        "ipcQualificationCode={}). " +
                                        "It is NOT deleted!",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr(),
                                index.getIpcEditionCode(),
                                index.getIpcSectionCode(),
                                index.getIpcClassCode(),
                                index.getIpcSubclassCode(),
                                index.getIpcGroupCode(),
                                index.getIpcSubgroupCode(),
                                index.getIpcQualificationCode());
                        log.debug(e.getMessage());
                    }
                } catch (HibernateException e) {
                    log.error("Cannot index row with the identifier: " +
                                    "IpLogoViennaClassesPK(" +
                                    "fileSeq={}, " +
                                    "fileTyp={}, " +
                                    "fileSer={}, " +
                                    "fileNbr={}, " +
                                    "ipcEditionCode={}, " +
                                    "ipcSectionCode={}, " +
                                    "ipcClassCode={}, " +
                                    "ipcSubclassCode={}, " +
                                    "ipcGroupCode={}, " +
                                    "ipcSubgroupCode={}, " +
                                    "ipcQualificationCode={})!",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getIpcEditionCode(),
                            index.getIpcSectionCode(),
                            index.getIpcClassCode(),
                            index.getIpcSubclassCode(),
                            index.getIpcGroupCode(),
                            index.getIpcSubgroupCode(),
                            index.getIpcQualificationCode());
                    log.error(e.getMessage(), e);
                }
                break;
            case IP_PATENT_CPC_CLASSES_TYPE:
                try {
                    indexService.index(getIpPatentCpcClassesPKFromIndexQueue(index), IpPatentCpcClasses.class);
                    index.setIndexedAt(new Date());

                    log.debug("Insert or update an IpPatentcpcClasses with id IpPatentCpcClassesPK("  +
                                    "fileSeq={}, " +
                                    "fileTyp={}, " +
                                    "fileSer={}, " +
                                    "fileNbr={}, " +
                                    "cpcEditionCode={}, " +
                                    "cpcSectionCode={}, " +
                                    "cpcClassCode={}, " +
                                    "cpcSubclassCode={}, " +
                                    "cpcGroupCode={}, " +
                                    "cpcSubgroupCode={}, " +
                                    "cpcQualificationCode={}) into index",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getCpcEditionCode(),
                            index.getCpcSectionCode(),
                            index.getCpcClassCode(),
                            index.getCpcSubclassCode(),
                            index.getCpcGroupCode(),
                            index.getCpcSubgroupCode(),
                            index.getCpcQualificationCode());
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> all = indexQueueRepository.findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndCpcEditionCodeAndCpcSectionCodeAndCpcClassCodeAndCpcSubclassCodeAndCpcGroupCodeAndCpcSubgroupCodeAndCpcQualificationCodeAndOperationAndCheckedFalse(
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getCpcEditionCode(),
                            index.getCpcSectionCode(),
                            index.getCpcClassCode(),
                            index.getCpcSubclassCode(),
                            index.getCpcGroupCode(),
                            index.getCpcSubgroupCode(),
                            index.getCpcQualificationCode(),
                            "D");

                    if (all.size() > 0) {
                        log.warn("Unable to find IpPatentCpcClasses with id IpPatentCpcClassesPK(" +
                                        "fileSeq={}, " +
                                        "fileTyp={}, " +
                                        "fileSer={}, " +
                                        "fileNbr={}, " +
                                        "cpcEditionCode={}, " +
                                        "cpcSectionCode={}, " +
                                        "cpcClassCode={}, " +
                                        "cpcSubclassCode={}, " +
                                        "cpcGroupCode={}, " +
                                        "cpcSubgroupCode={}, " +
                                        "cpcQualificationCode={}). " +
                                        "It is deleted!",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr(),
                                index.getCpcEditionCode(),
                                index.getCpcSectionCode(),
                                index.getCpcClassCode(),
                                index.getCpcSubclassCode(),
                                index.getCpcGroupCode(),
                                index.getCpcSubgroupCode(),
                                index.getCpcQualificationCode());
                        index.setIndexedAt(new Date());
                    } else {
                        log.error("Unable to find IpPatentCpcClasses with id IpPatentCpcClassesPK(" +
                                        "fileSeq={}, " +
                                        "fileTyp={}, " +
                                        "fileSer={}, " +
                                        "fileNbr={}, " +
                                        "cpcEditionCode={}, " +
                                        "cpcSectionCode={}, " +
                                        "cpcClassCode={}, " +
                                        "cpcSubclassCode={}, " +
                                        "cpcGroupCode={}, " +
                                        "cpcSubgroupCode={}, " +
                                        "cpcQualificationCode={}). " +
                                        "It is NOT deleted!",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr(),
                                index.getCpcEditionCode(),
                                index.getCpcSectionCode(),
                                index.getCpcClassCode(),
                                index.getCpcSubclassCode(),
                                index.getCpcGroupCode(),
                                index.getCpcSubgroupCode(),
                                index.getCpcQualificationCode());
                        log.debug(e.getMessage());
                    }
                } catch (HibernateException e) {
                    log.error("Cannot index row with the identifier: " +
                                    "IpLogoViennaClassesPK(" +
                                    "fileSeq={}, " +
                                    "fileTyp={}, " +
                                    "fileSer={}, " +
                                    "fileNbr={}, " +
                                    "cpcEditionCode={}, " +
                                    "cpcSectionCode={}, " +
                                    "cpcClassCode={}, " +
                                    "cpcSubclassCode={}, " +
                                    "cpcGroupCode={}, " +
                                    "cpcSubgroupCode={}, " +
                                    "cpcQualificationCode={})!",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getCpcEditionCode(),
                            index.getCpcSectionCode(),
                            index.getCpcClassCode(),
                            index.getCpcSubclassCode(),
                            index.getCpcGroupCode(),
                            index.getCpcSubgroupCode(),
                            index.getCpcQualificationCode());
                    log.error(e.getMessage(), e);
                }
                break;
            case IP_PATENT_SUMMARY_TYPE:
                try {
                    indexService.index(getIpPatentSummaryPKFromIndexQueue(index), IpPatentSummary.class);
                    index.setIndexedAt(new Date());

                    log.debug("Insert or update an IpPatentSummary with id IpPatentSummaryPK(" +
                                    "fileSeq={}, " +
                                    "fileTyp={}, " +
                                    "fileSer={}, " +
                                    "fileNbr={}, " +
                                    "languageCode={}) into index",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getLanguageCode());
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> all = indexQueueRepository.findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndLanguageCodeAndOperationAndCheckedFalse(
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getLanguageCode(),
                            "D");
                    if (all.size() > 0) {
                        log.warn("Unable to find IpPatentSummary with id IpPatentSummaryPK(" +
                                        "fileSeq={}, " +
                                        "fileTyp={}, " +
                                        "fileSer={}, " +
                                        "fileNbr={}, " +
                                        "languageCode={}). " +
                                        "It is deleted!",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr(),
                                index.getLanguageCode());
                        index.setIndexedAt(new Date());
                    } else {
                        log.error("Unable to find IpPatentSummary with id IpLogoViennaClassesPK(" +
                                        "fileSeq={}, " +
                                        "fileTyp={}, " +
                                        "fileSer={}, " +
                                        "fileNbr={}, " +
                                        "getLanguageCode={}). " +
                                        "It is NOT deleted!",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr(),
                                index.getLanguageCode());
                        log.debug(e.getMessage());
                    }
                } catch (HibernateException e) {
                    log.error("Cannot index row with the identifier: " +
                                    "IpLogoViennaClassesPK(" +
                                    "fileSeq={}, " +
                                    "fileTyp={}, " +
                                    "fileSer={}, " +
                                    "fileNbr={}, " +
                                    "getLanguageCode={})!",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getLanguageCode());
                    log.error(e.getMessage(), e);
                }
                break;
            case IP_DOC_TYPE:
                try {
                    IpDocPK ipDocPK = new IpDocPK(index.getDocOri(), index.getDocLog(), index.getDocSer(), index.getDocNbr());
                    indexService.index(ipDocPK, IpDoc.class);
                    index.setIndexedAt(new Date());

                    log.debug("Insert or update an IpDoc with docOri={}, docLog={}, docSer = {} and docNbr = {} into index",
                            index.getDocOri(),
                            index.getDocLog(),
                            index.getDocSer(),
                            index.getDocNbr());
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> all = indexQueueRepository
                            .findAllByDocOriAndDocLogAndDocSerAndDocNbrAndOperationAndCheckedFalse(
                                    index.getDocOri(),
                                    index.getDocLog(),
                                    index.getDocSer(),
                                    index.getDocNbr(),
                                    "D");

                    if (all.size() > 0) {
                        log.warn("Unable to find IpDoc with docOri={}, docLog={}, docSer = {} and docNbr = {}. It is deleted!",
                                index.getDocOri(), index.getDocLog(), index.getDocSer(), index.getDocNbr());
                        index.setIndexedAt(new Date());
                    } else {
                        log.error("Unable to find IpDoc with docOri={}, docLog={}, docSer = {} and docNbr = {}. It is NOT deleted!",
                                index.getDocOri(), index.getDocLog(), index.getDocSer(), index.getDocNbr());
                        log.debug(e.getMessage());
                    }
                } catch (HibernateException e) {
                    log.error("Cannot index row with the identifier: " +
                                    "IpDocPK(docOri={}, docLog={}, docSer = {} and docNbr = {})!",
                            index.getDocOri(), index.getDocLog(), index.getDocSer(), index.getDocNbr());
                    log.error(e.getMessage(), e);
                }
                break;
            case IP_USERDOC_TYPE:
                try {
                    IpDocPK userdocPK = new IpDocPK(index.getDocOri(), index.getDocLog(), index.getDocSer(), index.getDocNbr());
                    indexService.index(userdocPK, IpUserdoc.class);
                    index.setIndexedAt(new Date());

                    log.debug("Insert or update an IpUserdoc with docOri={}, docLog={}, docSer = {} and docNbr = {} into index",
                            index.getDocOri(),
                            index.getDocLog(),
                            index.getDocSer(),
                            index.getDocNbr());
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> all = indexQueueRepository
                            .findAllByDocOriAndDocLogAndDocSerAndDocNbrAndOperationAndCheckedFalse(
                                    index.getDocOri(),
                                    index.getDocLog(),
                                    index.getDocSer(),
                                    index.getDocNbr(),
                                    "D");

                    if (all.size() > 0) {
                        log.warn("Unable to find IpUserdoc with docOri={}, docLog={}, docSer = {} and docNbr = {}. It is deleted!",
                                index.getDocOri(), index.getDocLog(), index.getDocSer(), index.getDocNbr());
                        index.setIndexedAt(new Date());
                    } else {
                        log.error("Unable to find IpUserdoc with docOri={}, docLog={}, docSer = {} and docNbr = {}. It is NOT deleted!",
                                index.getDocOri(), index.getDocLog(), index.getDocSer(), index.getDocNbr());
                        log.debug(e.getMessage());
                    }
                } catch (HibernateException e) {
                    log.error("Cannot index IpUserdoc with the identifier: IpDocPK(docOri={}, docLog={}, docSer = {} and docNbr = {})!",
                            index.getDocOri(), index.getDocLog(), index.getDocSer(), index.getDocNbr());
                    log.error(e.getMessage(), e);
                }
                break;
            case IP_USERDOC_PERSON_TYPE:
                try {
                    IpUserdocPersonPK ipUserdocPersonPK = new IpUserdocPersonPK(index.getDocOri(),
                            index.getDocLog(),
                            index.getDocSer(),
                            index.getDocNbr(),
                            index.getPersonNbr(),
                            index.getAddrNbr(),
                            index.getRole());
                    indexService.index(ipUserdocPersonPK, IpUserdocPerson.class);
                    index.setIndexedAt(new Date());

                    log.debug("Insert or update an ipUserdocPersonPK with docOri={}, " +
                                    "docLog={}, " +
                                    "docSer = {}, " +
                                    "docNbr = {}, " +
                                    "personNbr = {} " +
                                    "addrNbr = {} and " +
                                    "role = {} into index",
                            index.getDocOri(),
                            index.getDocLog(),
                            index.getDocSer(),
                            index.getDocNbr(),
                            index.getPersonNbr(),
                            index.getAddrNbr(),
                            index.getRole());
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> all = indexQueueRepository
                            .findAllByDocOriAndDocLogAndDocSerAndDocNbrAndPersonNbrAndAddrNbrAndRoleAndOperationAndCheckedFalse(
                                    index.getDocOri(),
                                    index.getDocLog(),
                                    index.getDocSer(),
                                    index.getDocNbr(),
                                    index.getPersonNbr(),
                                    index.getAddrNbr(),
                                    index.getRole(),
                                    "D");

                    if (all.size() > 0) {
                        log.warn("Unable to find ipUserdocPersonPK with docOri={}, " +
                                        "docLog={}, " +
                                        "docSer = {}, " +
                                        "docNbr = {} " +
                                        "personNbr = {} " +
                                        "addrNbr = {} and " +
                                        "role = {}. It is deleted!",
                                index.getDocOri(),
                                index.getDocLog(),
                                index.getDocSer(),
                                index.getDocNbr(),
                                index.getPersonNbr(),
                                index.getAddrNbr(),
                                index.getRole());
                        index.setIndexedAt(new Date());
                    } else {
                        log.error("Unable to find ipUserdocPersonPK with docOri={}, " +
                                        "docLog={}, " +
                                        "docSer = {}, " +
                                        "docNbr = {}, " +
                                        "personNbr = {}, " +
                                        "addrNbr = {} and " +
                                        "role = {}. It is NOT deleted!",
                                index.getDocOri(),
                                index.getDocLog(),
                                index.getDocSer(),
                                index.getDocNbr(),
                                index.getPersonNbr(),
                                index.getAddrNbr(),
                                index.getRole());
                        log.debug(e.getMessage());
                    }
                } catch (HibernateException e) {
                    log.error("Cannot index row with the identifier: " +
                                    "ipUserdocPersonPK( " +
                                    "docOri={}, " +
                                    "docLog={}, " +
                                    "docSer = {}, " +
                                    "docNbr = {}, " +
                                    "personNbr = {}, " +
                                    "addrNbr = {}, " +
                                    "role = {})!",
                            index.getDocOri(),
                            index.getDocLog(),
                            index.getDocSer(),
                            index.getDocNbr(),
                            index.getPersonNbr(),
                            index.getAddrNbr(),
                            index.getRole());
                    log.error(e.getMessage(), e);
                }
                break;

            case IP_PATENT_LOCARNO_CLASSES_TYPE:
                try {
                    IpPatentLocarnoClassesPK ipPatentLocarnoClassesPK = new IpPatentLocarnoClassesPK(index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getLocarnoClassCode());
                    indexService.index(ipPatentLocarnoClassesPK, IpPatentLocarnoClasses.class);
                    index.setIndexedAt(new Date());

                    log.debug("Insert or update an IpPatentLocarnoClasses with id ipPatentLocarnoClassesPK(" +
                                    "fileSeq={}, " +
                                    "fileTyp={}, " +
                                    "fileSer={}, " +
                                    "fileNbr={}, " +
                                    "locarnoClassCode={}) from index",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getLocarnoClassCode());
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> all = indexQueueRepository
                            .findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndLocarnoClassCodeAndOperationAndCheckedFalse(
                                    index.getFileSeq(),
                                    index.getFileTyp(),
                                    index.getFileSer(),
                                    index.getFileNbr(),
                                    index.getLocarnoClassCode(),
                                    "D");

                    if (all.size() > 0) {
                        log.warn("Unable to find IpPatentLocarnoClasses with ipPatentLocarnoClassesPK(" +
                                        "fileSeq={}, " +
                                        "fileTyp={}, " +
                                        "fileSer={}, " +
                                        "fileNbr={}, " +
                                        "locarnoClassCode={}) from index",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr(),
                                index.getLocarnoClassCode());
                        index.setIndexedAt(new Date());
                    } else {
                        log.error("Unable to find IpPatentLocarnoClasses with IpPatentLocarnoClassesPK(" +
                                        "fileSeq={}, " +
                                        "fileTyp={}, " +
                                        "fileSer={}, " +
                                        "fileNbr={}, " +
                                        "locarnoClassCode={}) from index",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr(),
                                index.getLocarnoClassCode());
                        log.debug(e.getMessage());
                    }
                } catch (HibernateException e) {
                    log.error("Cannot index row with the identifier: " +
                                    "IpPatentLocarnoClassesPK(" +
                                    "fileSeq={}, " +
                                    "fileTyp={}, " +
                                    "fileSer={}, " +
                                    "fileNbr={}, " +
                                    "locarnoClassCode={}) from index",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getLocarnoClassCode());
                    log.error(e.getMessage(), e);
                }
                break;
            case IP_FILE_RELATIONSHIP_TYPE:
                try {
                    IpFileRelationshipPK ipFileRelationshipPK = new IpFileRelationshipPK(index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getFileSeq2(),
                            index.getFileTyp2(),
                            index.getFileSer2(),
                            index.getFileNbr2(),
                            index.getRelationshipTyp());
                    indexService.index(ipFileRelationshipPK, IpFileRelationship.class);
                    index.setIndexedAt(new Date());

                    log.debug("Insert or update an IpFileRelationshipPK with id IpFileRelationshipPK(" +
                                    "fileSeq={}, " +
                                    "fileTyp={}, " +
                                    "fileSer={}, " +
                                    "fileNbr={}, " +
                                    "fileSeq2={}, " +
                                    "fileTyp2={}, " +
                                    "fileSer2={}, " +
                                    "fileNbr2={}, " +
                                    "relationshipTyp={}) from index",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getFileSeq2(),
                            index.getFileTyp2(),
                            index.getFileSer2(),
                            index.getFileNbr2(),
                            index.getRelationshipTyp());
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> all = indexQueueRepository
                            .findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndFileSeq2AndFileTyp2AndFileSer2AndFileNbr2AndRelationshipTypAndOperationAndCheckedFalse(
                                    index.getFileSeq(),
                                    index.getFileTyp(),
                                    index.getFileSer(),
                                    index.getFileNbr(),
                                    index.getFileSeq2(),
                                    index.getFileTyp2(),
                                    index.getFileSer2(),
                                    index.getFileNbr2(),
                                    index.getRelationshipTyp(),
                                    "D");

                    if (all.size() > 0) {
                        log.warn("Unable to find IpFileRelationshipPK with id IpFileRelationshipPK(" +
                                        "fileSeq={}, " +
                                        "fileTyp={}, " +
                                        "fileSer={}, " +
                                        "fileNbr={}, " +
                                        "fileSeq2={}, " +
                                        "fileTyp2={}, " +
                                        "fileSer2={}, " +
                                        "fileNbr2={}, " +
                                        "relationshipTyp={}) from index",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr(),
                                index.getFileSeq2(),
                                index.getFileTyp2(),
                                index.getFileSer2(),
                                index.getFileNbr2(),
                                index.getRelationshipTyp());
                        index.setIndexedAt(new Date());
                    } else {
                        log.error("Unable to find IpFileRelationshipPK with id IpFileRelationshipPK(" +
                                        "fileSeq={}, " +
                                        "fileTyp={}, " +
                                        "fileSer={}, " +
                                        "fileNbr={}, " +
                                        "fileSeq2={}, " +
                                        "fileTyp2={}, " +
                                        "fileSer2={}, " +
                                        "fileNbr2={}, " +
                                        "relationshipTyp={}) from index",
                                index.getFileSeq(),
                                index.getFileTyp(),
                                index.getFileSer(),
                                index.getFileNbr(),
                                index.getFileSeq2(),
                                index.getFileTyp2(),
                                index.getFileSer2(),
                                index.getFileNbr2(),
                                index.getRelationshipTyp());
                        log.error(e.getMessage(), e);
                    }
                } catch (HibernateException e) {
                    log.error("Cannot index row with the identifier: " +
                                    "IpFileRelationshipPK(" +
                                    "fileSeq={}, " +
                                    "fileTyp={}, " +
                                    "fileSer={}, " +
                                    "fileNbr={}, " +
                                    "fileSeq2={}, " +
                                    "fileTyp2={}, " +
                                    "fileSer2={}, " +
                                    "fileNbr2={}, " +
                                    "relationshipTyp={}) from index",
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getFileSeq2(),
                            index.getFileTyp2(),
                            index.getFileSer2(),
                            index.getFileNbr2(),
                            index.getRelationshipTyp());
                    log.error(e.getMessage(), e);
                }
                break;
            case IP_MARK_ATTACHMENT_VIENNA_CLASSES_TYPE:
                try {
                    indexService.index(getIpMarkAttachmentViennaClassesPKFromIndexQueue(index), IpMarkAttachmentViennaClasses.class);
                    index.setIndexedAt(new Date());
                    indexLogger.logIpMarkAttachmentViennaClassess("Insert or update", "", index, log, Level.DEBUG);
                } catch (EntityNotFoundException e) {
                    List<IndexQueue> all = indexQueueRepository.findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndViennaClassCodeAndViennaGroupCodeAndViennaElemCodeAndAttachmentIdAndOperationAndCheckedFalse(
                            index.getFileSeq(),
                            index.getFileTyp(),
                            index.getFileSer(),
                            index.getFileNbr(),
                            index.getViennaClassCode(),
                            index.getViennaGroupCode(),
                            index.getViennaElemCode(),
                            index.getAttachmentId(),
                            "D");

                    if (all.size() > 0) {
                        indexLogger.logIpMarkAttachmentViennaClassess("Unable to find", "It's deleted", index, log, Level.WARN);
                        index.setIndexedAt(new Date());
                    } else {
                        indexLogger.logIpMarkAttachmentViennaClassess("Unable to find", "It's NOT deleted", index, log, Level.ERROR);
                        log.debug(e.getMessage());
                    }
                } catch (HibernateException e) {
                    indexLogger.logIpMarkAttachmentViennaClassess("Cannot index row", "", index, log, Level.ERROR);
                    log.error(e.getMessage(), e);
                }
                break;
        }

        index.setChecked(true);
        indexQueueRepository.save(index);
    }

    private void delIndexEntity(IndexQueue index) {
        index.setChecked(true);
        switch (index.getType()) {
            case IP_PATENT_TYPE:
                indexService.delete(getIpFilePkFromIndexQueue(index), IpPatent.class);
                index.setIndexedAt(new Date());
                indexQueueRepository.save(index);

                log.debug("Delete an IpPatent with id IpFilePK(fileSeq={}, fileTyp={}, fileSer={}, fileNbr={}) from index",
                        index.getFileSeq(),
                        index.getFileTyp(),
                        index.getFileSer(),
                        index.getFileNbr());
                break;
            case IP_MARK_TYPE:
                indexService.delete(getIpFilePkFromIndexQueue(index), IpMark.class);
                index.setIndexedAt(new Date());
                indexQueueRepository.save(index);

                log.debug("Delete an IpMark with id IpFilePK(fileSeq={}, fileTyp={}, fileSer={}, fileNbr={}) from index",
                        index.getFileSeq(),
                        index.getFileTyp(),
                        index.getFileSer(),
                        index.getFileNbr());
                break;
            case IP_PERSON_ADDRESS_TYPE:
                IpPersonAddressesPK ipPersonAddressesPK = new IpPersonAddressesPK(index.getPersonNbr(), index.getAddrNbr());
                indexService.delete(ipPersonAddressesPK, IpPersonAddresses.class);
                index.setIndexedAt(new Date());

                log.debug("Delete an ipPersonAddress with personNbr={} and addrNbr={} from index",
                        index.getPersonNbr(),
                        index.getAddrNbr());
                indexQueueRepository.save(index);
                break;
            case IP_PROC_TYPE:
                indexService.delete(getIpProcPKFromIndexQueue(index), IpProc.class);
                index.setIndexedAt(new Date());
                indexQueueRepository.save(index);

                log.debug("Delete an IpProc with id IpFilePK(procTyp={}, procNbr={}) from index",
                        index.getProcTyp(),
                        index.getProcNbr());
                break;
            case IP_ACTION_TYPE:
                indexService.delete(getIpActionPKFromIndexQueue(index), IpAction.class);
                index.setIndexedAt(new Date());
                indexQueueRepository.save(index);

                log.debug("Delete an IpAction with id IpActionPK(procTyp={}, procNbr={}, actionNbr={}) from index",
                        index.getProcTyp(),
                        index.getProcNbr(),
                        index.getActionNbr());
                break;
            case IP_LOGO_VIENNA_CLASSES_TYPE:
                indexService.delete(getIpLogoViennaClassesPKFromIndexQueue(index), IpLogoViennaClasses.class);
                index.setIndexedAt(new Date());
                indexQueueRepository.save(index);

                log.debug("Delete an IpLogoViennaClasses with id IpLogoViennaClassesPK(" +
                                "fileSeq={}, " +
                                "fileTyp={}, " +
                                "fileSer={}, " +
                                "fileNbr={}, " +
                                "viennaClassCode={}, " +
                                "viennaGroupCode={}, " +
                                "viennaElemCode={}) from index",
                        index.getFileSeq(),
                        index.getFileTyp(),
                        index.getFileSer(),
                        index.getFileNbr(),
                        index.getViennaClassCode(),
                        index.getViennaGroupCode(),
                        index.getViennaElemCode());
                break;
            case IP_MARK_NICE_TYPE:
                indexService.delete(getIpMarkNiceClassesPKFromIndexQueue(index), IpMarkNiceClasses.class);
                index.setIndexedAt(new Date());
                indexQueueRepository.save(index);

                log.debug("Delete an IpMarkNiceClasses with id IpLogoViennaClassesPK(" +
                                "fileSeq={}, " +
                                "fileTyp={}, " +
                                "fileSer={}, " +
                                "fileNbr={}, " +
                                "niceClassCode={}, " +
                                "niceClassStatusWcode={}) from index",
                        index.getFileSeq(),
                        index.getFileTyp(),
                        index.getFileSer(),
                        index.getFileNbr(),
                        index.getNiceClassCode(),
                        index.getNiceClassStatusWcode());
                break;
            case IP_PATENT_IPC_CLASSES_TYPE:
                indexService.delete(getIpPatentIpcClassesPKFromIndexQueue(index), IpPatentIpcClasses.class);
                index.setIndexedAt(new Date());
                indexQueueRepository.save(index);

                log.debug("Delete an IpPatentIpcClasses with id IpLogoViennaClassesPK("  +
                                "fileSeq={}, " +
                                "fileTyp={}, " +
                                "fileSer={}, " +
                                "fileNbr={}, " +
                                "ipcEditionCode={}, " +
                                "ipcSectionCode={}, " +
                                "ipcClassCode={}, " +
                                "ipcSubclassCode={}, " +
                                "ipcGroupCode={}, " +
                                "ipcSubgroupCode={}, " +
                                "ipcQualificationCode={}) from index",
                        index.getFileSeq(),
                        index.getFileTyp(),
                        index.getFileSer(),
                        index.getFileNbr(),
                        index.getIpcEditionCode(),
                        index.getIpcSectionCode(),
                        index.getIpcClassCode(),
                        index.getIpcSubclassCode(),
                        index.getIpcGroupCode(),
                        index.getIpcSubgroupCode(),
                        index.getIpcQualificationCode());
                break;
            case IP_PATENT_CPC_CLASSES_TYPE:
                indexService.delete(getIpPatentCpcClassesPKFromIndexQueue(index), IpPatentCpcClasses.class);
                index.setIndexedAt(new Date());
                indexQueueRepository.save(index);

                log.debug("Delete an IpPatentCpcClasses with id IpLogoViennaClassesPK("  +
                                "fileSeq={}, " +
                                "fileTyp={}, " +
                                "fileSer={}, " +
                                "fileNbr={}, " +
                                "cpcEditionCode={}, " +
                                "cpcSectionCode={}, " +
                                "cpcClassCode={}, " +
                                "cpcSubclassCode={}, " +
                                "cpcGroupCode={}, " +
                                "cpcSubgroupCode={}, " +
                                "cpcQualificationCode={}) from index",
                        index.getFileSeq(),
                        index.getFileTyp(),
                        index.getFileSer(),
                        index.getFileNbr(),
                        index.getCpcEditionCode(),
                        index.getCpcSectionCode(),
                        index.getCpcClassCode(),
                        index.getCpcSubclassCode(),
                        index.getCpcGroupCode(),
                        index.getCpcSubgroupCode(),
                        index.getCpcQualificationCode());
                break;
            case IP_PATENT_SUMMARY_TYPE:
                indexService.delete(getIpPatentSummaryPKFromIndexQueue(index), IpPatentSummary.class);
                index.setIndexedAt(new Date());
                indexQueueRepository.save(index);

                log.debug("Delete an IpPatentSummary with id IpPatentSummaryPK(" +
                                "fileSeq={}, " +
                                "fileTyp={}, " +
                                "fileSer={}, " +
                                "fileNbr={}, " +
                                "languageCode={}) from index",
                        index.getFileSeq(),
                        index.getFileTyp(),
                        index.getFileSer(),
                        index.getFileNbr(),
                        index.getLanguageCode());
                break;
            case IP_DOC_TYPE:
                IpDocPK ipDocPK = new IpDocPK(index.getDocOri(), index.getDocLog(), index.getDocSer(), index.getDocNbr());
                indexService.delete(ipDocPK, IpDoc.class);
                index.setIndexedAt(new Date());
                indexQueueRepository.save(index);
                log.debug("Delete IpDoc with docOri={}, docLog={}, docSer = {} and docNbr = {} from index",
                        index.getDocOri(),
                        index.getDocLog(),
                        index.getDocSer(),
                        index.getDocNbr());
                break;
            case IP_USERDOC_TYPE:
                IpDocPK userdocPK = new IpDocPK(index.getDocOri(), index.getDocLog(), index.getDocSer(), index.getDocNbr());
                indexService.delete(userdocPK, IpUserdoc.class);
                index.setIndexedAt(new Date());
                indexQueueRepository.save(index);
                log.debug("Delete IpUserdoc with docOri={}, docLog={}, docSer = {} and docNbr = {} from index",
                        index.getDocOri(),
                        index.getDocLog(),
                        index.getDocSer(),
                        index.getDocNbr());
                break;
            case IP_USERDOC_PERSON_TYPE:
                IpUserdocPersonPK ipUserdocPersonPK = new IpUserdocPersonPK(index.getDocOri(),
                        index.getDocLog(),
                        index.getDocSer(),
                        index.getDocNbr(),
                        index.getPersonNbr(),
                        index.getAddrNbr(),
                        index.getRole());
                indexService.delete(ipUserdocPersonPK, IpUserdocPerson.class);
                index.setIndexedAt(new Date());
                indexQueueRepository.save(index);
                log.debug("Delete ipUserdocPersonPK with docOri={}, " +
                                "docLog={}, " +
                                "docSer = {}, " +
                                "docNbr = {}, " +
                                "personNbr = {} " +
                                "addrNbr = {} and " +
                                "role = {} into index",
                        index.getDocOri(),
                        index.getDocLog(),
                        index.getDocSer(),
                        index.getDocNbr(),
                        index.getPersonNbr(),
                        index.getAddrNbr(),
                        index.getRole());
                break;
            case IP_PATENT_LOCARNO_CLASSES_TYPE:
                IpPatentLocarnoClassesPK ipPatentLocarnoClassesPK = new IpPatentLocarnoClassesPK(index.getFileSeq(),
                        index.getFileTyp(),
                        index.getFileSer(),
                        index.getFileNbr(),
                        index.getLocarnoClassCode());
                indexService.delete(ipPatentLocarnoClassesPK, IpPatentLocarnoClasses.class);
                index.setIndexedAt(new Date());
                indexQueueRepository.save(index);

                log.debug("Delete an IpPatentLocarnoClasses with id ipPatentLocarnoClassesPK(" +
                                "fileSeq={}, " +
                                "fileTyp={}, " +
                                "fileSer={}, " +
                                "fileNbr={}, " +
                                "locarnoClassCode={}) from index",
                        index.getFileSeq(),
                        index.getFileTyp(),
                        index.getFileSer(),
                        index.getFileNbr(),
                        index.getLocarnoClassCode());
                break;
            case IP_FILE_RELATIONSHIP_TYPE:
                IpFileRelationshipPK ipFileRelationshipPK = new IpFileRelationshipPK(index.getFileSeq(),
                        index.getFileTyp(),
                        index.getFileSer(),
                        index.getFileNbr(),
                        index.getFileSeq2(),
                        index.getFileTyp2(),
                        index.getFileSer2(),
                        index.getFileNbr2(),
                        index.getRelationshipTyp());
                indexService.delete(ipFileRelationshipPK, IpFileRelationship.class);
                index.setIndexedAt(new Date());
                indexQueueRepository.save(index);

                log.debug("Delete an IpFileRelationshipPK with id IpFileRelationshipPK(" +
                                "fileSeq={}, " +
                                "fileTyp={}, " +
                                "fileSer={}, " +
                                "fileNbr={}, " +
                                "fileSeq2={}, " +
                                "fileTyp2={}, " +
                                "fileSer2={}, " +
                                "fileNbr2={}, " +
                                "relationshipTyp={}) from index",
                        index.getFileSeq(),
                        index.getFileTyp(),
                        index.getFileSer(),
                        index.getFileNbr(),
                        index.getFileSeq2(),
                        index.getFileTyp2(),
                        index.getFileSer2(),
                        index.getFileNbr2(),
                        index.getRelationshipTyp());
                break;
            case IP_MARK_ATTACHMENT_VIENNA_CLASSES_TYPE:
                indexService.delete(getIpMarkAttachmentViennaClassesPKFromIndexQueue(index), IpMarkAttachmentViennaClasses.class);
                index.setIndexedAt(new Date());
                indexQueueRepository.save(index);
                indexLogger.logIpMarkAttachmentViennaClassess("Delete ", "", index, log, Level.DEBUG);
                break;
        }
    }

    private static IpFilePK getIpFilePkFromIndexQueue(IndexQueue index) {
        IpFilePK ipFilePK = new IpFilePK(index.getFileSeq(), index.getFileTyp(), index.getFileSer(), index.getFileNbr());

        return ipFilePK;
    }

    private static IpProcPK getIpProcPKFromIndexQueue(IndexQueue index) {
        IpProcPK ipProcPK = new IpProcPK(index.getProcTyp(), index.getProcNbr());

        return ipProcPK;
    }

    private static IpActionPK getIpActionPKFromIndexQueue(IndexQueue index) {
        IpActionPK ipActionPK = new IpActionPK(index.getProcTyp(), index.getProcNbr(), index.getActionNbr());

        return ipActionPK;
    }

    private static IpLogoViennaClassesPK getIpLogoViennaClassesPKFromIndexQueue(IndexQueue index) {
        IpLogoViennaClassesPK ipLogoViennaClassesPK = new IpLogoViennaClassesPK(
                index.getFileSeq(),
                index.getFileTyp(),
                index.getFileSer(),
                index.getFileNbr(),
                index.getViennaClassCode(),
                index.getViennaGroupCode(),
                index.getViennaElemCode());

        return ipLogoViennaClassesPK;
    }

    private static IpMarkAttachmentViennaClassesPK getIpMarkAttachmentViennaClassesPKFromIndexQueue(IndexQueue index) {
        IpMarkAttachmentViennaClassesPK ipLogoViennaClassesPK = new IpMarkAttachmentViennaClassesPK (
                index.getAttachmentId(),
                index.getFileSeq(),
                index.getFileTyp(),
                index.getFileSer(),
                index.getFileNbr(),
                index.getViennaClassCode(),
                index.getViennaGroupCode(),
                index.getViennaElemCode());
        return ipLogoViennaClassesPK;
    }
    private static IpMarkNiceClassesPK getIpMarkNiceClassesPKFromIndexQueue(IndexQueue index) {
        IpMarkNiceClassesPK ipMarkNiceClassesPK = new IpMarkNiceClassesPK(
                index.getFileSeq(),
                index.getFileTyp(),
                index.getFileSer(),
                index.getFileNbr(),
                index.getNiceClassCode(),
                index.getNiceClassStatusWcode());

        return ipMarkNiceClassesPK;
    }

    private static IpPatentIpcClassesPK getIpPatentIpcClassesPKFromIndexQueue(IndexQueue index) {
        IpPatentIpcClassesPK ipPatentIpcClassesPK = new IpPatentIpcClassesPK(
                index.getFileSeq(),
                index.getFileTyp(),
                index.getFileSer(),
                index.getFileNbr(),
                index.getIpcEditionCode(),
                index.getIpcSectionCode(),
                index.getIpcClassCode(),
                index.getIpcSubclassCode(),
                index.getIpcGroupCode(),
                index.getIpcSubgroupCode(),
                index.getIpcQualificationCode());

        return ipPatentIpcClassesPK;
    }

    private static IpPatentCpcClassesPK getIpPatentCpcClassesPKFromIndexQueue(IndexQueue index) {
        IpPatentCpcClassesPK ipPatentCpcClassesPK = new IpPatentCpcClassesPK(
                index.getFileSeq(),
                index.getFileTyp(),
                index.getFileSer(),
                index.getFileNbr(),
                index.getCpcEditionCode(),
                index.getCpcSectionCode(),
                index.getCpcClassCode(),
                index.getCpcSubclassCode(),
                index.getCpcGroupCode(),
                index.getCpcSubgroupCode(),
                index.getCpcQualificationCode());

        return ipPatentCpcClassesPK;
    }

    private static IpPatentSummaryPK getIpPatentSummaryPKFromIndexQueue(IndexQueue index) {
        IpPatentSummaryPK ipPatentSummaryPK = new IpPatentSummaryPK(
                index.getFileSeq(),
                index.getFileTyp(),
                index.getFileSer(),
                index.getFileNbr(),
                index.getLanguageCode());

        return ipPatentSummaryPK;
    }

    private <T> void addFailRecordsToIndexQueue(Util<T> util) {
        QueryBuilder qb = hibernateSearchService.getQueryBuilder(util.getClazz());
        Query query = qb.all().createQuery();
        FullTextQuery jpaQuery = hibernateSearchService.getFullTextQuery(query, util.getClazz());
        jpaQuery.setProjection(util.getPkFieldName());

        List<Object> resultList = jpaQuery.getResultList();
        log.info("" + resultList.size());


        Function<T, String> finctionPkToString = pk -> {
            String strPk = util.getBridge().objectToString(pk);

            return strPk;
        };

        Set<String> setLucenePks = resultList.stream().parallel()
                .map(objPK -> {
                    Object[] arrLucene = (Object[]) objPK;
                    String strPk = finctionPkToString.apply((T)arrLucene[0]);

                    return strPk;
                })
                .collect(Collectors.toSet());
        log.info("" + setLucenePks.size());

        Set<String> setDbPks = util.getDbPks().stream().parallel()
                .map(finctionPkToString)
                .collect(Collectors.toSet());
        log.info("" + setDbPks.size());

        setDbPks.removeAll(setLucenePks);
        log.info("" + setDbPks.size());

        List<IndexQueue> pKS = setDbPks.stream().parallel()
                .map(util.getFunctionStringToIndexQueue())
                .collect(Collectors.toList());

        indexQueueRepository.saveAll(pKS);
    }
    @Transactional
    public Optional<Integer> getQueueMaxId() {
        return indexQueueRepository.getMaxId();
    }
    @Transactional
    public void deleteQueueOldRecords(int id) {
        indexQueueRepository.deleteAll(id);
    }

    @Getter
    @AllArgsConstructor
    private class Util<T> {

        private final Class clazz;

        private final String pkFieldName;

        private final TwoWayFieldBridge bridge;

        private final Function<String, IndexQueue> functionStringToIndexQueue;

        private final List<T> dbPks;
    }
}
