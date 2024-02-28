package bg.duosoft.ipas.integration.tmview.service.impl;

import bg.duosoft.ipas.integration.tmview.model.TMViewAutocompleteResult;
import bg.duosoft.ipas.integration.tmview.service.TMViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TMViewServiceImpl implements TMViewService {

    @Autowired
    @Qualifier("noAuthRestTemplate")
    private RestTemplate restTemplate;

    @Value("${tmview.search.autocomplete.service.url}")
    private String autocompleteServiceUrl;

    private static final int AUTOCOMPLETE_ROWS_COUNT = 100;
    private static final String EU_OFFICE_CODE = "EM";

    @Override
    public List<TMViewAutocompleteResult> getEuTrademarksAutocomplete(String regNbrTerm) {
        TMViewAutocompleteResult[] autocompleteResults = restTemplate.getForObject(getUrl(regNbrTerm, AUTOCOMPLETE_ROWS_COUNT, EU_OFFICE_CODE), TMViewAutocompleteResult[].class);

        if (Objects.isNull(autocompleteResults) || autocompleteResults.length < 1) {
            return new ArrayList<>();
        }

        return List.of(autocompleteResults);
    }

    private String getUrl(String word, int rows, String officeCode) {
        StringBuilder urlBuilder = new StringBuilder(autocompleteServiceUrl);
        urlBuilder.append("?word=").append("*").append(word);
        urlBuilder.append("&rows=").append(rows);
        urlBuilder.append("&oc=").append(officeCode);
        return urlBuilder.toString();
    }
}
