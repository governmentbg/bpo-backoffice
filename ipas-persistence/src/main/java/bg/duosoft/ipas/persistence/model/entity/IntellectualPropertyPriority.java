package bg.duosoft.ipas.persistence.model.entity;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfGeoCountry;

/**
 * User: ggeorgiev
 * Date: 3.4.2019 г.
 * Time: 11:20
 */
public interface IntellectualPropertyPriority {
    IntellectualPropertyPriorityPK getPk();

    Integer getRowVersion();

    java.util.Date getPriorityDate();

    String getIndAccepted();

    String getNotes();

    Long getPriorityApplIdAlt();

    String getNotesUnused();

    CfGeoCountry getCountry();

//    void setPk(IntellectualPropertyPriorityPK pk);

    void setRowVersion(Integer rowVersion);

    void setPriorityDate(java.util.Date priorityDate);

    void setIndAccepted(String indAccepted);

    void setNotes(String notes);

    void setPriorityApplIdAlt(Long priorityApplIdAlt);

    void setNotesUnused(String notesUnused);

    void setCountry(CfGeoCountry country);
}
