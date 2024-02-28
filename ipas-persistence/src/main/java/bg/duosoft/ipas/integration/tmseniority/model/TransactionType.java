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
 * <p>Java class for TransactionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransactionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TransactionHeader" type="{http://www.duosoft.bg/EUTD/Seniority}TransactionHeaderType" minOccurs="0"/&gt;
 *         &lt;element name="TradeMarkTransactionBody" type="{http://www.duosoft.bg/EUTD/Seniority}TransactionBodyType" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "tradeMarkTransactionBody"
})
public class TransactionType {

    @XmlElement(name = "TransactionHeader")
    protected TransactionHeaderType transactionHeader;
    @XmlElement(name = "TradeMarkTransactionBody")
    protected List<TransactionBodyType> tradeMarkTransactionBody;

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
     * Gets the value of the tradeMarkTransactionBody property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tradeMarkTransactionBody property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTradeMarkTransactionBody().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TransactionBodyType }
     * 
     * 
     */
    public List<TransactionBodyType> getTradeMarkTransactionBody() {
        if (tradeMarkTransactionBody == null) {
            tradeMarkTransactionBody = new ArrayList<TransactionBodyType>();
        }
        return this.tradeMarkTransactionBody;
    }

}