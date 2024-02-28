package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CDrawing;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.util.CDrawingExt;
import bg.duosoft.ipas.core.service.patent.PatentService;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PatentDrawingUtils {

    public static void sortDrawingList(List<CDrawing> drawing) {
        long orderedDrawingNumber = 1;
        for (int i = 0; i < drawing.size(); i++) {
           CDrawing cDrawing = drawing.get(i);
            cDrawing.setDrawingNbr(orderedDrawingNumber);
            if (!Objects.isNull(cDrawing.getSingleDesignExtended())){
                cDrawing.getSingleDesignExtended().setDrawingNbr(orderedDrawingNumber);
            }
            orderedDrawingNumber++;
        }
    }

    public static Long getDrawingsMaxNumber(List<CDrawing> drawings) {
        long maxDrawingNbr = drawings.stream().mapToLong(r -> r.getDrawingNbr()).max().orElse(0);
        return ++maxDrawingNbr;
    }


    public static void swapDrawingPosition(List<CDrawing> drawings, Long firstDrawingNumber, Long secondDrawingNumber) {
        CDrawing firstDrawing = drawings.stream()
                .filter(cDrawing -> cDrawing.getDrawingNbr().equals(firstDrawingNumber))
                .findFirst()
                .orElse(null);
        if (Objects.isNull(firstDrawing))
            throw new RuntimeException("Drawing list not contain drawing with number " + firstDrawingNumber);

        CDrawing secondDrawing = drawings.stream()
                .filter(cDrawing -> cDrawing.getDrawingNbr().equals(secondDrawingNumber))
                .findFirst()
                .orElse(null);
        if (Objects.isNull(secondDrawing))
            throw new RuntimeException("Drawing list not contain drawing with number " + secondDrawingNumber);

        firstDrawing.setDrawingNbr(secondDrawingNumber);
        if (!Objects.isNull(firstDrawing.getSingleDesignExtended())){
            firstDrawing.getSingleDesignExtended().setDrawingNbr(secondDrawingNumber);
        }
        secondDrawing.setDrawingNbr(firstDrawingNumber);
        if (!Objects.isNull(secondDrawing.getSingleDesignExtended())){
            secondDrawing.getSingleDesignExtended().setDrawingNbr(firstDrawingNumber);
        }

        drawings.sort((o1, o2) -> (int) (o1.getDrawingNbr() - o2.getDrawingNbr()));
    }


    public static boolean validDrawingPositions(List<CDrawing> drawings, Long drawingNbrFirstParam, Long drawingNbrSecondParam) {
        return !CollectionUtils.isEmpty(drawings) && !Objects.isNull(drawingNbrFirstParam) && !Objects.isNull(drawingNbrSecondParam) && drawings.get(0).getDrawingNbr() <= drawingNbrFirstParam && drawings.get(0).getDrawingNbr() <= drawingNbrSecondParam
                && drawings.get(drawings.size() - 1).getDrawingNbr() >= drawingNbrFirstParam && drawings.get(drawings.size() - 1).getDrawingNbr() >= drawingNbrSecondParam;
    }


    public static void initCPatentDrawings(CPatent patent){

        List<CDrawing> cDrawingExtList;
        if (Objects.isNull(patent.getTechnicalData().getDrawingList()) || CollectionUtils.isEmpty(patent.getTechnicalData().getDrawingList())){
            cDrawingExtList = new ArrayList<>();
        }else{
            cDrawingExtList = patent.getTechnicalData().getDrawingList().stream()
                    .map(p -> {
                        CDrawingExt cDrawingExt = new CDrawingExt();
                        cDrawingExt = CDrawingExt.createCDrawingExtObject(p);
                        return cDrawingExt;
                    })
                    .collect(Collectors.toList());
        }
        patent.getTechnicalData().setDrawingList(cDrawingExtList);
    }

    public static void loadCPatentEmptyDrawings(CPatent patent, PatentService patentService){
        List<CDrawing> patentDrawings = patent.getTechnicalData().getDrawingList();
        if (!Objects.isNull(patentDrawings) && !CollectionUtils.isEmpty(patentDrawings)){
            CFileId fileId = patent.getFile().getFileId();
            patentDrawings.forEach(r->{
                if (!r.isLoaded()){
                    CDrawingExt cDrawingExt = (CDrawingExt)r;
                    CDrawing databaseDrawing = patentService.selectDrawing(fileId, Math.toIntExact(cDrawingExt.getDrawingNbrDb()));
                    r.setDrawingData(databaseDrawing.getDrawingData());
                }
            });
        }
        patent.getTechnicalData().setDrawingList(patentDrawings);
    }

}
