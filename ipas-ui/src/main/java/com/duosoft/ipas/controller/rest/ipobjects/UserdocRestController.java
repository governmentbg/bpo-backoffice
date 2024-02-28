package com.duosoft.ipas.controller.rest.ipobjects;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.model.mark.CUserdocSingleDesign;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocHierarchyNode;
import bg.duosoft.ipas.core.model.userdoc.international_registration.CAcceptInternationalRegistrationRequest;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.service.action.InternationalMarkActionService;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.file.FileRelationshipsService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.reception.UserdocReceptionRelationService;
import bg.duosoft.ipas.core.service.userdoc.UserdocInternationalMarkService;
import bg.duosoft.ipas.core.service.userdoc.UserdocIrregularityLetterService;
import bg.duosoft.ipas.core.service.userdoc.UserdocMadridEfilingService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.enums.AcceptUserdocEntryType;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.RelationshipType;
import bg.duosoft.ipas.enums.UserdocType;
import bg.duosoft.ipas.rest.custommodel.EmptyResponse;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.userdoc.*;
import bg.duosoft.ipas.rest.custommodel.userdoc.international_mark.RAcceptInternationalUserdocRequest;
import bg.duosoft.ipas.rest.custommodel.userdoc.international_registration.RAcceptInternationalRegistrationRequest;
import bg.duosoft.ipas.rest.custommodel.userdoc.international_registration.RAcceptMadridEfilingUserdocRequest;
import bg.duosoft.ipas.rest.custommodel.userdoc.international_registration.RAcceptedInternationalRegistrationResponse;
import bg.duosoft.ipas.rest.custommodel.userdoc.irregularity_letter.RAcceptIrregLetterUserdocRequest;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.userdoc.RGetFileUserdocHierarchyFilteredResponse;
import bg.duosoft.ipas.rest.model.userdoc.RUserdoc;
import bg.duosoft.ipas.rest.model.userdoc.RUserdocHierarchyNode;
import bg.duosoft.ipas.rest.model.userdoc.RUserdocRelationRestriction;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.controller.rest.BaseRestController;
import com.duosoft.ipas.controller.rest.mapper.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.duosoft.ipas.controller.rest.BaseRestController.BASE_REST_URL;

@Slf4j
@RestController
@Api(tags = {"USERDOCS"})
@RequestMapping(BASE_REST_URL + "/userdoc")
public class UserdocRestController extends BaseRestController {

    @Autowired
    private RUserdocMapper userdocMapper;

    @Autowired
    private RAttachmentMapper attachmentMapper;
    @Autowired
    private ReceptionService receptionService;
    @Autowired
    private UserdocService userdocService;
    @Autowired
    private RDocumentIdMapper rDocumentIdMapper;
    @Autowired
    private RFileIdMapper rFileIdMapper;

    @Autowired
    private RProcessIdMapper rProcessIdMapper;

    @Autowired
    private RRootGroundsFoFormatMapper foGroundsMapper;

    @Autowired
    private RReceptionResponseMapper receptionResponseMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private RUserdocHierarchyMapper rUserdocHierarchyMapper;

    @Autowired
    private InternationalMarkActionService internationalMarkActionService;

    @Autowired
    private RAcceptInternationalRegistrationMapper acceptInternationalRegistrationMapper;

    @Autowired
    private UserdocReceptionRelationService userdocReceptionRelationService;

    @Autowired
    private FileRelationshipsService fileRelationshipsService;

    @Autowired
    private UserdocMadridEfilingService userdocMadridEfilingService;

    @Autowired
    private UserdocIrregularityLetterService userdocIrregularityLetterService;

    @Autowired
    private UserdocInternationalMarkService userdocInternationalMarkService;


    @ApiOperation(value = "Accept userdoc")
    @PostMapping(value = "/accept", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
    public RestApiResponse<RAcceptedUserdocResponse> acceptUserdoc(@RequestBody RestApiRequest<RAcceptUserdocRequest> userdocRequest) {
        CUserdoc userdoc = userdocMapper.toCore(userdocRequest.getData().getUserdoc());
        if (Objects.nonNull(userdocRequest.getData().getGroundsNew()) && !CollectionUtils.isEmpty(userdocRequest.getData().getGroundsNew())) {
            userdoc.setUserdocRootGrounds(foGroundsMapper.toCoreList(userdocRequest.getData().getGroundsNew()));
        }
        List<CAttachment> attachmentList = attachmentMapper.toCoreList(userdocRequest.getData().getAttachments());
        CFileId affectedId = constructAffectedId(userdocRequest.getData().getAffectedId(), userdocRequest.getData().getAffectedRegistrationNbr(), userdocRequest.getData().getAffectedRegistrationDup(), AcceptUserdocEntryType.ACCEPT_USERDOC);
        correctUserdocSingleDesignsIds(userdoc.getSingleDesigns(), affectedId);
        RAcceptedUserdocResponse response = new RAcceptedUserdocResponse();
        if (Objects.nonNull(affectedId)) {
            CReceptionResponse res = receptionService.acceptUserdoc(userdoc, attachmentList, affectedId, userdocRequest.getData().getRelateRequestToObject(), rDocumentIdMapper.toCore(userdocRequest.getData().getParentDocumentId()));
            response = new RAcceptedUserdocResponse(receptionResponseMapper.toRest(res));
            response.setAffectedFileId(rFileIdMapper.toRest(affectedId));
        }
        return new RestApiResponse<>(response);
    }

    private void correctUserdocSingleDesignsIds(List<CUserdocSingleDesign> singleDesigns, CFileId affectedId) {
        if (singleDesigns != null) {
            List<CRelationship> dividedRelationships = fileRelationshipsService.findRelationships1ForFile(affectedId, RelationshipType.DIVISIONAL_DESIGN_TYPE);
            CRelationship dividedFrom = dividedRelationships.size() > 0 ? dividedRelationships.get(0) : null;

            singleDesigns.stream().forEach(sd -> {
                if (dividedFrom != null) {
                    sd.getFileId().setFileSeries(dividedFrom.getFileId().getFileSeries());
                    String currentNumberStr = sd.getFileId().getFileNbr().toString();
                    String designIndex = (currentNumberStr).substring(currentNumberStr.length() - 3);
                    sd.getFileId().setFileNbr(Integer.parseInt(dividedFrom.getFileId().getFileNbr() + designIndex));
                }
            });
        }
    }

    private CFileId constructAffectedId(RFileId affectedId, Integer affectedRegistrationNbr, String registrationDup, AcceptUserdocEntryType acceptUserdocEntryType) {
        if (Objects.nonNull(affectedId)) {
            return new CFileId(affectedId.getFileSeq(), affectedId.getFileType(), affectedId.getFileSeries(), affectedId.getFileNbr());
        }
        if (Objects.isNull(affectedRegistrationNbr)) {
            throw new RuntimeException("Empty affectedId and affectedRegistrationNbr !");
        }

        List<CFile> allFiles;
        switch (acceptUserdocEntryType) {
            case ACCEPT_USERDOC:
            case ACCEPT_INTERNATIONAL_USERDOC:
                allFiles = fileService.findAllByRegistrationNbrAndDupAndFileType(affectedRegistrationNbr, registrationDup, FileType.getInternationalMarkFileTypes());
                break;
            case ACCEPT_MADRID_USERDOC:
                allFiles = fileService.findAllByRegistrationNbrAndDupAndFileType(affectedRegistrationNbr, registrationDup, FileType.getNationalMarkFileTypes());
                break;
            default:
                allFiles = null;
        }

        if (CollectionUtils.isEmpty(allFiles)) {
            throw new RuntimeException("No available records of given affectedRegistrationNbr and registrationDup !");
        }

        CFile cFile = allFiles.stream().max(Comparator.comparing(fileId -> fileId.getProcessSimpleData().getCreationDate())).orElse(null);
        if (Objects.isNull(cFile)) {
            throw new RuntimeException("Cannot find the most current record of given affectedRegistrationNbr and registrationDup !");
        }

        return new CFileId(cFile.getFileId().getFileSeq(), cFile.getFileId().getFileType(), cFile.getFileId().getFileSeries(), cFile.getFileId().getFileNbr());
    }

    @ApiOperation(value = "Select Userdoc")
    @PostMapping(value = "/get", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).UserdocViewAll.code())")
    public RestApiResponse<RUserdoc> getMark(@RequestBody RestApiRequest<RDocumentId> findIpObjectRequest) {
        CUserdoc res = userdocService.findUserdoc(rDocumentIdMapper.toCore(findIpObjectRequest.getData()));
        return generateResponse(res == null ? null : userdocMapper.toRest(res));
    }

    @ApiOperation(value = "Deletes userdoc")
    @PostMapping(value = "/delete", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).UserdocEditAll.code())")
    public RestApiResponse<EmptyResponse> deleteUserdoc(@RequestBody RestApiRequest<RDeleteUserdocRequest> rq) {
        userdocService.deleteUserdoc(rDocumentIdMapper.toCore(rq.getData().getDocumentId()), rq.getData().isDeleteInDocflowSystem());
        return new RestApiResponse<>(new EmptyResponse());
    }

    @ApiOperation(value = "Get Userdocs, related to a file ")
    @PostMapping(value = "/get-file-hierarchy", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).UserdocViewAll.code())")
    public RestApiResponse<List<RUserdocHierarchyNode>> getFileUserdocHierarchy(@RequestBody RestApiRequest<RGetFileUserdocHierarchyRequest> request) {
        List<CUserdocHierarchyNode> res = userdocService.getFileUserdocHierarchy(rFileIdMapper.toCore(request.getData().getFileId()), Objects.equals(true, request.getData().getFlatHierarchy()));
        return new RestApiResponse<>(rUserdocHierarchyMapper.toRestList(res));
    }

    @ApiOperation(value = "Get Userdocs, related to a file, filtered by some criteria ")
    @PostMapping(value = "/get-file-hierarchy-filtered", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).UserdocViewAll.code())")
    public RestApiResponse<RGetFileUserdocHierarchyFilteredResponse> getFileUserdocHierarchyFiltered(@RequestBody RestApiRequest<RGetFileUserdocHierarchyFilteredRequest> request) {
        List<CUserdocHierarchyNode> res = userdocService.getFileUserdocHierarchy(rFileIdMapper.toCore(request.getData().getFileId()), Objects.equals(true, request.getData().getFlatHierarchy()));
        CFile file = fileService.findById(rFileIdMapper.toCore(request.getData().getFileId()));
        CProcessId fileProcessId = file.getProcessId();
        List<String> allowedRelations = request.getData().getFilter() != null && request.getData().getFilter().getLinkedUserdocType() != null ?
                userdocReceptionRelationService.selectAllMainTypesForUserdocType(request.getData().getFilter().getLinkedUserdocType()).stream()
                        .map(rel -> rel.getMainType()).collect(Collectors.toList())
                : null;
        RUserdocRelationRestriction restriction = determineUserdocRelationRestriction(request.getData().getFileId().getFileType(), allowedRelations);

        List<CUserdocHierarchyNode> filtered = res.stream().filter(node -> allowedRelations == null || allowedRelations.contains(node.getUserdocType())).collect(Collectors.toList());
        List<RUserdocHierarchyNode> nodes = rUserdocHierarchyMapper.toRestList(filtered);
        RGetFileUserdocHierarchyFilteredResponse response = new RGetFileUserdocHierarchyFilteredResponse();
        response.setNodeList(nodes);
        response.setLinkedUserdocRestriction(restriction);
        response.setMainProcessId(rProcessIdMapper.toRest(fileProcessId));
        //TODO check if user can see each userdocs and filter
        return new RestApiResponse<>(response);
    }

    private RUserdocRelationRestriction determineUserdocRelationRestriction(String requestFileType, List<String> allowedRelations) {
        List<String> allowedRelationsUserdocs = new ArrayList<>(allowedRelations);
        allowedRelationsUserdocs.removeAll(FileType.getAllFileTypes());
        RUserdocRelationRestriction restriction = null;
        if (allowedRelations.contains(requestFileType)) {
            if (allowedRelationsUserdocs.size() > 0) {
                restriction = RUserdocRelationRestriction.USERDOC_RELATION_OPTIONAL;
            } else {
                restriction = RUserdocRelationRestriction.OBJECT_ONLY;
            }
        } else if (allowedRelationsUserdocs.size() > 0) {
            restriction = RUserdocRelationRestriction.USERDOC_RELATION_MANDATORY;
        }
        return restriction;
    }

    @ApiOperation(value = "Get Userdocs, related to an userdoc ")
    @PostMapping(value = "/get-userdoc-hierarchy", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).UserdocViewAll.code())")
    public RestApiResponse<List<RUserdocHierarchyNode>> getUserdocUserdocHierarchy(@RequestBody RestApiRequest<RGetUserdocUserdocHierarchyRequest> request) {
        List<CUserdocHierarchyNode> res = userdocService.getUserdocUserdocHierarchy(rDocumentIdMapper.toCore(request.getData().getDocumentId()), Objects.equals(true, request.getData().getFlatHierarchy()));
        return new RestApiResponse<>(rUserdocHierarchyMapper.toRestList(res));
    }

    @ApiOperation(value = "Get userdoc's master object fileId")
    @PostMapping(value = "/get-userdoc-master-file-id", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).UserdocViewAll.code())")
    public RestApiResponse<RFileId> getUserdocMasterFileId(@RequestBody RestApiRequest<RDocumentId> request) {
        CFileId res = userdocService.selectMainObjectIdOfUserdoc(rDocumentIdMapper.toCore(request.getData()));
        return new RestApiResponse<>(rFileIdMapper.toRest(res));
    }


    @ApiOperation(value = "Accept International Registration Userdoc")
    @PostMapping(value = "/accept-international-registration", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
    public RestApiResponse<RAcceptedInternationalRegistrationResponse> acceptInternationalRegistrationUserdoc(@RequestBody RestApiRequest<RAcceptInternationalRegistrationRequest> request) {
        CAcceptInternationalRegistrationRequest acceptRegistrationRequest = acceptInternationalRegistrationMapper.toCore(request.getData());
        CUserdoc userdoc = userdocService.selectByDataTextAndType(acceptRegistrationRequest.getRegistrationRequestExternalSystemId() + "/" + acceptRegistrationRequest.getWipoReference(), UserdocType.MARK_INTERNATIONAL_REGISTRATION_REQUEST);
        CUserdoc parentUserdoc = Objects.nonNull(userdoc) ? userdoc : UserdocUtils.findCorrectUserdocFromWipoRequest(acceptRegistrationRequest.getRegistrationRequestExternalSystemId(), userdocService);
        CReceptionResponse res = receptionService.acceptInternationalRegistrationUserdoc(parentUserdoc, acceptRegistrationRequest);
        if (Objects.nonNull(res) && Objects.nonNull(parentUserdoc)) {
            internationalMarkActionService.insertUserdocNormalAction(parentUserdoc, ConfigParamService.EXT_CONFIG_PARAM_ZMR_ACCEPTED_ACTION_TYPE);
        }

        return new RestApiResponse<>(new RAcceptedInternationalRegistrationResponse(receptionResponseMapper.toRest(res)));
    }

    @ApiOperation(value = "Accept Madrid Efiling International Registration Request Userdoc")
    @PostMapping(value = "/accept-madrid-efiling-userdoc")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
    public RestApiResponse<RAcceptedUserdocResponse> acceptMadridEfilingUserdoc(@RequestBody RestApiRequest<RAcceptMadridEfilingUserdocRequest> request) {
        CUserdoc userdoc = userdocMapper.toCore(request.getData().getUserdoc());
        CFileId affectedId = constructAffectedId(request.getData().getAffectedId(), request.getData().getAffectedRegistrationNbr(), request.getData().getAffectedRegistrationDup(), AcceptUserdocEntryType.ACCEPT_MADRID_USERDOC);
        CReceptionResponse cReceptionResponse = userdocMadridEfilingService.acceptMadridEfilingUserdoc(request.getData().getApplicationReference(), affectedId, userdoc);
        return new RestApiResponse<>(new RAcceptedUserdocResponse(receptionResponseMapper.toRest(cReceptionResponse)));
    }

    @ApiOperation(value = "Accept International Mark Userdoc")
    @PostMapping(value = "/accept-international-mark-userdoc", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
    public RestApiResponse<RAcceptedUserdocResponse> acceptInternationalMarkUserdoc(@RequestBody RestApiRequest<RAcceptInternationalUserdocRequest> request) {
        CFileId affectedId = null;

        String parentDocumentNumber = request.getData().getParentDocumentNumber();
        if (!StringUtils.hasText(parentDocumentNumber)) {
            affectedId = constructAffectedId(null, request.getData().getAffectedRegistrationNbr(), request.getData().getAffectedRegistrationDup(), AcceptUserdocEntryType.ACCEPT_INTERNATIONAL_USERDOC);
        }

        CReceptionResponse res = userdocInternationalMarkService.acceptInternationalMarkUserdoc(parentDocumentNumber, affectedId, userdocMapper.toCore(request.getData().getUserdoc()), request.getData().getTransactionCode(), request.getData().getTrantyp());
        RAcceptedUserdocResponse response = new RAcceptedUserdocResponse(receptionResponseMapper.toRest(res));
        return new RestApiResponse<>(response);
    }

    @ApiOperation(value = "Accept WIPO Irregulation Letter Userdoc")
    @PostMapping(value = "/accept-wipo-irreg-letter-userdoc", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
    public RestApiResponse<RAcceptedUserdocResponse> acceptWipoIrregularityLetters(@RequestBody RestApiRequest<RAcceptIrregLetterUserdocRequest> request) {
        CUserdoc userdoc = userdocMapper.toCore(request.getData().getUserdoc());
        List<CAttachment> attachmentList = attachmentMapper.toCoreList(request.getData().getAttachments());
        CReceptionResponse res = userdocIrregularityLetterService.acceptWipoIrregularityLetters(request.getData().getParentDocumentNumber(), request.getData().getAffectedRegistrationNbr(), request.getData().getAffectedRegistrationDup(), userdoc, attachmentList);
        return new RestApiResponse<>(new RAcceptedUserdocResponse(receptionResponseMapper.toRest(res)));
    }
}
