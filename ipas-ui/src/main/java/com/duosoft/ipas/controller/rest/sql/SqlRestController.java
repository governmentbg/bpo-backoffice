package com.duosoft.ipas.controller.rest.sql;

import bg.duosoft.ipas.core.service.sql.SqlService;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.sql.RExecuteSqlParam;
import bg.duosoft.ipas.rest.custommodel.sql.RExecuteSqlRequest;
import bg.duosoft.ipas.rest.custommodel.sql.RSelectRowsRequest;
import bg.duosoft.logging.annotation.LogExecutionArguments;
import bg.duosoft.logging.annotation.LogExecutionTime;
import bg.duosoft.logging.annotation.LoggingLevel;
import com.duosoft.ipas.controller.rest.BaseRestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.duosoft.ipas.controller.rest.BaseRestController.BASE_REST_URL;

/**
 * User: Georgi
 * Date: 17.7.2020 г.
 * Time: 14:54
 */
@RestController
@RequestMapping(BASE_REST_URL + "/sql")
@Api(tags = {"SQL"})
@LogExecutionArguments(loggingLevel = LoggingLevel.INFO)
public class SqlRestController extends BaseRestController {
    @Autowired
    private SqlService sqlService;
    @ApiOperation(value = "Select to object array")
    @PostMapping(value = "/selectToObjectArray", produces = "application/json")
    public RestApiResponse<List<Object[]>> selectToObjectArray(@RequestBody RestApiRequest<RSelectRowsRequest> rq) {
        return new RestApiResponse<>(sqlService.selectToObjectArray(rq.getData().getSql(), _createParams(rq.getData().getParams())));
    }

    @ApiOperation(value = "Select to map")
    @PostMapping(value = "/selectToMap", produces = "application/json")
    public RestApiResponse<List<Map<String, Object>>> selectToMap(@RequestBody RestApiRequest<RSelectRowsRequest> rq) {
        return new RestApiResponse<>(sqlService.selectToMap(rq.getData().getSql(), _createParams(rq.getData().getParams())));
    }


    @ApiOperation(value = "Execute native sql")
    @PostMapping(value = "/execute", produces = "application/json")
    public RestApiResponse<Integer> execute(@RequestBody RestApiRequest<RExecuteSqlRequest> rq) {
        return new RestApiResponse<>(sqlService.execute(rq.getData().getSql(), _createParams(rq.getData().getParams())));
    }


    private static DateFormat UTC_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private Map<String, Object> _createParams(List<RExecuteSqlParam> params) {
        if (CollectionUtils.isEmpty(params)) {
            return null;
        }
        Map<String, Object> res = new HashMap<>();
        for (RExecuteSqlParam p : params) {
            Object val = p.getValue();
            if (val != null) {
                if (p.getType().equals(RExecuteSqlParam.PARAM_TYPE.INTEGER)) {
                    if (Number.class.isAssignableFrom(val.getClass())) {
                        val = ((Number) val).intValue();
                    } else {
                        val = Integer.parseInt(val.toString());
                    }
                } else if (p.getType().equals(RExecuteSqlParam.PARAM_TYPE.LONG)) {
                    if (Number.class.isAssignableFrom(val.getClass())) {
                        val = ((Number) val).longValue();
                    } else {
                        val = Long.parseLong(val.toString());
                    }
                } else if (p.getType().equals(RExecuteSqlParam.PARAM_TYPE.DECIMAL)) {
                    if (!BigDecimal.class.equals(val.getClass())) {
                        val = new BigDecimal(val.toString());
                    }
                } else if (p.getType().equals(RExecuteSqlParam.PARAM_TYPE.TIMESTAMP)) {
                    if (!Timestamp.class.isAssignableFrom(val.getClass())) {
                        if (Number.class.isAssignableFrom(val.getClass())) {
                            val = new Timestamp(((Number) val).longValue());
                        } else {
                            try {
                                val = new Timestamp(UTC_DATE_FORMAT.parse(val.toString()).getTime());
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
                } else if (p.getType().equals(RExecuteSqlParam.PARAM_TYPE.DATE)) {
                    if (!Date.class.isAssignableFrom(val.getClass())) {
                        val = new Date(Long.valueOf(val.toString()));
                    }
                } else if (p.getType().equals(RExecuteSqlParam.PARAM_TYPE.BOOLEAN)) {
                    if (!val.getClass().equals(Boolean.class) && !val.getClass().equals(boolean.class)) {
                        val = Boolean.valueOf(val.toString());
                    }
                }
            }
            res.put(p.getName(), val);
        }
        return res;
    }

}
