package bg.duosoft.ipas.core.model.mark;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Raya
 * 23.09.2020
 */
@Data
public class CNiceClassList implements Serializable {
    private Integer niceClassCode;
    private String alphaList;
    private String heading;
}
