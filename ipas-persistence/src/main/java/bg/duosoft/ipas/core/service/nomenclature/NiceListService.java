package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.mark.CNiceClassList;

/**
 * Created by Raya
 * 23.09.2020
 */
public interface NiceListService {

    CNiceClassList getNiceList(Integer niceClassCode);

    CNiceClassList saveNiceList(CNiceClassList cNiceClassList);

}
