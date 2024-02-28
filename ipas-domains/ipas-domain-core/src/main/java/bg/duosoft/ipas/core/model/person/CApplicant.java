package bg.duosoft.ipas.core.model.person;

import lombok.Data;

import java.io.Serializable;

@Data
public class CApplicant implements Serializable {
    private static final long serialVersionUID = 4342709728151045224L;
    private String applicantNotes;
    private CPerson person;
}
