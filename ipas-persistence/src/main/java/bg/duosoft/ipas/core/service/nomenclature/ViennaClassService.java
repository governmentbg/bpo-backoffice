package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.mark.CViennaClass;

import java.util.List;

public interface ViennaClassService {

    List<CViennaClass> selectAllCategories();

    List<CViennaClass> selectAllDivisionsByCategory(Integer viennaCategory);

    List<CViennaClass> selectAllSectionsByCategoryAndDivision(Integer viennaCategory, Integer viennaDivision);

    List<CViennaClass> selectAllByCategoryDivisionAndSection(Integer viennaCategory, Integer viennaDivision, Integer viennaSection);

}