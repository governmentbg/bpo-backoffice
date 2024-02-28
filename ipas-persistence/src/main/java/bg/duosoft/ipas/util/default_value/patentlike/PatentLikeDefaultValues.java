package bg.duosoft.ipas.util.default_value.patentlike;


import bg.duosoft.ipas.util.default_value.IpObjectDefaultValue;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PatentLikeDefaultValues extends IpObjectDefaultValue {

    public PatentLikeDefaultValues(Date entitlementDate, Date expirationDate) {
        super(entitlementDate, expirationDate);
    }

}
