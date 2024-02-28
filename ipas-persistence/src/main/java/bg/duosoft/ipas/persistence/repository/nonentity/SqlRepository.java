package bg.duosoft.ipas.persistence.repository.nonentity;

import java.util.List;
import java.util.Map;

/**
 * User: Georgi
 * Date: 17.7.2020 г.
 * Time: 14:27
 */
public interface SqlRepository {
    public int execute(String sql, Map<String, Object> args);
    List<Object[]> selectRowsAsObjectArray(String sql, Map<String, Object> args);
    List<Map<String, Object>> selectRowsAsMap(String sql, Map<String, Object> args);
}
