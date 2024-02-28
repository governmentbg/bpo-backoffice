package bg.duosoft.ipas.integration.decisiondesktop.model.backoffice;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 08.12.2021
 * Time: 15:41
 */
@Data
public class EarlierMarkVariable {
    public static String TYPE_NATIONAL = "BG";
    public static String TYPE_INTERNATIONAL = "WO";
    public static String TYPE_EUROPEAN = "EM";

    private String niceList;
    private String niceClasses;
    private String applicationNumber;
    private String registrationNumber;
    private String applicationDate;
    private String registrationDate;
    private String signName;
    private String signImage;
    private byte[] signImageBytes;
    private String type;

}
