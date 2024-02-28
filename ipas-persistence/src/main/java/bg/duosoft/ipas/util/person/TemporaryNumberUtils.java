package bg.duosoft.ipas.util.person;

import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddressesPK;

import java.util.Objects;

public class TemporaryNumberUtils {

    public static void removeTemporaryNumbers(IpPersonAddresses ipPersonAddresses) {
        if (Objects.nonNull(ipPersonAddresses)) {
            IpPersonAddressesPK pk = ipPersonAddresses.getPk();
            if (Objects.nonNull(pk)) {
                Integer personNbr = pk.getPersonNbr();
                Integer addrNbr = pk.getAddrNbr();
                if (Objects.nonNull(personNbr) && Objects.nonNull(addrNbr))
                    if (personNbr < 0 || addrNbr < 0)
                        setEmptyPersonPK(ipPersonAddresses);
            }
        }
    }

    private static void setEmptyPersonPK(IpPersonAddresses ipPersonAddresses) {
        ipPersonAddresses.setPk(new IpPersonAddressesPK());
        ipPersonAddresses.getIpPerson().setPersonNbr(null);
    }

}
