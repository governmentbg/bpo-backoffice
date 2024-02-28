package bg.duosoft.ipas.core.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CPdfAttachmentBookmark {
    private String bookmarkName;
    private Integer pageNumber;
    private Boolean bookmarkRequired;
    private Integer bookmarkOrder;
}
