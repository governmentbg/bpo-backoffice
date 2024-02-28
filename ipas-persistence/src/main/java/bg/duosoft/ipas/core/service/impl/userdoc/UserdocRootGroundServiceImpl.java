package bg.duosoft.ipas.core.service.impl.userdoc;

import bg.duosoft.ipas.core.mapper.userdoc.grounds.UserdocRootGroundsMapper;
import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocRootGrounds;
import bg.duosoft.ipas.core.service.userdoc.UserdocRootGroundService;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocRootGroundsPK;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocRootGroundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserdocRootGroundServiceImpl implements UserdocRootGroundService {
    @Autowired
    private IpUserdocRootGroundRepository ipUserdocRootGroundRepository;
    @Autowired
    private UserdocRootGroundsMapper userdocRootGroundsMapper;

    @Override
    public CUserdocRootGrounds findById(Integer rootGroundId, String docOri, String docLog, Integer docSeR, Integer docNbr) {
        IpUserdocRootGroundsPK pk = new IpUserdocRootGroundsPK();
        pk.setRootGroundId(rootGroundId);
        pk.setDocLog(docLog);
        pk.setDocNbr(docNbr);
        pk.setDocOri(docOri);
        pk.setDocSer(docSeR);
        return  userdocRootGroundsMapper.toCore(ipUserdocRootGroundRepository.findById(pk).orElse(null),true);
    }

    @Override
    public List<Object[]> findGroundMarkRelatedData(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr) {
        return ipUserdocRootGroundRepository.findGroundMarkRelatedData(fileSeq,fileTyp,fileSer,fileNbr);
    }
}
