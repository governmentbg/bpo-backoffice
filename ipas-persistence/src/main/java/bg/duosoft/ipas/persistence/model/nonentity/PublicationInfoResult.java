package bg.duosoft.ipas.persistence.model.nonentity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PublicationInfoResult {

    private String journalCode;

    private Date publicationDate;

    private String definition;

}
