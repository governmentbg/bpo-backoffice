package bg.duosoft.ipas.core.service.structure;

import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.structure.Group;

import java.util.List;
import java.util.Map;

/**
 * User: ggeorgiev
 * Date: 24.6.2019 г.
 * Time: 16:28
 */
public interface GroupService {

    Group getGroup(int groupId);

    Map<Integer, String> getGroupsMap();

    int saveGroup(Group group);

    Map<String, String> getRolesMap();

    List<CUser> getUsersByGroup(int groupId);

    void removeUserFromGroup(Integer groupId, Integer userId);

    void addUserToGroup(Integer groupId, Integer userId);

}
