package bg.duosoft.ipas.test.mock;

import bg.duosoft.abdocs.exception.AbdocsServiceException;
import bg.duosoft.abdocs.model.*;
import bg.duosoft.abdocs.model.response.*;
import bg.duosoft.abdocs.service.AbdocsAuthenticator;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.miscellaneous.CAbdocsDocumentType;
import bg.duosoft.ipas.core.service.reception.AbdocsDocumentTypeService;
import bg.duosoft.ipas.enums.IpasObjectType;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.general.BasicUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

/**
 * User: Georgi
 * Date: 18.6.2020 г.
 * Time: 13:49
 */

public class AbdocsMockServiceImpl implements AbdocsService {
    private static int CURRENT_DOCUMENT_ID = 1;
    public static Map<Integer, Document> CACHED_DOCUMENTS = new HashMap<>();
    @Autowired
    private AbdocsDocumentTypeService abdocsDocumentTypeService;
    @Override
    public void registerAuthenticator(AbdocsAuthenticator authenticator) {

    }

    @Override
    public boolean isConnectionOK() {
        return true;
    }

    @Override
    public ApiTokenResponse selectApiToken(String username) throws AbdocsServiceException {
        return new ApiTokenResponse("test-token", "test-type", DateUtils.convertToDate(LocalDateTime.now().plusDays(365)));
    }

    @Override
    public ElectronicServiceData selectEServiceData(Integer integer) throws AbdocsServiceException {
        return null;
    }

    @Override
    public DocumentBarcodeData selectDocumentBarcodeDataById(Integer integer) throws AbdocsServiceException {
        return null;
    }

    @Override
    public Document selectDocumentById(Integer id) throws AbdocsServiceException {
        Document res = CACHED_DOCUMENTS.get(id);
        if (res == null) {
            throw new RuntimeException("No cached document with the given id");
        }
        return res;
    }

    @Override
    public Document selectDocumentByRegistrationNumber(String s) throws AbdocsServiceException {
        return null;
    }

    @Override
    public Document selectDocumentByHashNumber(String s) throws AbdocsServiceException {
        return null;
    }

    @Override
    public Document registerDocument(DocCreation docCreation, Date registrationDate) throws AbdocsServiceException {
        Document res = new Document();
        int docId = CURRENT_DOCUMENT_ID++;
        res.setDocId(docId);
        res.setRegNumber(docId);
        res.setDocTypeId(docCreation.getDocTypeId());
        res.setDocDirection(docCreation.getDocDirection());
        res.setParentId(docCreation.getParentDocId());
        res.setReceivedOriginalState(docCreation.getReceivedOriginalState());
        res.setRootDocId(getRootDocId(docCreation.getParentDocId()));

        res.setDocCorrespondents(docCreation.getCorrespondents());
        res.setRegDate(registrationDate);

        res.setRegUri(generateDocUri(docCreation, docId));
        res.setDocSubject(docCreation.getDocSubject());
        res.setReceiptOrder(1);
        res.setDocFiles(new ArrayList<>());
        res.setDocSourceTypeId(docCreation.getDocSourceTypeId());
        CACHED_DOCUMENTS.put(docId, res);
        return res;
    }
    private String generateDocUri(DocCreation docCreation, int docId) {
        if (docCreation.getRegistration().getRegistrationNumber() != null) {
            return docCreation.getRegistration().getRegistrationNumber();
        } else {
            CAbdocsDocumentType docType = abdocsDocumentTypeService.selectByAbdocsDocTypeId(docCreation.getDocTypeId());
            if (docCreation.getRegistration().getDocRegistrationType() == DocRegistrationType.ByDocType) {
                if (docType.getIpasObject() == IpasObjectType.USERDOC) {
                    return docType.getType() + "-" + Year.now().getValue() + "/" + docId;
                } else {
                    return BasicUtils.createFilingNumber("BG", docType.getType(), Year.now().getValue(), docId);
                }
            } else {
                String parentRegUri = getParentRegUri(docCreation.getParentDocId());
                return parentRegUri + "-" + docId;
            }
        }
    }
    private Integer getRootDocId(Integer parentDocId) {
        if (parentDocId == null) {
            return null;
        }
        Document cachedParent = CACHED_DOCUMENTS.get(parentDocId);
        if (cachedParent.getParentId() != null) {
            return getRootDocId(cachedParent.getParentId());
        }
        return cachedParent.getDocId();
    }
    private String getParentRegUri(Integer parentId) {
        Document parent = CACHED_DOCUMENTS.get(parentId);
        if (parent == null) {
            throw new RuntimeException("No parent cached...");
        }
        if (parent.getParentId() != null) {
            return getParentRegUri(parent.getParentId());
        } else {
            return parent.getRegUri();
        }
    }

    @Override
    public Integer selectDocumentIdByRegistrationNumber(String registrationNumber) throws AbdocsServiceException {
        return CACHED_DOCUMENTS.values().stream().filter(r -> r.getRegUri().equals(registrationNumber)).map(r -> r.getDocId()).findFirst().orElse(null);
    }

    @Override
    public Integer selectDocumentIdByHashNumber(String s) throws AbdocsServiceException {
        return null;
    }

    @Override
    public void unregisterDocument(Integer id) throws AbdocsServiceException {

    }

    @Override
    public void cancelDocument(Integer id) throws AbdocsServiceException {

    }

    @Override
    public DeleteDocumentResponse deleteDocument(Integer id) throws AbdocsServiceException {
        return null;
    }

    @Override
    public void deleteDocumentHierarchy(Integer id) throws AbdocsServiceException {

    }

    @Override
    public FileStorageResponse fileStorage(byte[] content, String fileName) throws AbdocsServiceException {
        return null;
    }

    @Override
    public void uploadFileToExistingDocument(Integer docId, byte[] content, String fileName, boolean isPrimary, DocFileVisibility docFileVisibility) throws AbdocsServiceException {

    }

    @Override
    public void uploadFileToExistingDocument(Integer integer, byte[] bytes, String s, String s1, boolean b, DocFileVisibility docFileVisibility) throws AbdocsServiceException {

    }

    @Override
    public void updateFileContent(Integer docFileId, String newFileName, byte[] newContent) throws AbdocsServiceException {

    }

    @Override
    public void deleteFile(Integer docId, Integer docFileId) throws AbdocsServiceException {

    }

    @Override
    public void receiveOriginal(Integer id) throws AbdocsServiceException {

    }

    @Override
    public DownloadFileResponse downloadFile(String uuid, String fileName, Integer databaseId) throws AbdocsServiceException {
        return null;
    }

    @Override
    public void changeRegistrationDate(Integer id, Date date) throws AbdocsServiceException {

    }

    @Override
    public void changeTypeAndDirection(Integer id, Integer type, Integer direction) throws AbdocsServiceException {

    }

    @Override
    public void changeParent(Integer id, Integer parentId) throws AbdocsServiceException {

    }

    @Override
    public String generateViewDocumentUrl(Integer documentId) throws AbdocsServiceException {
        return null;
    }

    @Override
    public CreateCorrespondentResponse createCorrespondent(Correspondent correspondent) throws AbdocsServiceException {
        return null;
    }

    @Override
    public void updateCorrespondent(Correspondent correspondent) throws AbdocsServiceException {

    }

    @Override
    public boolean isAuthenticationTokenValid() {
        return false;
    }

    @Override
    public List<DocumentTypeDto> selectDocumentTypesByGroup(String group) throws AbdocsServiceException {
        return null;
    }

    @Override
    public List<DocumentTypeDto> selectAllDocumentTypes() throws AbdocsServiceException {
        return null;
    }

    @Override
    public CreateDocEmailDto readEmail(Integer integer) throws AbdocsServiceException {
        return null;
    }

    @Override
    public void updateEmailStatusAsRead(Integer integer, Integer documentId) throws AbdocsServiceException {

    }

    @Override
    public void updateSubject(Integer integer, String s) throws AbdocsServiceException {

    }

    @Override
    public List<String> selectSubstituteUsers(String s) throws AbdocsServiceException {
        return null;
    }

    @Override
    public boolean checkIfOffidocEmailExist(Integer integer, String s) throws AbdocsServiceException {
        return false;
    }

    @Override
    public void updateOffidocEmailStatusAsRead(Integer integer, String s) throws AbdocsServiceException {

    }

    @Override
    public void insertDocAction(DocActionRequest docActionRequest) {

    }

    @Override
    public Integer selectAbdocsUserIdByUsername(String s) {
        return null;
    }

    @Override
    public void connectDocuments(Integer integer, List<Integer> list) throws AbdocsServiceException {

    }

    @Override
    public List<Document> selectDocumentHierarchy(Integer integer) {
        return null;
    }
}
