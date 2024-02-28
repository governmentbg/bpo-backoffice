package bg.duosoft.ipas.persistence.repository.entity.ext;

import bg.duosoft.ipas.persistence.model.entity.ext.core.IpOffidocAbdocsDocument;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidocPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface IpOffidocAbdocsDocumentRepository extends BaseRepository<IpOffidocAbdocsDocument, IpOffidocPK> {

    IpOffidocAbdocsDocument findByAbdocsDocumentId(Integer abdocsDocumentId);

    IpOffidocAbdocsDocument findByRegistrationNumber(String registrationNumber);

    @Modifying
    @Query(value = "UPDATE IpOffidocAbdocsDocument u SET u.registrationNumber = ?1 WHERE u.abdocsDocumentId = ?2")
    void updateRegistrationNumber(String registrationNumber, Integer abdocsDocumentId);

    @Modifying
    @Query(value = "UPDATE IpOffidocAbdocsDocument u SET u.notificationFinishedDate = ?1 WHERE u.abdocsDocumentId = ?2")
    void updateNotificationFinishedDate(Date notificationFinishedDate, Integer abdocsDocumentId);

    @Modifying
    @Query(value = "UPDATE IpOffidocAbdocsDocument u SET u.emailNotificationReadDate = ?1 WHERE u.abdocsDocumentId = ?2")
    void updateEmailNotificationReadDate(Date date, Integer abdocsDocumentId);

    @Modifying
    @Query(value = "UPDATE IpOffidocAbdocsDocument u SET u.portalNotificationReadDate = ?1 WHERE u.abdocsDocumentId = ?2 AND (u.portalNotificationReadDate is null or u.portalNotificationReadDate < ?1)")
    void updatePortalNotificationReadDate(Date date, Integer abdocsDocumentId);

}
