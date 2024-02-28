package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassCpc;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassCpcPK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassIpc;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CfClassCpcRepository extends BaseRepository<CfClassCpc, CfClassCpcPK> {
    @Query("SELECT i FROM  CfClassCpc i WHERE i.pk.cpcEditionCode not in (:editionCode) and" +
            " i.pk.cpcSectionCode = :sectionCode and i.pk.cpcClassCode = :classCode and i.pk.cpcSubclassCode = :subclassCode" +
            " and i.pk.cpcGroupCode = :groupCode and i.pk.cpcSubgroupCode =:subgroupCode" +
            " and i.cpcLatestVersion =:cpcLatestVersion  order by i.pk.cpcSectionCode, i.pk.cpcClassCode, i.pk.cpcSubclassCode, i.pk.cpcGroupCode, i.pk.cpcSubgroupCode")
    List<CfClassCpc> getValidCpcsById(@Param("editionCode") String editionCode,@Param("sectionCode") String sectionCode,
                                      @Param("classCode") String classCode,@Param("subclassCode") String subclassCode,
                                      @Param("groupCode") String groupCode,@Param("subgroupCode") String subgroupCode
                                     ,@Param("cpcLatestVersion")String cpcLatestVersion);
    @Query(value = "SELECT i.* FROM ipasprod.CF_CLASS_CPC i WHERE CONCAT(i.CPC_SECTION_CODE,i.CPC_CLASS_CODE,i.CPC_SUBCLASS_CODE,i.CPC_GROUP_CODE,i.CPC_SUBGROUP_CODE) like :cpcNumber " +
            "            and i.CPC_EDITION_CODE = (SELECT MAX(cast(i2.CPC_EDITION_CODE as integer)) FROM ipasprod.CF_CLASS_CPC i2 " +
            "            WHERE i2.CPC_LATEST_VERSION = :cpcLatestVersion AND i.CPC_SECTION_CODE=i2.CPC_SECTION_CODE AND i.CPC_CLASS_CODE=i2.CPC_CLASS_CODE AND " +
            "            i.CPC_SUBCLASS_CODE=i2.CPC_SUBCLASS_CODE AND i.CPC_GROUP_CODE=i2.CPC_GROUP_CODE AND i.CPC_SUBGROUP_CODE=i2.CPC_SUBGROUP_CODE) order by i.CPC_SECTION_CODE, i.CPC_CLASS_CODE " +
            "            , i.CPC_SUBCLASS_CODE, i.CPC_GROUP_CODE, CPC_SUBGROUP_CODE",nativeQuery = true)
    List<CfClassCpc> findCpcClassesByCpcNumber(@Param("cpcNumber") String cpcNumber,@Param("cpcLatestVersion") String cpcLatestVersion, Pageable pageable);

    @Query(value = "SELECT cpcn.* FROM  ipasprod.IP_PATENT_CPC_CLASSES cpc " +
            "inner join IPASPROD.CF_CLASS_CPC cpcn on cpc.CPC_EDITION_CODE = cpcn.CPC_EDITION_CODE and cpc.CPC_SECTION_CODE = cpcn.CPC_SECTION_CODE and cpc.CPC_CLASS_CODE = cpcn.CPC_CLASS_CODE and cpc.CPC_SUBCLASS_CODE = cpcn.CPC_SUBCLASS_CODE and cpc.CPC_GROUP_CODE = cpcn.CPC_GROUP_CODE and cpc.CPC_SUBGROUP_CODE = cpcn.CPC_SUBGROUP_CODE " +
            "             WHERE  cpc.FILE_NBR = :fileNbr and cpc.FILE_SEQ = :fileSeq and cpc.FILE_TYP = :fileTyp and cpc.FILE_SER = :fileSer and " +
            "             CONCAT(cpc.CPC_SECTION_CODE,cpc.CPC_CLASS_CODE,cpc.CPC_SUBCLASS_CODE,cpc.CPC_GROUP_CODE,cpc.CPC_SUBGROUP_CODE) like :cpcNumber " +
            "             and cpc.CPC_EDITION_CODE = (SELECT MAX(cast(i2.CPC_EDITION_CODE as integer)) FROM ipasprod.CF_CLASS_CPC i2 " +
            "             WHERE i2.CPC_LATEST_VERSION = :cpcLatestVersion AND cpc.CPC_SECTION_CODE=i2.CPC_SECTION_CODE AND cpc.CPC_CLASS_CODE=i2.CPC_CLASS_CODE AND " +
            "            cpc.CPC_SUBCLASS_CODE=i2.CPC_SUBCLASS_CODE AND cpc.CPC_GROUP_CODE=i2.CPC_GROUP_CODE AND cpc.CPC_SUBGROUP_CODE=i2.CPC_SUBGROUP_CODE) order by cpc.CPC_SECTION_CODE, cpc.CPC_CLASS_CODE , " +
            "            cpc.CPC_SUBCLASS_CODE, cpc.CPC_GROUP_CODE, cpc.CPC_SUBGROUP_CODE",nativeQuery = true)
    List<CfClassCpc> findCpcClassesByCpcNumberForSpcs(@Param("cpcNumber") String cpcNumber,@Param("cpcLatestVersion") String cpcLatestVersion,@Param("fileNbr")Integer fileNbr,
                                                      @Param("fileSeq")String fileSeq,@Param("fileTyp")String fileTyp,@Param("fileSer")Integer fileSer, Pageable pageable);

    @Query("select i from CfClassCpc i where i.pk.cpcSectionCode = :sectionCode and i.pk.cpcClassCode = :classCode and i.pk.cpcSubclassCode = :subclassCode and i.pk.cpcGroupCode = :groupCode and i.pk.cpcSubgroupCode = :subgroupCode")
    List<CfClassCpc> findBySectionClassSubclassGroupAndSubgroup(@Param("sectionCode") String sectionCode,
                                                                @Param("classCode") String classCode, @Param("subclassCode") String subclassCode,
                                                                @Param("groupCode") String groupCode, @Param("subgroupCode") String subgroupCode);


    @Query(value = "SELECT i.* FROM ipasprod.CF_CLASS_CPC i WHERE CONCAT(i.CPC_SECTION_CODE,i.CPC_CLASS_CODE,i.CPC_SUBCLASS_CODE,i.CPC_GROUP_CODE,i.CPC_SUBGROUP_CODE,i.CPC_EDITION_CODE) like :cpcNumber " +
            "order by i.CPC_SECTION_CODE, i.CPC_CLASS_CODE , i.CPC_SUBCLASS_CODE, i.CPC_GROUP_CODE, CPC_SUBGROUP_CODE",nativeQuery = true)
    List<CfClassCpc> findAllCpcClassesByCpcNumber(@Param("cpcNumber") String cpcNumber, Pageable pageable);

    @Query(value = "SELECT i.* FROM ipasprod.CF_CLASS_CPC i WHERE CONCAT(i.CPC_SECTION_CODE,i.CPC_CLASS_CODE,i.CPC_SUBCLASS_CODE,i.CPC_GROUP_CODE,i.CPC_SUBGROUP_CODE,i.CPC_EDITION_CODE) = :cpcNumber " +
            "order by i.CPC_SECTION_CODE, i.CPC_CLASS_CODE , i.CPC_SUBCLASS_CODE, i.CPC_GROUP_CODE, CPC_SUBGROUP_CODE",nativeQuery = true)
    List<CfClassCpc> findCpcClassByCpcNumber(@Param("cpcNumber") String cpcNumber);

}
