package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.persistence.model.entity.process.IpProcResponsibleUserChanges;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpProcResponsibleUserChangesRepositoryCustom;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * User: Georgi
 * Date: 5.11.2020 г.
 * Time: 17:41
 */
public class IpProcResponsibleUserChangesRepositoryCustomImpl extends BaseRepositoryCustomImpl implements IpProcResponsibleUserChangesRepositoryCustom {
    @Override
    public void addResponsibleUserChanges(Integer userChanged, String processType, Integer processNbr, Integer oldResponsibleUser, Integer newResponsibleUser) {
        IpProcResponsibleUserChanges rec = new IpProcResponsibleUserChanges(processType, processNbr, getNextChangeNumber(processType, processNbr), userChanged, oldResponsibleUser, newResponsibleUser);
        em.persist(rec);
    }
    private Integer getNextChangeNumber(String procTyp, Integer procNbr) {
        Query q = em.createQuery("SELECT max(c.changeNbr) from IpProcResponsibleUserChanges c where c.procNbr = :procNbr and c.procTyp = :procTyp");
        q.setParameter("procNbr", procNbr);
        q.setParameter("procTyp", procTyp);
        Integer res = (Integer) q.getSingleResult();
        return  res == null ? 1 : res + 1;
    }
}
