package bg.duosoft.ipas.core.model.reception;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CCorrespondentType implements Serializable {
    private Integer id;
    private String name;
    private String nameEn;
}
