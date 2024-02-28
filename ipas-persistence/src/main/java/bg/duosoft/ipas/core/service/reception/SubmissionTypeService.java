package bg.duosoft.ipas.core.service.reception;

import bg.duosoft.ipas.core.model.reception.CSubmissionType;

import java.util.List;

public interface SubmissionTypeService {

    List<CSubmissionType> selectAll();

    CSubmissionType selectById(Integer id);

}
