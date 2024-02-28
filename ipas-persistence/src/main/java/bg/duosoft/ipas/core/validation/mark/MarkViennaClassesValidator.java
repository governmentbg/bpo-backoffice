package bg.duosoft.ipas.core.validation.mark;

import bg.duosoft.ipas.core.model.mark.CLogo;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CSignData;
import bg.duosoft.ipas.core.model.mark.CViennaClass;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class MarkViennaClassesValidator implements IpasValidator<CMark> {

    @Autowired
    private MarkService markService;

    @Override
    public List<ValidationError> validate(CMark mark, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
//        CSignData signData = mark.getSignData();
//        if (Objects.nonNull(signData)) {
//            CLogo logo = signData.getLogo();
//            if (Objects.nonNull(logo)) {
//                byte[] logoData = new byte[0];
//                if (logo.isLoaded()) {
//                    logoData = logo.getLogoData();
//                } else {
//                    CLogo cLogo = markService.selectMarkLogo(mark.getFile().getFileId());
//                    if (Objects.nonNull(cLogo))
//                        logoData = cLogo.getLogoData();
//                }
//                List<CViennaClass> viennaClassList = logo.getViennaClassList();
//                if (!CollectionUtils.isEmpty(viennaClassList) && (null == logoData || 0 == logoData.length)) {
//                    errors.add(ValidationError.builder().pointer("viennaEmptyLogo").messageCode("vienna.logo.empty").build());
//                }
//            }
//        }
        return errors;
    }
}
