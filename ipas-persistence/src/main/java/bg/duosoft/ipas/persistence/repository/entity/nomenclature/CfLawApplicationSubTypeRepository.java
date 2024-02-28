package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLawApplicationSubtype;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLawApplicationSubtypePK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CfLawApplicationSubTypeRepository extends BaseRepository<CfLawApplicationSubtype, CfLawApplicationSubtypePK> {

    @Query("select t from CfLawApplicationSubtype t where t.pk.applTyp = ?1 and t.pk.applSubtyp = ?2")
    CfLawApplicationSubtype findByApplicationTypeAndSubtype(String applicationType, String applicationSubtype);

    @Query("select t from CfLawApplicationSubtype t where t.pk.lawCode = ?1 and t.pk.applTyp = ?2 and t.pk.applSubtyp = ?3")
    Optional<CfLawApplicationSubtype> findByLawCodeApplicationTypeAndSubtype(Integer lawCode, String applicationType, String applicationSubType);

}
