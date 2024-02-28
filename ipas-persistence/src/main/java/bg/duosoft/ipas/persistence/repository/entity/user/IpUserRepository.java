package bg.duosoft.ipas.persistence.repository.entity.user;

import bg.duosoft.ipas.persistence.model.entity.user.CfSecurityRoles;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpUserRepositoryCustom;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IpUserRepository extends BaseRepository<IpUser, Integer>, IpUserRepositoryCustom {
    @Query(value = "SELECT TOP 1  USER_ID FROM IP_USER ORDER BY USER_ID DESC", nativeQuery = true)
    Integer getLastId();

    @Query(value = "select u from IpUser u where (lower(u.userName) like lower(concat('%', :username, '%'))) and (:onlyActive = false or u.indInactive = 'N')")
    List<IpUser> findByUsernameLike(@Param("username") String username, @Param("onlyActive") boolean onlyActive);

    IpUser findByLogin(String username);

    @Query(value = "select u from IpUser u join CfThisUserGroup ug on u.userId = ug.cfThisUserGroupPK.userId where ug.cfThisUserGroupPK.groupId = :groupId")
    List<IpUser> getUsersByGroup(@Param("groupId") Integer groupId);


    @Query("select sr from CfSecurityRoles sr JOIN CfGroupSecurityRole gsr ON sr.roleName = gsr.cfGroupSecurityRolePK.roleName JOIN CfThisUserGroup ug ON ug.cfThisUserGroupPK.groupId = gsr.cfGroupSecurityRolePK.groupId WHERE ug.cfThisUserGroupPK.userId = ?1")
    List<CfSecurityRoles> getSecurityRoles(int userId);

    @Query("SELECT coalesce(max(u.userId), 0) + 1 from IpUser u")
    int getNextUserId();

    @Query("SELECT u FROM IpUser u WHERE u.login IN (:loginNames)")
    List<IpUser> selectUsersByLoginNames(@Param("loginNames") List<String> loginNames);

    @Modifying
    @Query(value = "UPDATE IpUser d SET d.lastUpdateDate = ?1, d.lastUpdateUserId = ?2 WHERE d.userId = ?3")
    void updateLastUpdateDate(Date lastUpdateDate, Integer lastUpdateUserId, Integer userId);

}
