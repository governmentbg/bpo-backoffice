package bg.duosoft.ipas.integration.decisiondesktop.model.drafteditor;

import lombok.*;

import java.util.Map;
import java.util.Set;

/**
 * The type Drafting templateVariables dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DraftingContextDTO {

  private String context;
  private Boolean softDeleted;
  private LanguageEnum language;
  private Set<String> tagCodes;
  private Map<String, Object> variables;

  private String treeId;
  private String letterId;
  private String creatorUsername;

}
