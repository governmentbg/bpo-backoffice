package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.core.model.search.Sortable;
import bg.duosoft.ipas.util.filter.sorter.LastPaymentsSorterUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 14.01.2021
 * Time: 12:52
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LastPaymentsFilter implements Sortable, Pageable {
    public static Date DEFAULT_LAST_PAYMENT_FROM = DateUtils.localDateToDate(LocalDate.now().minusDays(30));
    private String sortOrder = DESC_ORDER;
    private String sortColumn = LastPaymentsSorterUtils.LAST_DATE_PAYMENT;
    @DateTimeFormat( pattern= "dd.MM.yyyy")
    private Date dateLastPaymentFrom =LastPaymentsFilter.DEFAULT_LAST_PAYMENT_FROM;
    @DateTimeFormat( pattern= "dd.MM.yyyy")
    private Date dateLastPaymentTo;

    private Integer page = this.DEFAULT_PAGE;
    private Integer pageSize = this.DEFAULT_PAGE_SIZE;

    public LastPaymentsFilter(Date dateLastPaymentFrom, Date dateLastPaymentTo) {
        this.dateLastPaymentFrom = dateLastPaymentFrom;
        this.dateLastPaymentTo = dateLastPaymentTo;
    }

    public LastPaymentsFilter(Date dateLastPaymentFrom, Date dateLastPaymentTo, Integer page, Integer pageSize) {
        this.dateLastPaymentFrom = dateLastPaymentFrom;
        this.dateLastPaymentTo = dateLastPaymentTo;
        this.page = page;
        this.pageSize = pageSize;
    }
}
