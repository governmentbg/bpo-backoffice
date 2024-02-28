package bg.duosoft.ipas.rest.custommodel.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * User: Georgi
 * Date: 17.7.2020 г.
 * Time: 14:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RExecuteSqlRequest {
    private String sql;
    private List<RExecuteSqlParam> params;
}
