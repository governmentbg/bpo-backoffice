package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassViennaSect;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassViennaSectPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;

import java.util.List;

public interface CfClassViennaSectionRepository extends BaseRepository<CfClassViennaSect, CfClassViennaSectPK> {

    List<CfClassViennaSect> findAllByPk_ViennaCategoryCodeAndPk_ViennaDivisionCode(Long viennaCategory, Long viennaDivision);

    List<CfClassViennaSect> findAllByPk_ViennaCategoryCodeAndPk_ViennaDivisionCodeAndPk_ViennaSectionCode(Long viennaCategory, Long viennaDivision, Long viennaSection);

}
