package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.util.date.DateUtils;
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
public class NotLinkedPaymentsFilter {
    public static Date DEFAULT_NOT_LINKED_PAYMENT_FROM = DateUtils.localDateToDate(LocalDate.now().minusDays(30));

    @DateTimeFormat( pattern= "dd.MM.yyyy")
    private Date dateFrom = NotLinkedPaymentsFilter.DEFAULT_NOT_LINKED_PAYMENT_FROM;
    @DateTimeFormat( pattern= "dd.MM.yyyy")
    private Date dateTo;

    public NotLinkedPaymentsFilter(Date dateFrom, Date dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}
