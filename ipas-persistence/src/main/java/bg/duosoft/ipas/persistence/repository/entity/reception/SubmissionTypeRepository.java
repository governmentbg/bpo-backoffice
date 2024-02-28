package bg.duosoft.ipas.persistence.repository.entity.reception;

import bg.duosoft.ipas.persistence.model.entity.ext.reception.SubmissionType;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionTypeRepository extends BaseRepository<SubmissionType,Integer> {

    SubmissionType findByNameEn(String nameEn);

}
