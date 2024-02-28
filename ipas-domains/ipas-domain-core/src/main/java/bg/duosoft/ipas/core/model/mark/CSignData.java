package bg.duosoft.ipas.core.model.mark;

import bg.duosoft.ipas.enums.MarkSignType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class CSignData implements Serializable {
    private MarkSignType signType;
    private String markName;
    private String markTranslation;
    private String seriesDescription;
    private String markNameInOtherLang;
    private String markTransliteration;
    private String markTransliterationInOtherLang;
    private String markTranslationInOtherLang;
    private List<CMarkAttachment> attachments;
}


