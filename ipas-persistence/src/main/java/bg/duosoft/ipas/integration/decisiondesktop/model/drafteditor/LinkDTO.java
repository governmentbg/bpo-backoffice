package bg.duosoft.ipas.integration.decisiondesktop.model.drafteditor;

import lombok.*;

/**
 * The type Link dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkDTO {

  private String url;
  private String label;
}
