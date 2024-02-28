package bg.duosoft.ipas.core.model.mark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CEnotif implements Serializable {
    private String gazno;
    private Date notDate;
    private Date pubDate;
    private String weekno;
    private String notLang;
    private Integer paidCount;
    private Integer licenceNewNameCount;
    private Integer licenceBirthCount;
    private Integer createdCount;
    private Integer processedCount;
    private Integer correctionCount;
    private Integer prolongCount;
    private Integer newBaseCount;
    private Integer restrictCount;
    private Integer newNameCount;
    private Integer deathCount;
    private Integer birthCount;
}
