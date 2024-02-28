package bg.duosoft.ipas.core.model.patent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CClaim implements Serializable {

    private static final long serialVersionUID = -5594817380781513813L;
    private Long claimNbr;
    private String claimDescription;
    private String claimEnglishDescription;
    
    
    
}
