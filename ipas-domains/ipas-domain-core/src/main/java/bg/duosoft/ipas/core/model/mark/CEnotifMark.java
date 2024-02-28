package bg.duosoft.ipas.core.model.mark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CEnotifMark implements Serializable {
    private Integer id;
    private String transaction;
    private String transcationType;
    private String originalLanguage;
    private String originalCountry;
    private String basicRegistrationNumber;
    private String designationType;
    private  CEnotif enotif;
}
