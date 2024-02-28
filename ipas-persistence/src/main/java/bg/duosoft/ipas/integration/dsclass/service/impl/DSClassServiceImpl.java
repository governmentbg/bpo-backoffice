package bg.duosoft.ipas.integration.dsclass.service.impl;

import bg.duosoft.ipas.core.model.design.CProductTerm;
import bg.duosoft.ipas.core.model.util.DSClassSearchRequest;
import bg.duosoft.ipas.core.model.util.LocalizationProperties;
import bg.duosoft.ipas.integration.dsclass.mapper.SearchTermsResultProductTermsMapper;
import bg.duosoft.ipas.integration.dsclass.mapper.VerificationResponseProductTermMapper;
import bg.duosoft.ipas.integration.dsclass.model.SearchTermsResult;
import bg.duosoft.ipas.integration.dsclass.model.VerificationRequest;
import bg.duosoft.ipas.integration.dsclass.model.VerificationResponse;
import bg.duosoft.ipas.integration.dsclass.model.VerificationTerm;
import bg.duosoft.ipas.integration.dsclass.service.DSClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static bg.duosoft.ipas.integration.dsclass.mapper.ProductClassMapperUtil.ipasToDsClassProductCass;

/**
 * Created by Raya
 * 16.04.2020
 */
@Service
public class DSClassServiceImpl implements DSClassService {

    @Autowired
    @Qualifier("noAuthRestTemplate")
    private RestTemplate restTemplate;

    private enum ServiceUrl {
        VERIFY, SEARCH
    }

    @Value("${dsclass.verify.rest.service.url}")
    private String verifyDSClassTermUrl;

    @Value("${dsclass.search.rest.service.url}")
    private String searchInDSClassTermUrl;

    @Autowired
    private VerificationResponseProductTermMapper verificationResponseProductTermMapper;

    @Autowired
    private SearchTermsResultProductTermsMapper searchTermsResultProductTermsMapper;

    private String getUrl(ServiceUrl serviceUrl, Object... args){
        switch (serviceUrl){
            case VERIFY: return verifyDSClassTermUrl;
            case SEARCH:
                StringBuilder urlBuilder = new StringBuilder(searchInDSClassTermUrl);
                urlBuilder.append("?").append(buildSearchQueryString((DSClassSearchRequest)args[0]));
                return urlBuilder.toString();
            default: throw new UnsupportedOperationException("No such service call");
        }
    }

    public StringBuilder buildSearchQueryString(DSClassSearchRequest searchRequest){
        if(searchRequest.getLocalizationProperties() == null){
            throw new RuntimeException("Can not call dsclass search without localization properties");
        }

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("language=").append(searchRequest.getLocalizationProperties().getLanguage());
        queryBuilder.append("&officeList=").append(searchRequest.getLocalizationProperties().getOffice());
        queryBuilder.append("&numPage=").append(searchRequest.getNumPage());
        queryBuilder.append("&numTerms=").append(searchRequest.getNumTerms());
        if(searchRequest.getSearchText() != null && !searchRequest.getSearchText().isEmpty()) {
            queryBuilder.append("&term=").append(searchRequest.getSearchText());
        }
        if(searchRequest.getSearchClass() != null && !searchRequest.getSearchClass().isEmpty()){
            queryBuilder.append("&class=").append(searchRequest.getSearchClass());
        }
        if(searchRequest.getSearchSubclass() != null && !searchRequest.getSearchSubclass().isEmpty()){
            queryBuilder.append("&subclass=").append(searchRequest.getSearchSubclass());
        }

        return queryBuilder;
    }

    @Override
    public CProductTerm verifyDesignProductTerm(LocalizationProperties localizationProperties, String termText, List<String> productClasses) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        headers.add(HttpHeaders.ACCEPT, "application/json");
        headers.add(HttpHeaders.COOKIE, "cookieconsent_status=dismiss");
        HttpEntity<VerificationRequest> request = new HttpEntity<>(buildVerificationRequest(localizationProperties, termText, productClasses), headers);

        ResponseEntity<VerificationResponse[]> verificationResponseEntity =
            restTemplate.exchange(getUrl(ServiceUrl.VERIFY, null),
                HttpMethod.POST, request, VerificationResponse[].class);

        CProductTerm productTerm = verificationResponseProductTermMapper.
            verificationResponseToProductTerm(verificationResponseEntity.getBody(), termText, productClasses);
        return productTerm;
    }

    public VerificationRequest buildVerificationRequest(LocalizationProperties localizationProperties, String termText, List<String> productClasses){
        VerificationRequest request = new VerificationRequest();
        request.setOfficeCode(localizationProperties.getOffice());
        List<String> classification = productClasses.stream().map( e-> ipasToDsClassProductCass(e)).collect(Collectors.toList());
        request.setClassificationList(classification);
        request.setTermList(new ArrayList<>());
        VerificationTerm verificationTerm = new VerificationTerm();
        verificationTerm.setLanguageCode(localizationProperties.getLanguage());
        verificationTerm.setTermText(termText);
        request.getTermList().add(verificationTerm);
        return request;
    }

    @Override
    public List<CProductTerm> searchTerms(DSClassSearchRequest searchRequest) {
        ResponseEntity<SearchTermsResult> searchTermsResultResponse = restTemplate.getForEntity(
            getUrl(ServiceUrl.SEARCH, searchRequest), SearchTermsResult.class);

        List<CProductTerm> resultTerms = searchTermsResultProductTermsMapper.searchTermsResultToProductTerms(searchTermsResultResponse.getBody());
        return resultTerms;
    }
}
