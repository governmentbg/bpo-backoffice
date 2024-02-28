package bg.duosoft.ipas.core.service.impl.payments;

import bg.duosoft.ipas.core.mapper.payments.ExtLiabilityDetailsExtendedMapper;
import bg.duosoft.ipas.core.mapper.payments.ExtLiabilityDetailsMapper;
import bg.duosoft.ipas.core.mapper.payments.NotLinkedPaymentMapper;
import bg.duosoft.ipas.core.model.CExtLiabilityDetail;
import bg.duosoft.ipas.core.model.CExtLiabilityDetailExtended;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.payments.CNotLinkedPayment;
import bg.duosoft.ipas.core.model.payments.CPaymentXmlDetail;
import bg.duosoft.ipas.core.model.userdoc.CUserdocHierarchyNode;
import bg.duosoft.ipas.core.service.payments.PaymentsService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.integration.payments.service.PaymentsIntegrationService;
import bg.duosoft.ipas.persistence.model.entity.ext.ExtLiabilityDetails;
import bg.duosoft.ipas.persistence.repository.entity.ext.ExtLiabilityDetailsRepository;
import bg.duosoft.ipas.persistence.repository.nonentity.ExtLiabilityDetailsExtendedRepository;
import bg.duosoft.ipas.util.general.BasicUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentsServiceImpl implements PaymentsService {

    @Autowired
    private ExtLiabilityDetailsRepository extLiabilityDetailsRepository;


    @Autowired
    private ExtLiabilityDetailsExtendedRepository extLiabilityDetailsExtendedRepository;

    @Autowired
    private ExtLiabilityDetailsMapper extLiabilityDetailsMapper;

    @Autowired
    private ExtLiabilityDetailsExtendedMapper extLiabilityDetailsExtendedMapper;

    @Autowired
    private PaymentsIntegrationService paymentsIntegrationService;
    @Autowired
    private UserdocService userdocService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private NotLinkedPaymentMapper notLinkedPaymentMapper;


    @Override
    public CExtLiabilityDetail getLiabilityDetailById(Integer id) {
        if (Objects.isNull(id))
            return null;

        ExtLiabilityDetails result = extLiabilityDetailsRepository.selectByLiabilityId(id);
        if (Objects.isNull(result))
            return null;

        return extLiabilityDetailsMapper.toCore(result);
    }

    public List<CExtLiabilityDetailExtended> getLastPayments(Date dateLastPaymentFrom, Date dateLastPaymentTo, List<Integer> responsibleUsers, Integer processed, Integer start, Integer limit, String sortColumn, String sortOrder) {
        return extLiabilityDetailsExtendedMapper.toCoreList(extLiabilityDetailsExtendedRepository.getLastPayments(dateLastPaymentFrom, dateLastPaymentTo, responsibleUsers, processed, start, limit, sortColumn, sortOrder));
    }

    public long getLastPaymentsCount(Date dateLastPaymentFrom, Date dateLastPaymentTo, List<Integer> responsibleUsers, Integer processed) {
        return extLiabilityDetailsExtendedRepository.getLastPaymentsCount(dateLastPaymentFrom, dateLastPaymentTo, responsibleUsers, processed);
    }

    @Override
    public void setLiabilityDetailAsProcessed(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, Integer id) {
        extLiabilityDetailsRepository.setLiabilityDetailAsProcessed(fileSeq, fileTyp, fileSer, fileNbr, id);
    }

    @Cacheable("allNotLinkedPayments")
    public List<CNotLinkedPayment> getAllNotLinkedPayments(Date dateFrom, Date dateTo) {
        List<CPaymentXmlDetail> notLinkedPayments = paymentsIntegrationService.getNotLinkedPayments(dateFrom, dateTo, null, null);
        Set<CFileId> fileIds = notLinkedPayments.stream().map(f -> f.getReferenceNumber()).map(BasicUtils::createCFileId).collect(Collectors.toSet());

        //pri pokazvane na vsichki neovbyrzani plashtaniq, ne se dobavqt userdoc-ovete kym osnovniq obekt
        return _filterNotLinkedPayments(notLinkedPayments, fileIds, new HashMap<>());
    }
    @Cacheable("notLinkedPaymentsPerResponsibleUsers")
    public List<CNotLinkedPayment> getNotLinkedPaymentsPerResponsibleUsers(Date dateFrom, Date dateTo, List<Integer> responsibleUsers) {

        List<CPaymentXmlDetail> notLinkedPayments = paymentsIntegrationService.getNotLinkedPayments(dateFrom, dateTo, null, null);

        Set<CFileId> fileIds = notLinkedPayments.stream().map(f -> f.getReferenceNumber()).map(BasicUtils::createCFileId).collect(Collectors.toSet());
        Set<CFileId> fileIdsFilteredByResponsibleUser = filterFileIds(fileIds, f -> responsibleUsers.contains(processService.selectIpObjectResponsibleUser(f)));

        //tyrsi se vyv vshicki fileIds, ne samo tezi s "pravilniq" responsible user!
        Map<CFileId, List<CUserdocHierarchyNode>> userdocsFilteredByResponsibleUser = fileIds
                .stream()
                .collect(Collectors.toMap(f -> f, f -> userdocService.getFileUserdocHierarchy(f, true)))
                .entrySet()
                .stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().stream().filter(f -> responsibleUsers.contains(f.getResponsibleUserId())).collect(Collectors.toList())))
                .filter(e -> CollectionUtils.isNotEmpty(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return _filterNotLinkedPayments(notLinkedPayments, fileIdsFilteredByResponsibleUser, userdocsFilteredByResponsibleUser);

    }

    List<CNotLinkedPayment> _filterNotLinkedPayments(List<CPaymentXmlDetail> notLinkedPayments, Set<CFileId> fileIds, Map<CFileId, List<CUserdocHierarchyNode>> userdocs) {
        List<CNotLinkedPayment> res = new ArrayList<>();
        for (CPaymentXmlDetail pdx : notLinkedPayments) {
            CFileId fileId = BasicUtils.createCFileId(pdx.getReferenceNumber());

            List<CUserdocHierarchyNode> userdocsByFileId = userdocs.get(fileId);
            if (fileIds.contains(fileId) || userdocsByFileId != null) {
                res.add(notLinkedPaymentMapper.toCore(pdx, fileId, userdocsByFileId));
            }
        }
        return res;
    }

    private Set<CFileId> filterFileIds(Set<CFileId> fileIds, Predicate<CFileId> filter) {
        return fileIds.stream().filter(filter).collect(Collectors.toSet());
    }

}
