package bg.duosoft.ipas.core.model.search;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class SearchPage implements Pageable, Sortable, Serializable {

    private Integer page;

    private Integer pageSize;

    private String sortOrder;

    private String sortColumn;

    protected SearchPage() {
        this(Pageable.DEFAULT_PAGE-1, Pageable.DEFAULT_PAGE_SIZE);
    }

    protected SearchPage(Integer page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    protected SearchPage(Integer page, Integer pageSize, String sortColumn, String sortOrder) {
        this(page, pageSize);
        this.sortColumn =sortColumn;
        this.sortOrder = sortOrder;
    }



    protected SearchPage(String sortColumn, String sortOrder) {
        this();
        this.sortColumn =sortColumn;
        this.sortOrder = sortOrder;
    }

    public static SearchPage create(Integer page, Integer pageSize) {
        return new SearchPage(page, pageSize);
    }

    @Override
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public SearchPage page(Integer page) {
        this.page = page;
        return this;
    }

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public SearchPage pageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public String getSortOrder() {
        if( this.sortOrder==null || this.sortOrder.isEmpty()) {
            this.sortOrder = Sortable.ASC_ORDER;
        }
        return this.sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public SearchPage sortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    @Override
    public String getSortColumn() {
        return this.sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public SearchPage sortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
        return this;
    }
}
