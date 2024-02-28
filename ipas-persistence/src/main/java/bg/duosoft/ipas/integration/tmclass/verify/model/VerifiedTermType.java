//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.02.14 at 01:17:22 PM EET 
//


package bg.duosoft.ipas.integration.tmclass.verify.model;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for VerifiedTermType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VerifiedTermType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Text" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TextPositionInInputTermList" type="{http://oami.europa.eu/trademark/goods-services/term-verification}SubStringPositionType" minOccurs="0"/&gt;
 *         &lt;element name="VerificationAssessment" type="{http://oami.europa.eu/trademark/goods-services/term-verification}VerifiedTermAssessmentType"/&gt;
 *         &lt;element name="VerificationResult" type="{http://oami.europa.eu/trademark/goods-services/term-verification}VerifiedTermResultType"/&gt;
 *         &lt;element name="MatchedTerms" type="{http://oami.europa.eu/trademark/goods-services/term-verification}MatchedTermsType" minOccurs="0"/&gt;
 *         &lt;element name="Taxonomy" type="{http://oami.europa.eu/trademark/goods-services/term-verification}TaxonomyType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerifiedTermType", propOrder = {
    "text",
    "textPositionInInputTermList",
    "verificationAssessment",
    "verificationResult",
    "matchedTerms",
    "taxonomy"
})
public class VerifiedTermType {

    @XmlElement(name = "Text", required = true)
    protected String text;
    @XmlElement(name = "TextPositionInInputTermList")
    protected SubStringPositionType textPositionInInputTermList;
    @XmlElement(name = "VerificationAssessment", required = true)
    @XmlSchemaType(name = "token")
    protected VerifiedTermAssessmentType verificationAssessment;
    @XmlElement(name = "VerificationResult", required = true)
    @XmlSchemaType(name = "token")
    protected VerifiedTermResultType verificationResult;
    @XmlElement(name = "MatchedTerms")
    protected MatchedTermsType matchedTerms;
    @XmlElement(name = "Taxonomy")
    protected TaxonomyType taxonomy;

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the textPositionInInputTermList property.
     * 
     * @return
     *     possible object is
     *     {@link SubStringPositionType }
     *     
     */
    public SubStringPositionType getTextPositionInInputTermList() {
        return textPositionInInputTermList;
    }

    /**
     * Sets the value of the textPositionInInputTermList property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubStringPositionType }
     *     
     */
    public void setTextPositionInInputTermList(SubStringPositionType value) {
        this.textPositionInInputTermList = value;
    }

    /**
     * Gets the value of the verificationAssessment property.
     * 
     * @return
     *     possible object is
     *     {@link VerifiedTermAssessmentType }
     *     
     */
    public VerifiedTermAssessmentType getVerificationAssessment() {
        return verificationAssessment;
    }

    /**
     * Sets the value of the verificationAssessment property.
     * 
     * @param value
     *     allowed object is
     *     {@link VerifiedTermAssessmentType }
     *     
     */
    public void setVerificationAssessment(VerifiedTermAssessmentType value) {
        this.verificationAssessment = value;
    }

    /**
     * Gets the value of the verificationResult property.
     * 
     * @return
     *     possible object is
     *     {@link VerifiedTermResultType }
     *     
     */
    public VerifiedTermResultType getVerificationResult() {
        return verificationResult;
    }

    /**
     * Sets the value of the verificationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link VerifiedTermResultType }
     *     
     */
    public void setVerificationResult(VerifiedTermResultType value) {
        this.verificationResult = value;
    }

    /**
     * Gets the value of the matchedTerms property.
     * 
     * @return
     *     possible object is
     *     {@link MatchedTermsType }
     *     
     */
    public MatchedTermsType getMatchedTerms() {
        return matchedTerms;
    }

    /**
     * Sets the value of the matchedTerms property.
     * 
     * @param value
     *     allowed object is
     *     {@link MatchedTermsType }
     *     
     */
    public void setMatchedTerms(MatchedTermsType value) {
        this.matchedTerms = value;
    }

    /**
     * Gets the value of the taxonomy property.
     * 
     * @return
     *     possible object is
     *     {@link TaxonomyType }
     *     
     */
    public TaxonomyType getTaxonomy() {
        return taxonomy;
    }

    /**
     * Sets the value of the taxonomy property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaxonomyType }
     *     
     */
    public void setTaxonomy(TaxonomyType value) {
        this.taxonomy = value;
    }

}
