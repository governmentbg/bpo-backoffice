package bg.duosoft.ipas.core.model.mark;

import bg.duosoft.ipas.core.model.CApplicationSubType;
import bg.duosoft.ipas.core.model.design.CLocarnoClasses;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class CUserdocSingleDesign implements Serializable {
    private CDocumentId documentId;
    private CFileId fileId;
    private CApplicationSubType applicationSubType;
    private List<CLocarnoClasses> locarnoClasses;
    private String productTitle;

    public String getLocarnoClassesAsString() {
        if (Objects.isNull(this.locarnoClasses) || this.locarnoClasses.size() < 1) {
            return "";
        }
        return this.locarnoClasses.stream()
                .map(CLocarnoClasses::getLocarnoClassCode)
                .collect(Collectors.joining("; "));
    }
}


