package bg.duosoft.ipas.core.model.ebddownload;

import bg.duosoft.ipas.core.model.file.CParisPriority;
import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.core.model.patent.CPctApplicationData;
import bg.duosoft.ipas.core.model.person.CPerson;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class CEbdPatent implements Serializable {
    private String title;
    private Integer statusCode;
    private String status;
    private String filingNumber;
    private Date filingDate;
    private String registrationNumber;
    private Date registrationDate;
    private Long backofficeFileNbr;
    private List<CPerson> owners;
    private CAuthorshipData authorshipData;
    private CPctApplicationData pctApplicationData;
    private List<CParisPriority> parisPriorities;
    private String ownerNames;
    private boolean withdrawn;
    private String ipasStatus;
}
