package bg.duosoft.ipas.core.service.impl.publication;

import bg.duosoft.ipas.core.service.publication.PublicationSearchService;
import bg.duosoft.ipas.persistence.model.nonentity.PublicationSection;
import bg.duosoft.ipas.persistence.repository.entity.vw.VwJournalRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@LogExecutionTime
public class PublicationSearchServiceImpl implements PublicationSearchService {

    @Autowired
    private VwJournalRepository vwJournalRepository;

    public List<String> getYearsByProcessTypes(List<String> procTypes) {
        return vwJournalRepository.getYearsByProcessTypes(procTypes);
    }

    public List<String> getBulletinsByProcessTypes(List<String> procTypes, String year) {
        return vwJournalRepository.getBulletinsByProcessTypes(procTypes, year);
    }

    public List<PublicationSection> getPublicationSectionsByProcessTypes(List<String> procTypes, String bulletin) {
        return vwJournalRepository.getPublicationSectionsByProcessTypes(procTypes, bulletin);
    }

    public List<PublicationSection> getPublicationSectionsByProcessTypes(List<String> procTypes, String year, String bulletin) {
        return vwJournalRepository.getPublicationSectionsByProcessTypes(procTypes, year, bulletin);
    }

    public List<String> getYearsByFileTypes(List<String> fileTypes) {
        return vwJournalRepository.getYearsByFileTypes(fileTypes);
    }

    public List<String> getBulletinsByFileTypes(List<String> fileTypes, String year) {
        if (year == null || year.isEmpty()) {
            return vwJournalRepository.getBulletinsByFileTypes(fileTypes);
        }
        return vwJournalRepository.getBulletinsByFileTypes(fileTypes, year);
    }

    public List<PublicationSection> getPublicationSectionsByFileTypes(List<String> fileTypes, String bulletin) {
        List<Tuple> publicationSectionsByFileTypes = new ArrayList<>();
        if (bulletin == null || bulletin.isEmpty()) {
            publicationSectionsByFileTypes = vwJournalRepository.getPublicationSectionsByFileTypes(fileTypes);
        } else {
            publicationSectionsByFileTypes = vwJournalRepository.getPublicationSectionsByFileTypesAndBuletin(fileTypes, bulletin);
        }

        return publicationSectionsByFileTypes
                .stream()
                .map(r->new PublicationSection(((BigDecimal)r.get("sect")).toString(), (String)r.get("sectDef")))
                .collect(Collectors.toList());
    }

    public List<PublicationSection> getPublicationSectionsByFileTypes(List<String> fileTypes, String year, String bulletin) {
        List<Tuple> publicationSectionsByFileTypes = new ArrayList<>();
        if (year == null || year.isEmpty()) {
            return getPublicationSectionsByFileTypes(fileTypes, bulletin);
        }

        if (bulletin == null || bulletin.isEmpty()) {
            publicationSectionsByFileTypes = vwJournalRepository
                    .getPublicationSectionsByFileTypesAndYear(fileTypes, year);
        } else {
            publicationSectionsByFileTypes = vwJournalRepository
                    .getPublicationSectionsByFileTypes(fileTypes, year, bulletin);
        }

        return publicationSectionsByFileTypes
                .stream()
                .map(r->new PublicationSection(((BigDecimal)r.get("sect")).toString(), (String)r.get("sectDef")))
                .collect(Collectors.toList());
    }
}