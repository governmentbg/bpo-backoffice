package bg.duosoft.ipas.rest.custommodel.abdocs.document;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RAbdocsDownloadFileResponse {
    private byte[] content;
    private String type;
    private String fileName;
}