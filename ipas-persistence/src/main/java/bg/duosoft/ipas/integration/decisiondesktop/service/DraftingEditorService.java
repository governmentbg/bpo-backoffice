package bg.duosoft.ipas.integration.decisiondesktop.service;

import bg.duosoft.ipas.core.model.decisiondesktop.CDecisionContext;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 22.04.2021
 * Time: 15:15
 */
public interface DraftingEditorService {

    String createContext(String templateId, String templateName, COffidoc offidoc, String username);

    void deleteContext(String contextId, String username);

    void cleanDraftingEditor(String context, String username);

    byte[] downloadLetterFile(String fileType, String contextId, String username);

    String uploadImage(byte[] imageBytes, String username);

    List<CDecisionContext> getFilteredContexts(COffidocId offidocId, String username);
}
