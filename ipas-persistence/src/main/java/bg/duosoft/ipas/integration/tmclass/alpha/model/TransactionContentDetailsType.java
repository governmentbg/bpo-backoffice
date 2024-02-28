//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.04.13 at 06:59:50 PM EEST 
//


package bg.duosoft.ipas.integration.tmclass.alpha.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransactionContentDetailsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransactionContentDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TransactionIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TransactionCode" type="{http://www.oami.europa.eu/CTM/CTM-GoodsServices}EM_CTM-GS_TransactionCodeType"/>
 *         &lt;element name="TransactionData" type="{http://www.oami.europa.eu/CTM/CTM-GoodsServices}TransactionDataType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionContentDetailsType", propOrder = {
    "transactionIdentifier",
    "transactionCode",
    "transactionData"
})
public class TransactionContentDetailsType {

    @XmlElement(name = "TransactionIdentifier")
    protected String transactionIdentifier;
    @XmlElement(name = "TransactionCode", required = true)
    @XmlSchemaType(name = "token")
    protected EMCTMGSTransactionCodeType transactionCode;
    @XmlElement(name = "TransactionData", required = true)
    protected TransactionDataType transactionData;

    /**
     * Gets the value of the transactionIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionIdentifier() {
        return transactionIdentifier;
    }

    /**
     * Sets the value of the transactionIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionIdentifier(String value) {
        this.transactionIdentifier = value;
    }

    /**
     * Gets the value of the transactionCode property.
     * 
     * @return
     *     possible object is
     *     {@link EMCTMGSTransactionCodeType }
     *     
     */
    public EMCTMGSTransactionCodeType getTransactionCode() {
        return transactionCode;
    }

    /**
     * Sets the value of the transactionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link EMCTMGSTransactionCodeType }
     *     
     */
    public void setTransactionCode(EMCTMGSTransactionCodeType value) {
        this.transactionCode = value;
    }

    /**
     * Gets the value of the transactionData property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionDataType }
     *     
     */
    public TransactionDataType getTransactionData() {
        return transactionData;
    }

    /**
     * Sets the value of the transactionData property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionDataType }
     *     
     */
    public void setTransactionData(TransactionDataType value) {
        this.transactionData = value;
    }

}