package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocPersonRole;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;

import java.util.List;

public interface CfUserdocPersonRoleRepository extends BaseRepository<CfUserdocPersonRole, UserdocPersonRole> {

    List<CfUserdocPersonRole> findAllByOrderByName();
}
