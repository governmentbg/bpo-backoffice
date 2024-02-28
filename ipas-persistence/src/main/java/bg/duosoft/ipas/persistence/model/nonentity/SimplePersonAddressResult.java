package bg.duosoft.ipas.persistence.model.nonentity;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class SimplePersonAddressResult implements Serializable {
    private Integer personNbr;
    private Integer addrNbr;
    private String personName;
    private String addressStreet;
    private String city;
    private String checkText;

    public SimplePersonAddressResult(Integer personNbr, Integer addrNbr, String personName, String addressStreet, String city) {
        this.personNbr = personNbr;
        this.addrNbr = addrNbr;
        this.personName = personName;
        this.addressStreet = addressStreet;
        this.city = city;
    }

    public SimplePersonAddressResult(Integer personNbr, Integer addrNbr, String personName, String addressStreet) {
        this.personNbr = personNbr;
        this.addrNbr = addrNbr;
        this.personName = personName;
        this.addressStreet = addressStreet;
    }
}
