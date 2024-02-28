package bg.duosoft.ipas.persistence.repository.nonentity;

import bg.duosoft.ipas.persistence.model.nonentity.PublicationInfoResult;

import java.util.List;

public interface PublicationRepository {

    List<PublicationInfoResult> selectPublications(String filingNumber);

}
