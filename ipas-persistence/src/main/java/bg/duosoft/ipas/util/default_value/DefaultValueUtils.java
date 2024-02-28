package bg.duosoft.ipas.util.default_value;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CRegistrationData;
import bg.duosoft.ipas.core.model.file.CRelationshipExtended;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.service.file.FileRelationshipsService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLawApplicationSubtype;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfLawApplicationSubTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentRepository;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.default_value.marklike.MarkLikeDefaultValues;
import bg.duosoft.ipas.util.default_value.patentlike.PatentLikeDefaultValues;
import bg.duosoft.ipas.util.default_value.patentlike.spc.SpcDefaultValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Component
public class DefaultValueUtils {

    @Autowired
    private CfLawApplicationSubTypeRepository cfLawApplicationSubTypeRepository;

    @Autowired
    private IpPatentRepository ipPatentRepository;

    @Autowired
    private FileRelationshipsService fileRelationshipsService;

    public MarkLikeDefaultValues createMarkLikeDefaultValuesObject(CMark mark) {
        return new MarkLikeDefaultValues(getEntitlementDate(mark.getFile(), mark.getRelationshipExtended()), getExpirationDate(mark.getFile()));
    }

    public PatentLikeDefaultValues createPatentLikeDefaultValuesObject(CPatent patent) {
        return new PatentLikeDefaultValues(getEntitlementDate(patent.getFile(), patent.getRelationshipExtended()), getExpirationDate(patent.getFile()));
    }

    public IpObjectDefaultValue createBaseDefaultValuesObject(CFile file, CRelationshipExtended relationshipExtended) {
        return new IpObjectDefaultValue(getEntitlementDate(file, relationshipExtended), getExpirationDate(file));
    }

    public SpcDefaultValues createSpcDefaultValuesObjectWithGivenExpirationDate(CPatent spc, Date expirationDate) {
        return new SpcDefaultValues(getEntitlementDateFromExpDate(expirationDate),
                getExpirationDatePlusRegYear(expirationDate, spc.getFile()));
    }

    public SpcDefaultValues createSpcDefaultValuesObject(CPatent spc) {
        Date expirationDate = null;
        if (spc.getFile().getRelationshipList() != null) {
            CFileId pk = spc.getFile().getRelationshipList().get(0).getFileId();
            expirationDate = ipPatentRepository.findPatentExpirationDate(pk.getFileSeq(), pk.getFileType(), pk.getFileSeries(), pk.getFileNbr()).orElse(null);
        }
        return new SpcDefaultValues(getEntitlementDateFromExpDate(expirationDate),
                getExpirationDatePlusRegYear(expirationDate, spc.getFile()));
    }

    public Date getEntitlementDateFromExpDate(Date expirationDate) {
        if (Objects.isNull(expirationDate)) {
            return null;
        }
        return expirationDate;
    }

    public Date getExpirationDatePlusRegYear(Date expirationDate, CFile spcFile) {

        if (Objects.isNull(expirationDate)) {
            return null;
        }

        Optional<CfLawApplicationSubtype> cfLawApplicationSubtypeOptional = getCfLawApplicationSubtype(spcFile);
        if (cfLawApplicationSubtypeOptional.isEmpty())
            return null;

        CfLawApplicationSubtype cfLawApplicationSubtype = cfLawApplicationSubtypeOptional.get();
        Integer registrationYear = cfLawApplicationSubtype.getRegistrationYear();

        if (Objects.isNull(registrationYear))
            return null;

        return DateUtils.convertToDate(DateUtils.convertToLocalDatTime(expirationDate).plusYears(registrationYear));
    }

    public Date getEntitlementDate(CFile file, CRelationshipExtended relationshipExtended) {
        FileType fileType = FileType.selectByCode(file.getFileId().getFileType());
        switch (fileType) {
            case PLANTS_AND_BREEDS: {
                CRegistrationData registrationData = file.getRegistrationData();
                if (Objects.isNull(registrationData))
                    return null;

                return registrationData.getRegistrationDate();
            }
            case PATENT:
            case EU_PATENT:
            case UTILITY_MODEL: {
                return fileRelationshipsService.getPatentLikeObjectEntitlementDateFromRelationships(file.getRelationshipList(), relationshipExtended, file.getFilingData().getFilingDate());
            }
            default:
                return file.getFilingData().getFilingDate();
        }
    }

    public Date getExpirationDate(CFile file) {
        if (Objects.isNull(file))
            return null;

        Optional<CfLawApplicationSubtype> cfLawApplicationSubtypeOptional = getCfLawApplicationSubtype(file);
        if (cfLawApplicationSubtypeOptional.isEmpty())
            return null;

        CfLawApplicationSubtype cfLawApplicationSubtype = cfLawApplicationSubtypeOptional.get();
        Integer registrationYear = cfLawApplicationSubtype.getRegistrationYear();
        if (Objects.isNull(registrationYear))
            return null;

        int registerExpirationWCode = cfLawApplicationSubtype.getRegisterExpirationWcode().intValue();
        switch (registerExpirationWCode) {
            case 1:
                return calculateExpirationDateFromFilingDate(file, registrationYear);
            case 3:
                return calculateExpirationDateFromRegistrationDate(file, registrationYear);
            default:
                return null;
        }
    }

    public Date getRenewalExpirationDate(CFile file) {
        if (Objects.isNull(file))
            return null;

        Integer renewalYears = selectRenewalYears(file);
        if (Objects.isNull(renewalYears))
            return null;

        Date expirationDate = file.getRegistrationData().getExpirationDate();
        if (Objects.isNull(expirationDate)) {
            Date calculatedBaseExpiration = getExpirationDate(file);
            return DateUtils.convertToDate(DateUtils.convertToLocalDatTime(calculatedBaseExpiration).plusYears(renewalYears));
        } else {
            return DateUtils.convertToDate(DateUtils.convertToLocalDatTime(expirationDate).plusYears(renewalYears));
        }
    }

    private Date calculateExpirationDateFromRegistrationDate(CFile file, Integer registrationYear) {
        CRegistrationData registrationData = file.getRegistrationData();
        if (Objects.isNull(registrationData))
            return null;

        Date registrationDate = registrationData.getRegistrationDate();
        if (Objects.isNull(registrationDate))
            return null;

        return DateUtils.convertToDate(DateUtils.convertToLocalDatTime(registrationDate).plusYears(registrationYear));
    }

    private Date calculateExpirationDateFromFilingDate(CFile file, Integer registrationYear) {
        Date filingDate = file.getFilingData().getFilingDate();
        if (Objects.isNull(filingDate))
            return null;

        return DateUtils.convertToDate(DateUtils.convertToLocalDatTime(filingDate).plusYears(registrationYear));
    }

    private Integer selectRenewalYears(CFile file) {
        Optional<CfLawApplicationSubtype> cfLawApplicationSubtypeOptional = getCfLawApplicationSubtype(file);
        if (cfLawApplicationSubtypeOptional.isEmpty())
            return null;

        CfLawApplicationSubtype cfLawApplicationSubtype = cfLawApplicationSubtypeOptional.get();
        Long renewalYears = cfLawApplicationSubtype.getRenewalYears();
        if (Objects.isNull(renewalYears))
            return null;

        return renewalYears.intValue();
    }

    private Optional<CfLawApplicationSubtype> getCfLawApplicationSubtype(CFile file) {
        Integer lawCode = file.getFilingData().getLawCode();
        String applicationType = file.getFilingData().getApplicationType();
        String applicationSubType = file.getFilingData().getApplicationSubtype();
        return cfLawApplicationSubTypeRepository.findByLawCodeApplicationTypeAndSubtype(lawCode, applicationType, applicationSubType);
    }

}
