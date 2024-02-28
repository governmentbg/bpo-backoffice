package bg.duosoft.ipas.integration.ebddownload.mapper;

import bg.bpo.ebd.ebddpersistence.entity.EbdDInventor;
import bg.duosoft.ipas.core.model.person.CAuthor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * User: Georgi
 * Date: 24.2.2020 г.
 * Time: 17:31
 */
@Mapper(componentModel = "spring", uses = {
        EbdPersonBaseMapper.class
})
public abstract class EbdAuthorMapper {
    @Mapping(target = "person",    source = "source")
    @Mapping(target = "authorSeq", source = "ord")
    public abstract CAuthor toCore(EbdDInventor source);
}
