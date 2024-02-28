package bg.duosoft.ipas.integration.decisiondesktop.vars;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.service.mark.MarkService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

import static bg.duosoft.ipas.integration.decisiondesktop.vars.CommonVariablesUtils.INNER_SEPARATOR;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 27.05.2021
 * Time: 12:16
 */
@Component
public class MarkVariablesCreator {

    @Autowired
    private MarkService markService;

    public Map<String, Object> createMarkDataVariables(CFileId fileId){
        Map<String, Object> variables = new HashMap<>();
        CMark mark = markService.findMark(fileId, true);
        variables.putAll(CommonVariablesUtils.createFileDataVariables(mark.getFile()));
        if(mark.getProtectionData() != null && CollectionUtils.isNotEmpty(mark.getProtectionData().getNiceClassList())){
            StringBuilder niceListBuilder = new StringBuilder();
            StringBuilder niceClassesBuilder = new StringBuilder();
            mark.getProtectionData().getNiceClassList().stream().forEach(nice -> {
                niceListBuilder.append(CommonVariablesUtils.buildFormattedNiceClassTerms(nice.getNiceClassNbr(), nice.getNiceClassDescription(), INNER_SEPARATOR));
                niceClassesBuilder.append(nice.getNiceClassNbr()).append(INNER_SEPARATOR);
            });
            variables.put(Variables.MARK_NICE_LIST, CommonVariablesUtils.getStringWithoutTrailing(niceListBuilder, INNER_SEPARATOR.length()));
            variables.put(Variables.MARK_NICE_CLASSES, CommonVariablesUtils.getStringWithoutTrailing(niceClassesBuilder, INNER_SEPARATOR.length()));
            variables.put(Variables.MARK_NICE_LIST_COMPLEX, CommonVariablesUtils.createLabeledContentArray(mark.getProtectionData().getNiceClassList(), nice -> nice.getNiceClassNbr().toString(), nice -> nice.getNiceClassDescription()));
        }
        if(mark.getSignData() != null ){
            if(mark.getSignData().getSignType() != null && mark.getSignData().getSignType().description() != null) {
                variables.put(Variables.MARK_TYPE_NAME, mark.getSignData().getSignType().description().toLowerCase());
            }
            byte[] imageData = CommonVariablesUtils.getMarkImageData(mark.getSignData().getAttachments());
            if(imageData != null){
                variables.put(Variables.MARK_SIGN_IMAGE, imageData);
            }
            variables.put(Variables.MARK_SIGN_NAME, mark.getSignData().getMarkName() != null ? mark.getSignData().getMarkName(): "");
        }
        return variables;
    }
}
