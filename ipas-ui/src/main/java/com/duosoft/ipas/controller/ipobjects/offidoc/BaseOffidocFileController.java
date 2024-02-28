package com.duosoft.ipas.controller.ipobjects.offidoc;

import bg.duosoft.abdocs.model.DocFile;
import bg.duosoft.abdocs.model.DocFileVisibility;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.util.offidoc.OffidocUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 26.04.2021
 * Time: 10:39
 */
public abstract class BaseOffidocFileController {

    protected void saveFile(byte[] bytes, String fileName, Integer id, Document document) {
        List<DocFile> docFiles = document.getDocFiles();
        if (!CollectionUtils.isEmpty(docFiles)) {
            DocFile existingFile = OffidocUtils.selectGeneratedFileForFileName(fileName, document);
            if (Objects.nonNull(existingFile)) {
                getAbdocsService().updateFileContent(existingFile.getId(), fileName, bytes);
            } else {
                uploadNewFile(bytes, fileName, id);
            }
        } else {
            uploadNewFile(bytes, fileName, id);
        }
    }

    protected void uploadNewFile(byte[] bytes, String fileName, Integer id) {
        getAbdocsService().uploadFileToExistingDocument(id, bytes, fileName, false, getFileVisibility());
    }

    protected abstract AbdocsService getAbdocsService();

    protected abstract DocFileVisibility getFileVisibility();

}
