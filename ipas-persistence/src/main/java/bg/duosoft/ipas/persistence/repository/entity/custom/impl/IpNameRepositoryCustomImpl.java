package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.persistence.model.entity.mark.IpName;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpNameRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * User: ggeorgiev
 * Date: 28.3.2019 г.
 * Time: 14:47
 */
@Repository
@Transactional
public class IpNameRepositoryCustomImpl extends BaseRepositoryCustomImpl implements IpNameRepositoryCustom {

    

    @Override
    public IpName getOrInsertIpName(IpName ipName) {

        Map<String, Object> params = new HashMap<>();
        String sql = "select t from IpName t where 1 = 1 and t.markName = :markName";
        params.put("markName", ipName.getMarkName());
        if (!StringUtils.isEmpty(ipName.getMarkNameLang2())) {
            sql += " and t.markNameLang2 = :markNameLang2";
            params.put("markNameLang2", ipName.getMarkNameLang2());
        }
        if (!StringUtils.isEmpty(ipName.getMapDenomination())) {
            sql += " and t.mapDenomination = :mapDenomination";
            params.put("mapDenomination", ipName.getMapDenomination());
        }

        Query query = em.createQuery(sql, IpName.class);
        params.forEach((k, v) -> query.setParameter(k, v));

        List lst = query.getResultList();
        IpName res = CollectionUtils.isEmpty(lst) ? null : (IpName) lst.get(0);
        if (res == null || !equalsIpNames(res,ipName)) {
            res = new IpName();

            res.setMarkCode(getLastId() + 1);
            res.setMapDenomination(ipName.getMapDenomination());
            res.setMarkName(ipName.getMarkName());
            res.setMarkNameLang2(ipName.getMarkNameLang2());
            res.setRowVersion(1);
            em.persist(res);
        }
        return res;
    }


    private boolean equalsIpNames(IpName ipName1,IpName ipName2){
        return Objects.equals(ipName1.getMarkName(),ipName2.getMarkName())
                && Objects.equals(ipName1.getMarkNameLang2(),ipName2.getMarkNameLang2())
                && Objects.equals(ipName1.getMapDenomination(),ipName2.getMapDenomination());
    }

    public Integer getLastId() {
        Query markCodeQuery = em.createNativeQuery("select top 1 coalesce(MARK_CODE,1) from IP_NAME order by MARK_CODE desc");
        BigDecimal maxMarkCode = (BigDecimal) markCodeQuery.getSingleResult();
        return maxMarkCode.intValue();
    }
}
