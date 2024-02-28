package bg.duosoft.ipas.core.service.impl.offidoc;

import bg.duosoft.ipas.core.mapper.offidoc.OffidocPublishedDecisionMapper;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.model.offidoc.COffidocPublishedDecision;
import bg.duosoft.ipas.core.service.offidoc.OffidocPublishedDecisionService;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidocPK;
import bg.duosoft.ipas.persistence.repository.entity.offidoc.OffidocPublishedDecisionRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@Transactional
@LogExecutionTime
public class OffidocPublishedDecisionServiceImpl implements OffidocPublishedDecisionService {

    @Autowired
    private OffidocPublishedDecisionRepository offidocPublishedDecisionRepository;

    @Autowired
    private OffidocPublishedDecisionMapper offidocPublishedDecisionMapper;

    @Override
    public COffidocPublishedDecision findById(COffidocId offidocId, Boolean loadFileContent) {
        IpOffidocPK pk = new IpOffidocPK(offidocId.getOffidocOrigin(), offidocId.getOffidocSeries(), offidocId.getOffidocNbr());
        COffidocPublishedDecision result = offidocPublishedDecisionMapper.toCore(offidocPublishedDecisionRepository.findById(pk).orElse(null), loadFileContent);
        return result;
    }


    @Override
    public void update(COffidocId offidocId,  COffidocPublishedDecision offidocDecision) {
        offidocPublishedDecisionRepository.delete(offidocId.getOffidocOrigin(),offidocId.getOffidocSeries(),offidocId.getOffidocNbr());
        offidocPublishedDecisionRepository.insert(offidocDecision.getAttachmentName(),offidocDecision.getAttachmentContent(),offidocId.getOffidocOrigin(),offidocId.getOffidocSeries()
                ,offidocId.getOffidocNbr(),offidocDecision.getDecisionDate());
    }

    @Override
    public void delete(COffidocId offidocId) {
        offidocPublishedDecisionRepository.delete(offidocId.getOffidocOrigin(),offidocId.getOffidocSeries(),offidocId.getOffidocNbr());
    }

}
