package bg.duosoft.ipas.core.model.search;

public interface Sortable {
    String ASC_ORDER = "ASC";
    String DESC_ORDER = "DESC";

    String getSortOrder();

    String getSortColumn();
}
