package bg.duosoft.ipas.integration.decisiondesktop.model.drafteditor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * The enum Tree enum.
 */
@Getter
public enum TreeEnum implements EnumKey<String> {

  /** Ag tree enum. */
  AG("AG", "AG", LanguageEnum.values()),

  /** Opposition tree enum. */
  OPPO("OPPO", "Opposition", LanguageEnum.getLimitedLanguages()),

  /** Cancellation tree enum. */
  CAN("CAN", "Cancellation", LanguageEnum.getLimitedLanguages()),

  SPBO("SPBO", "SPBO", LanguageEnum.values()),

  SPBOTEST1("SPBOTEST1", "SPBOTEST1", LanguageEnum.values()),

  /** For testing purposes. */
  SPBO_TEST1("SPBOTEST1", "SPBOTEST1", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_1("TEMPLATE_1", "Letter template 1", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_2("TEMPLATE_2", "Letter template 2", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_3("TEMPLATE_3", "Letter template 3", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_4("TEMPLATE_4", "Letter template 4", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_5("TEMPLATE_5", "Letter template 5", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_6("TEMPLATE_6", "Letter template 6", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_7("TEMPLATE_7", "Letter template 7", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_8("TEMPLATE_8", "Letter template 8", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_9("TEMPLATE_9", "Letter template 9", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_10("TEMPLATE_10", "Letter template 10", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_11("TEMPLATE_11", "Letter template 11", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_12("TEMPLATE_12", "Letter template 12", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_13("TEMPLATE_13", "Letter template 13", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_14("TEMPLATE_14", "Letter template 14", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_15("TEMPLATE_15", "Letter template 15", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_16("TEMPLATE_16", "Letter template 16", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_17("TEMPLATE_17", "Letter template 17", LanguageEnum.values()),

  /** For testing purposes. */
  TEMPLATE_18("TEMPLATE_18", "Letter template 18", LanguageEnum.values());

  /** The key. */
  private String key;

  /** The value. */
  private String value;

  /** The languages. */
  private LanguageEnum[] languages;

  /**
   * Instantiates a new enum.
   *
   * @param key       the key
   * @param value     the value
   * @param languages the languages
   */
  TreeEnum(final String key, final String value, final LanguageEnum[] languages) {
    this.key = key;
    this.value = value;
    this.languages = languages.clone();
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
  public static TreeEnum fromKey(final String key) {
    return EnumKey.fromKey(TreeEnum.class, key);
  }

}

