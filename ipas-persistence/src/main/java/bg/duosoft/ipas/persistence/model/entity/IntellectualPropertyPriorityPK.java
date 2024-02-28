package bg.duosoft.ipas.persistence.model.entity;

/**
 * User: ggeorgiev
 * Date: 3.4.2019 г.
 * Time: 11:20
 */
public interface IntellectualPropertyPriorityPK extends FileSeqTypSerNbrPK {
    public String getCountryCode();
    public void setCountryCode(String countryCode);

    public String getPriorityApplId();
    public void setPriorityApplId(String priorityApplId);

}
