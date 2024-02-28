package bg.duosoft.ipas.persistence.model;

import lombok.Data;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * User: Georgi
 * Date: 25.10.2020 г.
 * Time: 22:00
 */
@Entity
@RevisionEntity(IpasRevisionListener.class)
@Data
@Table(name = "REVINFO", schema = "AUDIT")
public class IpasRevisionEntity {
    private static final long serialVersionUID = 8530213963961662300L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "REV")
    private int id;

    @RevisionTimestamp
    private Date timestamp;

    @Column(name = "user_id")
    private Integer userId;
}
