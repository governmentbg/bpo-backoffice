/*******************************************************************************
 * $Id:: TreeFilter.java 2021/03/11 01:02
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

import java.util.Set;

/**
 * The type Tree filter.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreeFilter {

  private String id;
  private String businessKey;
  private String title;

  // this will filter the trees matching either the businessKey and/or the title
  private String businessKeyOrTitle;
  private TreeTypeEnum type;
  private Set<StatusEnum> statuses;
  private Set<String> dossierTypes;
  private Set<String> letterCategories;

  private DateTimeFilter created;
  private DateTimeFilter updated;

  private User updatedBy;
}
