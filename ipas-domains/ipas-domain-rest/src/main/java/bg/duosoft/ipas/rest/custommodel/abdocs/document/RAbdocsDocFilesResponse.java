package bg.duosoft.ipas.rest.custommodel.abdocs.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RAbdocsDocFilesResponse {
    private String documentStatus;
    private List<RAbdocsDocFile> rAbdocsDocFiles;
}
