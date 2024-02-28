/*******************************************************************************
 * $Id:: TreeDTO.java 2021/03/11 01:02
 *
 *        . * .
 *      * RRRR  *   Copyright (c) 2012-2021 EUIPO: European Intelectual
 *     .  RR  R  .  Property Organization (trademarks and designs).
 *     *  RRR    *
 *      . RR RR .   ALL RIGHTS RESERVED
 *       *. _ .*
 *
 *  The use and distribution of this software is under the restrictions exposed in 'license.txt'
 *
 ******************************************************************************/

package bg.duosoft.ipas.integration.decisiondesktop.model.admintool;

import lombok.*;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * The type Tree dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreeDTO extends Auditable {

  @Id
  private String id;
  @NotNull
  private String title;
  @NotNull
  private TreeTypeEnum type;
  @NotNull
  private StatusEnum status;
  private Set<DossierTypeDTO> dossierTypes;
}

