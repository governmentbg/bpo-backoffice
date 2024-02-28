package com.duosoft.ipas.controller.ipobjects.patentlike.common;

import bg.duosoft.ipas.core.model.patent.CDrawing;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.util.CDrawingExt;
import bg.duosoft.ipas.util.attachment.AttachmentUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/patent-like/drawing")
public class DrawingController extends PatentLikeDrawingController{

    private final String patentDrawingsView = "ipobjects/patentlike/common/drawings/drawings_panel :: drawings";

    @PostMapping("/edit-published-drawings")
    public String editDrawings(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {

        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        List<CDrawing> drawingList = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_DRAWINGS, sessionIdentifier, request);
        if (!isCancel) {
            patent.getTechnicalData().setDrawingList(new ArrayList<>());
            if (drawingList != null && !drawingList.isEmpty()) {
                drawingList.stream().forEach(r->patent.getTechnicalData().getDrawingList().add(r));
            }
        }
        model.addAttribute("drawingList", patent.getTechnicalData().getDrawingList());
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        PatentSessionUtils.removePatentDrawingsSessionObjects(request, sessionIdentifier);
        return patentDrawingsView;
    }

    @Override
    public CPatent getSessionPatentOnLoadImage(String filingNumber, HttpServletRequest request, String sessionIdentifier) {
       return PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
    }

    @Override
    public List<CDrawing> getDrawingList(String filingNumber, HttpServletRequest request, String sessionIdentifier) {
        List<CDrawing> drawingList = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_DRAWINGS, sessionIdentifier, request);
        return drawingList;
    }

    @Override
    public String getViewPage() {
        return patentDrawingsView;
    }

    @Override
    public void setModelAttributes(Model model, String filingNumber,String sessionIdentifier,HttpServletRequest request) {
    }

    @Override
    public void reinitDrawings(List<CDrawing> drawingList, String data) {
    }

    @Override
    public CDrawing initializeAttachedDrawing(long maxDrawingNbr, byte[] uploadedDrawing) {
        CDrawing cDrawing = new CDrawingExt();
        cDrawing.setDrawingNbr(maxDrawingNbr);
        cDrawing.setDrawingData(uploadedDrawing);
        cDrawing.setDrawingType(AttachmentUtils.getContentType(uploadedDrawing));
        return cDrawing;
    }

}
