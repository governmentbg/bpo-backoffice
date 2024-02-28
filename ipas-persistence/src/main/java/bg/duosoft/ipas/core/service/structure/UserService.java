package bg.duosoft.ipas.core.service.structure;

import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.integration.portal.model.core.CPortalUser;

import java.util.Date;
import java.util.List;

public interface UserService {

    List<Integer> getDepartmentAndAuthorizedByUserIds(Integer userId);

    User getUser(int id);

    User getUserByLogin(String login);

    int saveUser(User user);

    List<User> getUsersByPartOfName(String partOfName); //TODO:Dobre shte e toq method da se premesti v SimpleUserService, zashtoto toj se polzva SAMO za da generira strukturata pri tyrsene po partOfName... Problema e che togava v simpleUser-a shte mi trqbva da se vry6ta i officeStructureId-to i user-a nqma da e veche mnogo simple...

    void transferUsers(List<Integer> users, OfficeStructureId newStructureId);

    void archiveUser(User user);

    List<String> getRolesPerUser(int userId);

    List<User> selectUsersByLoginNames(List<String> userNames);

    User synchronizeUser(CPortalUser portalUser, User user);

    void updateLastUpdateDate(Date lastUpdateDate, int userId);

}
