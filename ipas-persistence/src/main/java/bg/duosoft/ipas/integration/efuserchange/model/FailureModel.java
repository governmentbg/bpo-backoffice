package bg.duosoft.ipas.integration.efuserchange.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FailureModel {
    private String about;
    private String description;
    private String code;
}
