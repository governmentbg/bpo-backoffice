package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUserdocType;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.CfUserdocTypeRepositoryCustom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface CfUserdocTypeRepository extends BaseRepository<CfUserdocType, String>, CfUserdocTypeRepositoryCustom {

    @Query(value = "SELECT DISTINCT t.*\n" +
            "FROM IPASPROD.CF_USERDOC_TYPE t\n" +
            "         JOIN EXT_RECEPTION.USERDOC_RECEPTION_RELATION r on r.LINKED_USERDOC_TYPE = t.USERDOC_TYP\n" +
            "         JOIN EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE adt on r.LINKED_USERDOC_TYPE = adt.TYPE\n" +
            "    where r.MAIN_TYPE = ?1", nativeQuery = true)
    List<CfUserdocType> selectUserdocTypesMapByUserdocParentType(String mainType);

    List<CfUserdocType> findAllByUserdocNameContainingOrderByUserdocName(String userdocname);

    @Query(value = "SELECT distinct GENERATE_PROC_TYP FROM ipasprod.CF_USERDOC_TYPE", nativeQuery = true)
    List<String> getAllProcTypes();


    @Query(value = "select DISTINCT ut.USERDOC_TYP as type, ut.USERDOC_NAME as name\n" +
            "from ipasprod.ip_proc p\n" +
            "         join ipasprod.CF_PROCESS_TYPE pt on p.PROC_TYP = pt.PROC_TYP\n" +
            "         join ipasprod.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE\n" +
            "         JOIN IPASPROD.CF_USERDOC_TYPE ut on p.USERDOC_TYP = ut.USERDOC_TYP\n" +
            "where pt.RELATED_TO_WCODE = 2\n" +
            "  and p.RESPONSIBLE_USER_ID in (?1) and p.USERDOC_FILE_TYP not in ('Е','У') \n" +
            " and  ut.USERDOC_GROUP_NAME =?2\n"+
            "order by ut.USERDOC_NAME", nativeQuery = true)
    List<Object[]> selectDistinctUserdocTypeMapForUserdocsByUsersAndUserdocGroupName(List<Integer>users, String userdocGroupName);

    @Query(value = "select DISTINCT ut.USERDOC_TYP as type, ut.USERDOC_NAME as name\n" +
            "from ipasprod.ip_proc p\n" +
            "         join ipasprod.CF_PROCESS_TYPE pt on p.PROC_TYP = pt.PROC_TYP\n" +
            "         join ipasprod.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE\n" +
            "         JOIN IPASPROD.CF_USERDOC_TYPE ut on p.USERDOC_TYP = ut.USERDOC_TYP\n" +
            "where pt.RELATED_TO_WCODE = 2\n" +
            "  and p.RESPONSIBLE_USER_ID = ?1\n" +
            "order by ut.USERDOC_NAME", nativeQuery = true)
    List<Object[]> selectDistinctUserdocTypeMapForUserdocsByResponsibleUser(Integer responsibleUserId);

    @Query(value = "select ut from CfUserdocType ut where ut.userdocTyp not in (:exludeUserdocTypes) and (lower(ut.userdocName) like lower(concat('%', :invalidateType, '%')))")
    List<CfUserdocType> selectAutocompleteUserdocTypesForInvalidation(@Param("exludeUserdocTypes") List<String> exludeUserdocTypes, @Param("invalidateType") String invalidateType);

    @Query(value = "select ut.userdocTyp from CfUserdocType ut where ut.userdocTyp <> :currentUserdocType")
    List<String> selectUserdocTypesForInvalidation(@Param("currentUserdocType") String currentUserdocType);

    List<CfUserdocType> findAllByUserdocTypInOrderByUserdocName(Collection<String> userdocTypes);

    @Query(value = "select ut.userdocName " +
            "from CfUserdocType ut " +
            "inner join CfUserdocInvalidationRelation ir on ut.userdocTyp = ir.pk.userdocType " +
            "where ir.pk.invalidatedUserdocType = :currentUserdocType ")
    List<String> selectUserdocsInvalidatingCurrentUserdoc(@Param("currentUserdocType") String currentUserdocType);

    @Query(value = "select lc.USERDOC_TYP\n" +
            "from IPASPROD.CF_USERDOC_TYPE_LIST_CONTENT lc\n" +
            "join IPASPROD.CF_USERDOC_TYPE_LIST l on lc.USERDOC_LIST_CODE = l.USERDOC_LIST_CODE\n" +
            "where lc.USERDOC_LIST_CODE= ?1",nativeQuery = true)
    List<String> selectUserdocTypesByListCode(String userdocListCode);

}
