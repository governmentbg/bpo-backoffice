package bg.duosoft.ipas.core.service.userdoc;

import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonRole;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.enums.UserdocPersonRole;

import java.util.List;
import java.util.Map;

public interface UserdocPersonRoleService {

    List<CUserdocPersonRole> findAll();

    CUserdocPersonRole selectByRole(UserdocPersonRole role);

    Map<UserdocPersonRole, String> getPersonRolesSelectOptions(CUserdocType cUserdocType);

    CUserdocPersonRole findPersonRoleByRoleAndUserdocType(UserdocPersonRole role, String userdocType);

}
