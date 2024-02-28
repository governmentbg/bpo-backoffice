package bg.duosoft.ipas.core.model.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CFileRestrictData implements Serializable {
    private String applicationSubType;
    private String applicationType;
    private String fileType;
    private Integer lawCode;
}


