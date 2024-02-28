package bg.duosoft.ipas.core.service.payments;

import bg.duosoft.ipas.core.model.CExtLiabilityDetail;
import bg.duosoft.ipas.core.model.CExtLiabilityDetailExtended;
import bg.duosoft.ipas.core.model.payments.CNotLinkedPayment;

import java.util.Date;
import java.util.List;

public interface PaymentsService {

    public static final int PAYMENT_NOT_PROCESSED = 0;
    public static final int PAYMENT_PROCESSED = 1;

    CExtLiabilityDetail getLiabilityDetailById(Integer id);

    /**
     *
     * @param dateLastPaymentFrom
     * @param dateLastPaymentTo
     * @param responsibleUsers
     * @param start
     * @param limit
     * @param sortColumn
     * @param sortOrder
     * @param processed - null -> ne uchastva vyv filtriraneto!
     * @return
     */
    public List<CExtLiabilityDetailExtended> getLastPayments(Date dateLastPaymentFrom, Date dateLastPaymentTo, List<Integer> responsibleUsers, Integer processed, Integer start, Integer limit, String sortColumn, String sortOrder);

    public long getLastPaymentsCount(Date dateLastPaymentFrom, Date dateLastPaymentTo, List<Integer> responsibleUsers, Integer processed);

    void setLiabilityDetailAsProcessed(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, Integer id);

    public List<CNotLinkedPayment> getAllNotLinkedPayments(Date dateFrom, Date dateTo);

    public List<CNotLinkedPayment> getNotLinkedPaymentsPerResponsibleUsers(Date dateFrom, Date dateTo, List<Integer> responsibleUsers);

}
