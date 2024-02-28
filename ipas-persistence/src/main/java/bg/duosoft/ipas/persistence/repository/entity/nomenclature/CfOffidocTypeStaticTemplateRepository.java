package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.ext.core.CfOffidocTypeStaticTemplate;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfOffidocTypeStaticTemplatePK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CfOffidocTypeStaticTemplateRepository extends BaseRepository<CfOffidocTypeStaticTemplate, CfOffidocTypeStaticTemplatePK> {

    @Query(value = "SELECT * FROM EXT_CORE.CF_OFFIDOC_TYPE_STATIC_TEMPLATE t where t.OFFIDOC_TYP = ?1 ", nativeQuery = true)
    List<CfOffidocTypeStaticTemplate> selectTemplatesByOffidocType(String offidocType);
}
