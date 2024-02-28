package com.duosoft.ipas.controller.ipobjects.common.attachment;

import bg.duosoft.abdocs.model.response.DownloadFileResponse;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CDrawing;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.util.CDrawingExt;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.util.attachment.AttachmentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Controller
@RequestMapping(value = "/file")
@Slf4j
public class FileController {

    @Autowired
    private AbdocsService abdocsService;

    @RequestMapping(value = "/abdocs-file")
    public void getAbdocsFile(HttpServletResponse response,
                              @RequestParam String uuid,
                              @RequestParam String fileName,
                              @RequestParam Integer databaseId) {
        DownloadFileResponse downloadFileResponse = abdocsService.downloadFile(uuid, fileName, databaseId);
        if (Objects.nonNull(downloadFileResponse))
            AttachmentUtils.writeData(response, downloadFileResponse.getContent(), downloadFileResponse.getType(), downloadFileResponse.getFileName());
    }

    public static ResponseEntity<byte[]> returnDrawingWhenIsNotLoaded(CPatent sessionPatent, CDrawingExt cDrawingExt, PatentService patentService) {
        final HttpHeaders headers = new HttpHeaders();
        CFileId fileId = sessionPatent.getFile().getFileId();
        CDrawing databaseDrawing = patentService.selectDrawing(fileId, Math.toIntExact(cDrawingExt.getDrawingNbrDb()));
        if (Objects.isNull(databaseDrawing))
            return null;
        return new ResponseEntity<>(databaseDrawing.getDrawingData(), headers, HttpStatus.OK);
    }

    public static ResponseEntity<byte[]> returnDrawingWhenIsLoaded(CDrawing cDrawing) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(cDrawing.getDrawingType()));
        return new ResponseEntity<>(cDrawing.getDrawingData(), headers, HttpStatus.OK);
    }


}