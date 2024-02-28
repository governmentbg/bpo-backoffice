package bg.duosoft.ipas.core.model.userdoc.reviewers;

import bg.duosoft.ipas.core.model.person.CUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CUserdocReviewer implements Serializable {
    private CUser user;
    private boolean main;
}
