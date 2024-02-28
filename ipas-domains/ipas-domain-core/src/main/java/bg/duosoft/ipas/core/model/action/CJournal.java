package bg.duosoft.ipas.core.model.action;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
public class CJournal implements Serializable {
    private static final long serialVersionUID = -6582520682710516656L;
    private String journalName;
    private Boolean indClosed;
    private Date publicationDate;
    private Date notificationDate;
    private String journalCode;

    public boolean isJournalClosed(){
        return this.indClosed || Objects.nonNull(publicationDate);
    }
}
