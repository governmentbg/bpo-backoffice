package bg.duosoft.ipas.core.service.sql;

import java.util.List;
import java.util.Map;

/**
 * User: Georgi
 * Date: 17.7.2020 г.
 * Time: 14:21
 */
public interface SqlService {
    int execute(String query, Map<String, Object> rq);

    List<Object[]> selectToObjectArray(String query, Map<String, Object> rq);

    List<Map<String, Object>> selectToMap(String query, Map<String, Object> rq);
}
