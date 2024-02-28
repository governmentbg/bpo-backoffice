package bg.duosoft.ipas.persistence.model.nonentity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUserdocRegNumberChangeLog {
    private String docOri;
    private String docLog;
    private Integer docSer;
    private Integer docNbr;
    private String oldRegistrationNumber;
    private String newRegistrationNumber;
    private Date date;
    private String responsibleUser;
}
