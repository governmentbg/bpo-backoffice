package bg.duosoft.ipas.core.model.file;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class CRelationshipExtended implements Serializable {
    @NotNull
    String applicationType;
    @NotNull
    String filingNumber;
    @NotNull
    Date filingDate;
    String registrationNumber;
    Date registrationDate;
    String registrationCountry;
    Date cancellationDate;
    Date priorityDate;
    Date serveMessageDate;
    String relationshipType;
}
