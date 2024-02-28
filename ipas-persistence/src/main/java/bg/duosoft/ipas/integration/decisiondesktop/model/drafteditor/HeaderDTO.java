package bg.duosoft.ipas.integration.decisiondesktop.model.drafteditor;

import lombok.*;

import java.util.List;

/**
 * The type Header dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeaderDTO {

  private String title;
  private String subTitle;
  private String quickLinksLabel;
  private List<LinkDTO> mainButtons;
  private List<LinkDTO> quickLinks;
  private String editDraftingOptionsCbk;

}
