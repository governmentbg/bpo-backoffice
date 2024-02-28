/*******************************************************************************
 * $Id:: StatusEnum.java 2021/03/11 01:02
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * The enum Status enum.
 */
@Getter
public enum StatusEnum implements EnumKey<Integer> {

  /** Draft status enum. */
  DRAFT(0, "Draft"),

  /** Published status enum. */
  PUBLISHED(1, "Published"),

  /** Edited status enum. */
  EDITED(2, "Edited");

  private Integer key;
  private String value;

  StatusEnum(Integer key, String value) {
    this.key = key;
    this.value = value;
  }

  @Override
  @JsonValue
  public Integer getKey() {
    return key;
  }

  /**
   * From key status enum.
   *
   * @param key the key
   * @return the status enum
   */
  @JsonCreator
  public static StatusEnum fromKey(final Integer key) {
    return EnumKey.fromKey(StatusEnum.class, key);
  }

}

