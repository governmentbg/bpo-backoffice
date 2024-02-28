package bg.duosoft.ipas.core.model.userdoc;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
public class CUserdocRegNumberChangeLog implements Serializable {
    private Integer id;
    private String docOri;
    private String docLog;
    private Integer docSer;
    private Integer docNbr;
    private String oldRegistrationNumber;
    private String newRegistrationNumber;
    private Date date;
    private String username;
}
