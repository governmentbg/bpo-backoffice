package bg.duosoft.ipas.core.service.impl.person;

import bg.duosoft.ipas.core.mapper.person.PersonAbdocsSyncMapper;
import bg.duosoft.ipas.core.model.person.CPersonAbdocsSync;
import bg.duosoft.ipas.core.service.person.AbdocsSyncPersonService;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpPersonAbdocsSync;
import bg.duosoft.ipas.persistence.repository.entity.person.IpPersonAbdocsSyncRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
public class AbdocsSyncPersonServiceImpl implements AbdocsSyncPersonService {

    @Autowired
    private IpPersonAbdocsSyncRepository ipPersonAbdocsSyncRepository;

    @Autowired
    private PersonAbdocsSyncMapper personAbdocsSyncMapper;

    @Override
    public List<CPersonAbdocsSync> selectNotProcessed() {
        List<IpPersonAbdocsSync> ipPersonAbdocsSyncs = ipPersonAbdocsSyncRepository.selectNotProcessed();
        if (CollectionUtils.isEmpty(ipPersonAbdocsSyncs))
            return null;

        return personAbdocsSyncMapper.toCoreList(ipPersonAbdocsSyncs);
    }

    @Override
    public CPersonAbdocsSync update(CPersonAbdocsSync record) {
        if (Objects.isNull(record))
            return null;

        IpPersonAbdocsSync ipPersonAbdocsSync = personAbdocsSyncMapper.toEntity(record);
        IpPersonAbdocsSync resultEntity = ipPersonAbdocsSyncRepository.save(ipPersonAbdocsSync);
        return personAbdocsSyncMapper.toCore(resultEntity);
    }

    @Override
    public CPersonAbdocsSync markAsProcessedAndSynchronized(CPersonAbdocsSync record) {
        if (Objects.isNull(record))
            return null;

        record.setIndSync(true);
        record.setProcessedAt(new Date());
        return update(record);
    }

    @Override
    public CPersonAbdocsSync markAsProcessedAndNotSynchronized(CPersonAbdocsSync record) {
        if (Objects.isNull(record))
            return null;

        record.setIndSync(false);
        record.setProcessedAt(new Date());
        return update(record);
    }
}
