package bg.duosoft.ipas.persistence.model.entity;

import bg.duosoft.ipas.persistence.model.entity.file.IpLogChangesPK;

/**
 * User: Georgi
 * Date: 4.9.2020 г.
 * Time: 9:05
 */
public interface IpFileLogChanges extends IpLogChanges {
    IpLogChangesPK getPk();

    String getChangeUserdocRef();

    void setChangeUserdocRef(String changeUserdocRef);

    void setPk(IpLogChangesPK pk);
}
