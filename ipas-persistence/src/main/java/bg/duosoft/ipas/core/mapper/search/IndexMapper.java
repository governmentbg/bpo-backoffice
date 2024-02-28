package bg.duosoft.ipas.core.mapper.search;

import bg.duosoft.ipas.persistence.model.entity.vw.ind.VwIndex;

/**
 * User: ggeorgiev
 * Date: 11.03.2021
 * Time: 12:55
 */
public interface IndexMapper<T, V extends VwIndex> {
    T toEntity(V vw);
}
