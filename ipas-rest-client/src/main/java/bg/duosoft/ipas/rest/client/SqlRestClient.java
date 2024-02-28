package bg.duosoft.ipas.rest.client;

import java.util.List;
import java.util.Map;

/**
 * User: Georgi
 * Date: 18.7.2020 г.
 * Time: 0:02
 */
public interface SqlRestClient {
    Integer executeSql(String sql, Map<String, Object> params);
    List<Map<String, Object>> selectRowsToMap(String sql, Map<String, Object> params);
    List<Object[]> selectRowsToObjectArray(String sql, Map<String, Object> params);
    <T> List<T> selectRows(String sql, Map<String, Object> params, Class<T> cls);
}
