package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.userdoc.grounds.CLegalGroundCategories;

import java.util.List;

public interface LegalGroundCategoriesService {
    List<CLegalGroundCategories> findAll();
    CLegalGroundCategories findById(String code);
}
