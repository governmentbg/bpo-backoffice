package bg.duosoft.ipas.persistence.repository.entity.action;

import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.action.IpActionPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpActionRepositoryCustom;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface IpActionRepository extends BaseRepository<IpAction, IpActionPK>, IpActionRepositoryCustom {

    @Query(value = "SELECT * FROM IP_ACTION a where a.PROC_TYP = ?1 AND a.PROC_NBR = ?2\n" +
            "    AND a.ACTION_NBR = (SELECT MAX(a.ACTION_NBR) FROM IPASPROD.IP_ACTION a where a.PROC_TYP = ?1 AND a.PROC_NBR = ?2 AND a.IND_CHANGES_STATUS = 'S')", nativeQuery = true)
    IpAction selectLastInsertedAction(String procTyp, Integer procNbr);

    @Query(value = "SELECT COUNT(*) FROM IPASPROD.IP_ACTION e WHERE e.PROC_TYP = ?1 AND e.PROC_NBR = ?2", nativeQuery = true)
    Integer countByProcessId(String procType, Integer procNumber);

    @Query(value = "SELECT count(*) FROM IP_ACTION a join IPASPROD.CF_STATUS cs on a.PROC_TYP = cs.PROC_TYP and a.NEW_STATUS_CODE = cs.STATUS_CODE where a.PROC_TYP = ?1 and a.PROC_NBR = ?2 and TRIGGER_ACTIVITY_WCODE = 2", nativeQuery = true)
    Integer isUserdocAuthorizationActionsExists(String procType, Integer procNumber);

    @Query(value = "SELECT MAX(a.ACTION_DATE) FROM IPASPROD.IP_ACTION a where a.PROC_TYP = ?1 AND a.PROC_NBR = ?2 AND a.ACTION_DATE >= ?3 AND a.ACTION_DATE <= ?4 ", nativeQuery = true)
    Date selectMaxActionDateByDate(String procType, Integer procNumber, Date form, Date to);

    @Query(value = "SELECT a.pk FROM IpAction a")
    List<IpActionPK> listIpActionPK();

    @Query(value="SELECT\n" +
            "a.ACTION_DATE, a.NOTES2, a.NOTES3, a.NOTES4, a.ACTION_TYP, j.PUBLICATION_DATE, j.JOURNAL_CODE\n" +
            "FROM [IP_ACTION] as a\n" +
            "JOIN [IP_PROC] as p on (p.PROC_NBR = a.PROC_NBR and a.PROC_TYP = p.PROC_TYP)\n" +
            "LEFT JOIN [IP_JOURNAL] as j on (j.JOURNAL_CODE = a.JOURNAL_CODE)\n" +
            "WHERE ((ACTION_TYP in (\n" +
            "'1834'\n" +
            ")) or (a.JOURNAL_CODE is not null AND j.PUBLICATION_DATE is not null))\n" +
            "AND\n" +
            "((p.FILE_SEQ = ?1 and p.FILE_TYP = ?2 and p.FILE_SER = ?3 and p.FILE_NBR = ?4 )\n" +
            "           OR (p.USERDOC_FILE_SEQ = ?1 and p.USERDOC_FILE_TYP = ?2 and p.USERDOC_FILE_SER = ?3\n" +
            "                   and p.USERDOC_FILE_NBR = ?4 ))", nativeQuery = true)
    List<Object[]> getPublications(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);
}
