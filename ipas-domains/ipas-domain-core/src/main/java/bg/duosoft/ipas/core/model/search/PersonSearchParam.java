package bg.duosoft.ipas.core.model.search;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PersonSearchParam extends SearchPage implements Serializable {

    private String personNbr;

    private String personName;

    public PersonSearchParam(SearchPage searchPage) {
        super();
        this.page(searchPage.getPage())
                .pageSize(searchPage.getPageSize())
                .sortOrder(searchPage.getSortOrder())
                .sortColumn(searchPage.getSortColumn());
    }

    public PersonSearchParam personNbr(String personNbr) {
        this.personNbr = personNbr;
        return this;
    }

    public PersonSearchParam personName(String personName) {
        this.personName = personName;
        return this;
    }
}