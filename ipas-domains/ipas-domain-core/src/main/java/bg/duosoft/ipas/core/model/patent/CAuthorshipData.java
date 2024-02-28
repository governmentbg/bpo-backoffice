package bg.duosoft.ipas.core.model.patent;



import bg.duosoft.ipas.core.model.person.CAuthor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CAuthorshipData implements Serializable {

    private static final long serialVersionUID = -2315632303602877269L;
    private Boolean indOwnerSameAuthor;
    private List<CAuthor> authorList;
}
