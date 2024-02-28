package bg.duosoft.ipas.core.service.impl.userdoc;

import bg.duosoft.ipas.core.mapper.userdoc.UserdocPersonMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.service.userdoc.UserdocPersonService;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPerson;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocPersonRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
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
@LogExecutionTime
public class UserdocPersonServiceImpl implements UserdocPersonService {

    @Autowired
    private IpUserdocPersonRepository ipUserdocPersonRepository;

    @Autowired
    private UserdocPersonMapper userdocPersonMapper;

    @Override
    public CUserdocPerson savePerson(CDocumentId documentId, CUserdocPerson person) {
        if (Objects.isNull(person))
            return null;

        IpUserdocPerson ipUserdocPerson = userdocPersonMapper.toEntity(person);
        setDocPrimaryKey(ipUserdocPerson, documentId);
        IpUserdocPerson save = ipUserdocPersonRepository.save(ipUserdocPerson);
        return userdocPersonMapper.toCore(save);
    }

    @Override
    public List<CUserdocPerson> savePersonList(CDocumentId documentId, List<CUserdocPerson> personList) {
        if (CollectionUtils.isEmpty(personList))
            return null;

        List<IpUserdocPerson> ipUserdocPeople = userdocPersonMapper.toEntityList(personList);
        ipUserdocPeople.forEach(r -> setDocPrimaryKey(r, documentId));
        List<IpUserdocPerson> save = ipUserdocPersonRepository.saveAll(ipUserdocPeople);
        return userdocPersonMapper.toCoreList(save);
    }

    @Override
    public long count() {
        return ipUserdocPersonRepository.count();
    }

    public int countByRole(CDocumentId documentId, UserdocPersonRole role) {
        return ipUserdocPersonRepository.countByRole(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr(), role.name());
    }

    private void setDocPrimaryKey(IpUserdocPerson p, CDocumentId pk) {
        p.getPk().setDocOri(pk.getDocOrigin());
        p.getPk().setDocLog(pk.getDocLog());
        p.getPk().setDocSer(pk.getDocSeries());
        p.getPk().setDocNbr(pk.getDocNbr());
    }
}
