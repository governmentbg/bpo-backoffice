package bg.duosoft.ipas.integration.ebddownload.mapper;

import bg.bpo.ebd.ebddpersistence.service.EbdDSearchResult;
import bg.duosoft.ipas.core.model.ebddownload.CEbdPatentSearchResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

/**
 * User: Georgi
 * Date: 24.2.2020 г.
 * Time: 12:12
 */
@Mapper(componentModel = "spring")
public abstract class EbdPatentSearchResultMapper {
    @Mapping(target = "filingNumber",       source = "idappli")
    @Mapping(target = "filingDate",         source = "dtappli")
    @Mapping(target = "registrationNumber", source = "idpatent")
    @Mapping(target = "registrationDate",   source = "dtgrant")
    @Mapping(target = "title",              source = "title")
    @Mapping(target = "statusCode",         source = "lgstappli")
    @Mapping(target = "status",             source = "lgstappliName")
    @Mapping(target = "backofficeFileNbr",  source = "fileNbr")
    @BeanMapping(ignoreByDefault = true)
    public abstract CEbdPatentSearchResult toCore(EbdDSearchResult e);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CEbdPatentSearchResult> toCoreList(List<EbdDSearchResult> entities);
}
