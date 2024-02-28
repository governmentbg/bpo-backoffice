package bg.duosoft.ipas.integration.tmclass.service.impl;

import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.util.LocalizationProperties;
import bg.duosoft.ipas.integration.tmclass.mapper.AlphaListNiceClassMapper;
import bg.duosoft.ipas.integration.tmclass.mapper.HeadingNiceClassMapper;
import bg.duosoft.ipas.integration.tmclass.mapper.TranslateNiceClassMapper;
import bg.duosoft.ipas.integration.tmclass.mapper.VerifyNiceClassMapper;
import bg.duosoft.ipas.integration.tmclass.service.TMClassService;
import bg.duosoft.ipas.integration.tmclass.verify.model.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Raya
 * 14.02.2019
 */
@Service
public class TMClassServiceImpl implements TMClassService {

    @Autowired
    @Qualifier("noAuthRestTemplate")
    private RestTemplate restTemplate;

    @Value("${tmclass.heading.rest.service.url}")
    private String headingUrl;

    @Value("${tmclass.verify.rest.service.url}")
    private String verifyUrl;

    @Value("${tmclass.alphaList.rest.service.url}")
    private String alphaListUrl;

    @Value("${tmclass.translate.rest.service.url}")
    private String translateUrl;

    private enum ServiceUrl {
        VERIFY, HEADING, ALPHA_LIST, TRANSLATE;
    }

    @Autowired
    private HeadingNiceClassMapper headingNiceClassMapper;

    @Autowired
    private VerifyNiceClassMapper verifyNiceClassMapper;

    @Autowired
    private AlphaListNiceClassMapper alphaListNiceClassMapper;

    @Autowired
    private TranslateNiceClassMapper translateNiceClassMapper;

    private String getUrl(ServiceUrl serviceUrl, Object... args){
        StringBuilder urlBuilder = new StringBuilder();
        switch (serviceUrl){
            case VERIFY:
                urlBuilder.append(verifyUrl);
                break;
            case HEADING:
                urlBuilder.append(headingUrl).append("?")
                    .append("sortBy=alphabetical")
                    .append("&lc=").append(args[0])
                    .append("&nc=").append(args[1]);
                break;
            case ALPHA_LIST:
                urlBuilder.append(alphaListUrl).append("?")
                    .append("sortMastersByLang=").append(args[0])
                    .append("&lc=").append(args[0])
                    .append("&nc=").append(args[1]);
                if(args.length>2 && args[2] != null){
                    urlBuilder.append("&version=").append(args[2]);
                }
                break;
            case TRANSLATE:
                urlBuilder.append(translateUrl);
                break;
             default: throw new UnsupportedOperationException("No such service call");

        }

        return urlBuilder.toString();
    }

    public CNiceClass verifyClassTerms(LocalizationProperties localizationProperties,
                                       String termList, String niceClass){
        String requestBody = buildXmlData(localizationProperties, termList, niceClass);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/xml; charset=utf-8");
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        TransactionType verifyTransactionType = restTemplate.postForObject(getUrl(ServiceUrl.VERIFY), request, TransactionType.class);
        CNiceClass cNiceClass = verifyNiceClassMapper.transactionTypeToCNiceClass(verifyTransactionType);
        return cNiceClass;
    }

    public CNiceClass getClassHeading(String language, String niceClass){
        bg.duosoft.ipas.integration.tmclass.heading.model.TransactionType headingTransaction =
            restTemplate.getForObject(getUrl(ServiceUrl.HEADING, language, niceClass), bg.duosoft.ipas.integration.tmclass.heading.model.TransactionType.class);
        CNiceClass cNiceClass = headingNiceClassMapper.transactionToCNiceClass(headingTransaction);
        return cNiceClass;
    }

    @Override
    public CNiceClass getClassAlphaList(String language, String niceClass, String version) {
        bg.duosoft.ipas.integration.tmclass.alpha.model.TransactionType alphaTransaction =
            restTemplate.getForObject(getUrl(ServiceUrl.ALPHA_LIST, language, niceClass, version),  bg.duosoft.ipas.integration.tmclass.alpha.model.TransactionType.class);
        CNiceClass cNiceClass = alphaListNiceClassMapper.transactionToCNiceClass(alphaTransaction);
        return cNiceClass;
    }

    @Override
    public CNiceClass translateClassTerms(LocalizationProperties localizationProperties, String targetLanguage, String termList, String niceClass) {
        String requestBody = buildXmlDataTranslate(localizationProperties, targetLanguage, termList, niceClass);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/xml; charset=utf-8");
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        bg.duosoft.ipas.integration.tmclass.translate.model.TransactionType translateTransactionType = restTemplate.postForObject(getUrl(ServiceUrl.TRANSLATE), request, bg.duosoft.ipas.integration.tmclass.translate.model.TransactionType.class);
        CNiceClass cNiceClass = translateNiceClassMapper.transactionTypeToCNiceClass(translateTransactionType);
        return cNiceClass;
    }

    private String buildXmlData(LocalizationProperties localizationProperties, String termList, String niceClass) {
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
            .append("<Transaction xmlns=\"http://oami.europa.eu/trademark/goods-services/term-verification\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ")
            .append( "xsi:schemaLocation=\"http://oami.europa.eu/trademark/term-verification GoodsServices-Verification-Request-V1-0.xsd\">")
            .append("<TransactionHeader><SenderDetails><RequestProducerDateTime>2001-12-31T12:00:00")
            .append("</RequestProducerDateTime></SenderDetails></TransactionHeader>")
            .append("<TransactionBody><TransactionContentDetails>")
            .append("<TransactionIdentifier>0000000</TransactionIdentifier>")
            .append("<TransactionCode>Goods and Services Verification Request</TransactionCode>")
            .append("<TransactionData><TermVerificationDetails>")
            .append("<LanguageCode>").append(localizationProperties.getLanguage())
            .append("</LanguageCode><OfficeCode>").append(localizationProperties.getOffice())
            .append("</OfficeCode><TermList>").append(termList.trim())
            .append("</TermList><ClassNumber>").append(niceClass)
            .append("</ClassNumber>")
            .append("</TermVerificationDetails></TransactionData>")
            .append("</TransactionContentDetails></TransactionBody></Transaction>");
        return builder.toString();
    }

    private String buildXmlDataTranslate(LocalizationProperties localizationProperties, String targetLanguage, String termList, String niceClass) {
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<Transaction xmlns=\"http://oami.europa.eu/trademark/goods-services/term-translation\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://oami.europa.eu/trademark/goods-services/term-translation GoodsServices-Translation-V0-3.xsd \">")
                .append("<TransactionBody>")
                .append("<TransactionContentDetails>")
                .append("<TransactionIdentifier>TransactionIdentifier</TransactionIdentifier>")
                .append("<TransactionCode>Goods and Services Translation Request</TransactionCode>")
                .append("<TransactionData>")
                .append("<TermTranslationRequest>")
                .append("<ClassificationTermText languageCode=\"").append(localizationProperties.getLanguage())
                .append("\">").append(termList.trim()).append("</ClassificationTermText>")
                .append("<TargetLanguageCode>").append(targetLanguage).append("</TargetLanguageCode>")
                .append("<ClassNumber>").append(niceClass).append("</ClassNumber>")
                .append("<SearchOnSublevel>true</SearchOnSublevel>")
                .append("</TermTranslationRequest>")
                .append("</TransactionData>")
                .append("</TransactionContentDetails>")
                .append("</TransactionBody>")
                .append("</Transaction>");


        return builder.toString();
    }

}
