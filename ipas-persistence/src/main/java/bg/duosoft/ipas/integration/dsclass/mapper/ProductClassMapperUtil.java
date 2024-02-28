package bg.duosoft.ipas.integration.dsclass.mapper;

/**
 * Created by Raya
 * 21.04.2020
 */
public class ProductClassMapperUtil {

    public static final String IPAS_CLASS_SEPARATOR = "-";
    public static final String DSCLASS_CLASS_SEPARATOR = ".";


    public static String ipasToDsClassProductCass(String originalProductClass){
        return originalProductClass.replace(IPAS_CLASS_SEPARATOR, DSCLASS_CLASS_SEPARATOR);
    }

    public static String dsClassToIpasProductClass(String originalProductClass){
        return originalProductClass.replace(DSCLASS_CLASS_SEPARATOR, IPAS_CLASS_SEPARATOR);
    }
}
