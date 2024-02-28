package bg.duosoft.ipas.core.validation.reception;

import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Georgi
 * Date: 16.10.2020 г.
 * Time: 13:46
 */
@Component
public class ReceptionAttachmentValidator implements IpasValidator<CReception> {
    @Override
    public List<ValidationError> validate(CReception obj, Object... additionalArgs) {
        List<ValidationError> res = new ArrayList<>();
        if (!CollectionUtils.isEmpty(obj.getAttachments())) {
            if (!obj.isRegisterInDocflowSystem()) {
                res.add(ValidationError.builder().pointer("reception.attachments").messageCode("attachments.no.docflowsystem.should.not.exist").build());
            } else {
                for (CAttachment att : obj.getAttachments()) {
                    if (att.getData() == null || att.getData().length == 0) {
                        res.add(ValidationError.builder().pointer("reception.attachments").messageCode("attachments.empty.data").build());
                    }
                    if (StringUtils.isEmpty(att.getFileName())) {
                        res.add(ValidationError.builder().pointer("reception.attachments").messageCode("attachments.empty.filename").build());
                    }
                }
            }

        }
        return res;
    }
}
