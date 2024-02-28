package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.core.model.search.CSearchResult;
import bg.duosoft.ipas.util.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(value = {"name", "filingDate", "registrationDate", "filingDateStr", "registrationDateStr"}, allowGetters = true)
@ToString
public class RelationshipData {
    protected boolean relationshipChanged;
    protected String name;
    protected String fileSeq;
    protected String fileType;
    protected Integer fileNbr;
    protected Integer fileSeries;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    protected Date filingDate;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    protected Date registrationDate;
    protected String filingDateStr;
    protected String registrationDateStr;

    public RelationshipData(String fileSeq, String fileType, Integer fileSeries, Integer fileNbr) {
        this.fileSeq = fileSeq;
        this.fileType = fileType;
        this.fileNbr = fileNbr;
        this.fileSeries = fileSeries;
    }

    public RelationshipData(CSearchResult searchResult) {
        name = searchResult.getTitle();
        fileSeq = searchResult.getPk().getFileSeq();
        fileType = searchResult.getPk().getFileType();
        fileSeries = searchResult.getPk().getFileSeries();
        fileNbr = searchResult.getPk().getFileNbr();
        filingDate = searchResult.getFilingDate();
        registrationDate = searchResult.getRegistrationDate();
        if (this.getFilingDate() != null) {
            this.setFilingDateStr(DateUtils.formatDate(this.getFilingDate()));
        }
        if (this.getRegistrationDate() != null) {
            this.setRegistrationDateStr(DateUtils.formatDate(this.getRegistrationDate()));
        }
    }
    public String getFullNumber() {
        return fileSeq + "/" + fileType + "/" + fileSeries + "/" + fileNbr;
    }
}
