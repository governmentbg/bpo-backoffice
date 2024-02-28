/*******************************************************************************
 * $Id:: DraftingOptionsDTO.java 2021/03/09 01:50
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

package bg.duosoft.ipas.integration.decisiondesktop.model.drafteditor;

import lombok.*;

import java.util.Map;
import java.util.Set;

/**
 * The type Drafting options dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DraftingOptionsDTO {

  private String dossierId;
  private DossierTypeEnum dossierType;
  private LanguageEnum language;
  private Set<String> tagCodes;
  private Map<String, Object> variables;
  private HeaderDTO header;
  private ConsumerDTO consumer;

  private DecisionInfoDTO decision;
  private DocPublisherDTO docPublisher;

}
