package bg.duosoft.ipas.integration.tmseniority.mapper;

import bg.duosoft.ipas.core.model.mark.CMarkSeniority;
import bg.duosoft.ipas.integration.tmseniority.model.SeniorityType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by Raya
 * 07.03.2019
 */
@Mapper(componentModel = "spring")
public abstract class MarkSeniorityMapper {

    @Mapping(source = "seniorityApplicationNumber", target = "number")
    @Mapping(source = "seniorityMarkName", target = "markName")
    @Mapping(source = "seniorityRegistrationDate", target = "registrationDate")
    @Mapping(source = "seniorityApplicationDate", target = "applicationDate")
    public abstract CMarkSeniority seniorityTypeToCMarkSeniority(SeniorityType seniorityType);
}
