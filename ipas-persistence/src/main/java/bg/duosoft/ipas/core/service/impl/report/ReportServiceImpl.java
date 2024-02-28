package bg.duosoft.ipas.core.service.impl.report;

import _int.wipo.ipas.ipasservices.CDocumentImage;
import _int.wipo.ipas.ipasservices.COfficedocId;
import _int.wipo.ipas.ipasservices.CProcessId;
import bg.duosoft.ipas.IpasDataUtils;
import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfConfigRepository;
import bg.duosoft.ipas.services.core.IpasServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import bg.duosoft.ipas.core.service.report.ReportService;
import bg.duosoft.ipas.services.core.IpasReportService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static bg.duosoft.ipas.core.service.nomenclature.ConfigParamService.REPORT_DOCUMENT_TEMPLATES_DIRECTORY;

/**
 * User: Georgi
 * Date: 23.9.2020 г.
 * Time: 15:19
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private IpasReportService ipasReportService;

    @Autowired
    private CfConfigRepository cfConfigRepository;


    public List<String> getReportTemplateFileNames() {
        try {
            String reportsFolder = cfConfigRepository.findById(REPORT_DOCUMENT_TEMPLATES_DIRECTORY).orElseThrow(() -> new RuntimeException(REPORT_DOCUMENT_TEMPLATES_DIRECTORY + " parameter should exist in the CF_CONFIG_PARAM table!")).getValue();
            Path dir = Paths.get(reportsFolder);
            if (!Files.isDirectory(dir)) {
                throw new RuntimeException("Directory " + reportsFolder + " should exist!");
            }
            return Files.list(dir).map(r -> r.toString()).sorted(Comparator.comparing(String::toUpperCase)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] generateDocument(String reportPath, List<CFileId> cFileId, List<CDocumentId> cDocumentId, List<COffidocId> cOffidocId, List<CActionId> cActionId, String contentType) throws IpasServiceException {
        CDocumentImage res = ipasReportService.generateDocument(reportPath, generateCFileIds(cFileId), generateDocumentIds(cDocumentId), generateActionIds(cActionId), generateOffidocIds(cOffidocId), contentType);
        return res.getContent();
    }
    private List<_int.wipo.ipas.ipasservices.CFileId> generateCFileIds(List<CFileId> fileIds) {
        List<_int.wipo.ipas.ipasservices.CFileId> res = null;
        if (!CollectionUtils.isEmpty(fileIds)) {
            res = new ArrayList<>();
            for (CFileId cFileId : fileIds) {
                if (cFileId == null) {
                    continue;
                }
                _int.wipo.ipas.ipasservices.CFileId fid = new _int.wipo.ipas.ipasservices.CFileId();
                fid.setFileSeq(cFileId.getFileSeq());
                fid.setFileType(cFileId.getFileType());
                fid.setFileSeries(IpasDataUtils.generateIpasInteger(cFileId.getFileSeries()));
                fid.setFileNbr(IpasDataUtils.generateIpasInteger(cFileId.getFileNbr()));
                res.add(fid);
            }
        }
        return res;
    }
    private List<_int.wipo.ipas.ipasservices.CDocumentId> generateDocumentIds(List<CDocumentId> cDocumentIds) {
        List<_int.wipo.ipas.ipasservices.CDocumentId> res = null;
        if (!CollectionUtils.isEmpty(cDocumentIds)) {
            res = new ArrayList<>();
            for (CDocumentId cDocumentId : cDocumentIds) {
                if (cDocumentId == null) {
                    continue;
                }
                _int.wipo.ipas.ipasservices.CDocumentId docId = new _int.wipo.ipas.ipasservices.CDocumentId();
                docId.setDocOrigin(cDocumentId.getDocOrigin());
                docId.setDocLog(cDocumentId.getDocLog());
                docId.setDocSeries(IpasDataUtils.generateIpasInteger(cDocumentId.getDocSeries()));
                docId.setDocNbr(IpasDataUtils.generateIpasInteger(cDocumentId.getDocNbr()));
                res.add(docId);
            }
        }
        return res;
    }
    private List<COfficedocId> generateOffidocIds(List<COffidocId> cOffidocIds) {
        List<COfficedocId> res = null;
        if (!CollectionUtils.isEmpty(cOffidocIds)) {
            res = new ArrayList<>();
            for (COffidocId cOffidocId : cOffidocIds) {
                if (cOffidocId == null) {
                    continue;
                }
                COfficedocId od = new COfficedocId();
                od.setOffidocOrigin(cOffidocId.getOffidocOrigin());
                od.setOffidocSeries(IpasDataUtils.generateIpasInteger(cOffidocId.getOffidocSeries()));
                od.setOffidocNbr(IpasDataUtils.generateIpasInteger(cOffidocId.getOffidocNbr()));
                res.add(od);
            }
        }
        return res;
    }
    private List<_int.wipo.ipas.ipasservices.CActionId> generateActionIds(List<CActionId> cActionIds) {
        List<_int.wipo.ipas.ipasservices.CActionId> res = null;
        if (!CollectionUtils.isEmpty(cActionIds)) {
            res = new ArrayList<>();
            for (CActionId cActionId : cActionIds) {
                if (cActionId == null) {
                    continue;
                }
                _int.wipo.ipas.ipasservices.CActionId a = new _int.wipo.ipas.ipasservices.CActionId();
                CProcessId pId = new CProcessId();
                a.setProcessId(pId);
                a.setActionNbr(IpasDataUtils.generateIpasInteger(cActionId.getActionNbr()));
                a.getProcessId().setProcessNbr(IpasDataUtils.generateIpasInteger(cActionId.getProcessId().getProcessNbr()));
                a.getProcessId().setProcessType(cActionId.getProcessId().getProcessType());
                res.add(a);
            }
        }
        return res;
    }

}
