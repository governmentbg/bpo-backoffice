package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassViennaDivis;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassViennaDivisPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;

import java.util.List;

public interface CfClassViennaDivisionRepository extends BaseRepository<CfClassViennaDivis, CfClassViennaDivisPK> {

    List<CfClassViennaDivis> findAllByPk_ViennaCategoryCode(Long pk_viennaCategoryCode);
}
