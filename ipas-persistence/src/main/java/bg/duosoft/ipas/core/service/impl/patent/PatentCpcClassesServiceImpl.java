package bg.duosoft.ipas.core.service.impl.patent;

import bg.duosoft.ipas.core.mapper.patent.PatentCpcClassMapper;
import bg.duosoft.ipas.core.model.patent.CPatentCpcClass;
import bg.duosoft.ipas.core.service.patent.PatentCpcClassesService;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentCpcClassesRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@LogExecutionTime
public class PatentCpcClassesServiceImpl implements PatentCpcClassesService {
    @Autowired
    private IpPatentCpcClassesRepository patentCpcClassesRepository;

    @Autowired
    private PatentCpcClassMapper cpcClassMapper;

    @Override
    public long count() {
        return patentCpcClassesRepository.count();
    }

    @Override
    public List<CPatentCpcClass> findByObjectId(String fileSeq, String fileType, Integer fileSer, Integer fileNbr) {
        return cpcClassMapper.toCoreList(patentCpcClassesRepository.findByObjectId(fileSeq, fileType, fileSer, fileNbr));
    }
}
