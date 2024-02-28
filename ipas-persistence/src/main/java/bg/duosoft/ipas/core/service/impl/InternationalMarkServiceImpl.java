package bg.duosoft.ipas.core.service.impl;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.InternationalMarkService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.persistence.model.nonentity.IPMarkSimpleResult;
import bg.duosoft.ipas.persistence.model.nonentity.InternationalMarkSimpleResult;
import bg.duosoft.ipas.persistence.repository.nonentity.InternationalMarkRepository;
import bg.duosoft.ipas.util.filter.InternationalMarkFilter;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.mark.InternationalMarkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional
public class InternationalMarkServiceImpl implements InternationalMarkService {
    @Autowired
    private InternationalMarkRepository internationalMarkRepository;

    @Override
    public List<IPMarkSimpleResult> getInternationalMarksList(InternationalMarkFilter filter) {
        return internationalMarkRepository.getInternationalMarksList(filter);
    }

    @Override
    public Integer getInternationalMarksCount(InternationalMarkFilter filter) {
        return internationalMarkRepository.getInternationalMarksCount(filter);
    }

    @Override
    public List<InternationalMarkSimpleResult> selectInternationalRegistrations(String registrationNumberWithDup) {
        Pair<Optional<Integer>, Optional<String>> registrationNumberAndDup = InternationalMarkUtils.separateRegistrationNumberAndDup(registrationNumberWithDup);
        Integer registrationNbr = Objects.nonNull(registrationNumberAndDup) ? registrationNumberAndDup.getFirst().orElse(null) : null;
        String registrationDup = Objects.nonNull(registrationNumberAndDup) ? registrationNumberAndDup.getSecond().orElse(null) : null;
        return internationalMarkRepository.selectInternationalMarksAutocompleteResult(registrationNbr, registrationDup, FileType.getInternationalMarkFileTypes());
    }

    @Override
    public InternationalMarkSimpleResult selectInternationalRegistration(String filingNumber) {
        if (!StringUtils.hasText(filingNumber)) {
            return null;
        }

        CFileId fileId = BasicUtils.createCFileId(filingNumber);
        return internationalMarkRepository.selectInternationalMarkByFileId(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
    }
}
