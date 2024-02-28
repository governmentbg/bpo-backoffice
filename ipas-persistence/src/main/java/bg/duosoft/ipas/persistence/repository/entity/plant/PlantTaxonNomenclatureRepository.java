package bg.duosoft.ipas.persistence.repository.entity.plant;

import bg.duosoft.ipas.persistence.model.entity.ext.plant.PlantTaxonNomenclature;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface PlantTaxonNomenclatureRepository extends BaseRepository<PlantTaxonNomenclature, Long>, Serializable {

    @Query("SELECT plt from PlantTaxonNomenclature  plt WHERE CONCAT(plt.commonClassifyBul,plt.commonClassifyEng,plt.latinClassify) like :taxonNomData")
    List<PlantTaxonNomenclature> findPlantTaxonNomenclatureByCommonClassifyBulAndCommonClassifyEngAndLatinClassify(@Param("taxonNomData") String taxonNomData, Pageable pageable);

}
