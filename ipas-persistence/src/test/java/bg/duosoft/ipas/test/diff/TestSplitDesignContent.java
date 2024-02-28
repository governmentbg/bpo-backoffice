package bg.duosoft.ipas.test.diff;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: ggeorgiev
 * Date: 06.12.2021
 * Time: 11:12
 */
public class TestSplitDesignContent {
    private static String DESIGN_CONTENT1 = "BG/Е/2009/11860001 <DIFF FIELD=\"CPatent\"> <DIFF FIELD=\"{CPatent.technicalData}\"> <DIFF FIELD=\"{CTechnicalData.drawingList}\"> <ITEM> <BEFORE> <ELEMENT> <LIST></LIST> </ELEMENT> </BEFORE> <AFTER> <ELEMENT> <LIST><ELEMENT> <FIELD NAME=\"{CDrawingExt.drawingData}\">66579 bytes</FIELD> <FIELD NAME=\"{CDrawingExt.drawingNbr}\">2</FIELD> <FIELD NAME=\"{CDrawingExt.drawingType}\">image/jpeg</FIELD> <FIELD NAME=\"{CDrawingExt.loaded}\">true</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CSingleDesignExtended.drawingNbr}\">2</FIELD> <FIELD NAME=\"{CSingleDesignExtended.imagePublished}\">false</FIELD> <FIELD NAME=\"{CSingleDesignExtended.imageRefused}\">false</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CImageViewType.viewTypeId}\">2</FIELD> <FIELD NAME=\"{CImageViewType.viewTypeName}\">Изглед в перспектива</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CDrawingExt.drawingData}\">44683 bytes</FIELD> <FIELD NAME=\"{CDrawingExt.drawingNbr}\">3</FIELD> <FIELD NAME=\"{CDrawingExt.drawingType}\">image/jpeg</FIELD> <FIELD NAME=\"{CDrawingExt.loaded}\">true</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CSingleDesignExtended.drawingNbr}\">3</FIELD> <FIELD NAME=\"{CSingleDesignExtended.imagePublished}\">false</FIELD> <FIELD NAME=\"{CSingleDesignExtended.imageRefused}\">false</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CImageViewType.viewTypeId}\">2</FIELD> <FIELD NAME=\"{CImageViewType.viewTypeName}\">Изглед в перспектива</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CDrawingExt.drawingData}\">1262 bytes</FIELD> <FIELD NAME=\"{CDrawingExt.drawingNbr}\">4</FIELD> <FIELD NAME=\"{CDrawingExt.drawingType}\">image/png</FIELD> <FIELD NAME=\"{CDrawingExt.loaded}\">true</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CSingleDesignExtended.drawingNbr}\">4</FIELD> <FIELD NAME=\"{CSingleDesignExtended.imagePublished}\">false</FIELD> <FIELD NAME=\"{CSingleDesignExtended.imageRefused}\">false</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CImageViewType.viewTypeId}\">2</FIELD> <FIELD NAME=\"{CImageViewType.viewTypeName}\">Изглед в перспектива</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CDrawingExt.drawingData}\">64447 bytes</FIELD> <FIELD NAME=\"{CDrawingExt.drawingNbr}\">5</FIELD> <FIELD NAME=\"{CDrawingExt.drawingType}\">image/jpeg</FIELD> <FIELD NAME=\"{CDrawingExt.loaded}\">true</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CSingleDesignExtended.drawingNbr}\">5</FIELD> <FIELD NAME=\"{CSingleDesignExtended.imagePublished}\">false</FIELD> <FIELD NAME=\"{CSingleDesignExtended.imageRefused}\">false</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CImageViewType.viewTypeId}\">2</FIELD> <FIELD NAME=\"{CImageViewType.viewTypeName}\">Изглед в перспектива</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CDrawingExt.drawingData}\">15438 bytes</FIELD> <FIELD NAME=\"{CDrawingExt.drawingNbr}\">6</FIELD> <FIELD NAME=\"{CDrawingExt.drawingType}\">image/png</FIELD> <FIELD NAME=\"{CDrawingExt.loaded}\">true</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CSingleDesignExtended.drawingNbr}\">6</FIELD> <FIELD NAME=\"{CSingleDesignExtended.imagePublished}\">false</FIELD> <FIELD NAME=\"{CSingleDesignExtended.imageRefused}\">false</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CImageViewType.viewTypeId}\">2</FIELD> <FIELD NAME=\"{CImageViewType.viewTypeName}\">Изглед в перспектива</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CDrawingExt.drawingData}\">80069 bytes</FIELD> <FIELD NAME=\"{CDrawingExt.drawingNbr}\">7</FIELD> <FIELD NAME=\"{CDrawingExt.drawingType}\">image/jpeg</FIELD> <FIELD NAME=\"{CDrawingExt.loaded}\">true</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CSingleDesignExtended.drawingNbr}\">7</FIELD> <FIELD NAME=\"{CSingleDesignExtended.imagePublished}\">false</FIELD> <FIELD NAME=\"{CSingleDesignExtended.imageRefused}\">false</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CImageViewType.viewTypeId}\">2</FIELD> <FIELD NAME=\"{CImageViewType.viewTypeName}\">Изглед в перспектива</FIELD> </ELEMENT> </LIST> </ELEMENT> </AFTER> </ITEM> </DIFF> </DIFF> </DIFF> ";
    private static String DESIGN_CONTENT2 = "BG/Д/2017/11357 <DIFF FIELD=\"CPatent\"> <DIFF FIELD=\"{CPatent.file}\"> <DIFF FIELD=\"{CFile.relationshipList}\"> <ITEM> <BEFORE> <ELEMENT> <LIST></LIST> </ELEMENT> </BEFORE> <AFTER> <ELEMENT> <LIST><ELEMENT> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CFileId.fileNbr}\">11357009</FIELD> <FIELD NAME=\"{CFileId.fileSeq}\">BG</FIELD> <FIELD NAME=\"{CFileId.fileSeries}\">2017</FIELD> <FIELD NAME=\"{CFileId.fileType}\">Е</FIELD> <FIELD NAME=\"{CRelationship.relationshipRole}\">1</FIELD> <FIELD NAME=\"{CRelationship.relationshipType}\">Д</FIELD> </ELEMENT> </LIST> </ELEMENT> </AFTER> </ITEM> </DIFF> </DIFF> <DIFF FIELD=\"{CPatent.technicalData}\"> <DIFF FIELD=\"{CTechnicalData.englishTitle}\"> <ITEM>  <BEFORE>   <VALUE></VALUE>  </BEFORE>  <AFTER>   <VALUE>null</VALUE>  </AFTER> </ITEM> </DIFF> </DIFF> </DIFF>  BG/Е/2017/11357009 <DIFF FIELD=\"CPatent\"> <DIFF FIELD=\"{CPatent.file}\"> <DIFF FIELD=\"{CFile.fileId}\"> <DIFF FIELD=\"{CFileId.fileNbr}\"> <ITEM>  <BEFORE>   <VALUE>null</VALUE>  </BEFORE>  <AFTER>   <VALUE>11357009</VALUE>  </AFTER> </ITEM> </DIFF> <DIFF FIELD=\"{CFileId.fileSeq}\"> <ITEM>  <BEFORE>   <VALUE>null</VALUE>  </BEFORE>  <AFTER>   <VALUE>BG</VALUE>  </AFTER> </ITEM> </DIFF> <DIFF FIELD=\"{CFileId.fileSeries}\"> <ITEM>  <BEFORE>   <VALUE>null</VALUE>  </BEFORE>  <AFTER>   <VALUE>2017</VALUE>  </AFTER> </ITEM> </DIFF> <DIFF FIELD=\"{CFileId.fileType}\"> <ITEM>  <BEFORE>   <VALUE>null</VALUE>  </BEFORE>  <AFTER>   <VALUE>Е</VALUE>  </AFTER> </ITEM> </DIFF> </DIFF> <DIFF FIELD=\"{CFile.filingData}\"> <DIFF FIELD=\"{CFilingData.applicationSubtype}\"> <ITEM>  <BEFORE>   <VALUE>null</VALUE>  </BEFORE>  <AFTER>   <VALUE>И</VALUE>  </AFTER> </ITEM> </DIFF> <DIFF FIELD=\"{CFilingData.applicationType}\"> <ITEM>  <BEFORE>   <VALUE>null</VALUE>  </BEFORE>  <AFTER>   <VALUE>ЕД</VALUE>  </AFTER> </ITEM> </DIFF> </DIFF> </DIFF> <DIFF FIELD=\"{CPatent.reception}\"> <ITEM>  <BEFORE>   <VALUE>null</VALUE>  </BEFORE>  <AFTER>   <VALUE>true</VALUE>  </AFTER> </ITEM> </DIFF> <DIFF FIELD=\"{CPatent.technicalData}\"> <DIFF FIELD=\"{CTechnicalData.drawingList}\"> <ITEM> <BEFORE> <ELEMENT> <LIST></LIST> </ELEMENT> </BEFORE> <AFTER> <ELEMENT> <LIST><ELEMENT> <FIELD NAME=\"{CDrawingExt.drawingData}\">47915 bytes</FIELD> <FIELD NAME=\"{CDrawingExt.drawingNbr}\">1</FIELD> <FIELD NAME=\"{CDrawingExt.drawingType}\">image/jpeg</FIELD> <FIELD NAME=\"{CDrawingExt.loaded}\">true</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CSingleDesignExtended.drawingNbr}\">1</FIELD> <FIELD NAME=\"{CSingleDesignExtended.imagePublished}\">false</FIELD> <FIELD NAME=\"{CSingleDesignExtended.imageRefused}\">false</FIELD> </ELEMENT> <ELEMENT> <FIELD NAME=\"{CImageViewType.viewTypeId}\">2</FIELD> <FIELD NAME=\"{CImageViewType.viewTypeName}\">Изглед в перспектива</FIELD> </ELEMENT> </LIST> </ELEMENT> </AFTER> </ITEM> </DIFF> <DIFF FIELD=\"{CTechnicalData.englishTitle}\"> <ITEM>  <BEFORE>   <VALUE>null</VALUE>  </BEFORE>  <AFTER>   <VALUE>xxx</VALUE>  </AFTER> </ITEM> </DIFF> <DIFF FIELD=\"{CTechnicalData.locarnoClassList}\"> <ITEM> <BEFORE> <ELEMENT> <LIST></LIST> </ELEMENT> </BEFORE> <AFTER> <ELEMENT> <LIST><ELEMENT> <FIELD NAME=\"{CPatentLocarnoClasses.locarnoClassCode}\">01-01</FIELD> <FIELD NAME=\"{CPatentLocarnoClasses.locarnoEditionCode}\">0</FIELD> </ELEMENT> </LIST> </ELEMENT> </AFTER> </ITEM> </DIFF> <DIFF FIELD=\"{CTechnicalData.title}\"> <ITEM>  <BEFORE>   <VALUE>null</VALUE>  </BEFORE>  <AFTER>   <VALUE>xxx</VALUE>  </AFTER> </ITEM> </DIFF> </DIFF> </DIFF> ";
    private static Pattern DESIGN_PATTERN = Pattern.compile("([A-Z]{2}/[A-ZА-Я]/\\d{4}/\\d+\\s*)(<DIFF.*?</DIFF>)\\s+([A-Z]{2}/[A-ZА-Я]/\\d{4}/\\d+|$)");

    public static void main(String[] args) {
//        parseDesignContent(DESIGN_CONTENT1);
        parseDesignContent(DESIGN_CONTENT2);
    }
    private static void parseDesignContent(String content) {
        Matcher matcher = DESIGN_PATTERN.matcher(content);
        int start = 0;
        while (matcher.find(start)) {
            String fileId = matcher.group(1);
            String diff = matcher.group(2);
            System.out.println(fileId);
            System.out.println(diff);
//            System.out.println(matcher.group());
//            System.out.println(matcher.start(1));
//            System.out.println(matcher.start(2));
            start = matcher.start(3);
//            System.out.println(matcher.start(3));
//            break;
        }
    }
}