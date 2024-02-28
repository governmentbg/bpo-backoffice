package bg.duosoft.ipas.core.mapper.offidoc;

import bg.duosoft.ipas.enums.OffidocTypeTemplateConfig;
import org.mapstruct.Mapper;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Mapper(componentModel = "spring")
public abstract class OffidocTypeTemplateConfigMapper {

    public OffidocTypeTemplateConfig toCore(String nameFileConfig) {
        if (!StringUtils.isEmpty(nameFileConfig)) {
            OffidocTypeTemplateConfig templateConfig = OffidocTypeTemplateConfig.selectByCode(nameFileConfig);
            if (Objects.nonNull(templateConfig))
                return templateConfig;
        }
        return null;
    }

    public String toEntity(OffidocTypeTemplateConfig templateConfig) {
        if (Objects.nonNull(templateConfig)) {
            return templateConfig.code();
        }
        return null;
    }
}
