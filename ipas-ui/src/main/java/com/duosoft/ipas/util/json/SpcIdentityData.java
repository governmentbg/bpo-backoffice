package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.util.DataConverter;
import bg.duosoft.ipas.util.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpcIdentityData {

    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date filingDate;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date registrationDate;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date entitlementDate;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date expirationDate;

    protected String fileSeq;
    protected String fileType;
    protected Integer fileNbr;
    protected Integer fileSeries;
    protected String applicationType;
    protected String applicationSubtype;
    protected String type;
    protected String mainPatent;
    protected  String isPageForReload;
    protected String notes;
    protected String publicationTyp;
    private String efilingDataLoginName;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date notInForceDate;

    public void splitMainPatent(){
        if (!Objects.isNull(this.mainPatent) && !StringUtils.isEmpty(this.mainPatent)){
            String[] mainPatentArray = this.mainPatent.split("/");
            this.fileSeq = mainPatentArray[0];
            this.fileType = mainPatentArray[1];
            this.fileSeries = DataConverter.parseInteger(mainPatentArray[2], null);
            this.fileNbr = DataConverter.parseInteger(mainPatentArray[3], null);
        }

    }

}
