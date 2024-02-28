package bg.duosoft.ipas.rest.custommodel.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: Georgi
 * Date: 21.7.2020 г.
 * Time: 15:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RGetPersonRequest {
    private int personNbr;
    private int addressNbr;
}
