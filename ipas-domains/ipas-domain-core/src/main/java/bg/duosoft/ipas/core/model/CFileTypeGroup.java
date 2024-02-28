package bg.duosoft.ipas.core.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 15.02.2022
 * Time: 11:05
 */
@Data
public class CFileTypeGroup implements Serializable {
    private String groupCode;
    private String groupName;
    private List<String> fileTypes;
    private boolean userdocFlag;
}
