package bg.duosoft.ipas.core.service.impl;

import bg.duosoft.ipas.core.mapper.action.PublicationInfoMapper;
import bg.duosoft.ipas.core.model.CPublicationInfo;
import bg.duosoft.ipas.core.service.PublicationService;
import bg.duosoft.ipas.persistence.model.nonentity.PublicationInfoResult;
import bg.duosoft.ipas.persistence.repository.nonentity.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class PublicationServiceImpl implements PublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private PublicationInfoMapper publicationInfoMapper;

    @Override
    public List<CPublicationInfo> selectPublications(String filingNumber) {
        if (StringUtils.isEmpty(filingNumber))
            return null;

        List<PublicationInfoResult> publicationInfoResults = publicationRepository.selectPublications(filingNumber);
        if(CollectionUtils.isEmpty(publicationInfoResults))
            return null;

        return publicationInfoMapper.toCoreList(publicationInfoResults);
    }
}