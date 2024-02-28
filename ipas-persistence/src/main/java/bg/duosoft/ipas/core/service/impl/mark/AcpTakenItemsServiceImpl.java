package bg.duosoft.ipas.core.service.impl.mark;

import bg.duosoft.ipas.core.service.mark.AcpTakenItemsService;
import bg.duosoft.ipas.persistence.repository.entity.mark.AcpTakenItemsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class AcpTakenItemsServiceImpl implements AcpTakenItemsService {

    @Autowired
    private AcpTakenItemsRepository acpTakenItemsRepository;



}
