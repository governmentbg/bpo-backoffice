package bg.duosoft.ipas.persistence.repository.entity.vw;

import bg.duosoft.ipas.persistence.model.entity.action.IpActionPK;
import bg.duosoft.ipas.persistence.model.entity.vw.VwJournal;
import bg.duosoft.ipas.persistence.model.nonentity.PublicationSection;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;

@Repository
public interface VwJournalRepository extends BaseRepository<VwJournal, IpActionPK> {
    @Query(value = "SELECT DISTINCT vj.year \n" +
            "  FROM VwJournal vj \n" +
            "  WHERE vj.pk.procTyp in ?1 " +
            "  ORDER BY vj.year DESC" )
    List<String> getYearsByProcessTypes(List<String> procTypes);

    @Query(value = "SELECT DISTINCT vj.buletin \n" +
            "  FROM VwJournal vj\n" +
            "  WHERE vj.pk.procTyp in ?1 AND vj.year = ?2\n" +
            "  ORDER BY vj.buletin")
    List<String> getBulletinsByProcessTypes(List<String> procTypes, String year);

    @Query(value = "SELECT DISTINCT new bg.duosoft.ipas.persistence.model.nonentity.PublicationSection(vj.sect, vj.sectDef) \n" +
            "  FROM VwJournal vj\n" +
            "  WHERE vj.pk.procTyp in ?1 AND vj.buletin = ?2 \n" +
            "  GROUP BY vj.sect, vj.sectDef \n" +
            "  ORDER BY vj.sectDef")
    List<PublicationSection> getPublicationSectionsByProcessTypes(List<String> procTypes, String bulletin);

    @Query(value = "SELECT DISTINCT new bg.duosoft.ipas.persistence.model.nonentity.PublicationSection(vj.sect, vj.sectDef) \n" +
            "  FROM VwJournal vj\n" +
            "  WHERE vj.pk.procTyp in ?1 AND vj.year = ?2 AND vj.buletin = ?3 \n" +
            "  GROUP BY vj.sect, vj.sectDef \n" +
            "  ORDER BY vj.sectDef")
    List<PublicationSection> getPublicationSectionsByProcessTypes(List<String> procTypes, String year, String bulletin);


    @Query(value = "SELECT DISTINCT vj.YEAR " +
            "  FROM [EXT_JOURNAL].[VW_JOURNAL] vj " +
            "  JOIN [IPASPROD].[CF_APPLICATION_TYPE] a ON vj.PROC_TYP = a.GENERATE_PROC_TYP " +
            "  WHERE a.FILE_TYP in ?1 " +
            "  ORDER BY vj.YEAR DESC", nativeQuery = true )
    List<String> getYearsByFileTypes(List<String> fileTypes);


    @Query(value = "SELECT DISTINCT vj.BULETIN " +
            "  FROM [EXT_JOURNAL].[VW_JOURNAL] vj " +
            "  JOIN [IPASPROD].[CF_APPLICATION_TYPE] a ON vj.PROC_TYP = a.GENERATE_PROC_TYP " +
            "  WHERE a.FILE_TYP in ?1 AND vj.YEAR = ?2 " +
            "  ORDER BY vj.BULETIN", nativeQuery = true )
    List<String> getBulletinsByFileTypes(List<String> fileTypes, String year);

    @Query(value = "SELECT DISTINCT vj.BULETIN " +
            "  FROM [EXT_JOURNAL].[VW_JOURNAL] vj " +
            "  JOIN [IPASPROD].[CF_APPLICATION_TYPE] a ON vj.PROC_TYP = a.GENERATE_PROC_TYP " +
            "  WHERE a.FILE_TYP in ?1 " +
            "  ORDER BY vj.BULETIN", nativeQuery = true )
    List<String> getBulletinsByFileTypes(List<String> fileTypes);

    @Query(value = "SELECT DISTINCT vj.SECT as sect, vj.SECT_DEF as sectDef " +
            "  FROM [EXT_JOURNAL].[VW_JOURNAL] vj " +
            "  JOIN [IPASPROD].[CF_APPLICATION_TYPE] a ON vj.PROC_TYP = a.GENERATE_PROC_TYP " +
            "  WHERE a.FILE_TYP in ?1 " +
            "  GROUP BY vj.SECT, vj.SECT_DEF " +
            "  ORDER BY vj.SECT_DEF", nativeQuery = true )
    List<Tuple> getPublicationSectionsByFileTypes(List<String> fileTypes);

    @Query(value = "SELECT DISTINCT vj.SECT as sect, vj.SECT_DEF as sectDef " +
            "  FROM [EXT_JOURNAL].[VW_JOURNAL] vj " +
            "  JOIN [IPASPROD].[CF_APPLICATION_TYPE] a ON vj.PROC_TYP = a.GENERATE_PROC_TYP " +
            "  WHERE a.FILE_TYP in ?1 AND vj.BULETIN = ?2 " +
            "  GROUP BY vj.SECT, vj.SECT_DEF " +
            "  ORDER BY vj.SECT_DEF", nativeQuery = true )
    List<Tuple> getPublicationSectionsByFileTypesAndBuletin(List<String> fileTypes, String bulletin);


    @Query(value = "SELECT DISTINCT vj.SECT as sect, vj.SECT_DEF as sectDef " +
            "  FROM [EXT_JOURNAL].[VW_JOURNAL] vj " +
            "  JOIN [IPASPROD].[CF_APPLICATION_TYPE] a ON vj.PROC_TYP = a.GENERATE_PROC_TYP " +
            "  WHERE a.FILE_TYP in ?1 AND vj.YEAR = ?2 " +
            "  GROUP BY vj.SECT, vj.SECT_DEF " +
            "  ORDER BY vj.SECT_DEF", nativeQuery = true )
    List<Tuple> getPublicationSectionsByFileTypesAndYear(List<String> fileTypes, String year);

    @Query(value = "SELECT DISTINCT vj.SECT as sect, vj.SECT_DEF as sectDef " +
            "  FROM [EXT_JOURNAL].[VW_JOURNAL] vj " +
            "  JOIN [IPASPROD].[CF_APPLICATION_TYPE] a ON vj.PROC_TYP = a.GENERATE_PROC_TYP " +
            "  WHERE a.FILE_TYP in ?1 AND vj.YEAR = ?2 AND vj.BULETIN = ?3 " +
            "  GROUP BY vj.SECT, vj.SECT_DEF " +
            "  ORDER BY vj.SECT_DEF", nativeQuery = true )
    List<Tuple> getPublicationSectionsByFileTypes(List<String> fileTypes, String year, String bulletin);
}
