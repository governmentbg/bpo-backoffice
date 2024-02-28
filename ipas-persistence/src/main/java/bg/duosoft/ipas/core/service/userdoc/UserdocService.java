package bg.duosoft.ipas.core.service.userdoc;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocHierarchyNode;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocIpDocSimpleResult;

import java.util.List;

public interface UserdocService {

    CUserdoc updateUserdoc(CUserdoc cUserdoc, boolean abdocsUpdate);

    CUserdoc findUserdoc(String docOrigin, String docLog, Integer docSeries, Integer docNbr, boolean loadContent);

    CUserdoc findUserdoc(String docOrigin, String docLog, Integer docSeries, Integer docNbr);

    CUserdoc findUserdoc(CDocumentId documentId);

    CUserdoc findUserdoc(CDocumentId documentId, boolean loadContent);

    String selectUserdocTypeByDocId(CDocumentId id);

    CFileId selectMainObjectIdOfUserdoc(CDocumentId id);

    void changeUserdocType(CDocumentId id, String newUserdocType);

    void updateRowVersion(CDocumentId id);

    long count();

    public void deleteUserdoc(CDocumentId documentId, boolean deleteInDocflowSystem);

    List<UserdocIpDocSimpleResult> selectUserdocsFromAcstre();

    CUserdoc selectByExternalSystemId(String externalSystemId);

    CUserdoc selectByDataTextAndType(String dataText, String type);

    /**
     * @param documentId
     * @param flatHierarchy - pri flat hierarchy = true, children vinagi e null, t.e. jerarhiqta e ploska - prosto spisyk ot vsichki userdocs (koito mogat da sa vyrzani i userdoc kym userdoc), a ne direktno kym parent-a,  dokato pri false, se generira dyrvovidna struktura, t.e. ako userdoc e vyrzan kym userdoc, to podchnieniq se namira v children na prent-a
     * @return - cqlata jerarhiq, t.e. vkliuchitelno userdocs, vyrzani kym userdocs
     */
    List<CUserdocHierarchyNode> getUserdocUserdocHierarchy(CDocumentId documentId, boolean flatHierarchy);

    /**
     * @see #getUserdocUserdocHierarchy(CDocumentId, boolean)
     * @param fileId
     * @param flatHierarchy
     * @return
     */
    List<CUserdocHierarchyNode> getFileUserdocHierarchy(CFileId fileId, boolean flatHierarchy);

}
