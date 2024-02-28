package bg.duosoft.ipas.integration.decisiondesktop.model.backoffice;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 11.11.2021
 * Time: 13:51
 */
@Data
@AllArgsConstructor
public class ByteArrayListVariable {
    private List<byte[]> byteArrayList;
}
