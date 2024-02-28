package com.duosoft.ipas.controller.ipobjects.common.reception.create.scan;

import bg.duosoft.abdocs.model.DocFileVisibility;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/reception/scan")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
public class ScanDocController {

    private static final String SCANNED_FILE_DEFAULT_NAME = "scan_{date}.pdf";
    private static final String SCANNED_FILE_REGISTRATION_NUMBER_NAME = "{registrationNumber}_{date}.pdf";

    @Autowired
    private AbdocsService abdocsService;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public boolean uploadScannedFile(@RequestParam MultipartFile file, @RequestParam String registrationNumber) {
        try {
            Integer documentId = abdocsService.selectDocumentIdByRegistrationNumber(registrationNumber);
            if (Objects.isNull(documentId)) {
                return false;
            }

            byte[] bytes = file.getBytes();
            if (bytes.length < 1) {
                return false;
            }

            uploadFile(selectScannedFileName(file), documentId, bytes);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    private String selectScannedFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        if (!StringUtils.isEmpty(originalFilename)) {
            originalFilename = SCANNED_FILE_REGISTRATION_NUMBER_NAME.replace("{registrationNumber}", originalFilename).replace("{date}", simpleDateFormat.format(new Date()));
        } else {
            originalFilename = SCANNED_FILE_DEFAULT_NAME.replace("{date}", simpleDateFormat.format(new Date()));
        }
        return originalFilename;
    }

    private void uploadFile(String fileName, Integer documentId, byte[] bytes) {
        abdocsService.uploadFileToExistingDocument(documentId, bytes, fileName, false, DocFileVisibility.PublicAttachedFile);
    }

}
