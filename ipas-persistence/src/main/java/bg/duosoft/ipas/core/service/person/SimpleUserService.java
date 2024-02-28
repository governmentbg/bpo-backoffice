package bg.duosoft.ipas.core.service.person;

import bg.duosoft.ipas.core.model.person.CUser;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 26.7.2019 г.
 * Time: 16:51
 */
public interface SimpleUserService {
    List<CUser> findByUsernameLike(String username, boolean onlyActive);

    CUser findSimpleUserById(Integer id);

    /**
     *
     * @param divisionCode
     * @param departmentCode - if null, searches only by divisionCode
     * @param sectionCode - if null, searches only by divisionCode/departmentCode
     * @param onlyInStructure - if true - searches only in the structure (if divisionCode eq 01 and departmentCode/sectionCode = null, then only the users, linked directly to the division will be returned). if false - returns all the
     *                        users in the structure, including it's substructures - (if divisionCode = 01 and departmentCode/sectionCode = null, it will return all the division's users including the users of the children's departments/sections)
     * @param onlyActive - if true - returns only the active users!
     * @return
     */
    List<CUser> getUsers(String divisionCode, String departmentCode, String sectionCode, boolean onlyInStructure, boolean onlyActive);
}
