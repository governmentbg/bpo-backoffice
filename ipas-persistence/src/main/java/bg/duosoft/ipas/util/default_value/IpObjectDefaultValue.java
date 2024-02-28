package bg.duosoft.ipas.util.default_value;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IpObjectDefaultValue {

    private Date entitlementDate;
    private Date expirationDate;

}
