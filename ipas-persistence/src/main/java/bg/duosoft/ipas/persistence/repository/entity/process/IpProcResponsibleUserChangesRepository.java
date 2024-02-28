package bg.duosoft.ipas.persistence.repository.entity.process;

import bg.duosoft.ipas.persistence.model.entity.process.IpProcResponsibleUserChanges;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcResponsibleUserChangesPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpProcResponsibleUserChangesRepositoryCustom;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpProcResponsibleUserChangesRepository extends BaseRepository<IpProcResponsibleUserChanges, IpProcResponsibleUserChangesPK>, IpProcResponsibleUserChangesRepositoryCustom {

    @Modifying
    @Query(value = "UPDATE IpProcResponsibleUserChanges ch SET ch.status = 1 where ch.procTyp = ?1 and ch.procNbr = ?2 and ch.newResponsibleUserId = ?3 and ch.status = 0")
    void setResponsibleUserChangeAsRead(String processType, Integer processNbr,Integer responsibleUser);

    @Query(value = "SELECT COUNT(*)  FROM ext_core.IP_PROC_RESPONSIBLE_USER_CHANGES WHERE PROC_TYP=?1 and PROC_NBR = ?2 and STATUS = 0", nativeQuery = true)
    Integer getUnreadResponsibleUserChangesCount(String processType, Integer processNbr);

    @Query(value = "SELECT * FROM EXT_CORE.IP_PROC_RESPONSIBLE_USER_CHANGES s WHERE s.ABDOCS_USER_TARGETING_PROCESSED_DATE IS NULL", nativeQuery = true)
    List<IpProcResponsibleUserChanges> selectNotProcessedAbdocsUserTargeting();

    @Modifying
    @Query(value = "UPDATE EXT_CORE.IP_PROC_RESPONSIBLE_USER_CHANGES SET ABDOCS_USER_TARGETING_PROCESSED_DATE = GETDATE() WHERE PROC_TYP = ?1 AND PROC_NBR = ?2 AND CHANGE_NBR = ?3", nativeQuery = true)
    void updateAbdocsUserTargetingAsProcessed(String processType, Integer processNbr, Integer changeNbr);

}
