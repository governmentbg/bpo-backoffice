package bg.duosoft.ipas.core.model.person;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CAuthor implements Serializable {

    private static final long serialVersionUID = -9163386660156044695L;
    private Long authorSeq;
    private CPerson person;
}
