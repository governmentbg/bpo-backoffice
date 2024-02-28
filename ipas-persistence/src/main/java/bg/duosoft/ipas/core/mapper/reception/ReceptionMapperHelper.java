
package bg.duosoft.ipas.core.mapper.reception;

import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import bg.duosoft.ipas.core.service.reception.ReceptionTypeService;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: Georgi
 * Date: 27.5.2020 г.
 * Time: 12:44
 */

@Mapper(componentModel = "spring")
public class ReceptionMapperHelper {
    @Autowired
    private ApplicationTypeService applicationTypeService;
    @Autowired
    private ReceptionTypeService receptionTypeService;

    @Named("applicationSubtypeMapper")
    public String getApplicationSubtype(CReception form) {
        return !StringUtils.isEmpty(form.getFile().getApplicationSubType()) ? form.getFile().getApplicationSubType() : applicationTypeService.getDefaultApplicationSubtype(form.getFile().getApplicationType());
    }
    @Named("receptionWcodeMapper")
    public String getReceptionWCode(CReception form) {
        return form.isUserdocRequest() ? "PE" : "SC";
    }
    @Named("loggedUserMapper")
    public Integer getLoggedUserId(CReception form) {
        return SecurityUtils.getLoggedUserId();
    }
}
