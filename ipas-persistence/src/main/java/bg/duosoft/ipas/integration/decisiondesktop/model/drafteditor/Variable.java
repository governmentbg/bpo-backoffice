package bg.duosoft.ipas.integration.decisiondesktop.model.drafteditor;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variable<T> {

    private String key;
    private T value;
    private VariableTypeEnum type;
    private String label;

}
