package com.duosoft.ipas.controller.ipobjects.patentlike.design;


import bg.duosoft.ipas.core.model.design.CProductTerm;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CFilingData;
import bg.duosoft.ipas.core.model.miscellaneous.CommonTerm;
import bg.duosoft.ipas.core.model.patent.CDrawing;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CTechnicalData;
import bg.duosoft.ipas.core.model.process.CProcessSimpleData;
import bg.duosoft.ipas.core.model.util.CDrawingExt;
import bg.duosoft.ipas.core.service.file.FileRelationshipsService;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import bg.duosoft.ipas.core.service.nomenclature.ImageViewTypeService;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.patent.DesignService;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.DesignUtils;
import com.duosoft.ipas.util.json.DesignDetailsData;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/design/detail")
public class DesignDetailController {

    @Autowired
    private StatusService statusService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private ImageViewTypeService imageViewTypeService;

    @Autowired
    private FileRelationshipsService fileRelationshipsService;

    @Autowired
    private DesignService designService;


    private final String patentSingleDesignsView = "ipobjects/patentlike/design/single_designs_panel :: single_designs";
    private final String patentSingleDesignView = "ipobjects/patentlike/design/single_design :: single_design";
    private final String mainDesignView = "ipobjects/patentlike/design/main_design_panel :: main_design_details";

    @PostMapping("/load-main-design-panel")
    public String loadMainDesignPanel(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier) {
        CPatent sessionPatent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);

        List<CPatent> singleDesigns = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS, sessionIdentifier, request);
        if (Objects.isNull(singleDesigns)){
            singleDesigns=new ArrayList<>();
            singleDesigns.addAll(DesignUtils.getAllSingleDesignsForIndDesign(sessionPatent,designService));
            singleDesigns.stream().forEach(design -> constructProductTermInitial(design));
            HttpSessionUtils.setSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS, sessionIdentifier, singleDesigns, request);
            PatentSessionUtils.setSessionAttributePatentHash(request,sessionIdentifier);
        }
        fillCommonModelAttributes(model,sessionIdentifier,sessionPatent,singleDesigns,true,true,true);
        return mainDesignView;
    }

    private void fillCommonModelAttributes(Model model,String sessionIdentifier,CPatent sessionPatent,List<CPatent> singleDesigns,boolean fillSessionPatent,boolean fillImages,boolean fillSingleDesigns){
        model.addAttribute("singleDesignAppSubTypes",applicationTypeService.findAllSingleDesignApplicationSubTypesByMasterApplTyp(sessionPatent.getFile().getFilingData().getApplicationType()));
        model.addAttribute("singleDesignStatuses",statusService.getInitialOrFinalStatusesByProcType(sessionPatent.getFile().getFileId().getFileType().equals(FileType.DESIGN.code())? ProcessType.NATIONAL_SINGLE_DESIGN_PROCESS_TYPE.code() :ProcessType.INTERNATIONAL_SINGLE_DESIGN_PROCESS_TYPE.code()));
        model.addAttribute("sessionObjectIdentifier",sessionIdentifier);
        if (fillSingleDesigns)
        model.addAttribute("singleDesigns",singleDesigns);
        if (fillSessionPatent)
        model.addAttribute("patent",sessionPatent);
        if (fillImages)
        model.addAttribute("imageViewTypeList",imageViewTypeService.getAllImageViewTypes());
    }

    @PostMapping("/create-single-design")
    public String createSingleDesign(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier) {
        List<CPatent> singleDesignsOnEdit = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS_ON_EDIT, sessionIdentifier, request);
        CPatent sessionPatent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        CPatent newSingleDesign = generateNewSingleDesign(singleDesignsOnEdit,sessionPatent);

        model.addAttribute("singleDesign", newSingleDesign);
        model.addAttribute("index", newSingleDesign.getFile().getFileId().getFileNbr());
        model.addAttribute("registrationNbr",sessionPatent.getFile().getRegistrationData().getRegistrationId().getRegistrationNbr());
        model.addAttribute("mainDesignFileNbr",sessionPatent.getFile().getFileId().getFileNbr());
        singleDesignsOnEdit.add(newSingleDesign);
        fillCommonModelAttributes(model,sessionIdentifier,sessionPatent,null,false,false,false);
        return patentSingleDesignView;
    }

    @PostMapping("/delete-single-design")
    public String deleteSingleDesign(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier,@RequestParam(required = false) String filingNumber) {
        List<CPatent> singleDesignsOnEdit = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS_ON_EDIT, sessionIdentifier, request);
        CFileId cFileId = CoreUtils.createCFileId(filingNumber);
        singleDesignsOnEdit.removeIf(r->r.getFile().getFileId().equals(cFileId));
        return patentSingleDesignView;
    }

    @PostMapping("/edit-single-designs")
    public String editSingleDesigns(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {

        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        List<CPatent> singleDesigns = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS, sessionIdentifier, request);
        List<CPatent> singleDesignsOnEdit = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS_ON_EDIT, sessionIdentifier, request);
        if (!isCancel) {
            singleDesigns.clear();
            singleDesigns.addAll(singleDesignsOnEdit);
            DesignDetailsData designDetailsData = JsonUtil.readJson(data, DesignDetailsData.class);
            fillDesignDetailData(designDetailsData,singleDesigns,patent);
            singleDesigns.stream().forEach(design -> constructProductTermInitial(design));
        }
        fillCommonModelAttributes(model,sessionIdentifier,patent,singleDesigns,true,true,true);
        PatentSessionUtils.removeDesignDrawingsSessionObjects(request,sessionIdentifier);
        return patentSingleDesignsView;
    }

    private void fillDesignDetailData(DesignDetailsData designDetailsData,List<CPatent> singleDesigns,CPatent sessionPatent){
        if (!Objects.isNull(designDetailsData)){
            sessionPatent.getTechnicalData().setTitle(designDetailsData.getDesignMainTitle());
            sessionPatent.getTechnicalData().setEnglishTitle(designDetailsData.getDesignMainTitleEn());
            designDetailsData.getSingleDesigns().stream().forEach(editedSingleDesign->{
                CPatent singleDesign = singleDesigns.stream().filter(sessionSingleDesign->sessionSingleDesign.getFile().getFileId().createFilingNumber().equals(editedSingleDesign.getFilingNumber())).findFirst().orElse(null);
                if (!Objects.isNull(singleDesign)){
                    singleDesign.getTechnicalData().setTitle(editedSingleDesign.getDesignSingleTitle());
                    singleDesign.getTechnicalData().setEnglishTitle(editedSingleDesign.getDesignSingleTitleEn());
                    singleDesign.getFile().getProcessSimpleData().setStatusCode(editedSingleDesign.getDesignSingleStatusCode());
                    singleDesign.getFile().getFilingData().setApplicationSubtype(editedSingleDesign.getDesignSingleApplSubType());
                    editedSingleDesign.getSingleDesignDrawings().stream().forEach(editedSingleDesignDrawing->{
                        CDrawingExt singleDesignDrawing = (CDrawingExt) singleDesign.getTechnicalData().getDrawingList().stream().filter(sessionSingleDesignDrawing -> sessionSingleDesignDrawing.getDrawingNbr().equals(editedSingleDesignDrawing.getDrawingNbr())).findFirst().orElse(null);
                        singleDesignDrawing.getSingleDesignExtended().setImagePublished(editedSingleDesignDrawing.getImagePublished());
                        singleDesignDrawing.getSingleDesignExtended().setImageRefused(editedSingleDesignDrawing.getImageRefused());
                        singleDesignDrawing.getSingleDesignExtended().setImageViewType(imageViewTypeService.getImageViewTypeById(editedSingleDesignDrawing.getViewType()));
                    });
                }
            });
        }
    }

    private void constructProductTermInitial(CPatent design){
        CProductTerm productTerm = new CProductTerm();
        productTerm.setTermText(design.getTechnicalData().getTitle());
        productTerm.setProductClasses(new ArrayList<>());
        if(design.getTechnicalData().getLocarnoClassList() != null && design.getTechnicalData().getLocarnoClassList().size() >0) {
            List<String> currentClassList = design.getTechnicalData().getLocarnoClassList().stream().map(
                cl -> cl.getLocarnoClassCode()
            ).collect(Collectors.toList());
            productTerm.getProductClasses().addAll(currentClassList);
        }
        productTerm.setTermStatus(CommonTerm.UNKNOWN);
        design.setProductTerm(productTerm);
    }

    private CPatent generateNewSingleDesign(List<CPatent> singleDesigns, CPatent sessionPatent){
        CPatent newSingleDesign = new CPatent();
        CFile newCFile = new CFile();

        CFileId sessionPatentFileId = (CFileId) SerializationUtils.clone(sessionPatent.getFile().getFileId());
        newCFile.setFileId(new CFileId(sessionPatentFileId.getFileSeq(), sessionPatent.getFile().getFileId().getFileType().equals(FileType.DESIGN.code()) ? FileType.SINGLE_DESIGN.code():FileType.INTERNATIONAL_SINGLE_DESIGN.code(),sessionPatentFileId.getFileSeries(),null));

        Integer singleDesignMaxNumberDb = fileRelationshipsService.findSingleDesignMaxNumberById1(sessionPatentFileId.getFileSeq(),sessionPatentFileId.getFileType(),
                sessionPatentFileId.getFileSeries(),sessionPatentFileId.getFileNbr(), RelationshipType.DESIGN_TYPE);

        singleDesigns.stream().forEach(singleDesign->{
            CFileId existingSingleDesignFieldId = singleDesign.getFile().getFileId();
            if (Objects.isNull(newCFile.getFileId().getFileNbr()) || newCFile.getFileId().getFileNbr()<existingSingleDesignFieldId.getFileNbr()){
                newCFile.getFileId().setFileNbr((Integer) SerializationUtils.clone(existingSingleDesignFieldId.getFileNbr()));
            }
        });

        if (Objects.isNull(newCFile.getFileId().getFileNbr()) && Objects.isNull(singleDesignMaxNumberDb)){
            newCFile.getFileId().setFileNbr(Integer.parseInt((sessionPatentFileId.getFileNbr().toString().concat("000"))));
        }else if(Objects.isNull(newCFile.getFileId().getFileNbr())){
            newCFile.getFileId().setFileNbr(singleDesignMaxNumberDb);
        }else{
            if (!Objects.isNull(singleDesignMaxNumberDb) && singleDesignMaxNumberDb>newCFile.getFileId().getFileNbr()){
                newCFile.getFileId().setFileNbr(singleDesignMaxNumberDb);
            }
        }
        newCFile.getFileId().setFileNbr(newCFile.getFileId().getFileNbr()+1);
        newCFile.setProcessSimpleData(new CProcessSimpleData());
        newSingleDesign.setTechnicalData(new CTechnicalData());
        newCFile.setFilingData(new CFilingData());

        if (sessionPatent.getFile().getFilingData().getApplicationType().equals(ApplTyp.DESIGN_APP_TYPE)
                || sessionPatent.getFile().getFilingData().getApplicationType().equals(ApplTyp.DIVIDED_NATIONAL_DESIGN) ){
            newCFile.getFilingData().setApplicationType(ApplTyp.SINGLE_DESIGN_TYPE);
            newCFile.getFilingData().setApplicationSubtype(ApplSubTyp.SINGLE_DESIGN_PRODUCT_SUB_TYPE);
        }else if(sessionPatent.getFile().getFilingData().getApplicationType().equals(ApplTyp.INTERNATIONAL_DESIGN_APP_TYPE)){
            newCFile.getFilingData().setApplicationType(ApplTyp.INTERNATIONAL_SINGLE_DESIGN_APP_TYPE);
            newCFile.getFilingData().setApplicationSubtype(ApplSubTyp.INTERNATIONAL_SINGLE_DESIGN_SUB_TYPE);
        }else{
            throw new RuntimeException("Unrecognized applTyp for design : "+ sessionPatent.getFile().getFilingData().getApplicationType());
        }
        newSingleDesign.getTechnicalData().setDrawingList(new ArrayList<CDrawing>());
        newSingleDesign.setFile(newCFile);
        newSingleDesign.setReception(true);
        return newSingleDesign;
    }
}
