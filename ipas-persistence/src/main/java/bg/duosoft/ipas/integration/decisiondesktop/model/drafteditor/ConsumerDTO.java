package bg.duosoft.ipas.integration.decisiondesktop.model.drafteditor;

import lombok.*;

/**
 * The type Consumer dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumerDTO {

  private String protocol;
  private Integer port;
  private String host;
  private String baseResourcePath;

}
