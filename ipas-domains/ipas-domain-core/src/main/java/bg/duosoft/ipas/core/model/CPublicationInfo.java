package bg.duosoft.ipas.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
public class CPublicationInfo implements Serializable {

    private String journalCode;

    private Date publicationDate;

    private String definition;

}
