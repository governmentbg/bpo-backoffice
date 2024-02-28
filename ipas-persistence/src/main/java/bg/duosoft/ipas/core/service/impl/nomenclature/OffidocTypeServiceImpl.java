package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.offidoc.OffidocTypeMapper;
import bg.duosoft.ipas.core.model.offidoc.COffidocType;
import bg.duosoft.ipas.core.model.offidoc.COffidocTypeTemplate;
import bg.duosoft.ipas.core.service.nomenclature.OffidocTypeService;
import bg.duosoft.ipas.core.validation.config.IpasValidatorDefinition;
import bg.duosoft.ipas.core.validation.offidoc.AddOffidocTemplateValidator;
import bg.duosoft.ipas.core.validation.offidoc.ChangeOffidocDefaultTemplateValidator;
import bg.duosoft.ipas.core.validation.offidoc.DeleteOffidocTemplateValidator;
import bg.duosoft.ipas.enums.OffidocTypeTemplateConfig;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfOffidocType;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfOffidocTypeRepository;
import bg.duosoft.ipas.util.filter.OffidocTypeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class OffidocTypeServiceImpl implements OffidocTypeService {

    @Autowired
    private CfOffidocTypeRepository cfOffidocTypeRepository;

    @Autowired
    private OffidocTypeMapper offidocTypeMapper;

    @Override
    public COffidocType selectById(String offidocType) {
        if(StringUtils.isEmpty(offidocType))
            return null;

        CfOffidocType cfOffidocType = cfOffidocTypeRepository.findById(offidocType).orElse(null);
        if(Objects.isNull(cfOffidocType))
            return null;

        return offidocTypeMapper.toCore(cfOffidocType);
    }

    @Override
    public List<COffidocType> selectOffidocTypes(OffidocTypeFilter filter) {
        List<CfOffidocType> offidocTypeList = cfOffidocTypeRepository.selectOffidocTypes(filter);
        return offidocTypeMapper.toCoreList(offidocTypeList);
    }

    @Override
    public Integer selectOffidocTypeCount(OffidocTypeFilter filter) {
        return cfOffidocTypeRepository.selectOffidocTypesCount(filter);
    }

    @Override
    public COffidocType saveOffidocType(COffidocType cOffidocType) {
        CfOffidocType cfOffidocType = cfOffidocTypeRepository.getOne(cOffidocType.getOffidocType());
        offidocTypeMapper.fillEntityFields(cOffidocType, cfOffidocType);
        CfOffidocType updated = cfOffidocTypeRepository.save(cfOffidocType);
        return offidocTypeMapper.toCore(updated);
    }

    @Override
    @IpasValidatorDefinition({AddOffidocTemplateValidator.class})
    public COffidocType addOffidocTemplate(String offidocType, String template) {
        COffidocType cOffidocType = selectById(offidocType);
        cOffidocType.getTemplates().add(new COffidocTypeTemplate(cOffidocType.getOffidocType(), template, OffidocTypeTemplateConfig.TEMPLATE));
        return saveOffidocType(cOffidocType);
    }

    @Override
    @IpasValidatorDefinition({DeleteOffidocTemplateValidator.class})
    public COffidocType deleteOffidocTemplate(String offidocType, String template) {
        COffidocType cOffidocType = selectById(offidocType);
        COffidocTypeTemplate deleteTemplate = cOffidocType.getTemplates().stream().filter(t -> t.getNameWFile().equals(template)).findFirst().orElse(null);
        if (Objects.nonNull(deleteTemplate)) {
        cOffidocType.getTemplates().remove(deleteTemplate);
        return saveOffidocType(cOffidocType);
        }
        return cOffidocType;
    }

    @Override
    @IpasValidatorDefinition({ChangeOffidocDefaultTemplateValidator.class})
    public COffidocType changeOffidocDefaultTemplate(String offidocType, String template) {
        COffidocType cOffidocType = selectById(offidocType);
        cOffidocType.setDefaultTemplate(template);
        return saveOffidocType(cOffidocType);
    }
}