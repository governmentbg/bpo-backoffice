package bg.duosoft.ipas.integration.tmview.service;

import bg.duosoft.ipas.integration.tmview.model.TMViewAutocompleteResult;

import java.util.List;

public interface TMViewService {
    List<TMViewAutocompleteResult> getEuTrademarksAutocomplete(String regNbrTerm);
}
