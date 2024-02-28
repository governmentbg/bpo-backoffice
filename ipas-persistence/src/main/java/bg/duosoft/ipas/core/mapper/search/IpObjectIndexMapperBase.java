package bg.duosoft.ipas.core.mapper.search;

import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyEntity;
import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyRelatedPerson;
import bg.duosoft.ipas.persistence.model.entity.person.IpPerson;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddressesPK;
import bg.duosoft.ipas.persistence.model.entity.vw.ind.VwIpObjectIndex;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.mapstruct.Named;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * User: ggeorgiev
 * Date: 16.03.2021
 * Time: 9:58
 */
public class IpObjectIndexMapperBase {


    protected  <T extends IntellectualPropertyRelatedPerson> List<T> mapRelatedPersons(String personNumbers, Supplier<T> constructor) {
        List<T> res = null;
        if (!StringUtils.isEmpty(personNumbers)) {
            res = new ArrayList<>();
            for (String on : personNumbers.split(";")) {
                IntellectualPropertyRelatedPerson o = constructor.get();
                o.setIpPersonAddresses(toIpPersonAddress(Integer.parseInt(on), null));
                res.add((T) o);
            }
        }
        return res;
    }

    public void mapServiceAndOwnerPersons(VwIpObjectIndex vw, IntellectualPropertyEntity e) {
        e.setMainOwner(toIpPersonAddress(null, vw.getFile().getMainOwnerPersonName()));
        e.setServicePerson(toIpPersonAddress(vw.getFile().getServicePersonNumber(), vw.getFile().getServicePersonName()));
    }

    private IpPersonAddresses toIpPersonAddress(Integer personNbr, String personName) {
        IpPersonAddresses pa = new IpPersonAddresses();
        IpPerson p = new IpPerson();
        pa.setIpPerson(p);

        if (!ObjectUtils.isEmpty(personName)) {
            p.setPersonName(personName);
        }
        if (personNbr != null) {
            pa.setPk(new IpPersonAddressesPK());
            pa.getPk().setPersonNbr(personNbr);
            pa.getIpPerson().setPersonNbr(personNbr);
        }
        return pa;
    }
}
