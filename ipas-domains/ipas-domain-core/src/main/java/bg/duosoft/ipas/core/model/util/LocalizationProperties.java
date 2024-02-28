package bg.duosoft.ipas.core.model.util;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Raya
 * 17.04.2020
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class LocalizationProperties implements Serializable {

    public static final String ENGLISH_LANG = "en";
    public static final String BULGARIAN_LANG = "bg";
    public static final String BULGARIAN_OFFICE = "BG";

    private String office;
    private String language;

}
