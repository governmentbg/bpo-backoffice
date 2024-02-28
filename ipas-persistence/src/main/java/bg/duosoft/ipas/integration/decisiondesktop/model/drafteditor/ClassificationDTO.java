/*******************************************************************************
 * $Id:: ClassificationDTO.java 2020/09/21 12:15
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

/**
 * The type Classification dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationDTO {

  private Long classId;
  private String terms;

}
