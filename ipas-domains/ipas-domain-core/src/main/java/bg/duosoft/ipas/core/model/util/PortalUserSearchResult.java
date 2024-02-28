package bg.duosoft.ipas.core.model.util;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PortalUserSearchResult implements Serializable {
    private String login;
    private String fullName;
}
