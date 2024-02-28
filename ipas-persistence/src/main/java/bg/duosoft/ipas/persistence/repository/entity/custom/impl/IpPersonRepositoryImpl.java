package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpPersonRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.math.BigDecimal;
@Repository
@Transactional
public class IpPersonRepositoryImpl extends BaseRepositoryCustomImpl implements IpPersonRepositoryCustom {

    /*public Integer getNewPersonNumber() {
        Query insertNewPersonNumberQuery = em.createNativeQuery("INSERT INTO IPASPROD.SYS_SEC_PERSONA values('')");
        insertNewPersonNumberQuery.executeUpdate();

        Query getNewPersonNumberQuery = em.createNativeQuery("SELECT @@IDENTITY");

        BigDecimal result = (BigDecimal) getNewPersonNumberQuery.getSingleResult();
        int personNbr = 1 + result.intValueExact();
        return personNbr;
    }*/


}
