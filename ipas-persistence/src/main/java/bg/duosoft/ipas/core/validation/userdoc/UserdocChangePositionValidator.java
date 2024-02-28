package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPanel;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserdocChangePositionValidator implements IpasValidator<CUserdoc> {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProcessService processService;

    @Autowired
    private UserdocTypesService userdocTypesService;


    @Override
    public List<ValidationError> validate(CUserdoc obj, Object... additionalArgs) {
        CProcessId newParentProcessId = Objects.isNull(additionalArgs) ? null : (CProcessId) additionalArgs[0];
        CProcess newParentProcess = processService.selectProcess(newParentProcessId, false);

        String mainType;
        if (Objects.nonNull(newParentProcess.getProcessOriginData().getFileId())) {
            mainType = newParentProcess.getProcessOriginData().getFileId().getFileType();
        } else if (Objects.nonNull(newParentProcess.getProcessOriginData().getDocumentId())) {
            mainType = newParentProcess.getProcessOriginData().getUserdocType();
        } else {
            throw new RuntimeException("New parent process must be userdoc or ipobject !");
        }

        CUserdocType userdocType = obj.getUserdocType();
        List<CUserdocType> allowedTypes = userdocTypesService.selectUserdocTypesMapByUserdocParentType(mainType);

        List<ValidationError> errors = new ArrayList<>();
        rejectIfEmptyCollection(errors, allowedTypes, "change.allowedType", "userdoc.change.position.config.error");
        if (CollectionUtils.isEmpty(errors)) {
            CUserdocType foundType = allowedTypes.stream()
                    .filter(type -> type.getUserdocType().equalsIgnoreCase(userdocType.getUserdocType()))
                    .findAny()
                    .orElse(null);
            rejectIfEmpty(errors, foundType, "change.allowedType", "userdoc.change.position.config.error");
        }

        CProcessId processId = obj.getProcessSimpleData().getProcessId();

        CProcessId currentTopProcess = obj.getProcessSimpleData().getFileProcessId();
        CProcessId newTopProcess = newParentProcess.getProcessOriginData().getTopProcessId();
        if (isSameProcess(currentTopProcess, newTopProcess)) {
            int linkedUserdocs = processService.selectUserdocUpperProcessesCount(processId.getProcessType(), processId.getProcessNbr());
            rejectIfTrue(errors, linkedUserdocs != 0, "change.allowedType", "userdoc.change.position.has.userdoc.children.error");
        } else {
            int upperProcessesCount = processService.selectUpperProcessesCount(processId.getProcessType(), processId.getProcessNbr());
            rejectIfTrue(errors, upperProcessesCount != 0, "change.allowedType", "userdoc.change.position.has.children.error");
        }
        return errors;
    }

    public static boolean isSameProcess(CProcessId processId, CProcessId anotherProcessId) {
        String processType = processId.getProcessType();
        Integer processNbr = processId.getProcessNbr();
        String anotherProcessType = anotherProcessId.getProcessType();
        Integer anotherProcessNbr = anotherProcessId.getProcessNbr();
        return (processNbr.equals(anotherProcessNbr)) && (processType.equalsIgnoreCase(anotherProcessType));
    }

}
