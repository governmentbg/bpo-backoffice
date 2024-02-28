package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.miscellaneous.ConfigParamMapper;
import bg.duosoft.ipas.core.mapper.miscellaneous.ExtConfigParamMapper;
import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.persistence.model.entity.ext.ExtConfigParam;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfConfigParam;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfConfigRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.ExtConfigParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
@Transactional
public class ConfigParamServiceImpl implements ConfigParamService {

    @Autowired
    private CfConfigRepository cfConfigRepository;

    @Autowired
    private ConfigParamMapper configParamMapper;

    @Autowired
    private ExtConfigParamRepository extConfigParamRepository;

    @Autowired
    private ExtConfigParamMapper extConfigParamMapper;

    @Override
    @Cacheable(value = "configParam", key = "{#code}")
    public CConfigParam selectConfigByCode(String code) {
        if (StringUtils.isEmpty(code))
            return null;

        CfConfigParam configParam = cfConfigRepository.findById(code).orElse(null);
        if (Objects.isNull(configParam))
            return null;

        return configParamMapper.toCore(configParam);
    }

    @Override
    @Cacheable(value = "extConfigParam", key = "{#code}")
    public CConfigParam selectExtConfigByCode(String code) {
        if (StringUtils.isEmpty(code))
            return null;

        ExtConfigParam configParam = extConfigParamRepository.findById(code).orElse(null);
        if (Objects.isNull(configParam))
            return null;

        return extConfigParamMapper.toCore(configParam);
    }
}