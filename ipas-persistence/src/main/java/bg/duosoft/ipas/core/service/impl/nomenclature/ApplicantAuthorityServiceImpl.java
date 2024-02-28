package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.userdoc.grounds.ApplicantAuthorityMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.userdoc.grounds.CApplicantAuthority;
import bg.duosoft.ipas.core.service.nomenclature.ApplicantAuthorityService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfApplicantAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicantAuthorityServiceImpl implements ApplicantAuthorityService {
    @Autowired
    private ApplicantAuthorityMapper applicantAuthorityMapper;
    @Autowired
    private CfApplicantAuthorityRepository cfApplicantAuthorityRepository;
    @Override
    public List<CApplicantAuthority> findAllApplicantAuthoritiesForSpecificRight(Integer earlierRightId) {

        List<CApplicantAuthority> cApplicantAuthorities=cfApplicantAuthorityRepository.findAllApplicantAuthoritiesForSpecificRight(earlierRightId)
                .stream().map(r->applicantAuthorityMapper.toCore(r)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(cApplicantAuthorities)){
            cApplicantAuthorities.sort(Comparator.comparing(CApplicantAuthority::getName));
        }
        return cApplicantAuthorities;
    }

    @Override
    public CApplicantAuthority findById(Integer earlierRightId) {
        return applicantAuthorityMapper.toCore(cfApplicantAuthorityRepository.findById(earlierRightId).orElse(null));
    }

    @Override
    public CApplicantAuthority findByCodeAndVersion(String version, String code) {
        return applicantAuthorityMapper.toCore(cfApplicantAuthorityRepository.findByCodeAndVersion(version,code));
    }
}
