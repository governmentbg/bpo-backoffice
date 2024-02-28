package bg.duosoft.ipas.util.abdocs;

import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.reception.MissingAbdocsDocumentService;

import java.util.Objects;

public class AbdocsUtils {

    public static void updateDocumentParent(Integer abdocsId, CFileId fileId, MissingAbdocsDocumentService missingAbdocsDocumentService, AbdocsService abdocsService) {
        String parentFilingNumber = fileId.createFilingNumber();
        missingAbdocsDocumentService.insertMissingDocument(parentFilingNumber);
        Integer parentAbdocsId = abdocsService.selectDocumentIdByRegistrationNumber(parentFilingNumber);
        if (Objects.isNull(parentAbdocsId)) {
            throw new RuntimeException("Cannot find parent document id for object  " + parentFilingNumber);
        }

        abdocsService.changeParent(abdocsId, parentAbdocsId);
    }
}
