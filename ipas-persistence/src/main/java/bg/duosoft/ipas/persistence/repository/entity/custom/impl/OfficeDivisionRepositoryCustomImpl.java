package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.OfficeDivisionRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Optional;

/**
 * User: ggeorgiev
 * Date: 15.7.2019 г.
 * Time: 15:34
 */
@Repository
@Transactional
public class OfficeDivisionRepositoryCustomImpl extends BaseRepositoryCustomImpl implements OfficeDivisionRepositoryCustom {

    public String getNextDivisionCode() {
        Integer res = getMaxDivisionId().orElse(0);
        return String.format("%03d", res + 1);
    }


    private Optional<Integer> getMaxDivisionId() {
        Query q = em.createNativeQuery("select max(cast (OFFICE_DIVISION_CODE as int)) from ipasprod.CF_OFFICE_DIVISION");
        Integer res = (Integer) q.getSingleResult();
        return Optional.ofNullable(res);
    }
}
