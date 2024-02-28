package bg.duosoft.ipas.core.service.impl.enotif;

import bg.duosoft.ipas.core.mapper.mark.EnotifMapper;
import bg.duosoft.ipas.core.model.mark.CEnotif;
import bg.duosoft.ipas.core.service.enotif.EnotifService;
import bg.duosoft.ipas.persistence.model.entity.mark.Enotif;
import bg.duosoft.ipas.persistence.repository.entity.enotif.EnotifRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class EnotifServiceImpl implements EnotifService {

    @Autowired
    private EnotifRepository enotifRepository;

    @Autowired
    private EnotifMapper enotifMapper;

    @Override
    public List<CEnotif> findAllOrOrderByGaznoDesc() {
        return enotifRepository.findAllOrOrderByGaznoDesc().stream().map(r->enotifMapper.toCore(r)).collect(Collectors.toList());
    }

    @Override
    public List<CEnotif> findAllWithTopOrderByGaznoDesc() {
        return enotifRepository.findAllWithTopOrderByGaznoDesc().stream().map(r->enotifMapper.toCore(r)).collect(Collectors.toList());
    }

    @Override
    public Integer getEnotifsCount() {
        return enotifRepository.getEnotifsCount();
    }

    @Override
    public CEnotif findById(String gazno) {
        return enotifMapper.toCore(enotifRepository.findById(gazno).orElse(null));
    }
}
