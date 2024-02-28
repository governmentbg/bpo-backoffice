package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassIpc;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassIpcPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CfClassIpcRepository extends BaseRepository<CfClassIpc, CfClassIpcPK> {
    @Query("SELECT i FROM  CfClassIpc i WHERE i.pk.ipcEditionCode not in (:editionCode) and" +
            " i.pk.ipcSectionCode = :sectionCode and i.pk.ipcClassCode = :classCode and i.pk.ipcSubclassCode = :subclassCode" +
            " and i.pk.ipcGroupCode = :groupCode and i.pk.ipcSubgroupCode =:subgroupCode" +
            " and i.ipcLatestVersion =:ipcLatestVersion  order by i.pk.ipcSectionCode, i.pk.ipcClassCode, i.pk.ipcSubclassCode, i.pk.ipcGroupCode, i.pk.ipcSubgroupCode")
    List<CfClassIpc> getValidIpcsById(@Param("editionCode") String editionCode,@Param("sectionCode") String sectionCode,
                                      @Param("classCode") String classCode,@Param("subclassCode") String subclassCode,
                                      @Param("groupCode") String groupCode,@Param("subgroupCode") String subgroupCode
                                     ,@Param("ipcLatestVersion")String ipcLatestVersion);


    @Query(value = "SELECT i.* FROM ipasprod.CF_CLASS_IPC i WHERE CONCAT(i.IPC_SECTION_CODE,i.IPC_CLASS_CODE,i.IPC_SUBCLASS_CODE,i.IPC_GROUP_CODE,i.IPC_SUBGROUP_CODE) like :ipcNumber " +
            "            and i.IPC_EDITION_CODE = (SELECT MAX(cast(i2.IPC_EDITION_CODE as integer)) FROM ipasprod.CF_CLASS_IPC i2 " +
            "            WHERE i2.IPC_LATEST_VERSION = :ipcLatestVersion AND i.IPC_SECTION_CODE=i2.IPC_SECTION_CODE AND i.IPC_CLASS_CODE=i2.IPC_CLASS_CODE AND " +
            "            i.IPC_SUBCLASS_CODE=i2.IPC_SUBCLASS_CODE AND i.IPC_GROUP_CODE=i2.IPC_GROUP_CODE AND i.IPC_SUBGROUP_CODE=i2.IPC_SUBGROUP_CODE) order by i.IPC_SECTION_CODE, i.IPC_CLASS_CODE " +
            "            , i.IPC_SUBCLASS_CODE, i.IPC_GROUP_CODE, IPC_SUBGROUP_CODE",nativeQuery = true)
    List<CfClassIpc> findIpcClassesByIpcNumber(@Param("ipcNumber") String ipcNumber,@Param("ipcLatestVersion") String ipcLatestVersion, Pageable pageable);



    @Query(value = "SELECT i.* FROM ipasprod.CF_CLASS_IPC i WHERE CONCAT(i.IPC_SECTION_CODE,i.IPC_CLASS_CODE,i.IPC_SUBCLASS_CODE,i.IPC_GROUP_CODE,i.IPC_SUBGROUP_CODE)  = :ipcNumber " +
            "            and i.IPC_EDITION_CODE = (SELECT MAX(cast(i2.IPC_EDITION_CODE as integer)) FROM ipasprod.CF_CLASS_IPC i2 " +
            "            WHERE i2.IPC_LATEST_VERSION = :ipcLatestVersion AND i.IPC_SECTION_CODE=i2.IPC_SECTION_CODE AND i.IPC_CLASS_CODE=i2.IPC_CLASS_CODE AND " +
            "            i.IPC_SUBCLASS_CODE=i2.IPC_SUBCLASS_CODE AND i.IPC_GROUP_CODE=i2.IPC_GROUP_CODE AND i.IPC_SUBGROUP_CODE=i2.IPC_SUBGROUP_CODE) order by i.IPC_SECTION_CODE, i.IPC_CLASS_CODE " +
            "            , i.IPC_SUBCLASS_CODE, i.IPC_GROUP_CODE, IPC_SUBGROUP_CODE",nativeQuery = true)
    CfClassIpc findIpcClassByIpcNumberWithLatestEdition(@Param("ipcNumber") String ipcNumber,@Param("ipcLatestVersion") String ipcLatestVersion);

    @Query(value = "SELECT ipcn.* FROM  ipasprod.IP_PATENT_IPC_CLASSES ipc " +
            "inner join IPASPROD.CF_CLASS_IPC ipcn on ipc.IPC_EDITION_CODE = ipcn.IPC_EDITION_CODE and ipc.IPC_SECTION_CODE = ipcn.IPC_SECTION_CODE and ipc.IPC_CLASS_CODE = ipcn.IPC_CLASS_CODE and ipc.IPC_SUBCLASS_CODE = ipcn.IPC_SUBCLASS_CODE and ipc.IPC_GROUP_CODE = ipcn.IPC_GROUP_CODE and ipc.IPC_SUBGROUP_CODE = ipcn.IPC_SUBGROUP_CODE " +
            "             WHERE  ipc.FILE_NBR = :fileNbr and ipc.FILE_SEQ = :fileSeq and ipc.FILE_TYP = :fileTyp and ipc.FILE_SER = :fileSer and " +
            "             CONCAT(ipc.IPC_SECTION_CODE,ipc.IPC_CLASS_CODE,ipc.IPC_SUBCLASS_CODE,ipc.IPC_GROUP_CODE,ipc.IPC_SUBGROUP_CODE) like :ipcNumber " +
            "             and ipc.IPC_EDITION_CODE = (SELECT MAX(cast(i2.IPC_EDITION_CODE as integer)) FROM ipasprod.CF_CLASS_IPC i2 " +
            "             WHERE i2.IPC_LATEST_VERSION = :ipcLatestVersion AND ipc.IPC_SECTION_CODE=i2.IPC_SECTION_CODE AND ipc.IPC_CLASS_CODE=i2.IPC_CLASS_CODE AND " +
            "            ipc.IPC_SUBCLASS_CODE=i2.IPC_SUBCLASS_CODE AND ipc.IPC_GROUP_CODE=i2.IPC_GROUP_CODE AND ipc.IPC_SUBGROUP_CODE=i2.IPC_SUBGROUP_CODE) order by ipc.IPC_SECTION_CODE, ipc.IPC_CLASS_CODE , " +
            "            ipc.IPC_SUBCLASS_CODE, ipc.IPC_GROUP_CODE, ipc.IPC_SUBGROUP_CODE",nativeQuery = true)
    List<CfClassIpc> findIpcClassesByIpcNumberForSpcs(@Param("ipcNumber") String ipcNumber,@Param("ipcLatestVersion") String ipcLatestVersion,@Param("fileNbr")Integer fileNbr,
                                                      @Param("fileSeq")String fileSeq,@Param("fileTyp")String fileTyp,@Param("fileSer")Integer fileSer, Pageable pageable);



    @Query(value = "SELECT i.* FROM ipasprod.CF_CLASS_IPC i WHERE CONCAT(i.IPC_SECTION_CODE,i.IPC_CLASS_CODE,i.IPC_SUBCLASS_CODE,i.IPC_GROUP_CODE,i.IPC_SUBGROUP_CODE,i.IPC_EDITION_CODE) like :ipcNumber " +
            "order by i.IPC_SECTION_CODE, i.IPC_CLASS_CODE , i.IPC_SUBCLASS_CODE, i.IPC_GROUP_CODE, IPC_SUBGROUP_CODE",nativeQuery = true)
    List<CfClassIpc> findAllIpcClassesByIpcNumber(@Param("ipcNumber") String ipcNumber, Pageable pageable);

    @Query(value = "SELECT i.* FROM ipasprod.CF_CLASS_IPC i WHERE CONCAT(i.IPC_SECTION_CODE,i.IPC_CLASS_CODE,i.IPC_SUBCLASS_CODE,i.IPC_GROUP_CODE,i.IPC_SUBGROUP_CODE,i.IPC_EDITION_CODE) = :ipcNumber " +
            "order by i.IPC_SECTION_CODE, i.IPC_CLASS_CODE , i.IPC_SUBCLASS_CODE, i.IPC_GROUP_CODE, IPC_SUBGROUP_CODE",nativeQuery = true)
    List<CfClassIpc> findIpcClassByIpcNumber(@Param("ipcNumber") String ipcNumber);

    @Query("select i from CfClassIpc i where i.pk.ipcSectionCode = :sectionCode and i.pk.ipcClassCode = :classCode and i.pk.ipcSubclassCode = :subclassCode and i.pk.ipcGroupCode = :groupCode and i.pk.ipcSubgroupCode = :subgroupCode")
    List<CfClassIpc> findBySectionClassSubclassGroupAndSubgroup(@Param("sectionCode") String sectionCode,
                                                                       @Param("classCode") String classCode,@Param("subclassCode") String subclassCode,
                                                                       @Param("groupCode") String groupCode,@Param("subgroupCode") String subgroupCode);

}
