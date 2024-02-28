package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatus;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatusPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CfStatusRepository extends BaseRepository<CfStatus, CfStatusPK> {

    @Query(value = "SELECT * FROM IP_FILE m\n" +
            "JOIN IP_PROC p on m.PROC_NBR = p.PROC_NBR and m.PROC_TYP = p.PROC_TYP\n" +
            "JOIN CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE\n" +
            "WHERE m.FILE_SEQ = ?1 AND m.FILE_TYP = ?2 AND m.FILE_SER = ?3 AND m.FILE_NBR = ?4", nativeQuery = true)
    CfStatus getStatusByFileId(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr);

    @Query(value = "SELECT * from CF_STATUS s\n" +
            "    INNER JOIN (SELECT distinct s.status_code, s.status_name from CF_STATUS s\n" +
            "         join CF_MIGRATION m on s.PROC_TYP = m.PROC_TYP and (s.STATUS_CODE = m.initial_STATUS_CODE or s.STATUS_CODE = m.FINAL_STATUS_CODE)\n" +
            "             where m.PROC_TYP=?1) s2 ON s.STATUS_CODE = s2.STATUS_CODE and s2.STATUS_NAME=s.STATUS_NAME order by s.STATUS_NAME", nativeQuery = true)
    List<CfStatus> getInitialOrFinalStatusesByProcType(Integer processType);

    List<CfStatus> findAllByPk_ProcTyp(String procTyp);

    List<CfStatus> findAllByPk_ProcTypIn(List<String> procTyp);

    List<CfStatus> findAllByPk_ProcTypInOrderByStatusName(List<String> procTyp);

    @Query(value = "SELECT DISTINCT s.* from [IPASPROD].[CF_STATUS] s " +
            "  JOIN [IPASPROD].[CF_APPLICATION_TYPE] a ON s.PROC_TYP = a.GENERATE_PROC_TYP " +
            "  WHERE a.FILE_TYP in ?1 " +
            "  ORDER BY s.STATUS_NAME", nativeQuery = true)
    List<CfStatus> findAllByFileTypesOrderByStatusName(List<String> fileTypes);


    @Query(value = "select p.PROC_TYP+'-'+p.STATUS_CODE as code,status_name+' - '+cast(count (*) as varchar) as name\n" +
            "from ipasprod.ip_proc p\n" +
            "join ipasprod.CF_PROCESS_TYPE pt on p.PROC_TYP = pt.PROC_TYP\n" +
            "join ipasprod.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE\n" +
            "where pt.RELATED_TO_WCODE=1 and p.RESPONSIBLE_USER_ID in (?1) " +
            "and p.FILE_TYP not in ('Е','У') " +
            "group by p.status_code, s.STATUS_NAME,p.PROC_TYP\n" +
            "order by count (*) desc", nativeQuery = true)
    List<Object[]> selectIpObjectsStatusMapByUsersIncludeCount(List<Integer> users);


    @Query(value = "select p.PROC_TYP+'-'+p.STATUS_CODE as code,status_name+' - '+cast(count (*) as varchar) as name\n" +
            "from ipasprod.ip_proc p\n" +
            "join ipasprod.CF_PROCESS_TYPE pt on p.PROC_TYP = pt.PROC_TYP\n" +
            "join ipasprod.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE\n" +
            "where pt.RELATED_TO_WCODE=1 and p.RESPONSIBLE_USER_ID in (?1) " +
            "and p.FILE_TYP in (?2) " +
            "group by p.status_code, s.STATUS_NAME,p.PROC_TYP\n" +
            "order by count (*) desc", nativeQuery = true)
    List<Object[]> selectIpObjectsStatusMapByUsersAndFileTypesIncludeCount(List<Integer> users,List<String> fileTypes);

    @Query(value = "select DISTINCT p.PROC_TYP + '-' + p.STATUS_CODE as code, status_name as name\n" +
            "from ipasprod.ip_proc p\n" +
            "join ipasprod.CF_PROCESS_TYPE pt on p.PROC_TYP = pt.PROC_TYP\n" +
            "join ipasprod.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE\n" +
            "where pt.RELATED_TO_WCODE = 2 and p.RESPONSIBLE_USER_ID=?1 " +
            "order by s.STATUS_NAME", nativeQuery = true)
    List<Object[]> selectUserdocsStatusMapByResponsibleUser(Integer responsibleUserId);

    @Query(value = "select s.PROCESS_RESULT_TYP from IPASPROD.CF_STATUS s where s.PROC_TYP = ?1 AND s.STATUS_CODE = ?2", nativeQuery = true)
    String selectProcessResultType(String processType, String statusCode);

}
