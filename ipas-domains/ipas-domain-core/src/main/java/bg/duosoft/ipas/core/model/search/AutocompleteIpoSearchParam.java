package bg.duosoft.ipas.core.model.search;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AutocompleteIpoSearchParam extends SearchPage implements Serializable {
    private String fileSeq;

    private List<String> fileTypes;

    private String fileSer;

    private String fileNbr;

    private String registrationNbr;

    public AutocompleteIpoSearchParam(SearchPage searchPage) {
        super();
        this.page(searchPage.getPage())
                .pageSize(searchPage.getPageSize())
                .sortOrder(searchPage.getSortOrder())
                .sortColumn(searchPage.getSortColumn());
    }
    public AutocompleteIpoSearchParam fileSeq(String fileSeq) {
        this.fileSeq = fileSeq;
        return this;
    }


    public AutocompleteIpoSearchParam addFileType(String fileType) {
        if (fileTypes == null) {
            fileTypes = new ArrayList<>();
        }
        fileTypes.add(fileType);
        return this;
    }

    public AutocompleteIpoSearchParam fileSer(String fileSer) {
        this.fileSer = fileSer;
        return this;
    }

    public AutocompleteIpoSearchParam fileNbr(String fileNbr) {
        this.fileNbr = fileNbr;
        return this;
    }

    public AutocompleteIpoSearchParam registrationNbr(String registrationNbr) {
        this.registrationNbr = registrationNbr;
        return this;
    }
}