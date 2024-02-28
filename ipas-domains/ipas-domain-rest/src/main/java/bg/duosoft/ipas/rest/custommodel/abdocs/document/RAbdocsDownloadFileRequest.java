package bg.duosoft.ipas.rest.custommodel.abdocs.document;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RAbdocsDownloadFileRequest {
    private String uuid;
    private String fileName;
    private Integer databaseId;
}