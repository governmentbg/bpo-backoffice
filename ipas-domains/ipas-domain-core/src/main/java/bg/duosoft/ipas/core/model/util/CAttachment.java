package bg.duosoft.ipas.core.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CAttachment implements Serializable {
    private String fileName;
    private byte[] data;
    private String description;
}
