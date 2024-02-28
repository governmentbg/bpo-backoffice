package bg.duosoft.ipas.persistence.model.nonentity;

import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 11.05.2022
 * Time: 13:50
 */
@Data
public class OffidocNotification {

    private String offidocOri;
    private Integer offidocSer;
    private Integer offidocNbr;
    private String closestParentObjectId;
    private String registrationNumber;
    private String offidocTypeName;
    private Date dateFinished;
    private Date dateReadPortal;
    private Date dateReadEmail;
    private Integer days;
    private String objectEfilingUser;
    private String userdocEfilingUser;
}
