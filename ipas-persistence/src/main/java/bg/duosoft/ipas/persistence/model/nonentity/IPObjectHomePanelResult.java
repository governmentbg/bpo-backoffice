package bg.duosoft.ipas.persistence.model.nonentity;

import bg.duosoft.ipas.util.general.BasicUtils;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class IPObjectHomePanelResult {
    private String fileSeq;
    private String fileTyp;
    private Integer fileSer;
    private Integer fileNbr;
    private String description;
    public String getFilingNumber() {
        return BasicUtils.createFilingNumber(fileSeq, fileTyp, fileSer, fileNbr);
    }
}