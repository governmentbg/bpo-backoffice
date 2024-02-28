package bg.duosoft.ipas.persistence.repository.entity.reception;

import bg.duosoft.ipas.persistence.model.entity.ext.reception.CfReceptionType;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;

public interface ReceptionTypeRepository extends BaseRepository<CfReceptionType,Integer> {

    CfReceptionType findByFileType(String fileType);

}
