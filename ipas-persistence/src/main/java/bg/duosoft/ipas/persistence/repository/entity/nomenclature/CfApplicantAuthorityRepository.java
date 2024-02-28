package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicantAuthority;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CfApplicantAuthorityRepository extends BaseRepository<CfApplicantAuthority, Integer> {

    @Query(value="SELECT auth.* FROM EXT_CORE.CF_APPLICANT_AUTHORITY auth" +
            " INNER JOIN EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY authto on auth.id = authto.applicant_authority_type_id" +
            " WHERE authto.earlier_right_type_id = ?1", nativeQuery = true)
    List<CfApplicantAuthority> findAllApplicantAuthoritiesForSpecificRight(Integer earlierRightId);

    @Query(value="SELECT auth.* FROM EXT_CORE.CF_APPLICANT_AUTHORITY auth" +
            " WHERE auth.version = ?1 and auth.applicant_code = ?2 ", nativeQuery = true)
    CfApplicantAuthority findByCodeAndVersion(String version,String code);
}
