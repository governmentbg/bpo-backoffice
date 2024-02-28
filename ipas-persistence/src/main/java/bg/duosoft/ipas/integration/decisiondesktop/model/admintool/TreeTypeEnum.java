/*******************************************************************************
 * $Id:: TreeTypeEnum.java 2021/03/11 01:02
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
 * The enum Tree type enum.
 */
@Getter
public enum TreeTypeEnum implements EnumKey<Integer> {

  /** Checkbox tree type enum. */
  CHECKBOX(0, "Checkbox"),

  /** Drag n drop tree type enum. */
  DRAG_N_DROP(1, "Drag&Drop");

  /** The key. */
  private Integer key;

  /** The value. */
  private String value;

  TreeTypeEnum(final Integer key, final String value) {
    this.key = key;
    this.value = value;
  }

  @Override
  @JsonValue
  public Integer getKey() {
    return key;
  }

  /**
   * From key tree enum.
   *
   * @param key the key
   * @return the tree enum
   */
  @JsonCreator
  public static TreeTypeEnum fromKey(final Integer key) {
    return EnumKey.fromKey(TreeTypeEnum.class, key);
  }

}

