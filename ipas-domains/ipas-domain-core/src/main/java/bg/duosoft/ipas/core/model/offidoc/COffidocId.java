package bg.duosoft.ipas.core.model.offidoc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class COffidocId implements Serializable {
    private static final long serialVersionUID = -7412016351357444322L;
    private String offidocOrigin;
    private Integer offidocSeries;
    private Integer offidocNbr;

    public String createFilingNumber() {
        return offidocOrigin + "/" + offidocSeries + "/" + offidocNbr;
    }

}


