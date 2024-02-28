package bg.duosoft.ipas.core.service.enotif;

import bg.duosoft.ipas.core.model.mark.CEnotif;

import java.util.List;

public interface EnotifService {
    List<CEnotif> findAllOrOrderByGaznoDesc();
    List<CEnotif> findAllWithTopOrderByGaznoDesc();
    Integer getEnotifsCount();
    CEnotif findById(String gazno);

}
