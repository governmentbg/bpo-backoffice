package bg.duosoft.ipas.core.model.userdoc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CUserdocPanel implements Serializable {
    private String panel;
    private Boolean indRecordal;
    private String name;
    private String nameEn;
    private List<CUserdocExtraDataType> extraDataTypes;
}


