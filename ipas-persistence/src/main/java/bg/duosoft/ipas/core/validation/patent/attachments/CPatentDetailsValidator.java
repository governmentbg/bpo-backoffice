package bg.duosoft.ipas.core.validation.patent.attachments;

import bg.duosoft.ipas.core.model.patent.CPatentAttachment;
import bg.duosoft.ipas.core.model.patent.CPatentDetails;
import bg.duosoft.ipas.core.validation.common.PdfFormatValidator;
import bg.duosoft.ipas.core.validation.common.XmlValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class CPatentDetailsValidator implements IpasValidator<CPatentDetails> {

    @Autowired
    private PdfFormatValidator pdfFormatValidator;

    @Autowired
    private XmlValidator xmlValidator;

    @Override
    public List<ValidationError> validate(CPatentDetails obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        if (Objects.nonNull(obj) && !CollectionUtils.isEmpty(obj.getPatentAttachments())) {
            obj.getPatentAttachments().stream().forEach(r -> {

                if (r.getAttachmentType().getAttachmentExtension().equals("pdf")) {
                    if (!pdfFormatValidator.isValidPdf(r.getAttachmentContent()))
                        errors.addAll(getPdfErrorList(r));
                }
                if (r.getAttachmentType().getAttachmentExtension().equals("xml")) {
                    if (!xmlValidator.isValidXml(r.getAttachmentContent()))
                        errors.addAll(getXmlErrorList(r));
                }

            });
        }

        return errors;
    }


    private List<ValidationError> getPdfErrorList(CPatentAttachment obj) {
        List<ValidationError> errors = new ArrayList<>();
        errors.add(ValidationError.builder().pointer("attachmentData").messageCode("invalid.pdf.attachment.data").invalidValue(obj).build());
        return errors;
    }


    private List<ValidationError> getXmlErrorList(CPatentAttachment obj) {
        List<ValidationError> errors = new ArrayList<>();
        errors.add(ValidationError.builder().pointer("attachmentData").messageCode("invalid.xml.attachment.data").invalidValue(obj).build());
        return errors;
    }

}
