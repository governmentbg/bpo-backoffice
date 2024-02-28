package bg.duosoft.ipas.core.service.nomenclature;

/**
 * User: ggeorgiev
 * Date: 06.12.2022
 * Time: 14:43
 */
public interface ConfigurationService {
    public static final String AUTOMATIC_ACTION_24H = "FecMovAutom24";
    public void deleteConfiguration(String configCode);
}
