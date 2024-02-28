package bg.duosoft.ipas.core.service.abdocs.notification;

import bg.duosoft.abdocs.model.Document;
import bg.duosoft.ipas.core.model.abdocs.notification.CDocumentNotificationProcessResult;
import bg.duosoft.ipas.core.model.miscellaneous.CAbdocsDocumentType;
import bg.duosoft.ipas.enums.IpasObjectType;

public interface AbdocsNotificationService {

    CDocumentNotificationProcessResult notifyRegistration(Document document, IpasObjectType ipasObjectType);

    CDocumentNotificationProcessResult notifyProcessed(Document document, IpasObjectType ipasObjectType);

    CDocumentNotificationProcessResult notifyEditDocCorrespondents(Document document, IpasObjectType ipasObjectType);

    CDocumentNotificationProcessResult notifyFinished(Document document, IpasObjectType ipasObjectType);

    CDocumentNotificationProcessResult notifyDeRegistration(Document document, IpasObjectType ipasObjectType);
}
