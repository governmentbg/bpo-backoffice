package bg.duosoft.ipas.rest.client.impl;

import bg.duosoft.ipas.rest.client.SqlRestClient;
import bg.duosoft.ipas.rest.client.SqlResultRowParser;
import bg.duosoft.ipas.rest.client.proxy.SqlRestProxy;
import bg.duosoft.ipas.rest.custommodel.sql.RExecuteSqlParam;
import bg.duosoft.ipas.rest.custommodel.sql.RExecuteSqlRequest;
import bg.duosoft.ipas.rest.custommodel.sql.RSelectRowsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: Georgi
 * Date: 17.7.2020 г.
 * Time: 23:57
 */
@Service
public class SqlRestClientImpl extends RestClientBaseImpl implements SqlRestClient {
    @Autowired
    private SqlRestProxy sqlRestProxy;

    public List<Map<String, Object>> selectRowsToMap(String sql, Map<String, Object> params) {
        return callService(new RSelectRowsRequest(sql, createSqlParams(params)), sqlRestProxy::selectToMap);
    }

    public List<Object[]> selectRowsToObjectArray(String sql, Map<String, Object> params) {
        return callService(new RSelectRowsRequest(sql, createSqlParams(params)), sqlRestProxy::selectToObjectArray);
    }

    public Integer executeSql(String sql, Map<String, Object> params) {
        return callService(new RExecuteSqlRequest(sql, createSqlParams(params)), sqlRestProxy::executeSql);
    }

    public <T> List<T> selectRows(String sql, Map<String, Object> params, Class<T> cls) {
        List<Map<String, Object>> res = selectRowsToMap(sql, params);
        return res.stream().map(r -> SqlResultRowParser.getResultRowParser(r, cls)).map(r -> r.createObject()).collect(Collectors.toList());
    }

    private List<RExecuteSqlParam> createSqlParams(Map<String, Object> params) {
        List<RExecuteSqlParam> sqlParams;
        if (params == null || params.size() == 0) {
            sqlParams = null;
        } else {
            sqlParams = params.entrySet().stream().map(r -> generateParam(r.getKey(), r.getValue())).collect(Collectors.toList());
        }
        return sqlParams;
    }

    private RExecuteSqlParam generateParam(String name, Object o) {
        if (o == null) {
            return new RExecuteSqlParam(name, o, RExecuteSqlParam.PARAM_TYPE.STRING);//TODO:Tova dali moje da syzdade potencialni problemi????
        }
        if (new HashSet<>(Arrays.asList(int.class, Integer.class, byte.class, Byte.class, Short.class, short.class)).contains(o.getClass())) {
            return new RExecuteSqlParam(name, ((Number) o).intValue(), RExecuteSqlParam.PARAM_TYPE.INTEGER);
        } else if (Arrays.asList(Long.class, long.class).contains(o.getClass())) {
            return new RExecuteSqlParam(name, ((Number) o).longValue(), RExecuteSqlParam.PARAM_TYPE.LONG);
        } else if (BigDecimal.class.equals(o.getClass())) {
            return new RExecuteSqlParam(name, o, RExecuteSqlParam.PARAM_TYPE.DECIMAL);
        } else if (Arrays.asList(Double.class, double.class).contains(o.getClass())) {
            return new RExecuteSqlParam(name, BigDecimal.valueOf((double) o), RExecuteSqlParam.PARAM_TYPE.DECIMAL);
        } else if (Arrays.asList(Float.class, float.class).contains(o.getClass())) {
            return new RExecuteSqlParam(name, BigDecimal.valueOf((float) o), RExecuteSqlParam.PARAM_TYPE.DECIMAL);
        } else if (Arrays.asList(Boolean.class, boolean.class).contains(o.getClass())) {
            return new RExecuteSqlParam(name, o, RExecuteSqlParam.PARAM_TYPE.BOOLEAN);
        } else if (o.getClass().equals(Timestamp.class)) {
            return new RExecuteSqlParam(name, o, RExecuteSqlParam.PARAM_TYPE.TIMESTAMP);
        } else if (Date.class.isAssignableFrom(o.getClass())) {
            return new RExecuteSqlParam(name, o, RExecuteSqlParam.PARAM_TYPE.DATE);
        } else {
            return new RExecuteSqlParam(name, o.toString(), RExecuteSqlParam.PARAM_TYPE.STRING);
        }
    }
}
