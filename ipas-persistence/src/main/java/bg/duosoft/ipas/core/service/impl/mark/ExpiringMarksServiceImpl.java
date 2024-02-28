package bg.duosoft.ipas.core.service.impl.mark;

import bg.duosoft.ipas.core.service.mark.ExpiringMarksService;
import bg.duosoft.ipas.persistence.model.nonentity.ExpiringMarkResult;
import bg.duosoft.ipas.persistence.repository.nonentity.ExpiringMarksRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@LogExecutionTime
@RequiredArgsConstructor
public class ExpiringMarksServiceImpl implements ExpiringMarksService {

    private final ExpiringMarksRepository expiringMarksRepository;

    @Override
    public List<ExpiringMarkResult> selectExpiringMarks(Date expirationDateFrom, Date expirationDateTo) {
        if (Objects.isNull(expirationDateFrom) || Objects.isNull(expirationDateTo))
            return null;

        return expiringMarksRepository.selectExpiringMarks(expirationDateFrom, expirationDateTo);
    }
}
