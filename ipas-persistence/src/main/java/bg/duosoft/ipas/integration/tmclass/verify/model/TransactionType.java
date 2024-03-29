//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.02.14 at 01:17:22 PM EET 
//


package bg.duosoft.ipas.integration.tmclass.verify.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransactionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransactionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TransactionHeader" type="{http://oami.europa.eu/trademark/goods-services/term-verification}TransactionHeaderType"/&gt;
 *         &lt;element name="TransactionBody" type="{http://oami.europa.eu/trademark/goods-services/term-verification}TransactionBodyType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionType", propOrder = {
    "transactionHeader",
    "transactionBody"
})
public class TransactionType {

    @XmlElement(name = "TransactionHeader", required = true)
    protected TransactionHeaderType transactionHeader;
    @XmlElement(name = "TransactionBody", required = true)
    protected TransactionBodyType transactionBody;

    /**
     * Gets the value of the transactionHeader property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionHeaderType }
     *     
     */
    public TransactionHeaderType getTransactionHeader() {
        return transactionHeader;
    }

    /**
     * Sets the value of the transactionHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionHeaderType }
     *     
     */
    public void setTransactionHeader(TransactionHeaderType value) {
        this.transactionHeader = value;
    }

    /**
     * Gets the value of the transactionBody property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionBodyType }
     *     
     */
    public TransactionBodyType getTransactionBody() {
        return transactionBody;
    }

    /**
     * Sets the value of the transactionBody property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionBodyType }
     *     
     */
    public void setTransactionBody(TransactionBodyType value) {
        this.transactionBody = value;
    }

}
