package bg.duosoft.ipas.persistence.repository.entity.person;

import bg.duosoft.ipas.persistence.model.entity.person.IpPerson;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpPersonRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpPersonRepository extends BaseRepository<IpPerson, Integer>, IpPersonRepositoryCustom {

    @Query(value = "SELECT * FROM IP_PERSON p where UPPER(p.PERSON_NAME) like UPPER(?1) ORDER BY p.PERSON_NAME ", nativeQuery = true)
    List<IpPerson> selectPersonsByNameLike(String nameLike, Pageable pageable);

    @Query(value = "SELECT * FROM IP_PERSON p\n" +
            "LEFT JOIN IP_AGENT a on p.AGENT_CODE = a.AGENT_CODE\n" +
            "LEFT JOIN EXT_AGENT.EXTENDED_PARTNERSHIP ep on p.PERSON_NBR = ep.PERSON_NBR\n" +
            "where UPPER(p.PERSON_NAME) like UPPER(?1) AND (a.AGENT_CODE is not null OR ep.PARTNERSHIP_CODE is not null) ORDER BY p.PERSON_NAME ", nativeQuery = true)
    List<IpPerson> selectRepresentativesByNameLike(String nameLike, Pageable pageable);

    @Query(value = "SELECT * FROM IP_PERSON p\n" +
            "LEFT JOIN EXT_AGENT.EXTENDED_PARTNERSHIP ep on p.PERSON_NBR = ep.PERSON_NBR\n" +
            "WHERE ep.PARTNERSHIP_CODE = ?1 ", nativeQuery = true)
    IpPerson selectRepresentativeByPartnershipCode(String code);

    @Query(value = "SELECT * FROM IP_PERSON p\n" +
            "LEFT JOIN IP_AGENT a ON a.AGENT_CODE = p.AGENT_CODE\n" +
            "WHERE a.AGENT_CODE = ?1", nativeQuery = true)
    IpPerson selectRepresentativeByAgentCode(Integer code);

}
