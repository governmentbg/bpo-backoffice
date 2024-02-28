package bg.duosoft.ipas.core.model.mark;

import lombok.Data;

import java.io.Serializable;


@Data
public class CLimitationData
        implements Serializable {
    private static final long serialVersionUID = -5602068486903415407L;
    private String disclaimer;
    private String byConsent;
    private String regulations;
    private String disclaimerInOtherLang;
}


