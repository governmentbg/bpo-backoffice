//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.02.14 at 01:17:22 PM EET 
//


package bg.duosoft.ipas.integration.tmclass.verify.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MatchProximityType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MatchProximityType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="Term match"/&gt;
 *     &lt;enumeration value="Linguistic match"/&gt;
 *     &lt;enumeration value="Match to verify"/&gt;
 *     &lt;enumeration value="No match"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MatchProximityType")
@XmlEnum
public enum MatchProximityType {

    @XmlEnumValue("Term match")
    TERM_MATCH("Term match"),
    @XmlEnumValue("Linguistic match")
    LINGUISTIC_MATCH("Linguistic match"),
    @XmlEnumValue("Match to verify")
    MATCH_TO_VERIFY("Match to verify"),
    @XmlEnumValue("No match")
    NO_MATCH("No match");
    private final String value;

    MatchProximityType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MatchProximityType fromValue(String v) {
        for (MatchProximityType c: MatchProximityType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
