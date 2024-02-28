package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.persistence.model.nonentity.ViennaClassSimple;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.ViennaClassSimpleRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ViennaClassSimpleRepositoryImpl extends BaseRepositoryCustomImpl implements ViennaClassSimpleRepository {

    private String sql = new StringBuilder()
            .append("SELECT * FROM (SELECT ")
            .append("  FORMAT([VIENNA_CATEGORY_CODE], '#00') AS 'VIENNA_CODE' ")
            .append("      ,[VIENNA_CATEGORY_NAME] as 'VIENNA_CLASS_NAME' ")
            .append("  FROM [IPASPROD].[CF_CLASS_VIENNA_CATEG] ")
            .append("UNION ")
            .append("SELECT CONCAT( ")
            .append(" FORMAT([VIENNA_CATEGORY_CODE], '#00'), '.' ")
            .append(",FORMAT([VIENNA_DIVISION_CODE], '#00')) AS 'VIENNA_CODE' ")
            .append("      ,[VIENNA_DIVISION_NAME] as 'VIENNA_CLASS_NAME' ")
            .append("  FROM [IPASPROD].[CF_CLASS_VIENNA_DIVIS] ")
            .append("UNION ").append("SELECT ")
            .append("  CONCAT(")
            .append(" FORMAT([VIENNA_CATEGORY_CODE], '#00'), '.' ")
            .append(",FORMAT([VIENNA_DIVISION_CODE], '#00'), '.' ")
            .append(",FORMAT([VIENNA_SECTION_CODE], '#00')) AS 'VIENNA_CODE' ")
            .append("      ,[VIENNA_SECTION_NAME] as 'VIENNA_CLASS_NAME' ")
            .append("  FROM [IPASPROD].[CF_CLASS_VIENNA_SECT]) vc ").toString();

    @Override
    public List<ViennaClassSimple> findAllByViennaCode(String viennaCode, Integer maxResult) {
        String cSql = sql + "WHERE VIENNA_CODE LIKE :viennaCode ";

        Query query = em.createNativeQuery(cSql);

        query.setParameter("viennaCode", viennaCode + "%");

        if (maxResult != null) {
            query.setFirstResult(0);
            query.setMaxResults(maxResult);
        }

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.getResultList();
        return resultList.stream()
                .map(objects -> {
                    ViennaClassSimple vcs = new ViennaClassSimple();
                    vcs.setViennaClass((String) objects[0]);
                    vcs.setViennaDescription((String) objects[1]);
                    return vcs;
                })
                .collect(Collectors.toList());
    }
}
