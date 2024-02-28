package com.duosoft.ipas.controller.ipobjects.patentlike.design;

import bg.duosoft.ipas.core.model.design.CImageViewType;
import bg.duosoft.ipas.core.model.design.CSingleDesignExtended;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CDrawing;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.util.CDrawingExt;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import bg.duosoft.ipas.core.service.nomenclature.ImageViewTypeService;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.enums.ApplTyp;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.ProcessType;
import bg.duosoft.ipas.util.attachment.AttachmentUtils;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.controller.ipobjects.patentlike.common.PatentLikeDrawingController;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.json.SingleDesignData;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping("/design/drawing")
public class DesignDrawingController extends PatentLikeDrawingController {
    private final String patentSingleDesignsView = "ipobjects/patentlike/design/single_design_drawings :: single_design_drawings";

    @Autowired
    private StatusService statusService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private ImageViewTypeService imageViewTypeService;


    @Override
    public CPatent getSessionPatentOnLoadImage(String filingNumber, HttpServletRequest request, String sessionIdentifier) {
        CFileId cFileId = CoreUtils.createCFileId(filingNumber);
        List<CPatent> singleDesigns = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS, sessionIdentifier, request);
        return singleDesigns.stream().filter(r->r.getFile().getFileId().equals(cFileId)).findFirst().orElse(null);
    }

    @Override
    public List<CDrawing> getDrawingList(String filingNumber, HttpServletRequest request, String sessionIdentifier) {
        CFileId cFileId = CoreUtils.createCFileId(filingNumber);
        List<CPatent> singleDesigns = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS_ON_EDIT, sessionIdentifier, request);
        if (Objects.isNull(singleDesigns) || CollectionUtils.isEmpty(singleDesigns)){
            return null;
        }else{
            CPatent singleDesign = singleDesigns.stream().filter(r->r.getFile().getFileId().equals(cFileId)).findFirst().orElse(null);
            return singleDesign.getTechnicalData().getDrawingList();
        }
    }

    @Override
    public String getViewPage() {
        return patentSingleDesignsView;
    }

    @Override
    public void reinitDrawings(List<CDrawing> drawingList, String data) {
        SingleDesignData singleDesignData = JsonUtil.readJson(data, SingleDesignData.class);

        singleDesignData.getSingleDesignDrawings().stream().forEach(editedSingleDesignDrawing->{
          CDrawingExt singleDesignDrawing = (CDrawingExt) drawingList.stream().filter(drawing->drawing.getDrawingNbr().equals(editedSingleDesignDrawing.getDrawingNbr())).findFirst().orElse(null);
            singleDesignDrawing.getSingleDesignExtended().setImagePublished(editedSingleDesignDrawing.getImagePublished());
            singleDesignDrawing.getSingleDesignExtended().setImageRefused(editedSingleDesignDrawing.getImageRefused());
            singleDesignDrawing.getSingleDesignExtended().getImageViewType().setViewTypeId(editedSingleDesignDrawing.getViewType());
        });
    }

    @Override
    public void setModelAttributes(Model model, String filingNumber,String sessionIdentifier,HttpServletRequest request) {
        CPatent sessionPatent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        model.addAttribute("filingNumber", filingNumber);
        model.addAttribute("singleDesignStatuses",statusService.getInitialOrFinalStatusesByProcType(sessionPatent.getFile().getFileId().getFileType().equals(FileType.DESIGN.code())? ProcessType.NATIONAL_SINGLE_DESIGN_PROCESS_TYPE.code() :ProcessType.INTERNATIONAL_SINGLE_DESIGN_PROCESS_TYPE.code()));
        model.addAttribute("singleDesignAppSubTypes",applicationTypeService.findAllSingleDesignApplicationSubTypesByMasterApplTyp(sessionPatent.getFile().getFilingData().getApplicationType()));
        model.addAttribute("imageViewTypeList",imageViewTypeService.getAllImageViewTypes());
    }

    @Override
    public CDrawing initializeAttachedDrawing(long maxDrawingNbr, byte[] uploadedDrawing) {
        CDrawing cDrawing = new CDrawingExt();
        cDrawing.setDrawingNbr(maxDrawingNbr);
        cDrawing.setDrawingData(uploadedDrawing);
        cDrawing.setDrawingType(AttachmentUtils.getContentType(uploadedDrawing));
        cDrawing.setSingleDesignExtended(new CSingleDesignExtended());
        cDrawing.getSingleDesignExtended().setDrawingNbr(maxDrawingNbr);
        cDrawing.getSingleDesignExtended().setImageViewType(new CImageViewType());
        return cDrawing;
    }
}
