package bg.duosoft.ipas.core.mapper.ipbase;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyEntity;
import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyOwner;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLaw;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import org.mapstruct.*;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: ggeorgiev
 * Date: 4.4.2019 г.
 * Time: 18:33
 */
//@Mapper
public abstract class IntellectualPropertyFileMapper<T extends IntellectualPropertyEntity> {

    @Mapping(target = "priorityData.exhibitionNotes",                       source = "exhibitionNotes")
    @Mapping(target = "priorityData.earliestAcceptedParisPriorityDate",     source = "firstPriorityDate")
    @Mapping(target = "priorityData.exhibitionDate",                        source = "exhibitionDate")
    @Mapping(target = "priorityData.parisPriorityList",                     source = "priorities")
    @Mapping(target = "ownershipData.ownerList",                            source = "owners")
    @Mapping(target = "representationData.representativeList",              source = "representatives")
    @Mapping(target = "filingData.novelty1Date",                            source = "novelty1Date")
    @Mapping(target = "filingData.novelty2Date",                            source = "novelty2Date")
    @Mapping(target = "filingData.indManualInterpretationRequired",         source = "indManualInterpretation")
    @Mapping(target = "servicePerson",                                      source = "servicePerson")
    @Mapping(target = "publicationData.publicationNotes",                   source = "publicationNotes")
    @BeanMapping(ignoreByDefault = true)
    public abstract void convertToCoreObjectBase(T entity, @MappingTarget CFile file);


    @InheritInverseConfiguration(name = "convertToCoreObjectBase")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "cfApplicationSubtype.cfApplicationType.applTyp",     source = "filingData.applicationType")
    @Mapping(target = "cfApplicationSubtype.pk.applTyp",                    source = "filingData.applicationType")
    @Mapping(target = "cfApplicationSubtype.pk.applSubtyp",                 source = "filingData.applicationSubtype")
    @Mapping(target = "filingDate",                                         source = "filingData.filingDate")
    @Mapping(target = "publicationTyp",                                     source = "filingData.publicationTyp")
    @Mapping(target = "captureDate",                                        source = "filingData.captureDate")
    @Mapping(target = "registrationDate",                                   source = "registrationData.registrationDate")
    @Mapping(target = "registrationDup",                                    source = "registrationData.registrationId.registrationDup")
    @Mapping(target = "registrationNbr",                                    source = "registrationData.registrationId.registrationNbr")
    @Mapping(target = "registrationSer",                                    source = "registrationData.registrationId.registrationSeries")
    @Mapping(target = "registrationTyp",                                    source = "registrationData.registrationId.registrationType")
    @Mapping(target = "entitlementDate",                                    source = "registrationData.entitlementDate")
    @Mapping(target = "expirationDate",                                     source = "registrationData.expirationDate")
    @Mapping(target = "firstPriorityDate",                                  source = "priorityData.earliestAcceptedParisPriorityDate")
    @Mapping(target = "publicationDate",                                    source = "publicationData.publicationDate")
    @Mapping(target = "journalCode",                                        source = "publicationData.journalCode")
    @Mapping(target = "indRegistered",                                      source = "registrationData.indRegistered")
//    @Mapping(target = "cfLaw.lawCode",      source = "filingData.lawCode")//tova se maha ot tuk i otiva v @AfterMapping, zashtoto moje lawCode-a da e null, no togava se syzdava cfLaw obekt!!!!
//    @Mapping(target = "captureUser.userId", source = "filingData.captureUserId")//tova se maha ot tuk i otiva v @AfterMapping, zashtoto dori i da e prazen captureUserId, se syzdava obekt ot tip IpUser!!!!
    public abstract void convertToEntityObjectBase(CFile file, @MappingTarget T mark);


    @AfterMapping
    //file To IntellectualPropertyEntity after mappings
    public void setOwnerOrderNumbersAndMainOwner(CFile source, @MappingTarget T target) {

        //updating owners orderNbrs, if they are not preset!
        AtomicInteger orderNbr = new AtomicInteger(0);
        if (target.getOwners() != null) {
            Optional<Integer> maxExistingOrderNbr = target.getOwners().stream().filter(o -> o.getOrderNbr() != null).sorted(Comparator.comparingInt(IntellectualPropertyOwner::getOrderNbr).reversed()).map(o -> o.getOrderNbr()).findFirst();
            maxExistingOrderNbr.ifPresent(o -> orderNbr.set(o));
            target.getOwners().stream().filter(o -> o.getOrderNbr() == null).forEach(o -> o.setOrderNbr(orderNbr.incrementAndGet()));
        }
        //end of updating owners orderNbrs


        //setting main owner...
        if (target.getOwners() != null) {
            target
                    .getOwners()
                    .stream()
                    .sorted(Comparator.comparing(IntellectualPropertyOwner::getOrderNbr))
                    .map(mo -> mo.getIpPersonAddresses())
                    .findFirst()
                    .ifPresent(target::setMainOwner);
        }
        //end of setting main owner

    }

    @AfterMapping
    public void setCaptureUserAndLawCode(CFile source, @MappingTarget T target) {
        if (source.getFilingData().getCaptureUserId() != null) {
            target.setCaptureUser(new IpUser());
            target.getCaptureUser().setUserId(source.getFilingData().getCaptureUserId().intValue());
            target.getCaptureUser().setRowVersion(1);
        }
        if (source.getFilingData().getLawCode() != null) {
            target.setCfLaw(new CfLaw());
            target.getCfLaw().setLawCode(source.getFilingData().getLawCode());
        }
    }
    //end of fileTo IntellectualPropertyEntity after mappings

}
