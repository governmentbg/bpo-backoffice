package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.mark.NiceClassListMapper;
import bg.duosoft.ipas.core.model.mark.CNiceClassList;
import bg.duosoft.ipas.core.service.nomenclature.NiceListService;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfNiceClassList;
import bg.duosoft.ipas.persistence.repository.entity.ext.CfNiceClassListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by Raya
 * 23.09.2020
 */
@Service
@Transactional
public class NiceListServiceImpl implements NiceListService {

    @Autowired
    private NiceClassListMapper mapper;

    @Autowired
    private CfNiceClassListRepository repository;

    @Override
    public CNiceClassList getNiceList(Integer niceClassCode) {
        Optional<CfNiceClassList> entityOptional = repository.findById(niceClassCode);
        if(entityOptional.isPresent()){
            return mapper.toCore(entityOptional.get());
        }
        return null;
    }

    @Override
    public CNiceClassList saveNiceList(CNiceClassList cNiceClassList) {
        CfNiceClassList entity = mapper.toEntity(cNiceClassList);
        CfNiceClassList saved = repository.save(entity);
        return mapper.toCore(saved);
    }
}
