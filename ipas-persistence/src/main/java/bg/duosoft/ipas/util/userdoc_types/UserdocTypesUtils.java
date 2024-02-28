package bg.duosoft.ipas.util.userdoc_types;

import bg.duosoft.abdocs.model.DocRegistrationType;
import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.core.model.userdoc.config.CUserdocTypeDepartment;
import bg.duosoft.ipas.enums.InheritResponsibleUserType;
import bg.duosoft.ipas.enums.RegisterToProcessType;
import bg.duosoft.ipas.enums.UserdocGroup;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class UserdocTypesUtils {

    public static LinkedHashMap<Boolean, String> generateStatusSelectOptions(MessageSource messageSource) {
        LinkedHashMap<Boolean, String> map = new LinkedHashMap<>();
        map.put(false, messageSource.getMessage("status.active", null, LocaleContextHolder.getLocale()));
        map.put(true, messageSource.getMessage("status.inactive", null, LocaleContextHolder.getLocale()));
        return map;
    }

    public static LinkedHashMap<String, String> generateGroupSelectOptions() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        for (UserdocGroup userdocGroup : UserdocGroup.values()) {
            map.put(userdocGroup.code(), userdocGroup.name());
        }
        return map;
    }

    public static LinkedHashMap<String, String> generateRegisterToProcessTypes(){
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        for (RegisterToProcessType type : RegisterToProcessType.values()) {
            map.put(type.code(), type.description());
        }
        return map;
    }

    public static boolean userdocTypeDepartmentSelected(String fullDepartmentCode, List<CUserdocTypeDepartment> departments){

        if (CollectionUtils.isEmpty(departments)){
            return false;
        }
        CUserdocTypeDepartment department = departments.stream().filter(r ->
          r.getDepartment().getOfficeStructureId().getFullDepartmentCode().equals(fullDepartmentCode)).findFirst().orElse(null);

        return Objects.nonNull(department);

    }

    public static LinkedHashMap<String, String> generateInheritResponsibleUserType(){
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        for (InheritResponsibleUserType type : InheritResponsibleUserType.values()) {
            map.put(type.code(), type.description());
        }
        return map;
    }

    public static LinkedHashMap<Integer, String> generateRegistrationTypeOptions(MessageSource messageSource) {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();

        for (DocRegistrationType docRegistrationType : DocRegistrationType.values()) {
            map.put(docRegistrationType.value(), messageSource.getMessage("abdocs.reg.type." + docRegistrationType.name(), null, docRegistrationType.name(), LocaleContextHolder.getLocale()));
        }
        return map;
    }

}
