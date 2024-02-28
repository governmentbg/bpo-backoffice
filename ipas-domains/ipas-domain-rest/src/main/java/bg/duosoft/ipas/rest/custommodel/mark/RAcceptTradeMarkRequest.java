package bg.duosoft.ipas.rest.custommodel.mark;

import bg.duosoft.ipas.rest.model.util.RAttachment;
import bg.duosoft.ipas.rest.model.mark.RMark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RAcceptTradeMarkRequest {
    private RMark mark;
    private List<RAttachment> attachments;
}

