package bg.duosoft.ipas.test.mapper.structure;

import bg.duosoft.ipas.core.mapper.structure.GroupMapper;
import bg.duosoft.ipas.core.model.structure.Group;
import bg.duosoft.ipas.persistence.model.entity.user.CfSecurityRoles;
import bg.duosoft.ipas.persistence.model.entity.user.CfThisGroup;
import bg.duosoft.ipas.persistence.repository.entity.user.CfThisGroupRepository;
import bg.duosoft.ipas.test.TestBase;
import de.danielbechler.util.Collections;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * User: ggeorgiev
 * Date: 25.7.2019 г.
 * Time: 16:52
 */
public class GroupMapperTest extends TestBase {
    @Autowired
    private CfThisGroupRepository repository;
    @Autowired
    private GroupMapper groupMapper;

    private CfThisGroup originalDbGroup;
    private Group transformedGroup;
    private CfThisGroup transformedDbGroup;
    private void init(int groupId) {
        originalDbGroup = repository.getOne(groupId);
        transformedGroup = groupMapper.toCore(originalDbGroup);
        transformedDbGroup = groupMapper.toEntity(transformedGroup);
    }

    @Test
    @Transactional
    public void testTransformCfThisGroupToCoreGroupTestData() {
        init(1);
        assertEquals(originalDbGroup.getGroupname(), transformedGroup.getGroupName());
        assertEquals(originalDbGroup.getDescription(), transformedGroup.getDescription());
        assertEquals(originalDbGroup.getGroupId(), transformedGroup.getGroupId());
        if (originalDbGroup.getGroupSecurityRoles().size() > 0) {
            assertNotNull(transformedDbGroup.getGroupSecurityRoles());
            assertEquals(originalDbGroup.getGroupSecurityRoles().size(), transformedGroup.getRoleNames().size());
            for (int i = 0; i < originalDbGroup.getGroupSecurityRoles().size(); i++) {
                CfSecurityRoles orignalRole = originalDbGroup.getGroupSecurityRoles().get(i).getSecurityRole();
                String transformedRoleName = transformedGroup.getRoleNames().get(i);
                assertEquals(orignalRole.getRoleName(), transformedRoleName);
            }
        } else {
            assertTrue(Collections.isEmpty(transformedGroup.getRoleNames()));
        }
    }

    @Test
    @Transactional
    public void testTransformCfThisGroupToCoreGroupRevertToCfThisGroupTestData() {
        init(1);
        _assertEquals(originalDbGroup, transformedDbGroup, CfThisGroup::getGroupname);
        _assertEquals(originalDbGroup, transformedDbGroup, CfThisGroup::getDescription);
        _assertEquals(originalDbGroup, transformedDbGroup, CfThisGroup::getGroupId);
        if (originalDbGroup.getGroupSecurityRoles().size() > 0) {
            assertNotNull(transformedDbGroup.getGroupSecurityRoles());
            assertEquals(originalDbGroup.getGroupSecurityRoles().size(), transformedDbGroup.getGroupSecurityRoles().size());
            for (int i = 0; i < originalDbGroup.getGroupSecurityRoles().size(); i++) {
                CfSecurityRoles orignalRole = originalDbGroup.getGroupSecurityRoles().get(i).getSecurityRole();
                CfSecurityRoles transformedRole = transformedDbGroup.getGroupSecurityRoles().get(i).getSecurityRole();
                _assertEquals(orignalRole, transformedRole, CfSecurityRoles::getRoleName);
            }
        } else {
            assertTrue(Collections.isEmpty(transformedDbGroup.getGroupSecurityRoles()));
        }
    }

}
