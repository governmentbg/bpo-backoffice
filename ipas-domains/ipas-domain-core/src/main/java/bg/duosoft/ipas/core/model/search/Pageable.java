package bg.duosoft.ipas.core.model.search;

public interface Pageable {
    Integer DEFAULT_PAGE = 1;
    Integer DEFAULT_PAGE_SIZE = 10;
    Integer PAGE_SIZE_100 = 100;

    Integer getPage();

    Integer getPageSize();

}
