package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.plant.PlantTaxonNomenclatureMapper;
import bg.duosoft.ipas.core.model.plant.CPlantTaxonNomenclature;
import bg.duosoft.ipas.core.service.nomenclature.PlantTaxonNomenclatureService;
import bg.duosoft.ipas.persistence.model.entity.ext.plant.PlantTaxonNomenclature;
import bg.duosoft.ipas.persistence.repository.entity.plant.PlantTaxonNomenclatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlantTaxonNomenclatureServiceImpl implements PlantTaxonNomenclatureService {

    @Autowired
    private PlantTaxonNomenclatureMapper plantTaxonNomenclatureMapper;

    @Autowired
    private PlantTaxonNomenclatureRepository plantTaxonNomenclatureRepository;

    @Override
    public List<CPlantTaxonNomenclature> findCPlantTaxonNomenclatureByCommonClassifyBulAndCommonClassifyEngAndLatinClassify(String taxonNomData, int maxResults) {
        Pageable pageable= PageRequest.of(0,maxResults);

        return plantTaxonNomenclatureRepository.findPlantTaxonNomenclatureByCommonClassifyBulAndCommonClassifyEngAndLatinClassify("%"+taxonNomData+"%",pageable)
                .stream()
                .map(r-> plantTaxonNomenclatureMapper.toCore(r))
                .collect(Collectors.toList());
    }

    @Override
    public CPlantTaxonNomenclature findById(Long id) {
        PlantTaxonNomenclature plantTaxonNomenclature = plantTaxonNomenclatureRepository.findById(id).orElseThrow(EntityNotFoundException::new);;
        return plantTaxonNomenclatureMapper.toCore(plantTaxonNomenclature);
    }
}
