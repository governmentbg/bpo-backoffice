package bg.duosoft.ipas.core.service.report;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.services.core.IpasServiceException;

import java.util.List;

/**
 * User: Georgi
 * Date: 23.9.2020 г.
 * Time: 15:19
 */
public interface ReportService {
    //lists all the files in the folder DirectorioReportesWord, configured inside CF_CONFIG_PARAM with the same CONFIG_CODE
    public List<String> getReportTemplateFileNames();

    public byte[] generateDocument(String reportPath, List<CFileId> cFileId, List<CDocumentId> cDocumentId, List<COffidocId> cOffidocId, List<CActionId> cActionId, String contentType) throws IpasServiceException;
}
