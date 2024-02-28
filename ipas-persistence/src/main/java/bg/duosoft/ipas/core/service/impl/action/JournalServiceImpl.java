package bg.duosoft.ipas.core.service.impl.action;

import bg.duosoft.ipas.core.mapper.action.JournalMapper;
import bg.duosoft.ipas.core.model.action.CJournal;
import bg.duosoft.ipas.core.service.action.JournalService;
import bg.duosoft.ipas.persistence.model.entity.action.IpJournal;
import bg.duosoft.ipas.persistence.model.entity.user.CfThisGroup;
import bg.duosoft.ipas.persistence.repository.entity.action.IpJournalRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@LogExecutionTime
public class JournalServiceImpl implements JournalService {

    @Autowired
    private IpJournalRepository ipJournalRepository;

    @Autowired
    private JournalMapper journalMapper;

    @Override
    public CJournal selectJournal(String journalCode) {
        if (StringUtils.isEmpty(journalCode))
            return null;

        IpJournal ipJournal = ipJournalRepository.findById(journalCode).orElse(null);
        if (Objects.isNull(ipJournal))
            return null;

        return journalMapper.toCore(ipJournal);
    }

    @Override
    public Map<String, String> selectOpenJournals() {
        List<IpJournal> ipJournals = ipJournalRepository.selectOpenJournals();
        if (CollectionUtils.isEmpty(ipJournals)) {
            return null;
        }

        return ipJournals.stream().collect(Collectors.toMap(IpJournal::getJournalCode, j -> String.format("%s - %s", j.getJornalName(), j.getJournalCode()), (r1, r2) -> {
            throw new RuntimeException();
        }, TreeMap::new));
    }
}