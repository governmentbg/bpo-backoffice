package bg.duosoft.ipas.core.service.mark;

import bg.duosoft.ipas.core.model.mark.CNiceClassList;

import java.util.List;

public interface NiceClassListService {

    List<Integer> getNiceClassesCodes();

    CNiceClassList selectByNiceClassCode(Integer niceClassCode);

    CNiceClassList saveNiceClassList(CNiceClassList cNiceClassList);
}
