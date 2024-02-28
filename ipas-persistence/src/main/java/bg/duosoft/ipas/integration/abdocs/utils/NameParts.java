package bg.duosoft.ipas.integration.abdocs.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NameParts {
    private String firstName;
    private String middleName;
    private String lastName;
}