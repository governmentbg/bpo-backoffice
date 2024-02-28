package bg.duosoft.ipas.persistence.model.entity.i18n;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: ggeorgiev
 * Date: 15.12.2021
 * Time: 14:57
 */
@Table(name = "CF_LANG_PROPERTIES", schema = "EXT_CORE")
@Entity
@Data
public class LangProperties implements Serializable {
    @EmbeddedId
    private LangPropertiesPK pk;

    @Column(name = "value")
    private String value;
}
