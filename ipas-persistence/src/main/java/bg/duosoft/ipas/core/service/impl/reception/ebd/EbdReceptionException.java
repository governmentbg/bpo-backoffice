package bg.duosoft.ipas.core.service.impl.reception.ebd;

/**
 * User: Georgi
 * Date: 15.6.2020 г.
 * Time: 13:13
 */
public class EbdReceptionException extends Exception {
    private Integer docflowSystemId;
    public EbdReceptionException(Integer docflowSystemId) {
        this.docflowSystemId = docflowSystemId;
    }

    public EbdReceptionException(Integer docflowSystemId, Throwable cause) {
        super(cause);
        this.docflowSystemId = docflowSystemId;
    }

    public Integer getDocflowSystemId() {
        return docflowSystemId;
    }
}
