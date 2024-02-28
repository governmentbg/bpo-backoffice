package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.ext.core.CfSettlement;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CfSettlementRepository extends BaseRepository<CfSettlement, Integer> {

    @Query(value = "SELECT c FROM CfSettlement c where (c.name like ?1 )")
    List<CfSettlement> selectByNameLikeOrSettlementNameLike(String name);

    @Query(value = "SELECT c FROM CfSettlement c where (c.name = ?1 ) OR (c.settlementname = ?1)")
    List<CfSettlement> selectByNameOrSettlementName(String name);

    List<CfSettlement> findAllBySettlementname(String settlementname);

    List<CfSettlement> findAllByIsdistrict(Boolean isdistrict);

    CfSettlement findByNameAndMunicipalitycode_NameAndDistrictcode_Name(String settlement, String municipality, String district);

}
