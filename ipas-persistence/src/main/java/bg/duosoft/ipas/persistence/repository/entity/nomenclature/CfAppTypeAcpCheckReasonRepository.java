package bg.duosoft.ipas.persistence.repository.entity.nomenclature;


import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAcpCheckReason;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAppTypeAcpCheckReason;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CfAppTypeAcpCheckReasonRepository extends BaseRepository<CfAppTypeAcpCheckReason, Integer> {

    @Query(value = "SELECT c.acpCheckReason FROM CfAppTypeAcpCheckReason c where c.applicationType = :applicationType")
    List<CfAcpCheckReason> selectAcpCheckReasonsByAppType(@Param("applicationType") String applicationType);
}
