package bg.duosoft.ipas.core.service.impl;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CRegistrationData;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyEntity;
import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyRelatedPerson;
import bg.duosoft.ipas.persistence.model.entity.ext.acp.AcpInfringerPerson;
import bg.duosoft.ipas.persistence.model.entity.ext.acp.AcpReprs;
import bg.duosoft.ipas.persistence.model.entity.ext.acp.AcpServicePerson;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.repository.entity.person.IpPersonAdressesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * User: ggeorgiev
 * Date: 25.3.2019 г.
 * Time: 18:55
 */
public class IntellectualPropertyServiceImpl extends ServiceBaseImpl {

    @Autowired
    private IpPersonAdressesRepository ipPersonAdressesRepository;


    private void mergeOrInsertPersonsOnMarkSpecificObjects(IntellectualPropertyEntity entity) {
        if (entity instanceof IpMark) {

            AcpServicePerson acpServicePerson = ((IpMark) entity).getAcpServicePerson();
            if (Objects.nonNull(acpServicePerson)) {
                IpPersonAddresses p = ipPersonAdressesRepository.mergeOrInsertPersonAddress(acpServicePerson.getServicePerson());
                acpServicePerson.setServicePerson(p);
                acpServicePerson.getServicePerson().getPk().setAddrNbr(p.getPk().getAddrNbr());
                acpServicePerson.getServicePerson().getPk().setPersonNbr(p.getPk().getPersonNbr());
            }

            AcpInfringerPerson acpInfringer = ((IpMark) entity).getAcpInfringerPerson();
            if (Objects.nonNull(acpInfringer)) {
                IpPersonAddresses p = ipPersonAdressesRepository.mergeOrInsertPersonAddress(acpInfringer.getInfringerPerson());
                acpInfringer.setInfringerPerson(p);
                acpInfringer.getInfringerPerson().getPk().setAddrNbr(p.getPk().getAddrNbr());
                acpInfringer.getInfringerPerson().getPk().setPersonNbr(p.getPk().getPersonNbr());
            }

            List<AcpReprs> acpRepresentatives = ((IpMark) entity).getAcpRepresentatives();
            if (!CollectionUtils.isEmpty(acpRepresentatives)) {
                acpRepresentatives.stream().forEach(this::updateIpPersonAddresses);
            }

        }
    }

    protected void mergeOrInsertPersons(IntellectualPropertyEntity entity) {
        entity.getOwners().stream().forEach(this::updateIpPersonAddresses);

        mergeOrInsertPersonsOnMarkSpecificObjects(entity);

        if (entity.getServicePerson() != null) {
            IpPersonAddresses p = ipPersonAdressesRepository.mergeOrInsertPersonAddress(entity.getServicePerson());
            entity.setServicePerson(p);
            entity.getServicePerson().getPk().setAddrNbr(p.getPk().getAddrNbr());
            entity.getServicePerson().getPk().setPersonNbr(p.getPk().getPersonNbr());
        }

        if (entity.getMainOwner() != null) {
            IpPersonAddresses p = ipPersonAdressesRepository.mergeOrInsertPersonAddress(entity.getMainOwner());
            entity.setMainOwner(p);
            entity.getMainOwner().getPk().setAddrNbr(p.getPk().getAddrNbr());
            entity.getMainOwner().getPk().setPersonNbr(p.getPk().getPersonNbr());
        }

        if (entity.getRepresentatives() != null) {
            entity.getRepresentatives().stream().forEach(this::updateIpPersonAddresses);
        }
    }

    protected void updateIpPersonAddresses(IntellectualPropertyRelatedPerson r) {
        IpPersonAddresses p = ipPersonAdressesRepository.mergeOrInsertPersonAddress(r.getIpPersonAddresses());
        r.setIpPersonAddresses(p);
        r.getPk().setAddrNbr(p.getPk().getAddrNbr());
        r.getPk().setPersonNbr(p.getPk().getPersonNbr());
    }

    protected void validateIpObjectRowVersion(IntellectualPropertyEntity originalEntity, IntellectualPropertyEntity entityToBeSaved) {
        if (!Objects.equals(originalEntity.getRowVersion(), entityToBeSaved.getRowVersion())) {
            throw new IpasValidationException(Arrays.asList(ValidationError.builder().pointer("rowVersion").messageCode("object.already.updated").build()));
        }
        validateFileRowVersion(originalEntity.getFile(), entityToBeSaved.getFile());
    }

    protected void validateFileRowVersion(IpFile originalFile, IpFile fileToBeSaved) {
        if (!Objects.equals(originalFile.getRowVersion(), fileToBeSaved.getRowVersion())) {
            throw new IpasValidationException(Arrays.asList(ValidationError.builder().pointer("rowVersion").messageCode("object.already.updated").build()));
        }
    }

    protected void incrementIpObjectRowVersion(IntellectualPropertyEntity entity) {
        entity.setRowVersion(entity.getRowVersion() + 1);
        incrementIpFileRowVersion(entity.getFile());
    }

    protected void incrementIpFileRowVersion(IpFile file) {
        file.setRowVersion(file.getRowVersion() + 1);
    }

    protected void transferRegistrationDataFromReceptionToFile(CFile receptionFile, CFile ipFile) {
        if (ipFile.getRegistrationData() == null) {
            ipFile.setRegistrationData(new CRegistrationData());
        }
        //entitlement date-a na ipFile-a se setva SAMO ako e null i v reception-a ima entitlementDate
        if (ipFile.getRegistrationData().getEntitlementDate() == null && receptionFile.getRegistrationData() != null && receptionFile.getRegistrationData().getEntitlementDate() != null) {
            ipFile.getRegistrationData().setEntitlementDate(receptionFile.getRegistrationData().getEntitlementDate());
        }
    }

}
