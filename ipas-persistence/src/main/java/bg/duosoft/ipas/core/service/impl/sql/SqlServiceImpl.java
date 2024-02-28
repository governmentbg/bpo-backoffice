package bg.duosoft.ipas.core.service.impl.sql;

import bg.duosoft.ipas.core.service.impl.ServiceBaseImpl;
import bg.duosoft.ipas.core.service.sql.SqlService;
import bg.duosoft.ipas.persistence.repository.nonentity.SqlRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * User: Georgi
 * Date: 17.7.2020 г.
 * Time: 14:50
 */
@Service
@Transactional
@LogExecutionTime
public class SqlServiceImpl extends ServiceBaseImpl implements SqlService {
    @Autowired
    private SqlRepository sqlRepository;

    @Override
    public int execute(String query, Map<String, Object> rq) {
        return sqlRepository.execute(query, rq);
    }

    @Override
    public List<Object[]> selectToObjectArray(String query, Map<String, Object> rq) {
        return sqlRepository.selectRowsAsObjectArray(query, rq);
    }

    @Override
    public List<Map<String, Object>> selectToMap(String query, Map<String, Object> rq) {
        return sqlRepository.selectRowsAsMap(query, rq);
    }
}
