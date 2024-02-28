package com.duosoft.ipas.controller.rest.abdocs;

import bg.duosoft.abdocs.model.DocFile;
import bg.duosoft.abdocs.model.DocFileVisibility;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.model.DocumentTypeDto;
import bg.duosoft.abdocs.model.response.DownloadFileResponse;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.rest.custommodel.EmptyResponse;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.abdocs.document.*;
import com.duosoft.ipas.controller.rest.BaseRestController;
import com.duosoft.ipas.controller.rest.RestApiException;
import com.duosoft.ipas.controller.rest.mapper.RAbdocsDocFileMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.duosoft.ipas.controller.rest.BaseRestController.BASE_REST_URL;

@Slf4j
@RestController
@Api(tags = {"ABDOCS"})
@RequestMapping(BASE_REST_URL + "/abdocs")
public class DocumentController extends BaseRestController {

    @Autowired
    private AbdocsService abdocsService;

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @Autowired
    private RAbdocsDocFileMapper rAbdocsDocFileMapper;

    @ApiOperation(value = "Update file content")
    @PostMapping(value = "/updateFileContent", produces = "application/json")
    public RestApiResponse<EmptyResponse> updateFileContent(@RequestBody RestApiRequest<RAbdocUpdateFileContentRequest> request) {

        if (Objects.isNull(request.getData().getFileName()))
            throw new RuntimeException("empty file name!");

        Document document= abdocsService.selectDocumentByRegistrationNumber(request.getData().getRegistrationNumber());
        Integer docFileId =null;
        if (Objects.nonNull(document)){
            for (DocFile docFile:document.getDocFiles()) {
                if (docFile.getName().equals(request.getData().getFileName())){
                    docFileId = docFile.getId();
                    break;
                }
            }
        }
        abdocsService.updateFileContent(docFileId,request.getData().getFileName(),request.getData().getNewContent());
        return new RestApiResponse<>(new EmptyResponse());
    }


    @ApiOperation(value = "Insert file")
    @PostMapping(value = "/insertFile", produces = "application/json")
    public RestApiResponse<EmptyResponse> insertFile(@RequestBody RestApiRequest<RAbdocUpdateFileContentRequest> request) {

        if (Objects.isNull(request.getData().getFileName()))
            throw new RuntimeException("empty file name!");
        Integer abdocsId = abdocsServiceAdmin.selectDocumentIdByRegistrationNumber(request.getData().getRegistrationNumber());
        abdocsService.uploadFileToExistingDocument(abdocsId, request.getData().getNewContent(), request.getData().getFileName(), request.getData().getDescription(), false, DocFileVisibility.PublicAttachedFile);
        return new RestApiResponse<>(new EmptyResponse());
    }


    @ApiOperation(value = "Get document content")
    @PostMapping(value = "/getDocumentContent", produces = "application/json")
    public RestApiResponse<RAbdocsDocumentContentResponse> getAbdocsDocumentContent(@RequestBody RestApiRequest<RAbdocsDocumentContentRequest> request) {
        if (Objects.isNull(request.getData().getFileName()))
            throw new RuntimeException("empty file name!");

        RAbdocsDocumentContentResponse response=new RAbdocsDocumentContentResponse();
        Document document = null;
        String key = null;
        Integer databaseId=null;

        if (Objects.nonNull(request.getData().getAbdocsId())){
            document = abdocsServiceAdmin.selectDocumentById(request.getData().getAbdocsId());
        }else if (Objects.nonNull(request.getData().getRegistrationNumber())){
            document= abdocsServiceAdmin.selectDocumentByRegistrationNumber(request.getData().getRegistrationNumber());
        }else{
            throw new RuntimeException("Empty registration number and abdocsId");
        }

        if (Objects.nonNull(document)){
            for (DocFile docFile:document.getDocFiles()) {
                if (docFile.getName().equals(request.getData().getFileName())){
                    key = docFile.getKey().toString();
                    databaseId=docFile.getDbId();
                    break;
                }
            }
        }
        DownloadFileResponse downloadFileResponse=new DownloadFileResponse();
        if (StringUtils.isEmpty(key) || Objects.isNull(databaseId)){
            downloadFileResponse.setContent(null);
        }else{
            downloadFileResponse = abdocsServiceAdmin.downloadFile(key, request.getData().getFileName(), databaseId);
        }
        response.setContent(downloadFileResponse.getContent());
        return new RestApiResponse<>(response);
    }


    @ApiOperation(value = "Get document files")
    @PostMapping(value = "/getDocFiles", produces = "application/json")
    public RestApiResponse<RAbdocsDocFilesResponse> getAbdocsDocFiles(@RequestBody RestApiRequest<RAbdocsDocFilesRequest> request) {
        Document document = null;
        if (Objects.nonNull(request.getData().getAbdocsId())){
            document = abdocsServiceAdmin.selectDocumentById(request.getData().getAbdocsId());
        }else if (Objects.nonNull(request.getData().getRegistrationNumber())){
             document= abdocsServiceAdmin.selectDocumentByRegistrationNumber(request.getData().getRegistrationNumber());
        }else{
            throw new RuntimeException("Empty registration number and abdocsId");
        }
        RAbdocsDocFilesResponse response=new RAbdocsDocFilesResponse();
        List<RAbdocsDocFile> rAbdocsDocFiles = new ArrayList<>();
        if (Objects.nonNull(document)){
            response.setDocumentStatus(document.getDocStatus().alias());
            for (DocFile docFile:document.getDocFiles()) {
                RAbdocsDocFile rAbdocsDocFile=new RAbdocsDocFile();
                rAbdocsDocFile.setId(docFile.getId());
                rAbdocsDocFile.setDocId(docFile.getDocId());
                rAbdocsDocFile.setDbId(docFile.getDbId());
                rAbdocsDocFile.setKey(docFile.getKey());
                rAbdocsDocFile.setName(docFile.getName());
                rAbdocsDocFile.setPrimary(docFile.isPrimary());
                rAbdocsDocFile.setDescription(docFile.getDescription());
                rAbdocsDocFile.setDocFileVisibility(docFile.getDocFileVisibility().alias());
                rAbdocsDocFiles.add(rAbdocsDocFile);
            }
        }
        response.setRAbdocsDocFiles(rAbdocsDocFiles);
        return new RestApiResponse<>(response);
    }

    @ApiOperation(value = "Download file")
    @PostMapping(value = "/downloadFile", produces = "application/json")
    public RestApiResponse<RAbdocsDownloadFileResponse> downloadFile(@RequestBody RestApiRequest<RAbdocsDownloadFileRequest> request) {
        if (Objects.isNull(request)
                || Objects.isNull(request.getData())
                || Objects.isNull(request.getData().getDatabaseId())
                || Objects.isNull(request.getData().getFileName())
                || Objects.isNull(request.getData().getUuid())) {
            throw new RestApiException("Bad request data !");
        }

        RAbdocsDownloadFileRequest requestData = request.getData();
        DownloadFileResponse file = abdocsServiceAdmin.downloadFile(requestData.getUuid(), requestData.getFileName(), requestData.getDatabaseId());
        return new RestApiResponse<>(RAbdocsDownloadFileResponse.builder()
                .content(file.getContent())
                .fileName(file.getFileName())
                .type(file.getType())
                .build());
    }


    @ApiOperation(value = "Select simple document by sha256 hashed document id + salt")
    @PostMapping(value = "/selectSimpleDocumentByHash", produces = "application/json")
    public RestApiResponse<RAbdocsSimpleDocumentResponse> selectDocumentByHash(@RequestBody RestApiRequest<String> request) {
        String abdocsIdSha256Hex = request.getData();
        if (StringUtils.isEmpty(abdocsIdSha256Hex)) {
            throw new RestApiException("Bad request !");
        }

        Document document = abdocsServiceAdmin.selectDocumentByHashNumber(abdocsIdSha256Hex);
        if (Objects.isNull(document)) {
            return generateResponse(null);
        }

        return new RestApiResponse<>(RAbdocsSimpleDocumentResponse.builder()
                .docId(document.getDocId())
                .docDirection(Objects.isNull(document.getDocDirection()) ? null : document.getDocDirection().name())
                .docStatus(Objects.isNull(document.getDocStatus()) ? null : document.getDocStatus().name())
                .docSubject(document.getDocSubject())
                .docTypeId(document.getDocTypeId())
                .docTypeName(selectDocumentTypeName(document))
                .receivedOriginalState(Objects.isNull(document.getReceivedOriginalState()) ? null : document.getReceivedOriginalState().name())
                .regDate(document.getRegDate())
                .regUri(document.getRegUri())
                .docFiles(selectFiles(document))
                .build());
    }

    private String selectDocumentTypeName(Document document) {
        List<DocumentTypeDto> documentTypeDtos = abdocsServiceAdmin.selectAllDocumentTypes();
        if (!CollectionUtils.isEmpty(documentTypeDtos)) {
            DocumentTypeDto documentTypeDto = documentTypeDtos.stream()
                    .filter(d -> d.getNomValueId().equals(document.getDocTypeId()))
                    .findFirst()
                    .orElse(null);

            if (Objects.nonNull(documentTypeDto)) {
                return documentTypeDto.getName();
            }
        }
        return null;
    }

    private List<RAbdocsDocFile> selectFiles(Document document) {
        List<RAbdocsDocFile> result = new ArrayList<>();

        List<DocFile> docFiles = document.getDocFiles();
        if (!CollectionUtils.isEmpty(docFiles)) {
            result.addAll(rAbdocsDocFileMapper.toRestList(docFiles));
        }
        List<DocFile> docFileLinks = document.getDocFileLinks();
        if (!CollectionUtils.isEmpty(docFileLinks)) {
            for (DocFile docFileLink : docFileLinks) {
                docFileLink.setDocFileVisibility(DocFileVisibility.PublicAttachedFile);// Linked files are always public, but they don't return that information in JSON
                result.add(rAbdocsDocFileMapper.toRest(docFileLink));
            }
        }

        return CollectionUtils.isEmpty(result) ? null : result;
    }

}
