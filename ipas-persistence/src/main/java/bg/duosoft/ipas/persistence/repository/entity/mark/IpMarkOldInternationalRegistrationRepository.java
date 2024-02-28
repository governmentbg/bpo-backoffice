package bg.duosoft.ipas.persistence.repository.entity.mark;

import bg.duosoft.ipas.persistence.model.entity.ext.core.IpMarkOldInternationalRegistration;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpMarkOldInternationalRegistrationRepository extends BaseRepository<IpMarkOldInternationalRegistration, Integer> {

    @Query("SELECT m FROM IpMarkOldInternationalRegistration m WHERE m.processedDate is null")
    List<IpMarkOldInternationalRegistration> selectUnprocessed();

}
