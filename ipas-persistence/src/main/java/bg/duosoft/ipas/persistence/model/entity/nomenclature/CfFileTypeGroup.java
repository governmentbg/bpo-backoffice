package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: ggeorgiev
 * Date: 14.02.2022
 * Time: 11:02
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_FILE_TYPE_GROUPS", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfFileTypeGroup implements Serializable {
    @Id
    @Column(name = "GROUP_CODE")
    private String groupCode;

    @Column(name = "GROUP_NAME")
    private String groupName;

    @Column(name = "FILE_TYPES")
    private String fileTypes;

    @Column(name = "USERDOC_FLAG")
    private String userdocFlag;
}
