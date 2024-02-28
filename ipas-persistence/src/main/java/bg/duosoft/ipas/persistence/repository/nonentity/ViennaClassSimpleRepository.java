package bg.duosoft.ipas.persistence.repository.nonentity;

import bg.duosoft.ipas.persistence.model.nonentity.ViennaClassSimple;

import java.util.List;

public interface ViennaClassSimpleRepository {

    List<ViennaClassSimple> findAllByViennaCode(String viennaCode, Integer maxResult);

}
