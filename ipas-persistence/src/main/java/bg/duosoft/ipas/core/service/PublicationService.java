package bg.duosoft.ipas.core.service;

import bg.duosoft.ipas.core.model.CPublicationInfo;

import java.util.List;

public interface PublicationService {

    List<CPublicationInfo> selectPublications(String filingNumber);

}