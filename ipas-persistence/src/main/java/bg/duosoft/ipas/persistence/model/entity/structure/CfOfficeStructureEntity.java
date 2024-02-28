package bg.duosoft.ipas.persistence.model.entity.structure;

/**
 * User: ggeorgiev
 * Date: 15.7.2019 г.
 * Time: 17:20
 */
public interface CfOfficeStructureEntity {
    Integer getRowVersion();

    String getXmlDesigner();

    bg.duosoft.ipas.persistence.model.entity.user.IpUser getSignatureUser();

    java.util.Date getCreationDate();

    Integer getCreationUserId();

    java.util.Date getLastUpdateDate();

    Integer getLastUpdateUserId();

    void setRowVersion(Integer rowVersion);

    void setName(String name);

    void setXmlDesigner(String xmlDesigner);

    void setSignatureUser(bg.duosoft.ipas.persistence.model.entity.user.IpUser signatureUser);

    void setCreationDate(java.util.Date creationDate);

    void setCreationUserId(Integer creationUserId);

    void setLastUpdateDate(java.util.Date lastUpdateDate);

    void setLastUpdateUserId(Integer lastUpdateUserId);
}
