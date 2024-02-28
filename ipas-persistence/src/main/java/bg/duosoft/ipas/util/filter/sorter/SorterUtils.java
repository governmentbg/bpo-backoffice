package bg.duosoft.ipas.util.filter.sorter;

import bg.duosoft.ipas.core.model.search.Sortable;

public class SorterUtils {

    public static String getNextSortOrder(boolean isSelect, String currentOrder) {
        if (isSelect) {
            if (currentOrder.equalsIgnoreCase(Sortable.ASC_ORDER))
                return Sortable.DESC_ORDER;
            else
                return Sortable.ASC_ORDER;
        } else
            return Sortable.ASC_ORDER;
    }

}
