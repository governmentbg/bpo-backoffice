package bg.duosoft.ipas.core.validation.reception;

import bg.duosoft.ipas.core.model.document.CDocument;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdoc;
import bg.duosoft.ipas.core.model.reception.CUserdocReceptionRelation;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.reception.UserdocReceptionRelationService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.SubmissionType;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.util.mark.InternationalMarkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * User: Georgi
 * Date: 2.6.2020 г.
 * Time: 0:19
 */
@Component
public class ReceptionUserdocValidator extends ReceptionBaseValidator {

    @Autowired
    private DocService docService;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserdocTypesService userdocTypesService;
    @Autowired
    private UserdocReceptionRelationService userdocReceptionRelationService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    /**
     * @param obj
     * @param additionalArgs - ako ima podaden additionalArg[0], to tova e CReception obekta na master document-a
     * @return
     */
    @Override
    public List<ValidationError> validate(CReception obj, Object... additionalArgs) {
        List<ValidationError> errors = super.validate(obj, additionalArgs);
        if (errors == null) {
            errors = new ArrayList<>();
        }
        CReception masterReceptionRequest = additionalArgs == null || additionalArgs.length == 0 ? null : (CReception) additionalArgs[0];
        validateUserdoc(masterReceptionRequest, obj, errors);
        return errors;
    }

    private boolean isUserdocPersonsExist(CReception reception) {
        return reception.getUserdoc() != null && !CollectionUtils.isEmpty(reception.getUserdoc().getUserdocPersons());
    }
    private void validateUserdocPersons(CReception reception, List<ValidationError> errors) {
        if (isUserdocPersonsExist(reception)) {
            boolean owrExists = !CollectionUtils.isEmpty(reception.getUserdoc().getPersonsByRole(UserdocPersonRole.APPLICANT));
            if (owrExists) {
                errors.add(ValidationError.builder().pointer("userdocPersons").message("Applicants can be added only through CReception.ownershipData").build());
            }
            boolean representativesExists =  !CollectionUtils.isEmpty(reception.getUserdoc().getPersonsByRole(UserdocPersonRole.REPRESENTATIVE));
            if (representativesExists) {
                errors.add(ValidationError.builder().pointer("userdocPersons").message("Representatives can be added only through CReception.representationData").build());
            }

        }
        validateUserdocPersonRoles(reception, errors);
    }
    private void validateUserdocPersonRoles(CReception reception,  List<ValidationError> errors) {
        if (reception.getUserdoc() != null && !StringUtils.isEmpty(reception.getUserdoc().getUserdocType())) {
            CUserdocType udt = userdocTypesService.selectUserdocTypeById(reception.getUserdoc().getUserdocType());
            if (udt != null) {
                Set<UserdocPersonRole> allowedRoles = udt.getRoles() == null ? new HashSet<>() : udt.getRoles().stream().map(r -> r.getRole()).collect(Collectors.toSet());
                if (!allowedRoles.contains(UserdocPersonRole.APPLICANT) && containsOwners(reception)) {
                    errors.add(ValidationError.builder().pointer("receptionPersons").message("Userdoc does not allow person with role" + UserdocPersonRole.APPLICANT).build());
                }
                if (!allowedRoles.contains(UserdocPersonRole.REPRESENTATIVE) && containsRepresentatives(reception)) {
                    errors.add(ValidationError.builder().pointer("receptionPersons").message("Userdoc does not allow person with role" + UserdocPersonRole.REPRESENTATIVE).build());
                }
                if (!CollectionUtils.isEmpty(reception.getUserdoc().getUserdocPersons())) {
                    reception
                            .getUserdoc()
                            .getUserdocPersons()
                            .stream()
                            .filter(udp -> !allowedRoles.contains(udp.getRole()))
                            .forEach(r -> errors.add(ValidationError.builder().pointer("receptionPersons").message("Userdoc does not allow person with role" + r.getRole()).build()));
                }

            }
        }
    }

    private void validateUserdoc(CReception masterReceptionRequest, CReception receptionForm, List<ValidationError> errors) {
        boolean isWithoutCorrespondents = receptionForm.getUserdoc().isWithoutCorrespondents();
        if (!isWithoutCorrespondents) {
            if (InternationalMarkUtils.isInternationalMark(receptionForm.getUserdoc().getFileId()) && receptionForm.getSubmissionType().equals(SubmissionType.IMPORT.code())) {
                if (!containsRepresentatives(receptionForm) && !isUserdocPersonsExist(receptionForm)) {
                    errors.add(ValidationError.builder().pointer("receptionRepresentatives").messageCode("reception.userdoc.persons.empty").build());
                }
            } else {
                boolean isOwnersExists = containsOwners(receptionForm);
                if (!isOwnersExists && !isUserdocPersonsExist(receptionForm)) {
                    errors.add(ValidationError.builder().pointer("receptionOwners").messageCode("reception.owners.empty").build());
                } else {
                    if (isOwnersExists) {
                        validateOwnersData(errors, receptionForm.getOwnershipData().getOwnerList());
                    }
                }
            }

            validateUserdocPersons(receptionForm, errors);
        }

        CReceptionUserdoc userdoc = receptionForm.getUserdoc();
        boolean emptyUdt = rejectIfEmptyString(errors, userdoc.getUserdocType(), "receptionUserdocType");
        if (!emptyUdt) {
            CUserdocType udt = userdocTypesService.selectUserdocTypeById(userdoc.getUserdocType());
            rejectIfTrue(errors, udt == null, "receptionUserdocType", "incorrect.userdoctype");
            if (udt != null) {
                String masterDocumentType = null;
                if (userdoc.isRelatedToFile()) {
                    masterDocumentType = userdoc.getFileId().getFileType();
                } else if (userdoc.isRelatedToUserdoc()){
                    CDocument affectedUd = docService.selectDocument(userdoc.getDocumentId());
                    if (affectedUd != null) {
                        masterDocumentType = affectedUd.getCUserdocType().getUserdocType();
                    }
                } else if (masterReceptionRequest != null && masterReceptionRequest.isFileRequest()) {
                    masterDocumentType = applicationTypeService.getFileTypeByApplicationType(masterReceptionRequest.getFile().getApplicationType());
                }

                if (masterDocumentType != null) {
                    List<CUserdocReceptionRelation> rels = userdocReceptionRelationService.selectUserdocsByMainType(masterDocumentType, false);
                    rejectIfTrue(errors, rels == null || !rels.stream().map(r -> r.getUserdocType()).anyMatch(r -> Objects.equals(r, userdoc.getUserdocType())), "receptionUserdocType", "reception.incorrect.userdoctype.relation", userdoc.getUserdocType());
                }

            }
        }
        //related document se proverqva samo ako masterReceptionRequest == null, t.e. ne se pravi reception na userdocs kym reception na osnoven obekt
        if (masterReceptionRequest == null) {
            if (!userdoc.isRelatedToFile() && !userdoc.isRelatedToUserdoc()) {
                rejectIfEmptyString(errors, "", "receptionUserdocObjectNumber");
            }
            if (userdoc.isRelatedToFile()) {
                rejectIfTrue(errors, userdoc.isRelatedToUserdoc(), "receptionUserdocObjectNumber", "reception.cannot.have.both.doc.and.file");
                boolean isFileExist = fileService.isFileExist(userdoc.getFileId().getFileSeq(), userdoc.getFileId().getFileType(), userdoc.getFileId().getFileSeries(), userdoc.getFileId().getFileNbr());
                rejectIfFalse(errors, isFileExist, "receptionUserdocObjectNumber", "object.not.exist");
            }
            if (userdoc.isRelatedToUserdoc()) {
                rejectIfTrue(errors, userdoc.isRelatedToFile(), "receptionUserdocObjectNumber", "reception.cannot.have.both.doc.and.file");
                CDocumentId docId = userdoc.getDocumentId();
                boolean documentExist = docService.isDocumentExist(docId.getDocOrigin(), docId.getDocLog(), docId.getDocSeries(), docId.getDocNbr());
                rejectIfFalse(errors, documentExist, "receptionUserdocObjectNumber", "object.not.exist");
            }
        }

    }
}
