package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.mark.CViennaClassSimple;

import java.util.List;

public interface ViennaClassSimpleService {

    List<CViennaClassSimple> findAllByViennaCode(String viennaCode, Integer maxResult);

    List<CViennaClassSimple> findAllByViennaCode(Integer maxResult);

}