//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.03.04 at 11:05:36 AM EET 
//


package bg.duosoft.ipas.integration.tmclass.translate.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the bg.duosoft.ipas.integration.tmclass.translate.model package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Transaction_QNAME = new QName("http://oami.europa.eu/trademark/goods-services/term-translation", "Transaction");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: bg.duosoft.ipas.integration.tmclass.translate.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TransactionType }
     * 
     */
    public TransactionType createTransactionType() {
        return new TransactionType();
    }

    /**
     * Create an instance of {@link TermTranslationResponseDetailsType }
     * 
     */
    public TermTranslationResponseDetailsType createTermTranslationResponseDetailsType() {
        return new TermTranslationResponseDetailsType();
    }

    /**
     * Create an instance of {@link SubStringPositionType }
     * 
     */
    public SubStringPositionType createSubStringPositionType() {
        return new SubStringPositionType();
    }

    /**
     * Create an instance of {@link MatchedTermType }
     * 
     */
    public MatchedTermType createMatchedTermType() {
        return new MatchedTermType();
    }

    /**
     * Create an instance of {@link TransactionBodyType }
     * 
     */
    public TransactionBodyType createTransactionBodyType() {
        return new TransactionBodyType();
    }

    /**
     * Create an instance of {@link TermTranslationType }
     * 
     */
    public TermTranslationType createTermTranslationType() {
        return new TermTranslationType();
    }

    /**
     * Create an instance of {@link TermTranslationRequestType }
     * 
     */
    public TermTranslationRequestType createTermTranslationRequestType() {
        return new TermTranslationRequestType();
    }

    /**
     * Create an instance of {@link MatchedTermsType }
     * 
     */
    public MatchedTermsType createMatchedTermsType() {
        return new MatchedTermsType();
    }

    /**
     * Create an instance of {@link TextType }
     * 
     */
    public TextType createTextType() {
        return new TextType();
    }

    /**
     * Create an instance of {@link TransactionContentDetailsType }
     * 
     */
    public TransactionContentDetailsType createTransactionContentDetailsType() {
        return new TransactionContentDetailsType();
    }

    /**
     * Create an instance of {@link TransactionDataType }
     * 
     */
    public TransactionDataType createTransactionDataType() {
        return new TransactionDataType();
    }

    /**
     * Create an instance of {@link TranslatedTermType }
     * 
     */
    public TranslatedTermType createTranslatedTermType() {
        return new TranslatedTermType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransactionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://oami.europa.eu/trademark/goods-services/term-translation", name = "Transaction")
    public JAXBElement<TransactionType> createTransaction(TransactionType value) {
        return new JAXBElement<TransactionType>(_Transaction_QNAME, TransactionType.class, null, value);
    }

}