package bg.duosoft.ipas.core.service.ext;

import bg.duosoft.ipas.core.model.offidoc.COffidocAbdocsDocument;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;

import java.util.Date;

public interface OffidocAbdocsDocumentService {

    COffidocAbdocsDocument selectById(COffidocId id);

    COffidocAbdocsDocument save(COffidocAbdocsDocument offidocAbdocsDocument);

    COffidocAbdocsDocument selectByAbdocsId(Integer id);

    COffidocAbdocsDocument selectByRegistrationNumber(String registrationNumber);

    void updateRegistrationNumber(String registrationNumber, Integer documentId);

    void updateNotificationFinishedDate(Date notificationFinistedDate, Integer documentId);

    void updateEmailNotificationReadDate(Date date, Integer documentId);

    void updatePortalNotificationReadDate(Date date, Integer documentId);

}
