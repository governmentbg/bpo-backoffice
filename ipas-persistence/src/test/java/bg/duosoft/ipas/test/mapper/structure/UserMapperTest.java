package bg.duosoft.ipas.test.mapper.structure;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.mapper.structure.UserMapper;
import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.persistence.model.entity.user.CfThisUserGroup;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.repository.entity.user.IpUserRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: ggeorgiev
 * Date: 24.6.2019 г.
 * Time: 14:47
 */
public class UserMapperTest extends TestBase {


    private static final int MOCK_USER_FULL_DATA = 4151;

    @Autowired
    private IpUserRepository ipUserRepository;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringToBooleanMapper stringToBooleanMapper;


    private IpUser originalIpUser;
    private User transformedUser;
    private IpUser transformedIpUser;


    public void init(int userId) {
        originalIpUser = ipUserRepository.getOne(userId);
        transformedUser = userMapper.toCore(originalIpUser);
        transformedIpUser = userMapper.toEntity(transformedUser);
    }



    @Test
    @Transactional
    public void transformIpUserToUserTestUserData() {
        init(MOCK_USER_FULL_DATA);
//        assertEquals(originalIpUser.getRowVersion(), transformedUser.getRowVersion());
        assertEquals(originalIpUser.getUserId(), transformedUser.getUserId());

        assertEquals(originalIpUser.getUserName(), transformedUser.getUserName());
        assertEquals(originalIpUser.getLogin(), transformedUser.getLogin());
        assertEquals(originalIpUser.getIndAdministrator(), stringToBooleanMapper.booleanToString(transformedUser.getIndAdministrator()));
        assertEquals(originalIpUser.getIndExaminer(), stringToBooleanMapper.booleanToString(transformedUser.getIndExaminer()));
        assertEquals(originalIpUser.getIndInactive(), stringToBooleanMapper.booleanToString(transformedUser.getIndInactive()));
        assertEquals(originalIpUser.getIndExternal(), stringToBooleanMapper.booleanToString(transformedUser.getIndExternal()));
        assertEquals(originalIpUser.getOfficeDepartmentCode(), transformedUser.getOfficeDepartmentCode());
        assertEquals(originalIpUser.getOfficeDivisionCode(), transformedUser.getOfficeDivisionCode());
        assertEquals(originalIpUser.getOfficeSectionCode(), transformedUser.getOfficeSectionCode());

        assertEquals(originalIpUser.getInitials(), transformedUser.getInitials());
//        assertEquals(originalIpUser.getFooterDescription(), transformedUser.getFooterDescription());
//        assertEquals(originalIpUser.getLoginPassword(), transformedUser.getLoginPassword());
//        assertEquals(originalIpUser.getLastLoginDate(), transformedUser.getLastLoginDate());
        assertEquals(originalIpUser.getEmail(), transformedUser.getEmail());
        assertEquals(originalIpUser.getTelephone(), transformedUser.getTelephone());
//        assertEquals(originalIpUser.getQtyWork(), transformedUser.getQtyWork());
//        assertEquals(originalIpUser.getIndTestUser(), stringToBooleanMapper.booleanToString(transformedUser.getIndTestUser()));
        assertEquals(originalIpUser.getFullName(), transformedUser.getFullName());
//        assertEquals(originalIpUser.getCreationDate(), transformedUser.getCreationDate());
//        assertEquals(originalIpUser.getCreationUserId(), transformedUser.getCreationUserId());
//        assertEquals(originalIpUser.getLastUpdateDate(), transformedUser.getLastUpdateDate());
//        assertEquals(originalIpUser.getLastUpdateUserId(), transformedUser.getLastUpdateUserId());
//        assertEquals(originalIpUser.getSignatureTyp(), transformedUser.getSignatureTyp());
//        assertEquals(originalIpUser.getSignatureData(), transformedUser.getSignatureData());
        if (originalIpUser.getUserGroups().size() == 0) {
            assertEquals(0, transformedUser.getGroupIds().size() );
        } else {
            for (CfThisUserGroup ug : originalIpUser.getUserGroups()) {
                assertTrue(transformedUser.getGroupIds().contains(ug.getCfThisUserGroupPK().getGroupId()));
            }
        }
    }

    @Test
    @Transactional
    public void transformIpUserToUserRevertToIpUserTestData() {
        init(MOCK_USER_FULL_DATA);
//        _assertEquals(originalIpUser, transformedIpUser, IpUser::getRowVersion);
        _assertEquals(originalIpUser, transformedIpUser, IpUser::getUserId);
        _assertEquals(originalIpUser, transformedIpUser, IpUser::getUserName);
        _assertEquals(originalIpUser, transformedIpUser, IpUser::getLogin);
        _assertEquals(originalIpUser, transformedIpUser, IpUser::getIndAdministrator);
        _assertEquals(originalIpUser, transformedIpUser, IpUser::getIndExaminer);
        _assertEquals(originalIpUser, transformedIpUser, IpUser::getIndInactive);
        _assertEquals(originalIpUser, transformedIpUser, IpUser::getIndExternal);
        _assertEquals(originalIpUser, transformedIpUser, IpUser::getOfficeDepartmentCode);
        _assertEquals(originalIpUser, transformedIpUser, IpUser::getOfficeDivisionCode);
        _assertEquals(originalIpUser, transformedIpUser, IpUser::getOfficeSectionCode);
        _assertEquals(originalIpUser, transformedIpUser, IpUser::getInitials);
//        _assertEquals(originalIpUser, transformedIpUser, IpUser::getFooterDescription);
//        _assertEquals(originalIpUser, transformedIpUser, IpUser::getLoginPassword);
//        _assertEquals(originalIpUser, transformedIpUser, IpUser::getLastLoginDate);
        _assertEquals(originalIpUser, transformedIpUser, IpUser::getEmail);
        _assertEquals(originalIpUser, transformedIpUser, IpUser::getTelephone);
//        _assertEquals(originalIpUser, transformedIpUser, IpUser::getQtyWork);
//        _assertEquals(originalIpUser, transformedIpUser, IpUser::getIndTestUser);
        _assertEquals(originalIpUser, transformedIpUser, IpUser::getFullName);
//        _assertEquals(originalIpUser, transformedIpUser, IpUser::getCreationDate);
//        _assertEquals(originalIpUser, transformedIpUser, IpUser::getCreationUserId);
//        _assertEquals(originalIpUser, transformedIpUser, IpUser::getLastUpdateDate);
//        _assertEquals(originalIpUser, transformedIpUser, IpUser::getLastUpdateUserId);
//        _assertEquals(originalIpUser, transformedIpUser, IpUser::getSignatureTyp);
//        _assertEquals(originalIpUser, transformedIpUser, IpUser::getSignatureData);





    }
    @Test
    @Transactional
    public void transformIpUserToUserRevertToIpUserTestUserGroups() {
        init(MOCK_USER_FULL_DATA);
        assertEquals(originalIpUser.getUserGroups().size(), transformedIpUser.getUserGroups().size());
        for (CfThisUserGroup ug : originalIpUser.getUserGroups()) {
            Optional<CfThisUserGroup> transformedGroup = transformedIpUser.getUserGroups().stream().filter(_ug -> ug.getCfThisUserGroupPK().getGroupId() == _ug.getCfThisUserGroupPK().getGroupId()).findFirst();
            assertTrue(transformedGroup.isPresent());
            assertEquals(ug.getCfThisUserGroupPK().getUserId(), transformedGroup.get().getCfThisUserGroupPK().getUserId());

        }

    }

}
