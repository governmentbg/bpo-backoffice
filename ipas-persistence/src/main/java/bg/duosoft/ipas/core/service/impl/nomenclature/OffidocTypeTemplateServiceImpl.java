package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.offidoc.OffidocTypeTemplateMapper;
import bg.duosoft.ipas.core.model.offidoc.COffidocTypeTemplate;
import bg.duosoft.ipas.core.service.nomenclature.OffidocTypeTemplateService;
import bg.duosoft.ipas.core.validation.config.IpasValidatorDefinition;
import bg.duosoft.ipas.core.validation.offidoc.UpdateOffidocTemplateValidator;
import bg.duosoft.ipas.enums.OffidocTypeTemplateConfig;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfOffidocTypeTemplate;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfOffidocTypeTemplatePK;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfOffidocTypeTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class OffidocTypeTemplateServiceImpl implements OffidocTypeTemplateService {

    @Autowired
    private CfOffidocTypeTemplateRepository cfOffidocTypeTemplateRepository;

    @Autowired
    private OffidocTypeTemplateMapper offidocTypeTemplateMapper;

    @Override
    public String findNameFileConfigById(String offidocTyp, String template) {

        if (Objects.isNull(offidocTyp) || Objects.isNull(template)) {
            throw new RuntimeException("Empty pk on findNameFileConfigById action!");
        }
        CfOffidocTypeTemplate cfOffidocTypeTemplate = cfOffidocTypeTemplateRepository.findById(new CfOffidocTypeTemplatePK(offidocTyp, template)).orElse(null);

        if (Objects.isNull(cfOffidocTypeTemplate)) {
            throw new RuntimeException("Offidoc template not found!");
        }
        return cfOffidocTypeTemplate.getNameFileConfig();
    }

    @Override
    public COffidocTypeTemplate selectById(String offidocTyp, String nameWFile) {
        if (Objects.isNull(offidocTyp) || Objects.isNull(nameWFile)) {
            return null;
        }

        return offidocTypeTemplateMapper.toCore(cfOffidocTypeTemplateRepository.findById(new CfOffidocTypeTemplatePK(offidocTyp, nameWFile)).orElse(null));
    }

    @Override
    @IpasValidatorDefinition({UpdateOffidocTemplateValidator.class})
    public void updateOffidocTemplateConfig(String offidocType, String nameWFile, String nameConfig) {
        COffidocTypeTemplate cOffidocTypeTemplate = selectById(offidocType,nameWFile);
        cOffidocTypeTemplate.setNameFileConfig(OffidocTypeTemplateConfig.selectByCode(nameConfig));
        saveOffidocTemplate(cOffidocTypeTemplate);
    }

    private void saveOffidocTemplate(COffidocTypeTemplate cOffidocTypeTemplate) {
        CfOffidocTypeTemplate cfOffidocTypeTemplate = offidocTypeTemplateMapper.toEntity(cOffidocTypeTemplate);
        cfOffidocTypeTemplateRepository.save(cfOffidocTypeTemplate);
    }
}
