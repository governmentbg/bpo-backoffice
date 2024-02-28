/*******************************************************************************
 * $Id:: EnumKey.java 2021/03/11 01:02
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

import java.util.Arrays;

/**
 * The interface {@link EnumKey} should be implemented by every enum that use the "getKey" and
 * "fromKey" methods.
 *
 * @param <K> the key type
 */
public interface EnumKey<K> {

  /**
   * Gets the key.
   *
   * @return the key
   */
  K getKey();

  /**
   * From key.
   *
   * @param <E> the enum type
   * @param <K> the key type
   * @param clazz the enum class
   * @param key the key
   * @return the enum value
   */
  static <E extends Enum<E> & EnumKey<K>, K> E fromKey(final Class<E> clazz, final K key) {
    return Arrays.stream(clazz.getEnumConstants()).filter(member -> member.getKey().equals(key)).findFirst()
        .orElseThrow(() -> new RuntimeException(
            String.format("Key [%s] does not exist in enum [%s]", key.toString(), clazz.getName())));
  }
}
