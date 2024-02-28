package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpUserRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ggeorgiev
 * Date: 24.6.2019 г.
 * Time: 17:38
 */
@Repository
@Transactional
public class IpUserRepositoryCustomImpl extends BaseRepositoryCustomImpl implements IpUserRepositoryCustom {
    public List<IpUser> getUsers(String divisionCode, String departmentCode, String sectionCode, boolean onlyInStructure, boolean onlyActive) {
        String sql = "select t from IpUser t WHERE 1 = 1";
        Map<String, Object> params = new HashMap<>();
        sql += "AND t.officeDivisionCode = :divisionCode";
        params.put("divisionCode", divisionCode);
        if (departmentCode != null) {
            sql += " AND t.officeDepartmentCode = :departmentCode";
            params.put("departmentCode", departmentCode);
        }
        if (sectionCode != null) {
            sql += " and t.officeSectionCode = :sectionCode";
            params.put("sectionCode", sectionCode);
        }
        if (onlyActive) {
            sql += " AND (t.indInactive ='N' or t.indInactive is null) ";
        }
        if (onlyInStructure) {
            if (departmentCode == null) {
                sql += " AND t.officeDepartmentCode is null ";
            }
            if (sectionCode == null) {
                sql += " AND t.officeSectionCode is null ";
            }
        }
        sql += " ORDER BY t.userName";
        TypedQuery<IpUser> q = em.createQuery(sql, IpUser.class);
        params.entrySet().stream().forEach(e -> q.setParameter(e.getKey(), e.getValue()));
        return q.getResultList();
    }

    public int transferUsers(List<Integer> userIds, String divisionCode, String departmentCode, String sectionCode) {
        Query query = em.createQuery("UPDATE IpUser u set u.officeDivisionCode = :divisionCode, u.officeDepartmentCode = :departmentCode, u.officeSectionCode = :sectionCode where u.userId in :userIds");
        query.setParameter("divisionCode", divisionCode);
        query.setParameter("departmentCode", departmentCode);
        query.setParameter("sectionCode", sectionCode);
        query.setParameter("userIds", userIds);
        return query.executeUpdate();
    }

    @Override
    public long countUsersSameStructure(List<Integer> userIds, String divisionCode, String departmentCode, String sectionCode) {
        Query q = em.createQuery("SELECT count(u) from IpUser u where (:divisionCode is null or u.officeDivisionCode = :divisionCode) and (:departmentCode is null or u.officeDepartmentCode = :departmentCode) and (:sectionCode is null or u.officeSectionCode = :sectionCode) and u.userId in :userIds");
        q.setParameter("divisionCode", divisionCode);
        q.setParameter("departmentCode", departmentCode);
        q.setParameter("sectionCode", sectionCode);
        q.setParameter("userIds", userIds);
        return (long) q.getSingleResult();
    }
}
