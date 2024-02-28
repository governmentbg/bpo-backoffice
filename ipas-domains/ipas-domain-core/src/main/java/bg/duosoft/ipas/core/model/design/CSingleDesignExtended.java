package bg.duosoft.ipas.core.model.design;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CSingleDesignExtended implements Serializable {

    private static final long serialVersionUID = -4985662158486518798L;
    private Long drawingNbr;
    private Boolean imageRefused;
    private Boolean imagePublished;
    private CImageViewType imageViewType;

}
