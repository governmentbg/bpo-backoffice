package bg.duosoft.ipas.core.model.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
public class AutocompleteIpoSearchResult implements Serializable {
    private PK pk = new PK();
    private String title;
    private Date filingDate;
    private Date registrationDate;
    private Integer registrationNbr;
    private String registrationDup;
    private Date entitlementDate;
    private Date expirationDate;
    private Integer procTyp;
    private Integer procNbr;
    private String statusCode;
    private String signWcode;

    @Getter
    @Setter
    @EqualsAndHashCode
    public class PK implements Serializable {
        private String fileSeq;
        private String fileType;
        private Integer fileSer;
        private Integer fileNbr;


        public PK fileSeq(String fileSeq) {
            this.fileSeq = fileSeq;
            return this;
        }

        public PK fileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public PK fileSer(Integer fileSer) {
            this.fileSer = fileSer;
            return this;
        }

        public PK fileNbr(Integer fileNbr) {
            this.fileNbr = fileNbr;
            return this;
        }

        @Override
        public String toString() {
            return fileSeq + '/'
                    + fileType + '/'
                    + fileSer + '/'
                    + fileNbr;
        }
    }

    public AutocompleteIpoSearchResult pk(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr) {
        this.pk
                .fileSeq(fileSeq)
                .fileType(fileTyp)
                .fileSer(fileSer)
                .fileNbr(fileNbr);
        return this;
    }

    public AutocompleteIpoSearchResult title(String title) {
        this.title = title;
        return this;
    }

    public AutocompleteIpoSearchResult filingDate(Date filingDate) {
        this.filingDate = filingDate;
        return this;
    }

    public AutocompleteIpoSearchResult registrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public AutocompleteIpoSearchResult registrationNbr(Integer registrationNbr) {
        this.registrationNbr = registrationNbr;
        return this;
    }

    public AutocompleteIpoSearchResult registrationDup(String registrationDup) {
        this.registrationDup = registrationDup;
        return this;
    }

    public AutocompleteIpoSearchResult entitlementDate(Date entitlementDate) {
        this.entitlementDate = entitlementDate;
        return this;
    }

    public AutocompleteIpoSearchResult expirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public AutocompleteIpoSearchResult procTyp(Integer procTyp) {
        this.procTyp = procTyp;
        return this;
    }

    public AutocompleteIpoSearchResult procNbr(Integer procNbr) {
        this.procNbr = procNbr;
        return this;
    }

    public AutocompleteIpoSearchResult statusCode(String statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public AutocompleteIpoSearchResult signWcode(String signWcode) {
        this.signWcode = signWcode;
        return this;
    }
}
