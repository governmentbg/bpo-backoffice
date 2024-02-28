package bg.duosoft.ipas.core.mapper.ipbase;

import bg.duosoft.ipas.core.model.file.CParisPriority;
import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyPriority;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

/**
 * User: ggeorgiev
 * Date: 5.4.2019 г.
 * Time: 12:04
 */
public abstract class IntellectualPropertyPriorityMapper<T extends IntellectualPropertyPriority> {
    @Mapping(target = "countryCode", source = "country.countryCode")
    @Mapping(target = "applicationId", source = "pk.priorityApplId")
    @Mapping(target = "priorityDate", source = "priorityDate")
    @Mapping(target = "notes", source = "notes")
    @BeanMapping(ignoreByDefault = true)
    public abstract CParisPriority toCore(T priority);


    @InheritInverseConfiguration
    @Mapping(target = "pk.countryCode", source = "countryCode")
    @Mapping(target = "rowVersion", constant = "1")
    @BeanMapping(ignoreByDefault = true)
    public abstract T toEntity(CParisPriority priority);


    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<T> toEntityList(List<CParisPriority> parisPriorities);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CParisPriority> toCoreList(List<T> ipPatentPriorities);


    @AfterMapping
    protected void afterToCore(@MappingTarget CParisPriority target, T source) {
        int status = 0;
        if (source.getIndAccepted() != null) {
            switch (source.getIndAccepted()) {
                case "S":
                    status = 1;
                    break;
                case "N":
                    status = 2;
                    break;
            }
        }
        target.setPriorityStatus(status);
    }


    @AfterMapping
    protected void afterToEntity(@MappingTarget T target, CParisPriority source) {
        String indAccepted = null;
        if (source.getPriorityStatus() != null) {
            if (source.getPriorityStatus() == 1) {
                indAccepted = "S";
            } else if (source.getPriorityStatus() == 2) {
                indAccepted = "N";
            }
        }
        target.setIndAccepted(indAccepted);

        String applId = target.getPk().getPriorityApplId();
        if(applId != null) {
            applId = applId.replaceAll("[^\\d]", "");
            applId = applId.length() < 14 ? applId : applId.substring(applId.length() - 14);
            target.setPriorityApplIdAlt(applId.isEmpty() ? null : Long.parseLong(applId));//v stariq kod slagat poslednite 14 characters kato priorityApplIdAlt
        }
    }
}
