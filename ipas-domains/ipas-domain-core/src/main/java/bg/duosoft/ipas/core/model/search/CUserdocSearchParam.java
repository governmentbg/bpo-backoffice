package bg.duosoft.ipas.core.model.search;

import bg.duosoft.ipas.enums.search.PersonNameSearchType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CUserdocSearchParam extends SearchPage implements SearchActionParam, Serializable {

    private String userDocType;

    private String fileType;

    private String fileTypeGroup;

    private String docNumber;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date fromFilingDate;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date toFilingDate;

    private List<String> statusCodes;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date fromStatusDate;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date toStatusDate;

    private List<String> actionTypes;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date fromActionDate;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date toActionDate;

    private Integer actionResponsibleUserId;

    private Integer actionCaptureUserId;

    private String personName;

    private PersonNameSearchType personNameSearchType;

    private String role;

    private Integer responsibleUserId;

    public CUserdocSearchParam(SearchPage searchPage) {
        super();
        this.page(searchPage.getPage())
                .pageSize(searchPage.getPageSize())
                .sortOrder(searchPage.getSortOrder())
                .sortColumn(searchPage.getSortColumn());
    }

    public CUserdocSearchParam userDocType(String userDocType) {
        this.userDocType = userDocType;
        return this;
    }

    public CUserdocSearchParam fileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public CUserdocSearchParam fileTypeGroup(String fileTypeGroup) {
        this.fileTypeGroup = fileTypeGroup;
        return this;
    }

    public CUserdocSearchParam docNumber(String docNumber) {
        this.docNumber = docNumber;
        return this;
    }

    public CUserdocSearchParam fromFilingDate(Date fromFilingDate) {
        this.fromFilingDate = fromFilingDate;
        return this;
    }

    public CUserdocSearchParam toFilingDate(Date toFilingDate) {
        this.toFilingDate = toFilingDate;
        return this;
    }

    public CUserdocSearchParam statusCodes(List<String> statusCodes) {
        this.statusCodes = statusCodes;
        return this;
    }

    public CUserdocSearchParam fromStatusDate(Date fromStatusDate) {
        this.fromStatusDate = fromStatusDate;
        return this;
    }

    public CUserdocSearchParam toStatusDate(Date toStatusDate) {
        this.toStatusDate = toStatusDate;
        return this;
    }

    public CUserdocSearchParam actionTypes(List<String> actionTypes) {
        this.actionTypes = actionTypes;
        return this;
    }

    public CUserdocSearchParam fromActionDate(Date fromActionDate) {
        this.fromActionDate = fromActionDate;
        return this;
    }

    public CUserdocSearchParam toActionDate(Date toActionDate) {
        this.toActionDate = toActionDate;
        return this;
    }

    public CUserdocSearchParam personName(String personName) {
        this.personName = personName;
        return this;
    }

    public CUserdocSearchParam role(String role) {
        this.role = role;
        return this;
    }

    public CUserdocSearchParam personNameSearchType(PersonNameSearchType personNameSearchType) {
        this.personNameSearchType = personNameSearchType;
        return this;
    }

    public CUserdocSearchParam responsibleUserId(Integer responsibleUserId) {
        this.responsibleUserId = responsibleUserId;
        return this;
    }
}