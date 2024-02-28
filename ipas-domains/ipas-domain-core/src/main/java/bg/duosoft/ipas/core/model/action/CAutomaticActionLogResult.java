package bg.duosoft.ipas.core.model.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User: ggeorgiev
 * Date: 05.12.2022
 * Time: 15:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CAutomaticActionLogResult implements Serializable {
    private String timerName = "unknown";
    private LocalDateTime dateStart = LocalDateTime.MIN;
    private LocalDateTime dateEnd = LocalDateTime.MAX;
    private LocalDateTime lastIpasServerStartDate;
    private Integer executionTimeSecond;
    private boolean isError;
}
