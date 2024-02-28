package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.util.AutocompleteIpoSearchResult;
import bg.duosoft.ipas.util.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserdocGroundNationalMark {

    protected String fileSeq;
    protected String fileType;
    protected Integer fileNbr;
    protected Integer fileSeries;
    protected String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "Europe/Sofia")
    private Date registrationDate;
    private Integer registrationNbr;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "Europe/Sofia")
    private Date filingDate;
    private String signWcode;

    public UserdocGroundNationalMark(AutocompleteIpoSearchResult searchResult) {
        fileSeq = searchResult.getPk().getFileSeq();
        fileType = searchResult.getPk().getFileType();
        fileSeries = searchResult.getPk().getFileSer();
        fileNbr = searchResult.getPk().getFileNbr();
        title = searchResult.getTitle();
        registrationDate=searchResult.getRegistrationDate();
        registrationNbr=searchResult.getRegistrationNbr();
        filingDate=searchResult.getFilingDate();
        signWcode=searchResult.getSignWcode();
    }
}
