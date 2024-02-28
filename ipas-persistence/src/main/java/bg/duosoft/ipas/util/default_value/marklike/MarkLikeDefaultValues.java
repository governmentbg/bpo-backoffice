package bg.duosoft.ipas.util.default_value.marklike;

import bg.duosoft.ipas.util.default_value.IpObjectDefaultValue;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MarkLikeDefaultValues extends IpObjectDefaultValue {

    public MarkLikeDefaultValues(Date entitlementDate, Date expirationDate) {
        super(entitlementDate, expirationDate);
    }
}
