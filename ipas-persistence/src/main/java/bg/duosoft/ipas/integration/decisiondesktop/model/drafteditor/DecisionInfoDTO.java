/*******************************************************************************
 * $Id:: DecisionInfoDTO.java 2020/09/21 12:15
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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * The type Decision info dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DecisionInfoDTO {

  private Integer decisionKind;
  private String rapporteur;
  private String firstCosigner;
  private String secondCosigner;
  private Boolean boaDecisionFlag;
  private List<ClassificationDTO> classifications;
  private List<Long> grounds;
  private Boolean art7Flag;
  private String decisionLetter;

}
