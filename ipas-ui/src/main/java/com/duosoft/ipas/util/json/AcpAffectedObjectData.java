package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.core.model.util.AutocompleteIpoSearchResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AcpAffectedObjectData {
    protected String fileSeq;
    protected String fileType;
    protected Integer fileNbr;
    protected Integer fileSeries;
    protected String title;
    protected Integer registrationNbr;

    public AcpAffectedObjectData(AutocompleteIpoSearchResult searchResult) {
        fileSeq = searchResult.getPk().getFileSeq();
        fileType = searchResult.getPk().getFileType();
        fileSeries = searchResult.getPk().getFileSer();
        fileNbr = searchResult.getPk().getFileNbr();
        title = searchResult.getTitle();
        registrationNbr = searchResult.getRegistrationNbr();
    }
}
