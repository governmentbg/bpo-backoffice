package bg.duosoft.ipas.core.model.mark;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Raya
 * 07.03.2019
 */
@Data
public class CMarkSeniority implements Serializable {

    private String number;
    private String markName;
    private Date registrationDate;
    private Date applicationDate;
}
