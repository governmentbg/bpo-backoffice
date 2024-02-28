package bg.duosoft.ipas.core.model.file;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;


@Data
public class CRegistrationId
        implements Serializable {
    private static final long serialVersionUID = 171846507580578950L;
    private String registrationType;
    private Long registrationSeries;
    private Long registrationNbr;
    private String registrationDup;
}


