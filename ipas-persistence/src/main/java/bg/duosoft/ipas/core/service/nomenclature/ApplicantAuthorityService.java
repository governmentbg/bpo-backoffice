package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.userdoc.grounds.CApplicantAuthority;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicantAuthority;

import java.util.List;

public interface ApplicantAuthorityService {

    List<CApplicantAuthority> findAllApplicantAuthoritiesForSpecificRight(Integer earlierRightId);
    CApplicantAuthority findById(Integer earlierRightId);
    CApplicantAuthority findByCodeAndVersion(String version, String code);
}
