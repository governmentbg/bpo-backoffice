package bg.duosoft.ipas.persistence.repository.entity.custom;

import bg.duosoft.ipas.persistence.model.entity.mark.IpName;

/**
 * User: ggeorgiev
 * Date: 28.3.2019 г.
 * Time: 14:47
 */
public interface IpNameRepositoryCustom {
    IpName getOrInsertIpName(IpName ipName);
    Integer getLastId();
}
