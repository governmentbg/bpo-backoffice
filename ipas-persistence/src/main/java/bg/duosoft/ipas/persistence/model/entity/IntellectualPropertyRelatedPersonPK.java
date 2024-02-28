package bg.duosoft.ipas.persistence.model.entity;

/**
 * User: ggeorgiev
 * Date: 25.3.2019 г.
 * Time: 18:46
 */
public interface IntellectualPropertyRelatedPersonPK extends FileSeqTypSerNbrPK{
    public Integer getPersonNbr();
    public Integer getAddrNbr();

    public void setPersonNbr(Integer personNbr);
    public void setAddrNbr(Integer addrNbr);
}
