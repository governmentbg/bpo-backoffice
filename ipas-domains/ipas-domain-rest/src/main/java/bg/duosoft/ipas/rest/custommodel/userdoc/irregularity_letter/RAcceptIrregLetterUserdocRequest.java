package bg.duosoft.ipas.rest.custommodel.userdoc.irregularity_letter;

import bg.duosoft.ipas.rest.model.userdoc.RUserdoc;
import bg.duosoft.ipas.rest.model.util.RAttachment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RAcceptIrregLetterUserdocRequest {
    private String parentDocumentNumber;
    private Integer affectedRegistrationNbr;
    private String affectedRegistrationDup;
    private String irregLetterTrantyp;
    private RUserdoc userdoc;
    private List<RAttachment> attachments;
}
