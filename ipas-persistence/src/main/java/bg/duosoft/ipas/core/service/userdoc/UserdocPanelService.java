package bg.duosoft.ipas.core.service.userdoc;

import bg.duosoft.ipas.core.model.userdoc.CUserdocPanel;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;

import java.util.Map;

public interface UserdocPanelService {

    Map<String, String> getUserdocPanelsSelectOptions(CUserdocType cUserdocType);

    CUserdocPanel findUserdocPanelByPanelAndUserdocType(String id, String userdocType);

    CUserdocPanel findPanelById(String id);
}
