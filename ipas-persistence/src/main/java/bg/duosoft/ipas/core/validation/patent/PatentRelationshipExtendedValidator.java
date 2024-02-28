package bg.duosoft.ipas.core.validation.patent;

import bg.duosoft.ipas.core.model.ebddownload.CEbdPatent;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CRelationshipExtended;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.PatentRelationshipExtApplType;
import bg.duosoft.ipas.integration.ebddownload.service.EbdPatentService;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationshipExtended;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentRelationshipExtendedRepository;
import bg.duosoft.ipas.util.DataConverter;
import bg.duosoft.ipas.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static bg.duosoft.ipas.enums.PatentRelationshipExtApplType.EUROPEAN_PATENT;
import static bg.duosoft.ipas.enums.PatentRelationshipExtApplType.selectByCode;

/**
 * User: Georgi
 * Date: 17.2.2020 г.
 * Time: 14:53
 */
@Component
public class PatentRelationshipExtendedValidator implements IpasValidator<CPatent> {
    @Autowired
    private IpPatentRelationshipExtendedRepository ipPatentRelationshipExtendedRepository;
    @Autowired
    private EbdPatentService ebdPatentService;
    @Override
    public List<ValidationError> validate(CPatent p, Object... additionalArgs) {
        CRelationshipExtended t = p.getRelationshipExtended();
        List<ValidationError> errors = new ArrayList<>();
        if (t != null && t.getApplicationType() != null) {
            PatentRelationshipExtApplType trans = selectByCode(t.getApplicationType());
            switch (trans) {
                case INTERNATIONAL_PATENT:
                    errors.addAll(validateInternationalPatent(p));
                    break;
                case EUROPEAN_PATENT:
                    errors.addAll(validateEuropeanPatent(p));
                    break;
            }
            if (t.getFilingDate() != null && p.getFile().getFilingData().getFilingDate() != null && DateUtils.dateToLocalDate(p.getFile().getFilingData().getFilingDate()).isBefore(DateUtils.dateToLocalDate(t.getFilingDate()))) {
                errors.add(ValidationError.builder().pointer("relationshipExtended.filingDate").messageCode("wrong.rel.ext.filing.date").build());
            }
        }

        return errors;
    }
    private List<ValidationError> validateInternationalPatent(CPatent patent) {
        List<ValidationError> errors = new ArrayList<>();
        if (StringUtils.isEmpty(patent.getRelationshipExtended().getRegistrationCountry())) {
            errors.add(ValidationError.builder().pointer("relationshipExtended.registrationCountry").messageCode("required.field").build());
        }
        return errors;

    }

    private List<ValidationError> validateEuropeanPatent(CPatent p) {
        List<ValidationError> errors = new ArrayList<>();
        if (!StringUtils.isEmpty(p.getRelationshipExtended().getFilingNumber())) {
            CFileId fileId = p.getFile().getFileId();
            Optional<IpFileRelationshipExtended> oldrelationshipExteded = ipPatentRelationshipExtendedRepository.findById(new IpFilePK(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr()));
            //proverka dali evorpejskia patent sy6testvuva v ebddownload stava ako nqma stara transformaciq ili starata transformacia ne e za evropejski patent ili nomera na starata transformacia ne e sy6tiq kato na novata!
            //na teoriq mojeshe vinagi kogato ima dobavena evropejska transformcia, da se pravi tyrsene v ebddownload, no reshih che shte e po-dobre da se spestqt malko obrashteniq i da se pravi SAMO ako ima promqna po evropejskata transformaciq (ili e nova ili e promenen tipa/nomera)
            if (oldrelationshipExteded.isEmpty() || (oldrelationshipExteded.isPresent() && (!EUROPEAN_PATENT.code().equals(oldrelationshipExteded.get().getApplicationType()) || !Objects.equals(oldrelationshipExteded.get().getFilingNumber(), p.getRelationshipExtended().getFilingNumber())))) {
                CEbdPatent ebdApp = ebdPatentService.selectByFileNumber(DataConverter.parseInteger(p.getRelationshipExtended().getFilingNumber(), null));
                if (ebdApp == null) {
                    errors.add(ValidationError.builder().pointer("relationshipExtended.filingNumber").messageCode("missing.epo.patent").build());
                }
            }
        }
        return errors;
    }
}
