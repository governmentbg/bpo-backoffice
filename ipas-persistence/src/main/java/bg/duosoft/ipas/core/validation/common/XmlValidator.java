package bg.duosoft.ipas.core.validation.common;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
public class XmlValidator {
    public boolean isValidXml(byte[] content) {
        try {
            TikaConfig tc = new TikaConfig();
            String mediaType = tc.getDetector().detect(new ByteArrayInputStream(content), new Metadata()).toString();
            return mediaType.equalsIgnoreCase(MediaType.APPLICATION_XML_VALUE) || mediaType.equalsIgnoreCase(MediaType.TEXT_XML_VALUE);
        } catch (TikaException e) {
            throw new RuntimeException("TikaConfig initialization failed!", e);
        } catch (IOException e) {
            throw new RuntimeException("TikaConfig detect method with IOException!", e);
        }
    }
}
