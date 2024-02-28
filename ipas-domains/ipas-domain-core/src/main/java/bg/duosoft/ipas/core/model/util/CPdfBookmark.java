package bg.duosoft.ipas.core.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: ggeorgiev
 * Date: 23.11.2023
 * Time: 15:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CPdfBookmark {
    private String bookmarkName;
    private Integer pageNumber;
}
