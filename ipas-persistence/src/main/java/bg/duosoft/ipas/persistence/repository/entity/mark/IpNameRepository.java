package bg.duosoft.ipas.persistence.repository.entity.mark;

import bg.duosoft.ipas.persistence.model.entity.mark.IpName;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpNameRepositoryCustom;

public interface IpNameRepository extends BaseRepository<IpName,Integer>, IpNameRepositoryCustom {

}
