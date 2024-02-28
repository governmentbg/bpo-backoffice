package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.offidoc.OffidocTypeStaticTemplateMapper;
import bg.duosoft.ipas.core.model.offidoc.COffidocTypeStaticTemplate;
import bg.duosoft.ipas.core.service.nomenclature.OffidocTypeStaticTemplateService;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfOffidocTypeStaticTemplate;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfOffidocTypeStaticTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OffidocTypeStaticTemplateServiceImpl implements OffidocTypeStaticTemplateService {

    private final CfOffidocTypeStaticTemplateRepository repository;
    private final OffidocTypeStaticTemplateMapper mapper;

    @Override
    public List<COffidocTypeStaticTemplate> selectAllTemplates(String offidocType) {
        if (!StringUtils.hasText(offidocType)) {
            return null;
        }

        List<CfOffidocTypeStaticTemplate> entities = repository.selectTemplatesByOffidocType(offidocType);
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        }

        return mapper.toCoreList(entities);
    }
}
