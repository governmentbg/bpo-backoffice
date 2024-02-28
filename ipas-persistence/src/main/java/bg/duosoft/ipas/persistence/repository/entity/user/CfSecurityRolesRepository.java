package bg.duosoft.ipas.persistence.repository.entity.user;

import bg.duosoft.ipas.persistence.model.entity.user.CfSecurityRoles;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;

/**
 * User: ggeorgiev
 * Date: 20.6.2019 г.
 * Time: 17:59
 */
public interface CfSecurityRolesRepository extends BaseRepository<CfSecurityRoles, String> {

//    @Query("select sr from CfSecurityRoles sr JOIN CfGroupSecurityRole gsr where gsr.cfGroupSecurityRolePK.groupId = :groupId")
//    List<CfSecurityRoles> getSecurityRolesByGroupId(int groupId);
}
