package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.mark.ViennaClassSimpleMapper;
import bg.duosoft.ipas.core.model.mark.CViennaClassSimple;
import bg.duosoft.ipas.core.service.nomenclature.ViennaClassSimpleService;
import bg.duosoft.ipas.persistence.model.nonentity.ViennaClassSimple;
import bg.duosoft.ipas.persistence.repository.nonentity.ViennaClassSimpleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ViennaClassSimpleServiceImpl implements ViennaClassSimpleService {

    @Autowired
    private ViennaClassSimpleRepository viennaClassRepository;

    @Autowired
    private ViennaClassSimpleMapper viennaClassSimpleMapper;


    @Override
    public List<CViennaClassSimple> findAllByViennaCode(String viennaCode, Integer maxResult) {
        List<ViennaClassSimple> allByViennaCode = viennaClassRepository.findAllByViennaCode(viennaCode, maxResult);
        return viennaClassSimpleMapper.toCoreList(allByViennaCode);
    }

    @Override
    public List<CViennaClassSimple> findAllByViennaCode(Integer maxResult) {
        return findAllByViennaCode("", maxResult);
    }
}