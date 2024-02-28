package com.duosoft.ipas.webmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatedUserdocObjectDetails {
    private boolean isUserdoc;
    private String title;
    private String filingNumber;
    private String externalSystemId;
    private String userdocType;
    private String userdocName;
}
