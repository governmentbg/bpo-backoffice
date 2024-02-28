package bg.duosoft.ipas.core.model.reception;

import bg.duosoft.ipas.core.model.file.CFileId;
import lombok.Data;

import java.io.Serializable;

/**
 * User: Georgi
 * Date: 29.5.2020 г.
 * Time: 15:07
 */
@Data
public class CReceptionFile implements Serializable {
    private CFileId fileId;
    private String title;
    private boolean emptyTitle;//if the object's title is empty. For an example the figurative marks, sound marks and some other objects do not contain titles
    private String applicationType;
    private String applicationSubType;
}
