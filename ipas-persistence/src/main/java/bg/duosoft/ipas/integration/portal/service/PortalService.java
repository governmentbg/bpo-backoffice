package bg.duosoft.ipas.integration.portal.service;

import bg.duosoft.ipas.integration.portal.model.core.CPortalUser;
import bg.duosoft.ipas.integration.portal.model.core.CPortalUserGroup;

import java.util.List;


public interface PortalService {

    String SELECT_GROUP_BY_NAME = "/usergroup/get-user-group/name/{groupName}";
    String SELECT_USERS_BY_GROUP_ID = "/user/get-user-group-users/user-group-id/{groupId}";

    CPortalUserGroup selectGroupByName(String groupName);

    List<CPortalUser> selectUsersOfGroupByGroupName(String groupName);

    List<CPortalUser> selectCachedUsersOfGroupByGroupName(String groupName);

    List<CPortalUser> resetCachedUsersOfGroupByGroupNameCache(String groupName);

    List<CPortalUser> selectNotSynchronizedUsersByGroupName(String groupName);

}
