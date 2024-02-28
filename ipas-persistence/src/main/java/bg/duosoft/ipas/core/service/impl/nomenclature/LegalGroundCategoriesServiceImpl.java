package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.userdoc.grounds.LegalGroundCategoriesMapper;
import bg.duosoft.ipas.core.model.userdoc.grounds.CLegalGroundCategories;
import bg.duosoft.ipas.core.service.nomenclature.LegalGroundCategoriesService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfLegalGroundCategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LegalGroundCategoriesServiceImpl implements LegalGroundCategoriesService {
    @Autowired
    private CfLegalGroundCategoriesRepository cfLegalGroundCategoriesRepository;
    @Autowired
    private LegalGroundCategoriesMapper legalGroundCategoriesMapper;

    @Override
    public List<CLegalGroundCategories> findAll() {
        return cfLegalGroundCategoriesRepository.findAll().stream().map(r->legalGroundCategoriesMapper.toCore(r)).collect(Collectors.toList());
    }

    @Override
    public CLegalGroundCategories findById(String code) {
        return legalGroundCategoriesMapper.toCore(cfLegalGroundCategoriesRepository.findById(code).orElse(null));
    }
}
