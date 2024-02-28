package bg.duosoft.ipas.rest.custommodel.abdocs.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RAbdocsDocumentContentRequest {
    private String fileName;
    private String registrationNumber;
    private Integer abdocsId;
}
