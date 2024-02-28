package bg.duosoft.ipas.core.model.ebddownload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Georgi
 * Date: 24.2.2020 г.
 * Time: 12:11
 */
@Data
public class CEbdPatentSearchResult implements Serializable {
    private String title;
    private Integer statusCode;
    private String status;
    private String filingNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "Europe/Sofia")
    private Date filingDate;
    private String registrationNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "Europe/Sofia")
    private Date registrationDate;
    private Long backofficeFileNbr;
}
