package bg.duosoft.ipas.core.mapper.userdoc.reviewers;


import bg.duosoft.ipas.core.model.userdoc.reviewers.CUserdocReviewer;
import bg.duosoft.ipas.persistence.model.entity.userdoc.reviewers.IpUserdocReviewer;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UserdocReviewerMapper {

    @Mapping(target = "user",  source = "user")
    @Mapping(target = "main",  source = "main")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocReviewer toCore(IpUserdocReviewer ipUserdocReviewer);

    @InheritInverseConfiguration
    @Mapping(target = "pk.userId",  source = "user.userId")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpUserdocReviewer toEntity(CUserdocReviewer cUserdocReviewer);
}
