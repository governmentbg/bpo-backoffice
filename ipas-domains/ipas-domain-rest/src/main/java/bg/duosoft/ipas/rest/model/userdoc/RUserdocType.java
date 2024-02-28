package bg.duosoft.ipas.rest.model.userdoc;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.enums.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUserdocType implements Serializable {
	private String userdocType;
	private String userdocName;
	private String generateProcTyp;
	private List<RUserdocPersonRole> roles;
	private List<RUserdocPanel> panels;
	private List<String> invalidatedUserdocTypes;
	private UserdocGroup userdocGroup;
	private Boolean indInactive;
	private Boolean indChangesOwner;
	private Boolean indChangesRepres;
	private Boolean indRenewal;
	public List<RUserdocPersonRole> getRoles() {
		if (roles == null) {
			roles = new ArrayList<>();
		}
		return roles;
	}
	public List<RUserdocPanel> getPanels() {
		if (panels == null) {
			panels = new ArrayList<>();
		}
		return panels;
	}
	public List<String> getInvalidatedUserdocTypes() {
		if (invalidatedUserdocTypes == null) {
			invalidatedUserdocTypes = new ArrayList<>();
		}
		return invalidatedUserdocTypes;
	}
}

