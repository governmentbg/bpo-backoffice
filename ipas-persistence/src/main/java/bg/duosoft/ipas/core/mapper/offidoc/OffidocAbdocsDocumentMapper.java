package bg.duosoft.ipas.core.mapper.offidoc;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.offidoc.COffidocAbdocsDocument;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpOffidocAbdocsDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = OffidocIdMapper.class)
public abstract class OffidocAbdocsDocumentMapper extends BaseObjectMapper<IpOffidocAbdocsDocument, COffidocAbdocsDocument> {

}
