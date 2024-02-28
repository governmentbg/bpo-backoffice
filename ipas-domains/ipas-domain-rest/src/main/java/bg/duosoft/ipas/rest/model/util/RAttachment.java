package bg.duosoft.ipas.rest.model.util;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RAttachment implements Serializable {
	private String fileName;
	private byte[] data;
	private String description;

	public RAttachment(String fileName, byte[] data) {
		this.fileName = fileName;
		this.data = data;
	}
}

