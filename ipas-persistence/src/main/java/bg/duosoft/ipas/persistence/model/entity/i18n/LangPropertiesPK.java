package bg.duosoft.ipas.persistence.model.entity.i18n;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class LangPropertiesPK implements Serializable {

    @Column(name = "lang")
    private String lang;

    @Column(name = "[key]")
    private String key;

}
