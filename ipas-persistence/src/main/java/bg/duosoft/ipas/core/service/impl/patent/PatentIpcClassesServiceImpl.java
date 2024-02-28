package bg.duosoft.ipas.core.service.impl.patent;

import bg.duosoft.ipas.core.mapper.miscellaneous.IpcClassMapper;
import bg.duosoft.ipas.core.mapper.patent.PatentIpcClassMapper;
import bg.duosoft.ipas.core.model.patent.CPatentIpcClass;
import bg.duosoft.ipas.core.service.patent.PatentIpcClassesService;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentIpcClassesRepository;
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
public class PatentIpcClassesServiceImpl implements PatentIpcClassesService {
    @Autowired
    private IpPatentIpcClassesRepository patentIpcClassesRepository;

    @Autowired
    private PatentIpcClassMapper ipcClassMapper;

    @Override
    public long count() {
        return patentIpcClassesRepository.count();
    }

    @Override
    public List<CPatentIpcClass> findByObjectId(String fileSeq, String fileType, Integer fileSer, Integer fileNbr) {
        return ipcClassMapper.toCoreList(patentIpcClassesRepository.findByObjectId(fileSeq, fileType, fileSer, fileNbr));
    }
}
