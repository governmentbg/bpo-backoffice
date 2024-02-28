package bg.duosoft.ipas.core.model.miscellaneous;

import lombok.Data;

import java.io.Serializable;

@Data
public class CGeoCountry implements Serializable {
    private String rowVersion;
    private String countryCode;
    private String countryName;
    private String nationality;
}
