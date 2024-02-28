package bg.duosoft.ipas.integration.decisiondesktop.vars;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.util.file.FileTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 18.05.2021
 * Time: 12:25
 */
@Component
public class VariablesCreator {

    @Autowired
    private UserdocVariablesCreator userdocVariablesCreator;

    @Autowired
    private MarkVariablesCreator markVariablesCreator;

    @Autowired
    private PatentVariablesCreator patentVariablesCreator;


    public Map<String, Object> createVariablesForOffidoc(COffidoc offidoc){
        Map<String, Object> variables = new HashMap<>();
        variables.putAll(CommonVariablesUtils.createGeneralVariables());
        if(offidoc.getOffidocParentData() != null){
            if(offidoc.getOffidocParentData().getFileId() != null){
               variables.putAll(createObjectDataVariables(offidoc.getOffidocParentData().getFileId(), true));
            } else if (offidoc.getOffidocParentData().getUserdocId() != null){
                variables.putAll(userdocVariablesCreator.createUserdocDataVariables(offidoc.getOffidocParentData().getUserdocId(), false));
            } else if(offidoc.getOffidocParentData().getFileId() == null && offidoc.getOffidocParentData().getUserdocId() == null
                    && offidoc.getOffidocParentData().getParent() != null && offidoc.getOffidocParentData().getParent().getUserdocId() != null){
                variables.putAll(userdocVariablesCreator.createUserdocDataVariables(offidoc.getOffidocParentData().getParent().getUserdocId(), false));
            }

            if(offidoc.getOffidocParentData().getFileId() == null &&
                    offidoc.getOffidocParentData().getTopProcessFileData() != null &&
                    offidoc.getOffidocParentData().getTopProcessFileData().getFileId() != null){
                variables.putAll(createObjectDataVariables(offidoc.getOffidocParentData().getTopProcessFileData().getFileId(), false));
            }
        }
        return variables;
    }

    public Map<String, Object> createObjectDataVariables(CFileId fileId, boolean offidocIsForObject){
        Map<String, Object> variables = new HashMap<>();
        if(FileTypeUtils.isMarkFileType(fileId.getFileType())){
            variables.putAll(markVariablesCreator.createMarkDataVariables(fileId));
        } else if(FileTypeUtils.isPatentFileType(fileId.getFileType())){
            variables.putAll(patentVariablesCreator.createPatentDataVariables(fileId, !offidocIsForObject));
        }
        return variables;
    }

}
