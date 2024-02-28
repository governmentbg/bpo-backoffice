package bg.duosoft.ipas.core.service.userdoc.config;

import bg.duosoft.ipas.core.model.userdoc.config.CInternationalUserdocTypeConfig;

public interface InternationalUserdocTypeConfigService {
    CInternationalUserdocTypeConfig findById(String userdocType);
}
