package bg.duosoft.ipas.rest.custommodel.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: Georgi
 * Date: 12.8.2020 г.
 * Time: 15:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RExecuteSqlParam {
    public static enum PARAM_TYPE {
        INTEGER,
        LONG,
        BOOLEAN,
        DECIMAL,//с плаваща запетая
        TIMESTAMP,//yyyy-MM-dd'T'HH:mm:ss.SSSZ
        DATE,//milliseconds since unix time
        STRING
    }
    private String name;
    private Object value;
    private PARAM_TYPE type;
}
