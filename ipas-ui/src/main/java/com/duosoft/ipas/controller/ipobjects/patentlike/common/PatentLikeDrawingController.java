package com.duosoft.ipas.controller.ipobjects.patentlike.common;

import bg.duosoft.ipas.core.model.patent.CDrawing;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.util.CDrawingExt;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.patent.drawing.CDrawingDrawingTypeValidator;
import bg.duosoft.ipas.core.validation.patent.drawing.CDrawingNumberValidator;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.controller.ipobjects.common.attachment.FileController;
import com.duosoft.ipas.util.PatentDrawingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class PatentLikeDrawingController {

    @Autowired
    private PatentService patentService;

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @Autowired
    private YAMLConfig yamlConfig;

    public abstract CDrawing initializeAttachedDrawing(long maxDrawingNbr,byte[] uploadedDrawing);
    public abstract  void reinitDrawings(List<CDrawing> drawingList,String data);
    public abstract CPatent getSessionPatentOnLoadImage(String filingNumber,HttpServletRequest request,String sessionIdentifier);
    public abstract List<CDrawing> getDrawingList(String filingNumber,HttpServletRequest request,String sessionIdentifier);
    public abstract String getViewPage();
    public abstract void setModelAttributes(Model model,String filingNumber,String sessionIdentifier,HttpServletRequest request);

    @PostMapping("/load-on-add-complete")
    public String loadDrawingPageOnAddComplete(HttpServletRequest request, Model model,@RequestParam String sessionIdentifier,@RequestParam(required = false) String filingNumber,
                                   @RequestParam(required = false) String data){
        List<CDrawing> drawingList = getDrawingList(filingNumber,request,sessionIdentifier);
        reinitDrawings(drawingList,data);
        setBaseModelAttributes(model,drawingList,sessionIdentifier,filingNumber,request);
        return getViewPage();
    }

    @PostMapping("/add")
    public String addPatentDrawing(HttpServletRequest request, Model model, @RequestParam(value = "uploadDrawing") MultipartFile uploadFile,
                                   @RequestParam String sessionIdentifier,@RequestParam(required = false) String filingNumber) {
        List<ValidationError> errors;
        String name = uploadFile.getOriginalFilename();
        int fileLen = (int) uploadFile.getSize();
        byte[] fileBytes = new byte[fileLen];
        try {
            CDrawing cDrawing = new CDrawing();
            cDrawing.setDrawingData(uploadFile.getBytes());
            IpasTwoArgsValidator<CDrawing, List<CDrawing>> validatorCDrawingType = validatorCreator.createTwoArgsValidator(true, CDrawingNumberValidator.class, CDrawingDrawingTypeValidator.class);
            errors = validatorCDrawingType.validate(cDrawing, new ArrayList<CDrawing>(), Integer.valueOf(yamlConfig.getLogoMaxSize()),name);

            if (CollectionUtils.isEmpty(errors)) {
                uploadFile.getInputStream().read(fileBytes);
                List<CDrawing> drawingList = getDrawingList(filingNumber,request,sessionIdentifier);
                long maxDrawingNbr = PatentDrawingUtils.getDrawingsMaxNumber(drawingList);
                drawingList.add(initializeAttachedDrawing(maxDrawingNbr,fileBytes));
            }
            else{
                model.addAttribute("errors", errors);
                model.addAttribute("id", "drawingData");
            }

        } catch (IOException e) {
            throw new RuntimeException("Upload of file failed with IOException. Filename: " + name, e);
        }
        return "base/validation :: validation-message";
    }

    @PostMapping("/delete")
    public String deletePatentDrawing(HttpServletRequest request, Model model,
                                      @RequestParam String sessionIdentifier,
                                      @RequestParam Long drawingn, @RequestParam(required = false) String filingNumber,@RequestParam(required = false) String data) {

        List<CDrawing> drawingList = getDrawingList(filingNumber,request,sessionIdentifier);
        reinitDrawings(drawingList,data);
        if (!CollectionUtils.isEmpty(drawingList)){
            drawingList.removeIf(cDrawing -> Objects.equals(cDrawing.getDrawingNbr(), Long.valueOf(drawingn)));
        }
        PatentDrawingUtils.sortDrawingList(drawingList);
        setBaseModelAttributes(model,drawingList,sessionIdentifier,filingNumber,request);
        return getViewPage();
    }


    @PostMapping("/swap-position")
    public String swapDrawingPosition(HttpServletRequest request, Model model,
                                      @RequestParam String sessionIdentifier,
                                      @RequestParam Long drawingNbr,@RequestParam(required = false) String filingNumber,
                                      @RequestParam boolean isHigherPosition,@RequestParam(required = false) String data) {
        List<CDrawing> drawingList = getDrawingList(filingNumber,request,sessionIdentifier);
        reinitDrawings(drawingList,data);
        long nextDrawing = isHigherPosition ? drawingNbr + 1 : drawingNbr - 1;
        PatentDrawingUtils.swapDrawingPosition(drawingList, drawingNbr, nextDrawing);
        setBaseModelAttributes(model,drawingList,sessionIdentifier,filingNumber,request);
        return getViewPage();
    }

    @PostMapping("/swap-position-global")
    public String globalSwapDrawingPosition(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier, @RequestParam Long drawingNbrFirstParam,
                                            @RequestParam Long drawingNbrSecondParam,@RequestParam(required = false) String filingNumber,@RequestParam(required = false) String data) {

        List<CDrawing> drawingList = getDrawingList(filingNumber,request,sessionIdentifier);
        reinitDrawings(drawingList,data);
        List<ValidationError> errors = new ArrayList<>();
        boolean validDrawingPositions = PatentDrawingUtils.validDrawingPositions(drawingList, drawingNbrFirstParam, drawingNbrSecondParam);
        if (validDrawingPositions) {
            PatentDrawingUtils.swapDrawingPosition(drawingList, drawingNbrFirstParam, drawingNbrSecondParam);
            setBaseModelAttributes(model,drawingList,sessionIdentifier,filingNumber,request);
            return getViewPage();
        } else {
            errors.add(ValidationError.builder().pointer("drawingNumbersSwap").messageCode("invalid.swap.drawing").build());
            model.addAttribute("errors", errors);
            model.addAttribute("id", "drawingNumbersSwap");
            return "base/validation :: validation-message";
        }
    }


    @RequestMapping(value = "/content")
    public ResponseEntity<byte[]> getImage(HttpServletRequest request,
                                           @RequestParam String sessionIdentifier,
                                           @RequestParam Long drawingn,@RequestParam(required = false) String filingNumber) {
        CDrawingExt cDrawingExt;
        List<CDrawing> drawingList = getDrawingList(filingNumber,request,sessionIdentifier);
        CPatent sessionPatent = getSessionPatentOnLoadImage(filingNumber,request,sessionIdentifier);
        if (Objects.isNull(drawingList)) {
            cDrawingExt =  (CDrawingExt) sessionPatent.getTechnicalData().getDrawingList()
                    .stream().filter(r->r.getDrawingNbr().equals(drawingn)).findFirst().orElse(null);

        }else{
            cDrawingExt = (CDrawingExt) drawingList.stream().filter(r->r.getDrawingNbr().equals(drawingn)).findFirst().orElse(null);
        }
        if (Objects.isNull(cDrawingExt))
            throw new RuntimeException("Drawing not found " + drawingn);
        if (cDrawingExt.isLoaded()){
            return  FileController.returnDrawingWhenIsLoaded(cDrawingExt);
        }
        return FileController.returnDrawingWhenIsNotLoaded(sessionPatent,cDrawingExt,patentService);
    }

    private void setBaseModelAttributes(Model model,List<CDrawing> drawingList,String sessionIdentifier,String filingNumber,HttpServletRequest request){
        model.addAttribute("drawingList", drawingList);
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        setModelAttributes(model,filingNumber,sessionIdentifier,request);
    }
}
