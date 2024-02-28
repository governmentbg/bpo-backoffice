package bg.duosoft.ipas.rest.custommodel.search;

import bg.duosoft.ipas.rest.model.search.RSearchResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * User: Georgi
 * Date: 17.9.2020 г.
 * Time: 22:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RSearchResponse {
    private List<RSearchResult> rows;
    int totalPages;
    long totalElements;
}
