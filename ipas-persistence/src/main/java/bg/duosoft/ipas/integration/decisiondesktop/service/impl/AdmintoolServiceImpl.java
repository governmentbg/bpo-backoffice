package bg.duosoft.ipas.integration.decisiondesktop.service.impl;

import bg.duosoft.ipas.core.model.decisiondesktop.CDdCategorizationTags;
import bg.duosoft.ipas.core.model.decisiondesktop.CDecisionTemplate;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.service.dd.DdCategorizationTagsService;
import bg.duosoft.ipas.integration.decisiondesktop.mapper.DecisionTemplateMapper;
import bg.duosoft.ipas.integration.decisiondesktop.model.admintool.StatusEnum;
import bg.duosoft.ipas.integration.decisiondesktop.model.admintool.TreeDTO;
import bg.duosoft.ipas.integration.decisiondesktop.model.admintool.TreeFilter;
import bg.duosoft.ipas.integration.decisiondesktop.service.AdmintoolService;
import bg.duosoft.ipas.properties.PropertyAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 22.04.2021
 * Time: 15:15
 */
@Service
public class AdmintoolServiceImpl implements AdmintoolService {

    @Autowired
    private DecisionTemplateMapper decisionTemplateMapper;

    @Autowired
    private DdCategorizationTagsService ddCategorizationTagsService;

    @Autowired
    private RestTemplate ddProxyRestTemplate;

    private UriBuilderFactory uriBuilderFactory;

    private String filterTemplatesString;

    @Autowired
    public AdmintoolServiceImpl(PropertyAccess propertyAccess) {
       this.uriBuilderFactory = new DefaultUriBuilderFactory();
       this.filterTemplatesString = propertyAccess.getDecisionDesktopProxyBaseUrl()+
               "/public/trees/filter?username={username}";
    }

    private URI getUri(String uriString, Object... args){
        return uriBuilderFactory.expand(uriString, args);
    }

    @Override
    public List<CDecisionTemplate> getTemplates(String username, COffidoc offidoc){
        String userdocType = null;
        if(offidoc.getOffidocParentData() != null && offidoc.getOffidocParentData().getUserdocId() == null && offidoc.getOffidocParentData().getFileId() == null &&
                offidoc.getOffidocParentData().getParent() != null && offidoc.getOffidocParentData().getParent().getUserdocId() != null){
            userdocType = offidoc.getOffidocParentData().getParent().getUserdocType();
        } else if(offidoc.getOffidocParentData() != null && offidoc.getOffidocParentData().getUserdocId() != null){
            userdocType = offidoc.getOffidocParentData().getUserdocType();
        }

        CDdCategorizationTags ddConf = ddCategorizationTagsService.getCategorizationTags(offidoc.getOffidocParentData().getTopProcessFileData().getFileId().getFileType(), userdocType);
        if(ddConf == null || ddConf.getDossierType() == null || ddConf.getCategories() == null){
            return new ArrayList<>();
        }
        if(ddConf.isFetchFromParent() && offidoc.getOffidocParentData() != null &&
                offidoc.getOffidocParentData().getParent() != null){
            String parentUserdocType = null;
            if(offidoc.getOffidocParentData().getParent().getUserdocId() != null) {
                offidoc.getOffidocParentData().getParent().getUserdocType();
            }
            ddConf = ddCategorizationTagsService.getCategorizationTags(offidoc.getOffidocParentData().getTopProcessFileData().getFileId().getFileType(), parentUserdocType);
        }

        TreeFilter treeFilter  = new TreeFilter();
        treeFilter.setDossierTypes(Set.of(ddConf.getDossierType()));
        treeFilter.setLetterCategories(ddConf.getCategoriesSet());
        treeFilter.setStatuses(Set.of(StatusEnum.PUBLISHED));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MimeTypeUtils.APPLICATION_JSON_VALUE);
        HttpEntity<TreeFilter> entity = new HttpEntity<>(treeFilter, headers);
        TreeDTO[] trees =
                ddProxyRestTemplate.postForObject(getUri(filterTemplatesString, username),
                        entity,TreeDTO[].class);
        if(trees == null){
            return new ArrayList<>();
        }
        return Arrays.stream(trees).map(tree -> decisionTemplateMapper.toCDecisionTemplate(tree)).collect(Collectors.toList());
    }

}
