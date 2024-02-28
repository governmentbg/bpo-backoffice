package bg.duosoft.ipas.persistence.model.entity;

import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 25.7.2019 г.
 * Time: 17:33
 */
public interface UpdatableEntity {
    Date getLastUpdateDate();

    void setLastUpdateDate(Date date);

    Integer getLastUpdateUserId();

    void setLastUpdateUserId(Integer userId);

}
