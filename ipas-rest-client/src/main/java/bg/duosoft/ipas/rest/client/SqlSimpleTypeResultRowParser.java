package bg.duosoft.ipas.rest.client;

import java.util.Map;
import java.util.Set;

/**
 * User: Georgi
 * Date: 17.11.2020 г.
 * Time: 16:36
 */
public class SqlSimpleTypeResultRowParser<T> extends SqlComplexTypeResultRowParser<T> {
    private String key;
    public SqlSimpleTypeResultRowParser(Map<String, Object> row, Class<T> objectType) {
        super(row, objectType);
        Set<String> keySet = row.keySet();
        if (keySet.size() != 1) {
            throw new RuntimeException("There should be only one column in the result!");
        }
        key = row.keySet().iterator().next();
    }

    public T createObject() {
        return resultRowValueParser.getValue(key, objectType);
    }
}
