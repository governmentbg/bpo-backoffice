/*******************************************************************************
 * $Id:: DossierTypeDTO.java 2021/03/11 01:02
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

import java.util.List;

/**
 * The type Dossier Type dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DossierTypeDTO {

  // the dossier type name
  private String name;

  // the letter categories associated with the dossier type
  private List<String> letterCategories;
}
