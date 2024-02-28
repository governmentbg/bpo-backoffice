package bg.duosoft.ipas.util.design;

import bg.duosoft.ipas.core.model.CApplicationSubType;
import bg.duosoft.ipas.core.model.design.CLocarnoClasses;
import bg.duosoft.ipas.core.model.design.CPatentLocarnoClasses;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFilingData;
import bg.duosoft.ipas.core.model.mark.CUserdocSingleDesign;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CTechnicalData;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SingleDesignUtils {

    public static CUserdocSingleDesign convertToUserdocSingleDesign(CPatent singleDesign, CDocumentId documentId, ApplicationTypeService applicationTypeService) {
        CUserdocSingleDesign userdocSingleDesign = new CUserdocSingleDesign();
        userdocSingleDesign.setDocumentId(documentId);
        userdocSingleDesign.setFileId(singleDesign.getFile().getFileId());

        CFilingData filingData = singleDesign.getFile().getFilingData();
        if (Objects.nonNull(filingData)) {
            CApplicationSubType applicationSubtype = applicationTypeService.getApplicationSubtype(filingData.getApplicationType(), filingData.getApplicationSubtype());
            if (Objects.nonNull(applicationSubtype)) {
                userdocSingleDesign.setApplicationSubType(applicationSubtype);
            }
        }

        CTechnicalData technicalData = singleDesign.getTechnicalData();
        if (Objects.nonNull(technicalData)) {
            List<CPatentLocarnoClasses> locarnoClassList = technicalData.getLocarnoClassList();
            if (!CollectionUtils.isEmpty(locarnoClassList)) {
                List<CLocarnoClasses> locarnoList = locarnoClassList.stream()
                        .map(CPatentLocarnoClasses::getLocarnoClasses)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                if (!CollectionUtils.isEmpty(locarnoList)) {
                    userdocSingleDesign.setLocarnoClasses(locarnoList);
                }
            }
            userdocSingleDesign.setProductTitle(technicalData.getTitle());
        }
        return userdocSingleDesign;
    }

    public static List<CUserdocSingleDesign> convertToUserdocSingleDesign(List<CPatent> singleDesigns, CDocumentId documentId, ApplicationTypeService applicationTypeService) {
        List<CUserdocSingleDesign> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(singleDesigns)) {
            for (CPatent singleDesign : singleDesigns) {
                CUserdocSingleDesign cUserdocSingleDesign = convertToUserdocSingleDesign(singleDesign, documentId, applicationTypeService);
                list.add(cUserdocSingleDesign);
            }
        }
        return list;
    }
}
