package bg.duosoft.ipas.core.model.mark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CSound
        implements Serializable {
    private static final long serialVersionUID = 5835826797406698685L;
    private String soundType;
    private byte[] soundData;

}


