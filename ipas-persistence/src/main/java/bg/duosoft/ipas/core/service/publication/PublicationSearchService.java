package bg.duosoft.ipas.core.service.publication;

import bg.duosoft.ipas.persistence.model.nonentity.PublicationSection;

import java.util.List;

public interface PublicationSearchService {

    List<String> getYearsByProcessTypes(List<String> procTypes);

    List<String> getBulletinsByProcessTypes(List<String> procTypes, String year);

    List<PublicationSection> getPublicationSectionsByProcessTypes(List<String> procTypes, String bulletin);

    List<PublicationSection> getPublicationSectionsByProcessTypes(List<String> procTypes, String year, String bulletin);

    List<String> getYearsByFileTypes(List<String> fileTypes);

    List<String> getBulletinsByFileTypes(List<String> fileTypes, String year);

    List<PublicationSection> getPublicationSectionsByFileTypes(List<String> fileTypes, String bulletin);

    List<PublicationSection> getPublicationSectionsByFileTypes(List<String> fileTypes, String year, String bulletin);
}