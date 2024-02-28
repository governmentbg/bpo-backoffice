package bg.duosoft.ipas.core.service.impl;

import bg.duosoft.ipas.core.service.UserdocCorrespondenceTermsService;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.persistence.repository.nonentity.UserdocCorrespondenceTermsRepository;
import bg.duosoft.ipas.util.filter.MyUserdocsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserdocCorrespondenceTermsServiceImpl implements UserdocCorrespondenceTermsService {

    @Autowired
    private UserdocCorrespondenceTermsRepository userdocCorrespondenceTermsRepository;

    @Override
    public List<UserdocSimpleResult> getUserdocCorrespondenceTermsList(MyUserdocsFilter filter) {
        return userdocCorrespondenceTermsRepository.getUserdocCorrespondenceTermsList(filter);
    }

    @Override
    public Integer getUserdocCorrespondenceTermsCount(MyUserdocsFilter filter) {
        return userdocCorrespondenceTermsRepository.getUserdocCorrespondenceTermsCount(filter);
    }

    @Override
    public Map<String, String> getUserdocCorrespondenceTermsUserdocTypesMap(MyUserdocsFilter filter) {
        return userdocCorrespondenceTermsRepository.getUserdocCorrespondenceTermsUserdocTypesMap(filter);
    }

    @Override
    public Map<String, String> getStatuses(MyUserdocsFilter filter) {
        return userdocCorrespondenceTermsRepository.getStatuses(filter);
    }

    @Override
    public Map<String, String> getUserdocCorrespondenceTermsObjectType(MyUserdocsFilter filter) {
        return userdocCorrespondenceTermsRepository.getUserdocCorrespondenceTermsObjectType(filter);
    }
}
