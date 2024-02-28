package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "CF_USERDOC_EXTRA_DATA_TYPE", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfUserdocExtraDataType implements Serializable {
    @Id
    @Column(name = "CODE")
    private String code;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "TITLE_EN")
    private String titleEn;

    @Column(name = "IS_TEXT")
    private Boolean isText;

    @Column(name = "IS_NUMBER")
    private Boolean isNumber;

    @Column(name = "IS_DATE")
    private Boolean isDate;

    @Column(name = "IS_BOOLEAN")
    private Boolean isBoolean;

    @Column(name = "BOOLEAN_TEXT_TRUE")
    private String booleanTextTrue;

    @Column(name = "BOOLEAN_TEXT_FALSE")
    private String booleanTextFalse;

    @Column(name = "BOOLEAN_TEXT_TRUE_EN")
    private String booleanTextTrueEn;

    @Column(name = "BOOLEAN_TEXT_FALSE_EN")
    private String booleanTextFalseEn;
}
