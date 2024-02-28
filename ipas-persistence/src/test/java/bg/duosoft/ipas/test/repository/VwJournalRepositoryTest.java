package bg.duosoft.ipas.test.repository;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.service.publication.PublicationSearchService;
import bg.duosoft.ipas.persistence.model.nonentity.PublicationSection;
import bg.duosoft.ipas.persistence.repository.entity.vw.VwJournalRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Transactional
public class VwJournalRepositoryTest extends TestBase {

    @Autowired
    private VwJournalRepository vwJournalRepository;

    @Autowired
    private PublicationSearchService publicationSearchService;
    List<String> procTypes = new ArrayList<>();
    List<String> fileTypes = new ArrayList<>();

    @Before
    public void setup(){
        procTypes.add("10");
        fileTypes.add("S");
    }

    @Test
    public void testPublicationYear() {

        List<String> result =
                vwJournalRepository.getYearsByProcessTypes(procTypes);

        assertEquals(5, result.size());
        assertEquals("2018", result.get(0));
    }

    @Test
    public void testPublicationBulletin() {
        List<String> result =
                vwJournalRepository.getBulletinsByProcessTypes(procTypes, "2018");

        assertEquals(23, result.size());
    }

    @Test
    public void testPublicationSectionByBulletin() {
        List<PublicationSection> result =
                vwJournalRepository.getPublicationSectionsByProcessTypes(procTypes, "01");

        assertEquals(7, result.size());
    }

    @Test
    public void testPublicationSectionByYearAndBulletin() {
        List<PublicationSection> result =
                vwJournalRepository.getPublicationSectionsByProcessTypes(procTypes, "2017", "01");

        assertEquals(2, result.size());
    }

    @Test
    public void testServicePublicationYear() {
        List<String> result =
                publicationSearchService.getYearsByProcessTypes(procTypes);

        assertEquals(5, result.size());
        assertEquals("2018", result.get(0));
    }

    @Test
    public void testServicePublicationBulletin() {
        List<String> result =
                publicationSearchService.getBulletinsByProcessTypes(procTypes, "2018");

        assertEquals(23, result.size());
    }

    @Test
    public void testServicePublicationSectionByBulletin() {
        List<PublicationSection> result =
                publicationSearchService.getPublicationSectionsByProcessTypes(procTypes, "01");

        assertEquals(7, result.size());
    }

    @Test
    public void testServicePublicationSectionByYearAndBulletin() {
        List<PublicationSection> result =
                publicationSearchService.getPublicationSectionsByProcessTypes(procTypes, "2017", "01");

        assertEquals(2, result.size());
    }

    @Test
    public void testPublicationYearByFileTypes() {

        List<String> result =
                vwJournalRepository.getYearsByFileTypes(fileTypes);

        assertEquals(5, result.size());
        assertEquals("2018", result.get(0));
    }

    @Test
    public void testPublicationBulletinByFileTypes() {

        List<String> result =
                vwJournalRepository.getBulletinsByFileTypes(fileTypes, "2018");

        assertEquals(13, result.size());
    }

    @Test
    public void testServicePublicationSectionByFileTypesAndBulletin() {
        List<PublicationSection> result =
                publicationSearchService.getPublicationSectionsByFileTypes(fileTypes, "09");

        assertEquals(2, result.size());
        assertEquals(result.get(0).getSectDef(), "Издадени есертификати за допълнителна закрила");
    }

    @Test
    public void testServicePublicationSectionByFileTypesAndYearAndBulletin() {
        List<PublicationSection> result =
                publicationSearchService.getPublicationSectionsByFileTypes(fileTypes, "2014", "09");

        assertEquals(2, result.size());
        assertEquals(result.get(0).getSectDef(), "Издадени есертификати за допълнителна закрила");
    }
}
