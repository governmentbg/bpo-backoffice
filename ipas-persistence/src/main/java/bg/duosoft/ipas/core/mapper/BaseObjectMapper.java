package bg.duosoft.ipas.core.mapper;

import org.mapstruct.InheritInverseConfiguration;

import java.util.List;

public abstract class BaseObjectMapper<E, C> {

    public abstract C toCore(E e);

    public abstract List<C> toCoreList(List<E> eList);

    @InheritInverseConfiguration
    public abstract E toEntity(C c);

    @InheritInverseConfiguration
    public abstract List<E> toEntityList(List<C> cList);
}
