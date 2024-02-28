package bg.duosoft.ipas.integration.portal.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortalUserSyncResult {
    private String login;
    private String username;
    private String email;
    private boolean sync;
}

