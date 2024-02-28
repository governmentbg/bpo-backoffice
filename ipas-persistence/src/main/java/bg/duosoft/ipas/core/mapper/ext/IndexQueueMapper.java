package bg.duosoft.ipas.core.mapper.ext;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.error.CIndexQueue;
import bg.duosoft.ipas.persistence.model.entity.ext.IndexQueue;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class IndexQueueMapper extends BaseObjectMapper<IndexQueue, CIndexQueue> {


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id",source = "id")
    @Mapping(target = "type",source = "type")
    @Mapping(target = "insertedAt",source = "insertedAt")
    @Mapping(target = "operation",source = "operation")
    @Mapping(target = "checked",source = "checked")
    public abstract CIndexQueue toCore(IndexQueue entity);

}
