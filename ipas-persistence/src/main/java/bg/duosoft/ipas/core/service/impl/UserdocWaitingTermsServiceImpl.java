package bg.duosoft.ipas.core.service.impl;

import bg.duosoft.ipas.core.service.UserdocWaitingTermsService;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.persistence.repository.nonentity.UserdocWaitingTermsRepository;
import bg.duosoft.ipas.util.filter.MyUserdocsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserdocWaitingTermsServiceImpl implements UserdocWaitingTermsService {

    @Autowired
    private UserdocWaitingTermsRepository userdocWaitingTermsRepository;

    @Override
    public List<UserdocSimpleResult> getUserdocWaitingTermsList(MyUserdocsFilter filter) {
       return userdocWaitingTermsRepository.getUserdocWaitingTermsList(filter);
    }

    @Override
    public Integer getUserdocWaitingTermsCount(MyUserdocsFilter filter) {
        return userdocWaitingTermsRepository.getUserdocWaitingTermsCount(filter);
    }

    @Override
    public Map<String, String> getUserdocWaitingTermsUserdocTypesMap(MyUserdocsFilter filter) {
        return userdocWaitingTermsRepository.getUserdocWaitingTermsUserdocTypesMap(filter);
    }
}
