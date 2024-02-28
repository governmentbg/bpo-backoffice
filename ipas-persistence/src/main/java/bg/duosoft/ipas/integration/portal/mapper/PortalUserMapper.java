package bg.duosoft.ipas.integration.portal.mapper;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.PortalUserSyncStatus;
import bg.duosoft.ipas.enums.UserGroupName;
import bg.duosoft.ipas.integration.portal.model.PortalUser;
import bg.duosoft.ipas.integration.portal.model.core.CPortalUser;
import bg.duosoft.ipas.integration.portal.utils.PortalUserUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class PortalUserMapper extends BaseObjectMapper<PortalUser, CPortalUser> {

    @Autowired
    private UserService userService;

    public void setSyncStatuses(List<CPortalUser> portalUsers, String currentGroup){
        if(!UserGroupName.ROLE_IPAS_MANAGEMENT.getValue().equals(currentGroup)){
            return;
        }
        if (!CollectionUtils.isEmpty(portalUsers)) {
            List<String> usernames = portalUsers.stream().map(CPortalUser::getScreenName).collect(Collectors.toList());
            List<User> users = userService.selectUsersByLoginNames(usernames);
            for (CPortalUser portalUser : portalUsers) {
                String screenName = portalUser.getScreenName();
                User ipasUser = users.stream().filter(user -> user.getLogin().equals(screenName)).findAny().orElse(null);
                if (Objects.isNull(ipasUser)) {
                    portalUser.setSyncStatus(PortalUserSyncStatus.NOT_SYNCHRONIZED_NEW);
                } else {
                    if (PortalUserUtils.isUserChanged(portalUser, ipasUser)) {
                        portalUser.setSyncStatus(PortalUserSyncStatus.NOT_SYNCHRONIZED_CHANGED);
                    } else {
                        portalUser.setSyncStatus(PortalUserSyncStatus.SYNCHRONIZED);
                    }
                }
            }
        }
    }
}
