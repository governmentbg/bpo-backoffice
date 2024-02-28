package com.duosoft.ipas.controller.rest.abdocs.notification;

import bg.duosoft.abdocs.model.DocStatus;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.model.DocumentNotificationType;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.abdocs.notification.CDocumentNotificationProcessResult;
import bg.duosoft.ipas.core.model.miscellaneous.CAbdocsDocumentType;
import bg.duosoft.ipas.core.service.abdocs.notification.AbdocsNotificationService;
import bg.duosoft.ipas.core.service.reception.AbdocsDocumentTypeService;
import bg.duosoft.ipas.enums.IpasObjectType;
import bg.duosoft.ipas.rest.custommodel.abdocs.notification.RDocumentNotification;
import bg.duosoft.ipas.rest.custommodel.abdocs.notification.RDocumentNotificationProcessResult;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.controller.rest.BaseRestController;
import com.duosoft.ipas.controller.rest.mapper.RDocumentNotificationResultMapper;
import bg.duosoft.ipas.rest.custommodel.Message;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@Api(tags = {"ABDOCS"})
public class NotificationController extends BaseRestController {

    private final String ABDOCS_NOTIFICATION_SUCCESS_MESSAGE = "success";
    private final String ABDOCS_NOTIFICATION_RECEIVED_NOT_PROCESSED = "Notification is received, but it's not processed ! ";
    private final Object lockNotification = new Object();

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @Autowired
    private AbdocsDocumentTypeService abdocsDocumentTypeService;

    @Autowired
    private AbdocsNotificationService abdocsNotificationService;

    @Autowired
    private RDocumentNotificationResultMapper documentNotificationResultMapper;

    @ApiOperation(value = "Change document notification")
    @PostMapping(value = "/notifyDocumentChange", produces = "application/json")
    public RestApiResponse<Message> notifyDocumentChange(@RequestBody RestApiRequest<RDocumentNotification> notificationRequest) {
        String notificationRequestJson = JsonUtil.createJson(notificationRequest);
        log.info("=======ABDOCS NOTIFICATION======= Notification has been received. Request JSON: " + notificationRequestJson);

        RDocumentNotification data = notificationRequest.getData();
        if (Objects.isNull(data)) {
            throw new AbdocsNotificationException(appendToReceivedNotProcessedMessageAndLogIt("Empty data !"));
        }

        Integer documentId = data.getDocumentId();
        if (Objects.isNull(documentId)) {
            throw new AbdocsNotificationException(appendToReceivedNotProcessedMessageAndLogIt("Document Id is empty !"));
        }

        String type = data.getType();
        if (StringUtils.isEmpty(type)) {
            throw new AbdocsNotificationException(appendToReceivedNotProcessedMessageAndLogIt("Type is empty !"));
        }

        DocumentNotificationType notificationType = DocumentNotificationType.selectByCode(data.getType());
        if (Objects.isNull(notificationType)) {
            return new RestApiResponse<>(new Message(appendToReceivedNotProcessedMessageAndLogIt("Type " + type + " is not defined in IPAS")));
        }

        if (DocumentNotificationType.Deleted == notificationType) {
            return new RestApiResponse<>(new Message(appendToReceivedNotProcessedMessageAndLogIt("Notification for deleted document: " + documentId)));
        }

        Document document = abdocsServiceAdmin.selectDocumentById(documentId);
        if (Objects.isNull(document)) {
            throw new AbdocsNotificationException(appendToReceivedNotProcessedMessageAndLogIt("Document with ID = " + documentId + " cannot be found in ABDOCS"));
        }

        DocStatus docStatus = document.getDocStatus();
        if (DocStatus.Deleted == docStatus || DocStatus.Canceled == docStatus || DocStatus.Archived == docStatus) {
            return new RestApiResponse<>(new Message(appendToReceivedNotProcessedMessageAndLogIt("Document status: " + docStatus.name())));
        }

        IpasObjectType ipasObjectType = abdocsDocumentTypeService.selectIpasObjectTypeByAbdocsDocumentId(document.getDocTypeId());
        if (Objects.isNull(ipasObjectType)) {
            return new RestApiResponse<>(new Message(appendToReceivedNotProcessedMessageAndLogIt("ABDOCS document type is not defined in IPAS. Document Type: " + document.getDocTypeId())));
        }

        synchronized (lockNotification) {
            try {
                RDocumentNotificationProcessResult result = processNotification(notificationType, document, ipasObjectType);
                if (result.isSuccessful()) {
                    log.info("=======ABDOCS NOTIFICATION======= Notification has been processed successfully " + notificationRequestJson);
                    return new RestApiResponse<>(new Message(ABDOCS_NOTIFICATION_SUCCESS_MESSAGE));
                } else {
                    throw new AbdocsNotificationException(!CollectionUtils.isEmpty(result.getMessages()) ? String.join("; ", result.getMessages()) : null);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new AbdocsNotificationException(e.getMessage(), e);
            }
        }
    }

    public RDocumentNotificationProcessResult processNotification(DocumentNotificationType notificationType, Document document, IpasObjectType ipasObjectType) {
        CDocumentNotificationProcessResult result = new CDocumentNotificationProcessResult(true, null);
        switch (notificationType) {
            case Registration:
                result = abdocsNotificationService.notifyRegistration(document, ipasObjectType);
                break;
            case Processed:
                result = abdocsNotificationService.notifyProcessed(document, ipasObjectType);
                break;
            case EditDocCorrespondents:
                result = abdocsNotificationService.notifyEditDocCorrespondents(document, ipasObjectType);
                break;
            case Finished:
                result = abdocsNotificationService.notifyFinished(document, ipasObjectType);
                break;
            case DeRegistration:
                result = abdocsNotificationService.notifyDeRegistration(document, ipasObjectType);
                break;
        }
        return documentNotificationResultMapper.toRest(result);
    }

    private static class AbdocsNotificationException extends RuntimeException {
        public AbdocsNotificationException(String message) {
            super(message);
        }

        public AbdocsNotificationException(String message, Throwable throwable) {
            super(message, throwable);
        }
    }

    private String appendToReceivedNotProcessedMessageAndLogIt(String message) {
        String newMessage = ABDOCS_NOTIFICATION_RECEIVED_NOT_PROCESSED + message;
        log.info(newMessage);
        return newMessage;
    }

}
