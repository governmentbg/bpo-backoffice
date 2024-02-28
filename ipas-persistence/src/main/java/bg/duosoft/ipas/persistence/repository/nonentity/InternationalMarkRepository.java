package bg.duosoft.ipas.persistence.repository.nonentity;

import bg.duosoft.ipas.persistence.model.nonentity.IPMarkSimpleResult;
import bg.duosoft.ipas.persistence.model.nonentity.InternationalMarkSimpleResult;
import bg.duosoft.ipas.util.filter.InternationalMarkFilter;

import java.util.List;

public interface InternationalMarkRepository {
    List<IPMarkSimpleResult> getInternationalMarksList(InternationalMarkFilter filter);

    Integer getInternationalMarksCount(InternationalMarkFilter filter);

    List<InternationalMarkSimpleResult> selectInternationalMarksAutocompleteResult(Integer registrationNbr, String registrationDup, List<String> internationalFileTypes);

    InternationalMarkSimpleResult selectInternationalMarkByFileId(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);
}
