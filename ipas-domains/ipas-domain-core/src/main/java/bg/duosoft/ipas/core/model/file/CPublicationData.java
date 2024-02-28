package bg.duosoft.ipas.core.model.file;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class CPublicationData
        implements Serializable {
    private static final long serialVersionUID = 5032881452790880512L;
    private String journalCode;
    private Date publicationDate;
    private String publicationNotes;
    private Date specialPublicationRequestDate;
    private Date specialPublicationDate;

}


