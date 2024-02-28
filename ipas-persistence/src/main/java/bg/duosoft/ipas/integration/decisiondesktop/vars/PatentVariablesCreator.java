package bg.duosoft.ipas.integration.decisiondesktop.vars;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.service.patent.DesignService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.integration.decisiondesktop.model.backoffice.DesignImageWrapperVariable;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static bg.duosoft.ipas.integration.decisiondesktop.vars.CommonVariablesUtils.SEPARATOR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 27.05.2021
 * Time: 12:18
 */
@Component
public class PatentVariablesCreator {

    @Autowired
    private PatentService patentService;

    @Autowired
    private DesignService designService;

    public Map<String, Object> createPatentDataVariables(CFileId fileId, boolean addImages){
        Map<String, Object> variables = new HashMap<>();
        CPatent patent = patentService.findPatent(fileId, false);
        variables.putAll(CommonVariablesUtils.createFileDataVariables(patent.getFile()));
        if(patent != null){
            if(patent.getFile().getFileId().getFileType().equals(FileType.DESIGN.code()) ||
                    patent.getFile().getFileId().getFileType().equals(FileType.DIVISIONAL_DESIGN.code())){
                List<CPatent> designs = designService.getAllSingleDesignsForIndustrialDesign(patent, addImages);
                if(CollectionUtils.isNotEmpty(designs)) {
                    variables.putAll(createDesignsDataVariables(patent, designs));
                }
            }
        }

        return variables;
    }

    private Map<String, Object> createDesignsDataVariables(CPatent patent, List<CPatent> designs){
        Map<String, Object> variables = new HashMap<>();

        StringBuilder stringBuilderIds = new StringBuilder();
        StringBuilder stringBuilderNames = new StringBuilder();
        StringBuilder stringBuilderLocarno = new StringBuilder();
        StringBuilder stringBuilderDesigns = new StringBuilder();
        List<DesignImageWrapperVariable> images = new ArrayList<>();
        Function<CPatent, String> getSingleDesignIdFn = cPat -> {
            String dsNbr = cPat.getFile().getFileId().getFileNbr().toString();
            String singleDesignNumberFirstPart =  dsNbr.substring(0,dsNbr.length() - 3);
            String singleDesignNumberSecondPart =  dsNbr.substring(dsNbr.length() - 3);
            String singleDesignId = singleDesignNumberFirstPart+"-"+singleDesignNumberSecondPart;
            return singleDesignId;
        } ;
        designs.stream().forEach(design -> {
            String singleDesignId = getSingleDesignIdFn.apply(design);

            if(design.getTechnicalData() != null && design.getTechnicalData().getDrawingList() != null && design.getTechnicalData().getDrawingList().size()>0) {
                DesignImageWrapperVariable wrapperVariable = CommonVariablesUtils.buildDesignImageWrapperVariable(design.getTechnicalData().getDrawingList(), singleDesignId);
                images.add(wrapperVariable);
            }

            stringBuilderIds.append(singleDesignId);
            stringBuilderIds.append(SEPARATOR);

            stringBuilderNames.append(design.getTechnicalData().getTitle());
            stringBuilderNames.append(SEPARATOR);

            stringBuilderLocarno.append(design.getTechnicalData().getLocarnoClassList().stream().map(loc -> loc.getLocarnoClassCode())
                    .reduce((l1, l2) ->
                    new StringBuilder().append(l1).append(SEPARATOR).append(l2).toString()).get());
            stringBuilderLocarno.append(SEPARATOR);

            stringBuilderDesigns.append(singleDesignId).append(" ").
                    append(design.getTechnicalData().getTitle()).append(SEPARATOR);
        });

        variables.put(Variables.DESIGN_LIST_IDS, CommonVariablesUtils.getStringWithoutTrailing(stringBuilderIds, SEPARATOR.length()));
        variables.put(Variables.DESIGN_LIST_PRODUCTS, CommonVariablesUtils.getStringWithoutTrailing(stringBuilderNames, SEPARATOR.length()));
        variables.put(Variables.DESIGN_LIST_LOCARNO, CommonVariablesUtils.getStringWithoutTrailing(stringBuilderLocarno, SEPARATOR.length()));
        variables.put(Variables.DESIGN_LIST, CommonVariablesUtils.getStringWithoutTrailing(stringBuilderDesigns, SEPARATOR.length()));
        variables.put(Variables.DESIGN_IMAGES, images.toArray(DesignImageWrapperVariable[]::new));
        variables.put(Variables.DESIGN_LIST_COUNT, designs.size());
        if(patent.getTechnicalData() != null) {
            variables.put(Variables.DESIGN_NAME, patent.getTechnicalData().getTitle());
        }
        variables.put(Variables.DESIGN_LIST_COMPLEX, CommonVariablesUtils.createLabeledContentArray(designs, getSingleDesignIdFn, ds -> ds.getTechnicalData().getTitle()));

        return variables;
    }

}
