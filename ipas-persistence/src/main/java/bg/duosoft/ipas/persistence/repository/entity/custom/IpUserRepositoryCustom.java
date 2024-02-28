package bg.duosoft.ipas.persistence.repository.entity.custom;

import bg.duosoft.ipas.persistence.model.entity.user.IpUser;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 24.6.2019 г.
 * Time: 17:36
 */
public interface IpUserRepositoryCustom {
    List<IpUser> getUsers(String divisionCode, String departmentCode, String sectionCode, boolean onlyInStructure, boolean onlyActive);

    int transferUsers(List<Integer> userIds, String divisionCode, String departmentCode, String sectionCode);

    long countUsersSameStructure(List<Integer> userIds, String divisionCode, String departmentCode, String sectionCode);
}
