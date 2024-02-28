package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.mark.ViennaClassMapper;
import bg.duosoft.ipas.core.model.mark.CViennaClass;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.service.nomenclature.ViennaClassService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassViennaCateg;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassViennaDivis;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassViennaSect;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfClassViennaCategoryRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfClassViennaDivisionRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfClassViennaSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ViennaClassesServiceImpl implements ViennaClassService {

    @Autowired
    private CfClassViennaCategoryRepository cfClassViennaCategoryRepository;

    @Autowired
    private CfClassViennaDivisionRepository cfClassViennaDivisionRepository;

    @Autowired
    private CfClassViennaSectionRepository cfClassViennaSectionRepository;

    @Autowired
    private ViennaClassMapper viennaClassMapper;


    private void sortVienaClassListByVienaCategory(List<CViennaClass> vienaClasses){
        if (!CollectionUtils.isEmpty(vienaClasses)){
            vienaClasses.sort(Comparator.comparing(CViennaClass::getViennaCategory));
        }
    }

    @Override
    public List<CViennaClass> selectAllCategories() {
        List<CfClassViennaCateg> viennaCategories = cfClassViennaCategoryRepository.findAll();
        if (CollectionUtils.isEmpty(viennaCategories))
            return null;

        List<CViennaClass> vienaClasses = viennaCategories.stream()
                .map(category -> viennaClassMapper.toCore(category))
                .collect(Collectors.toList());

        sortVienaClassListByVienaCategory(vienaClasses);
        return vienaClasses;
    }

    @Override
    public List<CViennaClass> selectAllDivisionsByCategory(Integer viennaCategory) {
        if (Objects.isNull(viennaCategory))
            return null;

        List<CfClassViennaDivis> divisions = cfClassViennaDivisionRepository.findAllByPk_ViennaCategoryCode(Long.valueOf(viennaCategory));
        if (CollectionUtils.isEmpty(divisions))
            return null;

        List<CViennaClass> vienaClasses =divisions.stream()
                .map(division -> viennaClassMapper.toCore(division))
                .collect(Collectors.toList());

        sortVienaClassListByVienaCategory(vienaClasses);
        return vienaClasses;
    }

    @Override
    public List<CViennaClass> selectAllSectionsByCategoryAndDivision(Integer viennaCategory, Integer viennaDivision) {
        if (Objects.isNull(viennaCategory) || Objects.isNull(viennaDivision))
            return null;

        List<CfClassViennaSect> sections = cfClassViennaSectionRepository.findAllByPk_ViennaCategoryCodeAndPk_ViennaDivisionCode(Long.valueOf(viennaCategory), Long.valueOf(viennaDivision));
        if (CollectionUtils.isEmpty(sections))
            return null;

        List<CViennaClass> vienaClasses = sections.stream()
                .map(section -> viennaClassMapper.toCore(section))
                .collect(Collectors.toList());

        sortVienaClassListByVienaCategory(vienaClasses);
        return vienaClasses;
    }

    @Override
    public List<CViennaClass> selectAllByCategoryDivisionAndSection(Integer viennaCategory, Integer viennaDivision, Integer viennaSection) {
        if (Objects.isNull(viennaCategory) || Objects.isNull(viennaDivision) || Objects.isNull(viennaSection))
            return null;

        List<CfClassViennaSect> sections = cfClassViennaSectionRepository.findAllByPk_ViennaCategoryCodeAndPk_ViennaDivisionCodeAndPk_ViennaSectionCode(Long.valueOf(viennaCategory), Long.valueOf(viennaDivision), Long.valueOf(viennaSection));
        if (CollectionUtils.isEmpty(sections))
            return null;

        return sections.stream()
                .map(section -> viennaClassMapper.toCore(section))
                .collect(Collectors.toList());
    }
}