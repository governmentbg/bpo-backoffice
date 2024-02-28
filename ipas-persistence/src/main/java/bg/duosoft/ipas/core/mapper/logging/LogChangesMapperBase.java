package bg.duosoft.ipas.core.mapper.logging;

import bg.duosoft.ipas.core.model.logging.CFileLogChanges;
import bg.duosoft.ipas.core.model.logging.CLogChangeDetail;
import bg.duosoft.ipas.core.model.logging.CLogChangesBase;
import bg.duosoft.ipas.core.service.person.SimpleUserService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.persistence.model.entity.IpFileLogChanges;
import bg.duosoft.ipas.persistence.model.entity.IpLogChanges;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: ggeorgiev
 * Date: 06.12.2021
 * Time: 11:56
 */
@Slf4j
public abstract class LogChangesMapperBase<E extends IpLogChanges, C extends CLogChangesBase> {
    @Autowired
    private SimpleUserService simpleUserService;
    private static Pattern DESIGN_PATTERN = Pattern.compile("([A-Z]{2}/[A-ZА-Я]/\\d{4}/\\d+\\s*)(<DIFF.*?</DIFF>)\\s+([A-Z]{2}/[A-ZА-Я]/\\d{4}/\\d+|$)", Pattern.DOTALL | Pattern.MULTILINE);


    protected Map<String, CLogChangeDetail> generateLogChangeDetails(String content, String baseKey) {
        if (content == null) {
            return null;
        }
        try {

            Map<String, CLogChangeDetail> res = new HashMap<>();
            boolean isDesign = false;
            Matcher matcher = DESIGN_PATTERN.matcher(content);
            int start = 0;
            while (matcher.find(start)) {
                isDesign = true;
                String designKey = matcher.group(1);
                String designContent = matcher.group(2);
                res.put(designKey, generateCLogChangeDetail(designKey, designContent));
                start = matcher.start(3);
            }
            if (!isDesign) {
                res.put(baseKey, generateCLogChangeDetail(baseKey, content));
            }

            return res;
        } catch (IOException | SAXException | ParserConfigurationException e) {
            log.error("Cannot generate logChangeDetails for content" + content, e);
            return null;
        }
    }


    @AfterMapping
    public void afterMappingBase(E ipFileLogChanges, @MappingTarget C target, @Context boolean addData) {
        target.setChangeUser(simpleUserService.findSimpleUserById(ipFileLogChanges.getChangeUserId()));
    }
    private CLogChangeDetail generateCLogChangeDetail(String key, String content) throws IOException, SAXException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        LogParseHandler handler = new LogParseHandler();
        ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes());
        saxParser.parse(is, handler);
        return new CLogChangeDetail(key, handler.getBefore(), handler.getAfter());
    }

}
