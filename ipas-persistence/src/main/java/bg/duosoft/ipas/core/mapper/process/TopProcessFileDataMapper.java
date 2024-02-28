package bg.duosoft.ipas.core.mapper.process;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.mapper.file.FileIdMapper;
import bg.duosoft.ipas.core.model.process.CTopProcessFileData;
import bg.duosoft.ipas.persistence.model.nonentity.TopProcessFileData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {FileIdMapper.class, ProcessIdMapper.class})
public abstract class TopProcessFileDataMapper extends BaseObjectMapper<TopProcessFileData, CTopProcessFileData> {

}
