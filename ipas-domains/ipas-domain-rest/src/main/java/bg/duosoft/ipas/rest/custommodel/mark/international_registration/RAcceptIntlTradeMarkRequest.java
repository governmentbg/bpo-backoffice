package bg.duosoft.ipas.rest.custommodel.mark.international_registration;

import bg.duosoft.ipas.rest.model.mark.RMark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RAcceptIntlTradeMarkRequest {
    private RMark mark;
    private boolean isFullyDivided;
}
