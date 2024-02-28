package bg.duosoft.ipas.persistence.model.entity;

public interface UserDocumentRelatedPersonPK extends DocumentOriLogTypNbrPK {
    Integer getPersonNbr();

    Integer getAddrNbr();

    void setPersonNbr(Integer personNbr);

    void setAddrNbr(Integer addrNbr);
}
