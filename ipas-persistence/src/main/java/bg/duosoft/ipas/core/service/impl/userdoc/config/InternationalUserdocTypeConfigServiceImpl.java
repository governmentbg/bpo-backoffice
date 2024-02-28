package bg.duosoft.ipas.core.service.impl.userdoc.config;

import bg.duosoft.ipas.core.mapper.userdoc.userdoc_type.config.InternationalUserdocTypeConfigMapper;
import bg.duosoft.ipas.core.model.userdoc.config.CInternationalUserdocTypeConfig;
import bg.duosoft.ipas.core.service.userdoc.config.InternationalUserdocTypeConfigService;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.config.CfInternationalUserdocTypeConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class InternationalUserdocTypeConfigServiceImpl implements InternationalUserdocTypeConfigService {

    @Autowired
    private CfInternationalUserdocTypeConfigRepository internationalUserdocTypeConfigRepository;

    @Autowired
    private InternationalUserdocTypeConfigMapper internationalUserdocTypeConfigMapper;

    @Override
    public CInternationalUserdocTypeConfig findById(String userdocType) {
        if (Objects.isNull(userdocType)) {
            return null;
        }
        return internationalUserdocTypeConfigMapper.toCore(internationalUserdocTypeConfigRepository.findById(userdocType).orElse(null));
    }
}
