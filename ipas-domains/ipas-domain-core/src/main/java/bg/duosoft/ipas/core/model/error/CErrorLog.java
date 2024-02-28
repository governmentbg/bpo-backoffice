package bg.duosoft.ipas.core.model.error;

import bg.duosoft.ipas.enums.ErrorLogAbout;
import bg.duosoft.ipas.enums.ErrorLogPriority;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CErrorLog implements Serializable {
    private Integer id;
    private String about;
    private String action;
    private String errorMessage;
    private String username;
    private Date dateCreated;
    private String customMessage;
    private Boolean needManualFix;
    private String instruction;
    private Date dateResolved;
    private String priority;
    private String usernameResolved;
    private String commentResolved;

    public static CErrorLog create(ErrorLogAbout about, String action, String errorMessage, String customMessage, boolean needManualFix, String instruction, ErrorLogPriority priority, String username) {
        return new CErrorLog(null, about.getValue(), action, errorMessage, username, new Date(), customMessage, needManualFix, instruction, null, priority.getValue(), null, null);
    }

}
