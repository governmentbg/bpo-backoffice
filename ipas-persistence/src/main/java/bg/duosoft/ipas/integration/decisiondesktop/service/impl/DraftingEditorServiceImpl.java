package bg.duosoft.ipas.integration.decisiondesktop.service.impl;

import bg.duosoft.ipas.core.model.decisiondesktop.CDdCategorizationTags;
import bg.duosoft.ipas.core.model.decisiondesktop.CDecisionContext;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.service.dd.DdCategorizationTagsService;
import bg.duosoft.ipas.integration.decisiondesktop.mapper.DecisionContextMapper;
import bg.duosoft.ipas.integration.decisiondesktop.model.backoffice.*;
import bg.duosoft.ipas.integration.decisiondesktop.model.common.ImageDTO;
import bg.duosoft.ipas.integration.decisiondesktop.model.drafteditor.*;
import bg.duosoft.ipas.integration.decisiondesktop.service.DraftingEditorService;
import bg.duosoft.ipas.integration.decisiondesktop.vars.CommonVariablesUtils;
import bg.duosoft.ipas.integration.decisiondesktop.vars.VariablesCreator;
import bg.duosoft.ipas.properties.PropertyAccess;
import bg.duosoft.ipas.util.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 22.04.2021
 * Time: 15:16
 */
@Slf4j
@Service
public class DraftingEditorServiceImpl implements DraftingEditorService {

    @Autowired
    private DecisionContextMapper decisionContextMapper;

    @Autowired
    private RestTemplate ddProxyRestTemplate;

    @Autowired
    private DdCategorizationTagsService ddCategorizationTagsService;

    @Autowired
    private VariablesCreator variablesCreator;

    private UriBuilderFactory uriBuilderFactory;

    private String createContextUriString;
    private String deleteContextUriString;
    private String downloadLetterFileUriString;
    private String contextsFilterUriString;
    private String draftingEditorAppContextUrl;
    private String draftingEditorImagesUrl;
    private String cleanDraftingEditorUriString;


    @Autowired
    public DraftingEditorServiceImpl(PropertyAccess propertyAccess) {
        this.uriBuilderFactory = new DefaultUriBuilderFactory();
        this.createContextUriString = propertyAccess.getDecisionDesktopProxyBaseUrl()+
                "/drafting/context/{treeId}/{letterId}?username={username}";
        this.deleteContextUriString = propertyAccess.getDecisionDesktopProxyBaseUrl()+
                "/drafting/context/{contextId}?username={username}";
        this.cleanDraftingEditorUriString = propertyAccess.getDecisionDesktopProxyBaseUrl()+
                "/drafting/editor/{context}?username={username}";
        this.downloadLetterFileUriString = propertyAccess.getDecisionDesktopProxyBaseUrl()+
                "/letters/{fileType}/{contextId}?username={username}";
        this.contextsFilterUriString = propertyAccess.getDecisionDesktopProxyBaseUrl()+
                "/drafting/context/filter?letterId={letterId}&username={username}";
        this.draftingEditorAppContextUrl = propertyAccess.getDraftingEditorAppUrl()+"/{contextId}";
        this.draftingEditorImagesUrl = propertyAccess.getDecisionDesktopProxyBaseUrl()+"/images?username={username}";
    }

    @Override
    public String createContext(String templateId, String templateName, COffidoc offidoc, String username){

        Map<String, Object> templateVarsMap = createTemplateVarsMap(templateName, offidoc);
        String fileIdStr = offidoc.getOffidocParentData().getTopProcessFileData().getFileId().createFilingNumber();

        String userdocType = null;
        if(offidoc.getOffidocParentData() != null && offidoc.getOffidocParentData().getUserdocId() != null){
            userdocType = offidoc.getOffidocParentData().getUserdocType();
        }
        CDdCategorizationTags ddConf = ddCategorizationTagsService.getCategorizationTags(offidoc.getOffidocParentData().getTopProcessFileData().getFileId().getFileType(), userdocType);

        BackOfficeDraftContext backofficeDraftContext = new BackOfficeDraftContext(fileIdStr, templateVarsMap);
        DraftingOptionsDTO draftingOptions = createDraftingOptions(fileIdStr, templateId, backofficeDraftContext, ddConf.getTagsSet());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE);
        HttpEntity<DraftingOptionsDTO> entity = new HttpEntity<>(draftingOptions, headers);
        DraftingContextDTO draftingContext = ddProxyRestTemplate.postForObject(uriBuilderFactory.expand(createContextUriString, templateId, offidocIdToLetterId(offidoc.getOffidocId()), username),
                entity, DraftingContextDTO.class);

        return uriBuilderFactory.expand(draftingEditorAppContextUrl, draftingContext.getContext()).toString();
    }

    @Override
    public void deleteContext(String contextId, String username){
        ddProxyRestTemplate.delete(uriBuilderFactory.expand(deleteContextUriString, contextId, username));
    }

    @Override
    public void cleanDraftingEditor(String context, String username) {
        ddProxyRestTemplate.delete(uriBuilderFactory.expand(cleanDraftingEditorUriString, context, username));
    }

    @Override
    public byte[] downloadLetterFile(String fileType, String contextId, String username){
        ResponseEntity<byte[]> responseEntity = ddProxyRestTemplate.exchange(uriBuilderFactory.expand(downloadLetterFileUriString, fileType, contextId, username),
                HttpMethod.GET, null, byte[].class);
        if(responseEntity.getStatusCode().equals(HttpStatus.OK)){
            return responseEntity.getBody();
        } else {
            log.error("Error downloading file from DD: "+responseEntity.getStatusCode().toString()+responseEntity.getBody());
            throw new RuntimeException("Could not get file");
        }
    }

    @Override
    public List<CDecisionContext> getFilteredContexts(COffidocId offidocId, String username){
        DraftingContextDTO[] filteredContexts = ddProxyRestTemplate.getForObject(uriBuilderFactory.expand(contextsFilterUriString, offidocIdToLetterId(offidocId), username), DraftingContextDTO[].class);
        if(filteredContexts != null){
            return Arrays.stream(filteredContexts).filter(context -> context.getSoftDeleted() == false).
                    map(context -> decisionContextMapper.toCDecisionContext(context)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String uploadImage(byte[] imageBytes, String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(imageBytes){
            @Override
            public String getFilename() {
                return "image.jpg";
            }
        });
        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);
        ResponseEntity<ImageDTO> response = ddProxyRestTemplate
                .postForEntity(uriBuilderFactory.expand(draftingEditorImagesUrl, username), requestEntity, ImageDTO.class);
        if(response != null && response.hasBody() && response.getBody().getLink() != null){
            return response.getBody().getLink();
        }
        return null;
    }

    private Map<String, Object> createTemplateVarsMap(String templateName, COffidoc offidoc) {
        Map<String, Object> context = new HashMap<>();
        Map<String, Object> createdVars = variablesCreator.createVariablesForOffidoc(offidoc);
        context.put("templateName", templateName);
        createdVars.keySet().stream().forEach(key -> {
            Object val = createdVars.get(key);
            if(val instanceof Date) {
                String newVal = CommonVariablesUtils.SIMPLE_DATE_FORMAT.format(val);
                context.put(key, newVal);
            } else if(val instanceof byte[]){
                String savedFileUrl = this.uploadImage((byte[]) val, SecurityUtils.getLoggedUsername());
                context.put(key, savedFileUrl);
            } else if(val instanceof ByteArrayListVariable) {
                ByteArrayListVariable castedVal = (ByteArrayListVariable) val;
                if(castedVal.getByteArrayList() != null && !castedVal.getByteArrayList().isEmpty()){
                    List<String> urls = new ArrayList<>();
                    castedVal.getByteArrayList().stream().forEach(byteArray -> {
                        String fileUrl = this.uploadImage(byteArray, SecurityUtils.getLoggedUsername());
                        urls.add(fileUrl);
                    });
                    context.put(key, urls.toArray());
                }
            } else if(val instanceof EarlierMarkVariable[]) {
                Arrays.stream(((EarlierMarkVariable[])val)).forEach(e -> {
                    if(e.getSignImageBytes() != null) {
                        String fileUrl = this.uploadImage(e.getSignImageBytes(), SecurityUtils.getLoggedUsername());
                        e.setSignImage(fileUrl);
                        e.setSignImageBytes(null);
                    }
                });
                context.put(key, val);
            } else if(val instanceof DesignImageWrapperVariable[]) {
                Arrays.stream(((DesignImageWrapperVariable[])val)).forEach(wrapper -> {
                    if(wrapper.getDesignImageVariables().length>0) {
                        Arrays.stream(wrapper.getDesignImageVariables()).forEach(d -> {
                            if (d.getImageBytes() != null) {
                                String fileUrl = this.uploadImage(d.getImageBytes(), SecurityUtils.getLoggedUsername());
                                d.setImage(fileUrl);
                                d.setImageBytes(null);
                            }
                        });
                    }
                });
                context.put(key, val);
            } else {
                context.put(key, val);
            }
        });

        return context;
    }

    private String offidocIdToLetterId(COffidocId offidocId){
        return new StringBuilder(offidocId.getOffidocOrigin())
                .append(offidocId.getOffidocSeries()).append(offidocId.getOffidocNbr()).toString();
    }

    private DraftingOptionsDTO createDraftingOptions(String dossierId, String treeId, BackOfficeDraftContext backofficeDraftContext, Set<String> tags) {

        return DraftingOptionsDTO.builder()
                .dossierId(dossierId)
                .dossierType(DossierTypeEnum.EUTM)//TODO - what is the idea of this
                .tagCodes(tags)
                .language(LanguageEnum.BG)
                .header(createHeaderDTO(backofficeDraftContext))
                .variables(buildVariablesMap(createVariables(backofficeDraftContext))) //TODO: template_variables - used for velocity expressions, the rest - used for tags and other things like that
                .docPublisher(buildDocPublisher()).build();
    }

    private DocPublisherDTO buildDocPublisher() {
        return DocPublisherDTO.builder()
                .language(LanguageEnum.BG)
                .pageDefaultHeader("")
                .ofDefaultHeader("/")
                .footer(true)
                .build();
    }

    private HeaderDTO createHeaderDTO(BackOfficeDraftContext backofficeDraftContext) {
        return HeaderDTO.builder()
                .title("BPO Drafted Document")
                .build();
    }

    private Variable[] createVariables(BackOfficeDraftContext backofficeDraftContext) {
        Variable<Map> templateVariables = new Variable<>("template_variables", backofficeDraftContext.getTemplateVariables(), VariableTypeEnum.complex, null);

        return new Variable[] { templateVariables };
    }

    private Map<String, Object> buildVariablesMap(Variable ...variables) {
        return Arrays.stream(variables).collect(Collectors.toMap(variable -> variable.getKey(), Function.identity()));
    }
}
