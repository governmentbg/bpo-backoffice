package bg.duosoft.ipas.persistence.model.entity.person;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfGeoCountry;
import bg.duosoft.ipas.util.search.IpPersonAddressPkBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

/*@NamedEntityGraph(
        name = "person-addresses-entity-graph",
        attributeNodes = {
                @NamedAttributeNode(value = "ipPerson", subgraph = "person-subgraph"),
                @NamedAttributeNode("residenceCountry")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "person-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("ipAgent"),
                                @NamedAttributeNode("extendedPartnership")
                        }
                )
        }
)*/
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Indexed
@Entity
@Table(name = "IP_PERSON_ADDRESSES", schema = "IPASPROD")
@Cacheable(value = false)
@Audited
@AuditTable(value = "IP_PERSON_ADDRESSES", schema = "AUDIT")
public class IpPersonAddresses implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @IndexedEmbedded
    @FieldBridge(impl = IpPersonAddressPkBridge.class)
    private IpPersonAddressesPK pk;

    @Column(name = "ADDR_STREET")
    @Field(analyze = Analyze.YES, index = Index.YES, store = Store.YES)
    @Field(name = "addrStreetExact", analyze = Analyze.NO, index = Index.YES, store = Store.YES, norms = Norms.NO)
    private String addrStreet;

    @Column(name = "ADDR_ZONE")
    @Field(analyze = Analyze.YES, index = Index.YES, store = Store.YES)
    private String addrZone;

    @Column(name = "CITY_NAME")
    @Field(analyze = Analyze.YES, index = Index.YES, store = Store.YES)
    @Field(name = "cityNameExact", analyze = Analyze.YES, index = Index.YES, store = Store.YES, norms = Norms.NO)
    private String cityName;

    @Column(name = "CITY_CODE")
    private String cityCode;

    @Column(name = "STATE_CODE")
    private String stateCode;

    @Column(name = "STATE_NAME")
    private String stateName;

    @Column(name = "ZIPCODE")
    @Field(name = "zipcodeExact", analyze = Analyze.NO, index = Index.YES, store = Store.YES, norms = Norms.NO)
    @Field(analyze = Analyze.NO, index = Index.YES, store = Store.YES)
    private String zipcode;

    @Column(name = "ADDR_STREET_LANG2")
    private String addrStreetLang2;

    @ManyToOne
    @JoinColumn(name = "RESIDENCE_COUNTRY_CODE", referencedColumnName = "COUNTRY_CODE")
    @IndexedEmbedded
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private CfGeoCountry residenceCountry;

    @ManyToOne
    @MapsId(value = "personNbr")
    @JoinColumn(name = "PERSON_NBR", referencedColumnName = "PERSON_NBR")
    @IndexedEmbedded
    private IpPerson ipPerson;

    @Transient
    private Integer tempParentPersonNbr;

}
