package bg.duosoft.ipas.core.mapper.offidoc;


import bg.duosoft.ipas.core.model.offidoc.COffidocPublishedDecision;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidocPublishedDecision;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class OffidocPublishedDecisionMapper {

    @Mapping(target = "attachmentName",  source = "attachmentName")
    @Mapping(target = "decisionDate",  source = "decisionDate")
    @Mapping(target = "attachmentContent", expression = "java(loadFileContent ? publishedDecision.getAttachmentContent() : null)")
    @BeanMapping(ignoreByDefault = true)
    public abstract COffidocPublishedDecision toCore(IpOffidocPublishedDecision publishedDecision, @Context Boolean loadFileContent);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "attachmentContent",source = "attachmentContent")
    public abstract IpOffidocPublishedDecision toEntity(COffidocPublishedDecision publishedDecision);
}
