package bg.duosoft.ipas.test.repository;

import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.repository.entity.custom.SequenceRepository;
import bg.duosoft.ipas.persistence.repository.entity.doc.IpDocRepository;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.util.DefaultValue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: Georgi
 * Date: 26.5.2020 г.
 * Time: 17:45
 */
public class SequenceRepositoryTests extends TestBase {
    @Autowired
    private SequenceRepository sequenceRepository;
    @Autowired
    private IpDocRepository ipDocRepository;
    @Test
    public void getNextSequenceNbr() {
        for (SequenceRepository.SEQUENCE_NAME sn : SequenceRepository.SEQUENCE_NAME.values()) {
            assertEquals(sequenceRepository.getNextSequenceValue(sn) + 1, (Object)sequenceRepository.getNextSequenceValue(sn));
        }
    }
    @Test
    public void getNextDocNumber() {
        Integer nextDocNumber = sequenceRepository.getNextDocNumber(DefaultValue.DEFAULT_DOC_ORI);
        assertNotNull(nextDocNumber);
    }
    @Test
    public void getDocSeries() {
        Integer docSeries = sequenceRepository.getDocSeries(DefaultValue.DEFAULT_DOC_ORI);
        assertEquals((Object)2019, docSeries);
    }
    @Test
    public void getDocNextSequence() {
        Integer nextDocSequence = sequenceRepository.getNextDocSequenceNumber("ОК", 2018);
        assertEquals((Object)70106525, nextDocSequence);
    }
}
