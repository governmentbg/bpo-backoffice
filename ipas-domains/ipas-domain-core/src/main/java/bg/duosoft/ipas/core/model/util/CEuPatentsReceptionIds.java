package bg.duosoft.ipas.core.model.util;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class CEuPatentsReceptionIds implements Serializable {
    private CFileId euPatentFileId;
    private CReceptionResponse finalUserdocReceptionResponse;
}
