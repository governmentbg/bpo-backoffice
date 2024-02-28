package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.miscellaneous.CourtsMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CCourt;
import bg.duosoft.ipas.core.service.nomenclature.CourtsService;
import bg.duosoft.ipas.persistence.model.entity.ext.legal.Courts;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CourtsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourtsServiceImpl implements CourtsService {

    @Autowired
    private CourtsRepository courtsRepository;

    @Autowired
    private CourtsMapper courtsMapper;

    @Override
    public List<CCourt> selectByNameLike(String name) {
        if (StringUtils.isEmpty(name))
            return null;

        String namelike = "%" + name.trim() + "%";
        List<Courts> courtsList = courtsRepository.selectByNameLike(namelike);
        if (CollectionUtils.isEmpty(courtsList))
            return null;

        return courtsMapper.toCoreList(courtsList);
    }

    @Override
    public List<CCourt> selectByName(String name) {
        if (StringUtils.isEmpty(name))
            return null;

        List<Courts> courtsList = courtsRepository.findByName(name);
        if (CollectionUtils.isEmpty(courtsList))
            return null;

        return courtsMapper.toCoreList(courtsList);
    }

    @Override
    public List<CCourt> findAll() {
        return courtsRepository.findAll().stream().map(r->courtsMapper.toCore(r)).collect(Collectors.toList());
    }

    @Override
    public CCourt findById(Integer id) {
       return courtsMapper.toCore(courtsRepository.findById(id).orElse(null));
    }

}