package bg.duosoft.ipas.integration.decisiondesktop.model.drafteditor;

import lombok.*;

/**
 * The type DocPublisher dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocPublisherDTO {

  private LanguageEnum language;
  private String coverHeader;
  private String defaultHeaderText;
  private String defaultHeaderId;
  private String codeHeader;
  private String pageDefaultHeader;
  private String ofDefaultHeader;
  private Boolean footer;
  private String topFooter;
  private String bottomFooter;
  private String defaultFooterText;
  private String hyperlinkText;
  private String hyperlinkUrl;

}
