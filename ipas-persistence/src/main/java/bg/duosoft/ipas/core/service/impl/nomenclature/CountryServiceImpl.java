package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.miscellaneous.GeoCountryMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CGeoCountry;
import bg.duosoft.ipas.core.service.nomenclature.CountryService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfGeoCountry;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfGeoCountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Transactional
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CfGeoCountryRepository geoCountryRepositroy;

    @Autowired
    private GeoCountryMapper geoCountryMapper;


    @Override
    public CGeoCountry findById(String code) {
        return geoCountryMapper.toCore(geoCountryRepositroy.findById(code).orElse(null));
    }

    @Override
    @Cacheable("countryMap")
    public Map<String, String> getCountryMap() {
        List<CfGeoCountry> countries = geoCountryRepositroy.findAll();
        if (!CollectionUtils.isEmpty(countries)) {
            CfGeoCountry bulgaria = countries.stream()
                    .filter(cfGeoCountry -> cfGeoCountry.getCountryCode().equalsIgnoreCase("BG"))
                    .findFirst().orElse(null);
            if(Objects.isNull(bulgaria))
                throw new RuntimeException("Bulgaria is missing !");

            countries.removeIf(cfGeoCountry -> cfGeoCountry.getCountryCode().equalsIgnoreCase(bulgaria.getCountryCode()));
            LinkedHashMap<String, String> collect = countries.stream()
                    .sorted(Comparator.comparing(CfGeoCountry::getCountryName))
                    .collect(LinkedHashMap::new,                                                                 //Supplier
                            (map, country) -> map.put(country.getCountryCode(), country.getCountryName()),      //Accumulator
                            Map::putAll);

            LinkedHashMap<String, String> tempMap = (LinkedHashMap<String, String>) collect.clone();
            collect.clear();
            collect.put(bulgaria.getCountryCode(), bulgaria.getCountryName());
            collect.putAll(tempMap);
            return collect;
        }

        return null;
    }
}