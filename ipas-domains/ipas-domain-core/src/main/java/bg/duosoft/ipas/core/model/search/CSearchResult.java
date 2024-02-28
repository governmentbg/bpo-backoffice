package bg.duosoft.ipas.core.model.search;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.util.PersonAddressResult;
import bg.duosoft.ipas.core.model.util.PersonSearchResult;
import bg.duosoft.ipas.enums.FileType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
public class CSearchResult implements Serializable {
    private CFileId pk;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Date filingDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Date registrationDate;
    private Integer registrationNbr;
    private String registrationDup;
    private String mainOwner;
    private String title;
    private Boolean hasImg;
    private CStatus status;
    private CProcessId processId;
    private List<String> requestForValidationNbr;
    private List<Long> niceClassCodes;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Date expirationDate;
    private List<PersonAddressResult> representatives;

    public CSearchResult pk(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr) {
        this.pk = new CFileId(fileSeq, fileTyp, fileSer, fileNbr);
        return this;
    }

    public String createFullRegistrationNumber() {
        if (Objects.nonNull(registrationNbr) && Objects.nonNull(pk) && pk.getFileType().equals(FileType.EU_PATENT.code())) {
            return registrationNbr.toString();
        }

        if (Objects.nonNull(registrationNbr) && Objects.nonNull(registrationDup)) {
            return registrationNbr.toString().concat(registrationDup);
        }
        if (Objects.nonNull(registrationNbr)) {
            return registrationNbr.toString();
        }
        return null;
    }

    public CSearchResult filingDate(Date filingDate) {
        this.filingDate = filingDate;
        return this;
    }

    public CSearchResult registrationNbr(Integer registrationNbr) {
        this.registrationNbr = registrationNbr;
        return this;
    }

    public CSearchResult mainOwner(String mainOwner) {
        this.mainOwner = mainOwner;
        return this;
    }

    public CSearchResult title(String title) {
        this.title = title;
        return this;
    }
    public CSearchResult processId(CProcessId processId) {
        this.processId = processId;
        return this;
    }

    public CSearchResult hasImg(Boolean hasImg) {
        this.hasImg = hasImg;
        return this;
    }

    public CSearchResult registrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public CSearchResult registrationDup(String registrationDup) {
        this.registrationDup = registrationDup;
        return this;
    }

    public CSearchResult status(CStatus status) {
        this.status = status;
        return this;
    }

    public CSearchResult requestForValidationNbr(List<String> requestForValidationNbr) {
        this.requestForValidationNbr = requestForValidationNbr;
        return this;
    }

    public CSearchResult niceClassCodes(List<Long> niceClassCodes) {
        this.niceClassCodes = niceClassCodes;
        return this;
    }

    public CSearchResult expirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }
    public CSearchResult representatives(List<PersonAddressResult> reps) {
        this.representatives = reps;
        return this;
    }
}
