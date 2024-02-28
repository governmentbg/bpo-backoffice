package bg.duosoft.ipas.persistence.repository.entity.person;

import bg.duosoft.ipas.core.model.person.CPersonId;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkNiceClassesPK;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddressesPK;
import bg.duosoft.ipas.persistence.model.nonentity.SimplePersonAddressResult;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpPersonAddressRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpPersonAdressesRepository extends BaseRepository<IpPersonAddresses, IpPersonAddressesPK>, IpPersonAddressRepositoryCustom {

    List<IpPersonAddresses> findAllByIpPerson_GralPersonIdNbr(Integer gralPersonIdNbr);

    @Query("SELECT new bg.duosoft.ipas.core.model.person.CPersonId (d.pk.personNbr,d.pk.addrNbr) FROM IpPersonAddresses d where d.pk.addrNbr > 1 ORDER BY d.pk.personNbr ASC, d.pk.addrNbr DESC")
    List<CPersonId> selectIdentifiersOfAllNotMainPersonAddresses();

    @Query("SELECT new bg.duosoft.ipas.persistence.model.nonentity.SimplePersonAddressResult (d.pk.personNbr,d.pk.addrNbr,p.personName,d.addrStreet,d.cityName) FROM IpPersonAddresses d JOIN IpPerson  p on p.personNbr = d.ipPerson.personNbr ORDER BY d.pk.personNbr ASC, d.pk.addrNbr DESC")
    List<SimplePersonAddressResult> selectAllSimple(Pageable pageable);

    @Query(value = "SELECT pa.pk FROM IpPersonAddresses pa")
    List<IpPersonAddressesPK> listIpPersonAddressPK();
}
