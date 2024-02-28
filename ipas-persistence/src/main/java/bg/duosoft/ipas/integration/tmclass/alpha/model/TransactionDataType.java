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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransactionDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransactionDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NiceClassHeadingsRequest" type="{http://www.oami.europa.eu/CTM/CTM-GoodsServices}NiceClassHeadingsRequestType" minOccurs="0"/>
 *         &lt;element name="GoodsServicesDetails" type="{http://www.oami.europa.eu/CTM/CTM-GoodsServices}GoodsServicesDetailsType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionDataType", propOrder = {
    "niceClassHeadingsRequest",
    "goodsServicesDetails"
})
public class TransactionDataType {

    @XmlElement(name = "NiceClassHeadingsRequest")
    protected NiceClassHeadingsRequestType niceClassHeadingsRequest;
    @XmlElement(name = "GoodsServicesDetails", required = true)
    protected GoodsServicesDetailsType goodsServicesDetails;

    /**
     * Gets the value of the niceClassHeadingsRequest property.
     * 
     * @return
     *     possible object is
     *     {@link NiceClassHeadingsRequestType }
     *     
     */
    public NiceClassHeadingsRequestType getNiceClassHeadingsRequest() {
        return niceClassHeadingsRequest;
    }

    /**
     * Sets the value of the niceClassHeadingsRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link NiceClassHeadingsRequestType }
     *     
     */
    public void setNiceClassHeadingsRequest(NiceClassHeadingsRequestType value) {
        this.niceClassHeadingsRequest = value;
    }

    /**
     * Gets the value of the goodsServicesDetails property.
     * 
     * @return
     *     possible object is
     *     {@link GoodsServicesDetailsType }
     *     
     */
    public GoodsServicesDetailsType getGoodsServicesDetails() {
        return goodsServicesDetails;
    }

    /**
     * Sets the value of the goodsServicesDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link GoodsServicesDetailsType }
     *     
     */
    public void setGoodsServicesDetails(GoodsServicesDetailsType value) {
        this.goodsServicesDetails = value;
    }

}