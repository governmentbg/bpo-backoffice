package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.service.nomenclature.ConfigurationService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * User: ggeorgiev
 * Date: 06.12.2022
 * Time: 14:44
 */
@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {
    private final CfConfigurationRepository repository;
    @Override
    public void deleteConfiguration(String configCode) {
        if (repository.existsById(configCode)) {
            repository.deleteById(configCode);
        }
    }
}
