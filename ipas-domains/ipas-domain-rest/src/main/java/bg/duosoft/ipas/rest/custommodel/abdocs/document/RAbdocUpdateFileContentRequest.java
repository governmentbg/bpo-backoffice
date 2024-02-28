package bg.duosoft.ipas.rest.custommodel.abdocs.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RAbdocUpdateFileContentRequest {
    private String registrationNumber;
    private String fileName;
    private byte[] newContent;
    private String description;

    public RAbdocUpdateFileContentRequest(String registrationNumber, String fileName, byte[] newContent) {
        this.registrationNumber = registrationNumber;
        this.fileName = fileName;
        this.newContent = newContent;
    }
}
