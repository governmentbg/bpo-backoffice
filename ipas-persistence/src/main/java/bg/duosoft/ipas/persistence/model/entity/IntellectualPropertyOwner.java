package bg.duosoft.ipas.persistence.model.entity;

/**
 * User: ggeorgiev
 * Date: 3.4.2019 г.
 * Time: 15:05
 */
public interface IntellectualPropertyOwner extends IntellectualPropertyRelatedPerson {

    public IntellectualPropertyOwnerPK getPk();

    public String getNotes();
    public void setNotes(String notes);


    public Integer getOrderNbr();
    public void setOrderNbr(Integer orderNbr);
}
