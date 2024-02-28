package bg.duosoft.ipas.core.service.reception;

import bg.duosoft.ipas.core.model.reception.CReceptionType;
import bg.duosoft.ipas.enums.ReceptionTypeConfig;

import java.util.List;

public interface ReceptionTypeService {

    List<CReceptionType> selectAll();

    List<CReceptionType> selectAllDependingOnConfiguration(List<ReceptionTypeConfig> configList);

    CReceptionType selectById(Integer id);

    CReceptionType selectByFileType(String fileType);


}
