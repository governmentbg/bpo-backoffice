package bg.duosoft.ipas.core.service.impl.userdoc;

import bg.duosoft.ipas.core.mapper.userdoc.UserdocRegNumberChangeLogMapper;
import bg.duosoft.ipas.core.model.userdoc.CUserdocRegNumberChangeLog;
import bg.duosoft.ipas.core.service.userdoc.UserdocRegNumberChangeLogService;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocRegNumberChangeLog;
import bg.duosoft.ipas.persistence.model.nonentity.SimpleUserdocRegNumberChangeLog;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocRegNumberChangeLogRepository;
import bg.duosoft.ipas.util.filter.UserdocRegNumberChangeLogFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
public class UserdocRegNumberChangeLogServiceImpl implements UserdocRegNumberChangeLogService {

    @Autowired
    private UserdocRegNumberChangeLogMapper userdocRegNumberChangeLogMapper;

    @Autowired
    private IpUserdocRegNumberChangeLogRepository ipUserdocRegNumberChangeLogRepository;

    @Override
    public CUserdocRegNumberChangeLog save(CUserdocRegNumberChangeLog log) {
        if (Objects.isNull(log))
            return null;

        IpUserdocRegNumberChangeLog entity = userdocRegNumberChangeLogMapper.toEntity(log);
        IpUserdocRegNumberChangeLog save = ipUserdocRegNumberChangeLogRepository.save(entity);
        return userdocRegNumberChangeLogMapper.toCore(save);
    }

    @Override
    public List<SimpleUserdocRegNumberChangeLog> getUserdocRegNumberChangeLogList(UserdocRegNumberChangeLogFilter filter) {
        List<SimpleUserdocRegNumberChangeLog> userdocRegNumberChangeLogList = ipUserdocRegNumberChangeLogRepository.getSimpleUserdocRegNumberChangeLogList(filter);
        if (CollectionUtils.isEmpty(userdocRegNumberChangeLogList)) {
            return null;
        }
        return userdocRegNumberChangeLogList;
    }

    @Override
    public Integer getUserdocRegNumberChangeLogCount(UserdocRegNumberChangeLogFilter filter) {
        return ipUserdocRegNumberChangeLogRepository.getSimpleUserdocRegNumberChangeLogCount(filter);
    }
}
