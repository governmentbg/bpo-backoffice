package bg.duosoft.ipas.integration.decisiondesktop.model.drafteditor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The enum Dossier type enum.
 */
@Getter
public enum DossierTypeEnum implements EnumKey<String> {

  /**
   * The Oppo.
   */
  OPPO("OPPO", "Opposition", TreeEnum.OPPO),

  /**
   * The Can.
   */
  CAN("CAN", "Cancellation", TreeEnum.CAN),

  /**
   * The IR.
   */
  IR("IR", "International Registration", TreeEnum.AG),

  /**
   * The EUTM.
   */
  EUTM("EUTM", "CTM", TreeEnum.AG);

  /** The key. */
  private String key;

  /** The value. */
  private String value;

  /** The main tree. */
  private TreeEnum mainTree;

  DossierTypeEnum(final String key, final String value, final TreeEnum mainTree) {
    this.key = key;
    this.value = value;
    this.mainTree = mainTree;
  }

  /**
   * Gets trees.
   *
   * @param dossierType the dossier type
   * @return the trees
   */
  public static Set<TreeEnum> getTrees(final DossierTypeEnum dossierType) {
    return Stream.of(dossierType.getMainTree()).collect(Collectors.toSet());
  }

  @Override
  @JsonValue
  public String getKey() {
    return key;
  }

  /**
   * From key tree enum.
   *
   * @param key the key
   * @return the tree enum
   */
  @JsonCreator
  public static DossierTypeEnum fromKey(final String key) {
    return EnumKey.fromKey(DossierTypeEnum.class, key);
  }

}
