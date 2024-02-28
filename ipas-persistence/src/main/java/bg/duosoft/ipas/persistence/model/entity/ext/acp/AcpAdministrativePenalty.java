package bg.duosoft.ipas.persistence.model.entity.ext.acp;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAcpAdministrativePenaltyPaymentStatus;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAcpAdministrativePenaltyType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "ACP_ADMINISTRATIVE_PENALTY", schema = "EXT_CORE")
@Cacheable(value = false)
public class AcpAdministrativePenalty  implements Serializable {

    @EmbeddedId
    private IpFilePK pk;

    @Column(name = "ADMINISTRATIVE_PENALTY_TYPE_ID")
    private Integer administrativePenaltyTypeId;

    @Column(name = "PAYMENTS_STATUS_ID")
    private Integer paymentStatusId;

    @ManyToOne
    @JoinColumn(name = "ADMINISTRATIVE_PENALTY_TYPE_ID", referencedColumnName = "ID",insertable = false,updatable = false)
    private CfAcpAdministrativePenaltyType penaltyType;


    @ManyToOne
    @JoinColumn(name = "PAYMENTS_STATUS_ID", referencedColumnName = "ID",insertable = false,updatable = false)
    private CfAcpAdministrativePenaltyPaymentStatus paymentStatus;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "PARTIALLY_PAID_AMOUNT")
    private BigDecimal partiallyPaidAmount;

    @Column(name = "NOTIFICATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notificationDate;

    @Column(name = "OTHER_TYPE_DESCRIPTION")
    private String otherTypeDescription;
}
