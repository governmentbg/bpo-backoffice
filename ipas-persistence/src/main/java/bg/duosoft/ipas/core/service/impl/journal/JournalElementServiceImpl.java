package bg.duosoft.ipas.core.service.impl.journal;

import bg.duosoft.ipas.core.mapper.journal.JournalElementMapper;
import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.journal.CJournalElement;
import bg.duosoft.ipas.core.service.journal.JournalElementService;
import bg.duosoft.ipas.persistence.model.entity.journal.JournalElement;
import bg.duosoft.ipas.persistence.model.nonentity.GenerateJournalData;
import bg.duosoft.ipas.persistence.repository.entity.journal.JournalElementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class JournalElementServiceImpl implements JournalElementService {

    private static final String DOWNLOAD_JOURNAL_PDF_URL = "/admin/downloadFile?regNum={regNum}&appNum={appNum}&elementNumber={elementNum}";

    @Autowired
    @Qualifier("noAuthRestTemplate")
    private RestTemplate restTemplate;

    private final JournalElementRepository journalElementRepository;
    private final JournalElementMapper journalElementMapper;

    @Override
    public CJournalElement selectByAction(CActionId actionId, Boolean loadPdfContent) {
        if (Objects.isNull(actionId)) {
            throw new RuntimeException("Action cannot be empty!");
        }

        JournalElement journalElement = journalElementRepository.selectByAction(actionId.getProcessId().getProcessType(), actionId.getProcessId().getProcessNbr(), actionId.getActionNbr());
        return journalElementMapper.toCore(journalElement, loadPdfContent);
    }

    @Override
    public byte[] selectNotPublishedElementFile(String journalUrl, CActionId actionId) {
        GenerateJournalData generateJournalData = selectGenerateJournalData(actionId);

        if (Objects.isNull(generateJournalData)) {
            throw new RuntimeException("Cannot generate journal - params data is empty");
        }

        ResponseEntity<byte[]> contentEntity =
                restTemplate.exchange(getDownloadUrl(journalUrl, generateJournalData),
                        HttpMethod.GET, new HttpEntity(new HttpHeaders()), byte[].class);
        return contentEntity.getBody();
    }

    private String getDownloadUrl(String journalUrl, GenerateJournalData generateJournalData) {
        String fullUrl = journalUrl + DOWNLOAD_JOURNAL_PDF_URL;
        return fullUrl.replace("{appNum}", generateJournalData.getApplicationNbr()).replace("{elementNum}", String.valueOf(generateJournalData.getElementNbr())).replace("{regNum}", Objects.nonNull(generateJournalData.getRegistrationNbr()) ? String.valueOf(generateJournalData.getRegistrationNbr()) : "");
    }

    private GenerateJournalData selectGenerateJournalData(CActionId actionId) {
        if (Objects.isNull(actionId)) {
            return null;
        }

        return journalElementRepository.selectJournalParamsByAction(actionId.getProcessId().getProcessType(), actionId.getProcessId().getProcessNbr(), actionId.getActionNbr());
    }
}
