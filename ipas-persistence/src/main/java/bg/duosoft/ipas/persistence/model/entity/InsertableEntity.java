package bg.duosoft.ipas.persistence.model.entity;

import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 25.7.2019 г.
 * Time: 17:33
 */
public interface InsertableEntity {
    void setCreationDate(Date date);
    void setCreationUserId(Integer userId);

    Date getCreationDate();
    Integer getCreationUserId();

}
