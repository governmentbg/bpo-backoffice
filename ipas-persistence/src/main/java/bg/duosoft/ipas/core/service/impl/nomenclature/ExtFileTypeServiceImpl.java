package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.service.nomenclature.ExtFileTypeService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfExtFileType;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.ExtFileTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ExtFileTypeServiceImpl implements ExtFileTypeService {

    @Autowired
    private ExtFileTypeRepository repository;


    @Override
    public Map<String, String> getFileTypesAsMapAndOrdered() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(CfExtFileType::getOrder))
                .collect(LinkedHashMap::new, (map, fileType) -> map.put(fileType.getFileTyp(), fileType.getName()), Map::putAll);
    }
}
