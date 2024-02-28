package bg.duosoft.ipas.rest.custommodel.offidoc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ROffidocUpdateNotificationReadDateRequest {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private Date date;
    private Integer abdocsDocumentId;
}
