package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.core.model.document.CExtraData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtraDataExtended extends CExtraData {
    private Boolean indExclusiveLicense;
    private Boolean indCompulsoryLicense;
}
