package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.miscellaneous.CSettlement;

import java.util.List;

public interface SettlementService {

    List<CSettlement> selectByNameLikeOrSettlementNameLike(String name);

    List<CSettlement> selectBySettlementName(String name);

    List<CSettlement> selectByNameOrSettlementName(String name);

    CSettlement selectById(Integer id);

    CSettlement selectBySettlementMunicipalityAndDistrict(String settlement, String municipality, String district);

}