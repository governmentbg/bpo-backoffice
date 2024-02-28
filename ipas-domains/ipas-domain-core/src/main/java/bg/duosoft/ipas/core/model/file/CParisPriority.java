package bg.duosoft.ipas.core.model.file;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.util.Date;


@Data
public class CParisPriority
        implements Serializable {
    private static final long serialVersionUID = -1926907466571513102L;
    @NotNull
    private String countryCode;
    @NotNull
    private String applicationId;
    @NotNull
    @PastOrPresent
    private Date priorityDate;
    private String notes;
    private Integer priorityStatus;
}


