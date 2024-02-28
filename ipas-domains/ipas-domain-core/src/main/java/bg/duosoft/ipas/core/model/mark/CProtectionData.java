package bg.duosoft.ipas.core.model.mark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CProtectionData implements Serializable {
    private static final long serialVersionUID = -4497447877453454563L;

    private List<CNiceClass> niceClassList;

}


