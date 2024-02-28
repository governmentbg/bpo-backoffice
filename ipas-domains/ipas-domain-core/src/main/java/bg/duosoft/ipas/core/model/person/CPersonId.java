package bg.duosoft.ipas.core.model.person;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CPersonId implements Serializable {
    private Integer personNbr;
    private Integer addressNbr;
}


