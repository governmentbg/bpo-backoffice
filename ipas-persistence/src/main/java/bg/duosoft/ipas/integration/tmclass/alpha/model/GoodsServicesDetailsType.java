//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.04.13 at 06:59:50 PM EEST 
//


package bg.duosoft.ipas.integration.tmclass.alpha.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GoodsServicesDetailsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GoodsServicesDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GoodServices" type="{http://www.oami.europa.eu/CTM/CTM-GoodsServices}GoodsServicesType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GoodsServicesDetailsType", propOrder = {
    "goodServices"
})
public class GoodsServicesDetailsType {

    @XmlElement(name = "GoodServices", required = true)
    protected List<GoodsServicesType> goodServices;

    /**
     * Gets the value of the goodServices property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the goodServices property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGoodServices().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GoodsServicesType }
     * 
     * 
     */
    public List<GoodsServicesType> getGoodServices() {
        if (goodServices == null) {
            goodServices = new ArrayList<GoodsServicesType>();
        }
        return this.goodServices;
    }

}
