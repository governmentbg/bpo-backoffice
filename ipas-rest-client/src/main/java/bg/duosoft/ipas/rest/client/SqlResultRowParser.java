package bg.duosoft.ipas.rest.client;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * User: Georgi
 * Date: 20.11.2020 г.
 * Time: 12:18
 */
public interface SqlResultRowParser<T> {
    static <U> SqlResultRowParser<U> getResultRowParser(Map<String, Object> row, Class<U> objectType) {
        if (objectType.isAssignableFrom(String.class) || objectType.isAssignableFrom(Timestamp.class)  || objectType.isAssignableFrom(Date.class) || objectType.isAssignableFrom(Integer.class) || objectType.isAssignableFrom(Long.class)
            || objectType.isAssignableFrom(BigDecimal.class)) {
            return new SqlSimpleTypeResultRowParser(row, objectType);
        } else {
            return new SqlComplexTypeResultRowParser(row, objectType);
        }
    }

    /**
     * returns object, generated from all the columns in the row.
     * @return
     */
    T createObject();
}
