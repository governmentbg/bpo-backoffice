package bg.duosoft.ipas.core.service.impl.mark;

import bg.duosoft.ipas.core.mapper.mark.NiceClassListMapper;
import bg.duosoft.ipas.core.model.mark.CNiceClassList;
import bg.duosoft.ipas.core.service.mark.NiceClassListService;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfNiceClassList;
import bg.duosoft.ipas.persistence.repository.entity.ext.CfNiceClassListRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
@LogExecutionTime
public class NiceClassListServiceImpl implements NiceClassListService {

    @Autowired
    private CfNiceClassListRepository cfNiceClassListRepository;

    @Autowired
    private NiceClassListMapper niceClassListMapper;


    @Override
    public List<Integer> getNiceClassesCodes() {
        return cfNiceClassListRepository.getNiceClassesCodes();
    }

    @Override
    public CNiceClassList selectByNiceClassCode(Integer niceClassCode) {
        if (Objects.isNull(niceClassCode)) {
            return null;
        }

        CfNiceClassList result = cfNiceClassListRepository.findById(niceClassCode).orElse(null);
        if (Objects.isNull(result)) {
            return null;
        }

        return niceClassListMapper.toCore(result);
    }

    @Override
    public CNiceClassList saveNiceClassList(CNiceClassList cNiceClassList) {
        CfNiceClassList cfNiceClassList = niceClassListMapper.toEntity(cNiceClassList);
        CfNiceClassList updated = cfNiceClassListRepository.save(cfNiceClassList);
        return niceClassListMapper.toCore(updated);
    }
}
