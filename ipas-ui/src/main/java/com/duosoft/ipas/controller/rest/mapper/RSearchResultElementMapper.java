package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.search.CSearchResult;
import bg.duosoft.ipas.core.model.util.PersonAddressResult;
import bg.duosoft.ipas.rest.model.search.RSearchResult;
import org.apache.commons.lang.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring", uses = {
        RFileIdMapper.class
})
public abstract class RSearchResultElementMapper extends BaseRestObjectMapper<RSearchResult, CSearchResult> {
    @Mapping(target = "representatives", ignore = true)
    public abstract CSearchResult toCore(RSearchResult e);

    @AfterMapping
    protected void afterToRest(CSearchResult source, @MappingTarget RSearchResult target) {
        if (source.getRepresentatives() != null) {
            target.setRepresentativeDetails(source.getRepresentatives().stream().map(r -> toRepresentativeNameAddressPhoneEmail(r)).collect(Collectors.joining("\r\n")));
        }
    }
    private String toRepresentativeNameAddressPhoneEmail(PersonAddressResult res) {
        return Stream.of(res.getPersonName(), res.getAddressStreet(), res.getTelephone(), res.getEmail()).filter(StringUtils::isNotEmpty).collect(Collectors.joining(", "));
    }
}
