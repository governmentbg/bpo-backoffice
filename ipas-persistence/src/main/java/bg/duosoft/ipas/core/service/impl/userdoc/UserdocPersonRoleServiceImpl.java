package bg.duosoft.ipas.core.service.impl.userdoc;

import bg.duosoft.ipas.core.mapper.userdoc.UserdocPersonRoleMapper;
import bg.duosoft.ipas.core.mapper.userdoc.userdoc_type.UserdocToPersonRoleMapper;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonRole;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.userdoc.UserdocPersonRoleService;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocTypeToPersonRole;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocTypeToPersonRolePK;
import bg.duosoft.ipas.persistence.repository.entity.ext.CfUserdocTypeToPersonRoleRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfUserdocPersonRoleRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@LogExecutionTime
public class UserdocPersonRoleServiceImpl implements UserdocPersonRoleService {

    @Autowired
    private CfUserdocPersonRoleRepository cfUserdocPersonRoleRepository;

    @Autowired
    private CfUserdocTypeToPersonRoleRepository cfUserdocTypeToPersonRoleRepository;

    @Autowired
    private UserdocPersonRoleMapper userdocPersonRoleMapper;

    @Autowired
    private UserdocToPersonRoleMapper userdocToPersonRoleMapper;

    @Override
    public List<CUserdocPersonRole> findAll() {
        List<CfUserdocPersonRole> cfUserdocPersonRoles = cfUserdocPersonRoleRepository.findAllByOrderByName();
        List<CUserdocPersonRole> cUserdocPersonRoles = userdocPersonRoleMapper.toCoreList(cfUserdocPersonRoles);

        return cUserdocPersonRoles;
    }

    @Override
    public CUserdocPersonRole selectByRole(UserdocPersonRole role) {
        CfUserdocPersonRole result = cfUserdocPersonRoleRepository.findById(role).orElse(null);
        if (Objects.isNull(result))
            return null;

        return userdocPersonRoleMapper.toCore(result);
    }

    @Override
    public Map<UserdocPersonRole, String> getPersonRolesSelectOptions(CUserdocType cUserdocType) {
        List<CfUserdocPersonRole> roles = cfUserdocPersonRoleRepository.findAll();
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        }

        return createAndSortRolesMap(roles);
    }

    @Override
    public CUserdocPersonRole findPersonRoleByRoleAndUserdocType(UserdocPersonRole role, String userdocType) {
        if (StringUtils.isEmpty(role) || StringUtils.isEmpty(userdocType)) {
            return null;
        }

        CfUserdocTypeToPersonRole cfUserdocTypeToPersonRole = cfUserdocTypeToPersonRoleRepository.findById(new CfUserdocTypeToPersonRolePK(userdocType,role)).orElse(null);
        if (Objects.isNull(cfUserdocTypeToPersonRole)) {
            return null;
        }

        return userdocToPersonRoleMapper.toCore(cfUserdocTypeToPersonRole);
    }

    private Map<UserdocPersonRole, String> createAndSortRolesMap(List<CfUserdocPersonRole> roles) {
        roles.sort(Comparator.comparing(CfUserdocPersonRole::getName));
        Map<UserdocPersonRole, String> rolesMap = roles.stream().collect(Collectors.toMap(CfUserdocPersonRole::getRole, CfUserdocPersonRole::getName));

        return rolesMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
