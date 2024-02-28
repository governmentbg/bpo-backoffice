package bg.duosoft.ipas.persistence.model.entity;

import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationSubtype;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLaw;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 25.3.2019 г.
 * Time: 18:34
 */
public interface IntellectualPropertyEntity {
    Integer getRowVersion();

    IpFilePK getPk();

    java.util.Date getFilingDate();

    java.util.Date getFirstPriorityDate();

    String getIndRegistered();

    Integer getRegistrationNbr();

    java.util.Date getRegistrationDate();

    java.util.Date getEntitlementDate();

    java.util.Date getExpirationDate();

    java.util.Date getCaptureDate();

    java.util.Date getPublicationDate();

    String getRegistrationDup();

    String getRegistrationTyp();

    Integer getRegistrationSer();

    String getJournalCode();

    String getIndManualInterpretation();

    String getPublicationNotes();

    java.util.Date getExhibitionDate();

    String getExhibitionNotes();

    java.util.Date getNovelty1Date();

    java.util.Date getNovelty2Date();

    Integer getPublicationNbr();

    String getPublicationSer();

    String getPublicationTyp();

    CfApplicationSubtype getCfApplicationSubtype();

    IpFile getFile();

    <T extends IntellectualPropertyRepresentative> List<T> getRepresentatives();

    <T extends IntellectualPropertyOwner> List<T> getOwners();

    <T extends IntellectualPropertyPriority> List<T> getPriorities();

    IpPersonAddresses getServicePerson();

    IpPersonAddresses getMainOwner();

    CfLaw getCfLaw();

    IpUser getCaptureUser();

    void setRowVersion(Integer rowVersion);

    void setPk(IpFilePK pk);

    void setFilingDate(java.util.Date filingDate);

    void setFirstPriorityDate(java.util.Date firstPriorityDate);

    void setIndRegistered(String indRegistered);

    void setRegistrationNbr(Integer registrationNbr);

    void setRegistrationDate(java.util.Date registrationDate);

    void setEntitlementDate(java.util.Date entitlementDate);

    void setExpirationDate(java.util.Date expirationDate);

    void setCaptureDate(java.util.Date captureDate);

    void setPublicationDate(java.util.Date publicationDate);

    void setRegistrationDup(String registrationDup);

    void setRegistrationTyp(String registrationTyp);

    void setRegistrationSer(Integer registrationSer);

    void setJournalCode(String journalCode);

    void setIndManualInterpretation(String indManualInterpretation);

    void setPublicationNotes(String publicationNotes);

    void setExhibitionDate(java.util.Date exhibitionDate);

    void setExhibitionNotes(String exhibitionNotes);

    void setNovelty1Date(java.util.Date novelty1Date);

    void setNovelty2Date(java.util.Date novelty2Date);

    void setPublicationNbr(Integer publicationNbr);

    void setPublicationSer(String publicationSer);

    void setPublicationTyp(String publicationTyp);

    void setCfApplicationSubtype(CfApplicationSubtype cfApplicationSubtype);

    void setFile(IpFile file);

    void setServicePerson(IpPersonAddresses servicePerson);

    void setMainOwner(IpPersonAddresses mainOwner);

    void setCfLaw(CfLaw cfLaw);

    void setCaptureUser(IpUser captureUser);


}
