package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.SqlRepository;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.query.spi.NativeQueryImplementor;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * User: Georgi
 * Date: 17.7.2020 г.
 * Time: 14:27
 */
@Repository
public class SqlRepositoryImpl extends BaseRepositoryCustomImpl implements SqlRepository {
    public int execute(String sql, Map<String, Object> args) {
        Query q = em.createNativeQuery(sql);
        if (args != null) {
            args.entrySet().stream().forEach(e -> q.setParameter(e.getKey(), e.getValue()));
        }
        return q.executeUpdate();
    }
    public List<Object[]> selectRowsAsObjectArray(String sql, Map<String, Object> args) {
        Query q = em.createNativeQuery(sql);
        if (args != null) {
            args.entrySet().stream().forEach(e -> q.setParameter(e.getKey(), e.getValue()));
        }
        return q.getResultList();
    }

    @Override
    public List<Map<String, Object>> selectRowsAsMap(String sql, Map<String, Object> args) {
        Query q = em.createNativeQuery(sql);
        if (args != null) {
            args.entrySet().stream().forEach(e -> q.setParameter(e.getKey(), e.getValue()));
        }
        return q.unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    }
}
