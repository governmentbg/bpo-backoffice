package bg.duosoft.ipas.persistence.model.nonentity;

import bg.duosoft.ipas.core.model.file.CFileId;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ExpiringMarkResult {
    private CFileId fileId;
    private String title;
    private Date expirationDate;
    private String statusName;

}
