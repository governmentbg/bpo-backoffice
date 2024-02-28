package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfProcessType;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;

import java.util.List;

public interface CfProcessTypeRepository extends BaseRepository<CfProcessType, String> {

    List<CfProcessType> findAllByRelatedToWcode(Integer relatedToWcode);

}
