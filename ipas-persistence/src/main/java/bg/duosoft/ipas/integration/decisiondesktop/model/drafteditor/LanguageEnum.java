package bg.duosoft.ipas.integration.decisiondesktop.model.drafteditor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import java.io.Serializable;
import java.util.stream.Stream;

/**
 * The enum Language enum.
 */
@Getter
@XmlEnum
public enum LanguageEnum implements EnumKey<String>, Serializable {

  /**
   * En language enum.
   */
  @XmlEnumValue("en")
  EN("en", "English"),
  /**
   * Bg language enum.
   */
  @XmlEnumValue("bg")
  BG("bg", "Bulgarian"),
  /**
   * Hr language enum.
   */
  @XmlEnumValue("hr")
  HR("hr", "Croatian"),
  /**
   * Cs language enum.
   */
  @XmlEnumValue("cs")
  CS("cs", "Czech"),
  /**
   * Da language enum.
   */
  @XmlEnumValue("da")
  DA("da", "Danish"),
  /**
   * Nl language enum.
   */
  @XmlEnumValue("nl")
  NL("nl", "Dutch"),
  /**
   * Et language enum.
   */
  @XmlEnumValue("et")
  ET("et", "Estonian"),
  /**
   * Fi language enum.
   */
  @XmlEnumValue("fi")
  FI("fi", "Finnish"),
  /**
   * Fr language enum.
   */
  @XmlEnumValue("fr")
  FR("fr", "French"),
  /**
   * De language enum.
   */
  @XmlEnumValue("de")
  DE("de", "German"),
  /**
   * El language enum.
   */
  @XmlEnumValue("el")
  EL("el", "Greek"),
  /**
   * Hu language enum.
   */
  @XmlEnumValue("hu")
  HU("hu", "Hungarian"),
  /**
   * It language enum.
   */
  @XmlEnumValue("it")
  IT("it", "Italian"),
  /**
   * Lv language enum.
   */
  @XmlEnumValue("lv")
  LV("lv", "Latvian"),
  /**
   * Lt language enum.
   */
  @XmlEnumValue("lt")
  LT("lt", "Lithuanian"),
  /**
   * Mt language enum.
   */
  @XmlEnumValue("mt")
  MT("mt", "Maltese"),
  /**
   * Pl language enum.
   */
  @XmlEnumValue("pl")
  PL("pl", "Polish"),
  /**
   * Pt language enum.
   */
  @XmlEnumValue("pt")
  PT("pt", "Portuguese"),
  /**
   * Ro language enum.
   */
  @XmlEnumValue("ro")
  RO("ro", "Romanian"),
  /**
   * Sk language enum.
   */
  @XmlEnumValue("sk")
  SK("sk", "Slovak"),
  /**
   * Sl language enum.
   */
  @XmlEnumValue("sl")
  SL("sl", "Slovenian"),
  /**
   * Es language enum.
   */
  @XmlEnumValue("es")
  ES("es", "Spanish"),
  /**
   * Sv language enum.
   */
  @XmlEnumValue("sv")
  SV("sv", "Swedish");

  public static final long serialVersionUID = -1255654126688L;

  /** The key. */
  private String key;

  /** The value. */
  private String value;

  /**
   * Instantiates a new enum.
   *
   * @param key the key
   * @param value the value
   */
  LanguageEnum(final String key, final String value) {
    this.key = key;
    this.value = value;
  }

  @Override
  @JsonValue
  public String getKey() {
    return key;
  }

  /**
   * From key language enum.
   *
   * @param key the key
   * @return the language enum
   */
  @JsonCreator
  public static LanguageEnum fromKey(final String key) {
    return EnumKey.fromKey(LanguageEnum.class, key);
  }

  /**
   * Gets Limited Languages.
   *
   * @return array of Limited Languages
   */
  public static LanguageEnum[] getLimitedLanguages() {
    return Stream.of(LanguageEnum.EN, LanguageEnum.FR, LanguageEnum.DE, LanguageEnum.IT, LanguageEnum.ES).toArray(
      LanguageEnum[]::new);
  }
}

