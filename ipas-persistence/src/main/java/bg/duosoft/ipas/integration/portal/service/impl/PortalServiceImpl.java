package bg.duosoft.ipas.integration.portal.service.impl;

import bg.duosoft.ipas.enums.PortalUserSyncStatus;
import bg.duosoft.ipas.integration.portal.mapper.PortalUserGroupMapper;
import bg.duosoft.ipas.integration.portal.mapper.PortalUserMapper;
import bg.duosoft.ipas.integration.portal.model.PortalUser;
import bg.duosoft.ipas.integration.portal.model.PortalUserGroup;
import bg.duosoft.ipas.integration.portal.model.core.CPortalUser;
import bg.duosoft.ipas.integration.portal.model.core.CPortalUserGroup;
import bg.duosoft.ipas.integration.portal.service.PortalService;
import bg.duosoft.ipas.properties.PropertyAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Service
public class PortalServiceImpl implements PortalService {

    @Autowired
    private PropertyAccess propertyAccess;

    @Autowired
    private RestTemplate portalRestTemplate;

    @Autowired
    private PortalUserMapper portalUserMapper;

    @Autowired
    private PortalUserGroupMapper portalUserGroupMapper;

    @Override
    public CPortalUserGroup selectGroupByName(String groupName) {
        try {
            String url = concatToBaseRestServiceUrl(SELECT_GROUP_BY_NAME).replace("{groupName}", groupName);
            ResponseEntity<PortalUserGroup> result = portalRestTemplate.getForEntity(url, PortalUserGroup.class);
            PortalUserGroup group = result.getBody();
            if (Objects.isNull(group))
                return null;

            return portalUserGroupMapper.toCore(group);
        } catch (RestClientException e) {
            log.error("Error reading data for portal group with name " + groupName, e);
            return null;
        }
    }

    @Override
    public List<CPortalUser> selectUsersOfGroupByGroupName(String groupName) {
        CPortalUserGroup group = selectGroupByName(groupName);
        if (Objects.isNull(group))
            return null;

        Integer userGroupId = group.getUserGroupId();
        if (Objects.isNull(userGroupId))
            return null;

        try {
            String url = concatToBaseRestServiceUrl(SELECT_USERS_BY_GROUP_ID).replace("{groupId}", String.valueOf(userGroupId));
            ResponseEntity<PortalUser[]> result = portalRestTemplate.getForEntity(url, PortalUser[].class);
            List<PortalUser> portalUsers = Objects.requireNonNull(result.getBody()).length == 0 ? null : Arrays.asList(result.getBody());
            if (CollectionUtils.isEmpty(portalUsers))
                return null;


            List<CPortalUser> cPortalUsers = portalUserMapper.toCoreList(portalUsers);
            portalUserMapper.setSyncStatuses(cPortalUsers,groupName);
            return cPortalUsers;
        } catch (RestClientException e) {
            log.error("Error getting users data for portal group with name " + groupName, e);
            return null;
        }
    }

    @Override
    @Cacheable(value = "portalUsersByGroup", key = "{#groupName}")
    public List<CPortalUser> selectCachedUsersOfGroupByGroupName(String groupName) {
        List<CPortalUser> cPortalUsers = selectUsersOfGroupByGroupName(groupName);
        return cPortalUsers;
    }

    @CachePut(value = "portalUsersByGroup", key = "{#groupName}")
    public List<CPortalUser> resetCachedUsersOfGroupByGroupNameCache(String groupName) {
        List<CPortalUser> cPortalUsers = selectUsersOfGroupByGroupName(groupName);
        return cPortalUsers;
    }

    @Override
    public List<CPortalUser> selectNotSynchronizedUsersByGroupName(String groupName) {
        List<CPortalUser> users = selectUsersOfGroupByGroupName(groupName);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        
        return users.stream()
                .filter(Objects::nonNull)
                .filter(user -> user.getSyncStatus() != PortalUserSyncStatus.SYNCHRONIZED)
                .collect(Collectors.toList());
    }

    private String concatToBaseRestServiceUrl(String service) {
        return propertyAccess.getPortalRestServiceUrl() + service;
    }

}
