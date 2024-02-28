package bg.duosoft.ipas.persistence.model.entity;

import bg.duosoft.ipas.persistence.model.entity.file.IpLogChangesPK;

/**
 * User: Georgi
 * Date: 4.9.2020 г.
 * Time: 9:05
 */
public interface IpLogChanges {
    Integer getRowVersion();

    Integer getChangeUserId();

    java.sql.Timestamp getChangeDate();

    String getDataCode();

    String getDataVersionWcode();

    String getDataValue();

    void setRowVersion(Integer rowVersion);

    void setChangeUserId(Integer changeUserId);

    void setChangeDate(java.sql.Timestamp changeDate);

    void setDataCode(String dataCode);

    void setDataVersionWcode(String dataVersionWcode);

    void setDataValue(String dataValue);
}
