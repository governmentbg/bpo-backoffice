package bg.duosoft.ipas.core.service.impl.offidoc;

import _int.wipo.ipas.ipasservices.CDocumentImage;
import bg.duosoft.ipas.core.mapper.offidoc.OffidocIdMapper;
import bg.duosoft.ipas.core.mapper.offidoc.OffidocMapper;
import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.model.process.CProcessEvent;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.service.offidoc.OffidocService;
import bg.duosoft.ipas.core.service.report.ReportService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfConfigParam;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidoc;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidocPK;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfConfigRepository;
import bg.duosoft.ipas.persistence.repository.entity.offidoc.IpOffidocRepository;
import bg.duosoft.ipas.services.core.IpasActionService;
import bg.duosoft.ipas.services.core.IpasReportService;
import bg.duosoft.ipas.services.core.IpasServiceException;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.util.*;


@Slf4j
@Service
@Transactional
@LogExecutionTime
public class OffidocServiceImpl implements OffidocService {
    @Autowired
    private ReportService reportService;
    @Autowired
    private IpOffidocRepository ipOffidocRepository;

    @Autowired
    private OffidocIdMapper offidocIdMapper;

    @Autowired
    private OffidocMapper offidocMapper;

    @Autowired
    private IpasActionService ipasActionService;

    @Autowired
    private CfConfigRepository cfConfigRepository;


    @Override
    public COffidoc findOffidoc(String offidocOri, Integer offidocSer, Integer offidocNbr) {
        if (StringUtils.isEmpty(offidocOri) || Objects.isNull(offidocSer) || Objects.isNull(offidocNbr))
            return null;

        COffidocId id = new COffidocId(offidocOri, offidocSer, offidocNbr);
        return findOffidoc(id);
    }

    @Override
    public COffidoc findOffidoc(COffidocId cOffidocId) {
        if (Objects.isNull(cOffidocId))
            return null;

        IpOffidocPK ipOffidocPK = offidocIdMapper.toEntity(cOffidocId);
        IpOffidoc ipOffidoc = ipOffidocRepository.findById(ipOffidocPK).orElse(null);
        if (Objects.isNull(ipOffidoc))
            return null;

        return offidocMapper.toCore(ipOffidoc);
    }

    @Override
    public String findOffidocType(COffidocId cOffidocId) {
        if (Objects.isNull(cOffidocId))
            return null;

        return ipOffidocRepository.selectOffidocType(cOffidocId.getOffidocOrigin(), cOffidocId.getOffidocSeries(), cOffidocId.getOffidocNbr());
    }

    @Override
    public boolean printOffidoc(COffidocId offidocId) throws IpasServiceException {
        if (Objects.isNull(offidocId))
            return false;

        return ipasActionService.printOffidoc(offidocId.getOffidocNbr(), offidocId.getOffidocOrigin(), offidocId.getOffidocSeries(), null);
    }

    @Override
    public byte[] generateOffidocDocument(String reportPath, CActionId cActionId) throws IpasServiceException {
        if (Objects.isNull(cActionId))
            throw new RuntimeException("Action id file cannot be empty for offidoc " + cActionId.toString());

        try {
            return reportService.generateDocument(reportPath, null, null, null, Arrays.asList(cActionId), IpasReportService.CONTENT_TYPE_DOCX);
        } catch (IpasServiceException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Map<String, byte[]> generateOffidocForProcessEvent(CProcessEvent processEvent, List<String> offidocTemplates) throws IpasServiceException {
        if (Objects.isNull(processEvent))
            throw new RuntimeException("Process event is empty !");

        return generateOffidocFromProcessEvent(processEvent, offidocTemplates);
    }

    @Override
    public Map<String, byte[]> generateOffidocFilesFromTemplate(CActionId actionId, List<String> offidocTemplates) throws IpasServiceException {
        if (Objects.isNull(actionId))
            throw new RuntimeException("Action id is empty !");

        return generateOffidocFiles(offidocTemplates, actionId);
    }

    private Map<String, byte[]> generateOffidocFromProcessEvent(CProcessEvent processEvent, List<String> offidocTemplates) throws IpasServiceException {
        CActionId actionId = processEvent.getEventAction().getActionId();
        return generateOffidocFiles(offidocTemplates, actionId);
    }

    private Map<String, byte[]> generateOffidocFiles(List<String> offidocTemplates, CActionId actionId) throws IpasServiceException {
        CfConfigParam offidocTemplatesDirectory = cfConfigRepository.findById(ConfigParamService.OFFICE_DOCUMENT_TEMPLATES_DIRECTORY).orElse(null);
        if (Objects.isNull(offidocTemplatesDirectory))
            throw new RuntimeException("Cannot find offidoc template path config !");

        if (StringUtils.isEmpty(offidocTemplates))
            return null;

        Map<String, byte[]> offidocFiles = new HashMap<>();
        for (String template : offidocTemplates) {
            String templatePath = offidocTemplatesDirectory.getValue() + File.separator + template + ".doc";
            byte[] bytes = generateOffidocDocument(templatePath, actionId);
            offidocFiles.put(template, bytes);
        }
        return offidocFiles;
    }



}
