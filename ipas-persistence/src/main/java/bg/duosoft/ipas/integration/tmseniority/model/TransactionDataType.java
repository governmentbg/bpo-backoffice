//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.07 at 02:28:02 PM EET 
//


package bg.duosoft.ipas.integration.tmseniority.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for TransactionDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransactionDataType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SeniorityDetails" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Seniority" type="{http://www.duosoft.bg/EUTD/Seniority}SeniorityType" maxOccurs="unbounded"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionDataType", propOrder = {
    "seniorityDetails"
})
public class TransactionDataType {

    @XmlElement(name = "SeniorityDetails")
    protected SeniorityDetails seniorityDetails;

    /**
     * Gets the value of the seniorityDetails property.
     * 
     * @return
     *     possible object is
     *     {@link SeniorityDetails }
     *     
     */
    public SeniorityDetails getSeniorityDetails() {
        return seniorityDetails;
    }

    /**
     * Sets the value of the seniorityDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link SeniorityDetails }
     *     
     */
    public void setSeniorityDetails(SeniorityDetails value) {
        this.seniorityDetails = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Seniority" type="{http://www.duosoft.bg/EUTD/Seniority}SeniorityType" maxOccurs="unbounded"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "seniority"
    })
    public static class SeniorityDetails {

        @XmlElement(name = "Seniority", required = true)
        protected List<SeniorityType> seniority;

        /**
         * Gets the value of the seniority property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the seniority property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSeniority().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SeniorityType }
         * 
         * 
         */
        public List<SeniorityType> getSeniority() {
            if (seniority == null) {
                seniority = new ArrayList<SeniorityType>();
            }
            return this.seniority;
        }

    }

}
