package com.duosoft.ipas.webmodel;

import bg.duosoft.ipas.enums.FileType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ReceptionUserdocForm {

    private String fileTypeGroup;//Main object file type group
    private String objectNumber;// IP_DOC_ID or IP_FILE_ID
    private String userdocType;

}
