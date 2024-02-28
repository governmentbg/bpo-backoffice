package bg.duosoft.ipas.persistence.model.nonentity;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class GenerateJournalData {
    private Integer elementNbr;
    private String applicationNbr;
    private Integer registrationNbr;
}
