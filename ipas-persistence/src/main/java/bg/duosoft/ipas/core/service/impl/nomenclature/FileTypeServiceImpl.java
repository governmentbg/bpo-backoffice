package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.service.nomenclature.FileTypeService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfFileType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfFileTypeGroup;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfFileTypeGroupsRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfFileTypeRepository;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileTypeServiceImpl implements FileTypeService {

    @Autowired
    private CfFileTypeRepository cfFileTypeRepository;

    @Autowired
    private CfFileTypeGroupsRepository cfFileTypeGroupsRepository;

    @Override
    @Cacheable(value = "fileTypesMap")
    public Map<String, String> getFileTypesMap() {
        List<CfFileType> cfFileTypes = cfFileTypeRepository.findAll();
        if (CollectionUtils.isEmpty(cfFileTypes))
            return null;

        return cfFileTypes.stream()
                .sorted(Comparator.comparing(CfFileType::getFileTypeName))
                .collect(LinkedHashMap::new, (map, fileType) -> map.put(fileType.getFileTyp(), fileType.getFileTypeName()), Map::putAll);
    }

    @Override
    public Map<String, String> getFileTypesMapBasedOnSecurityRights() {
        List<String> fileTypesParam = new ArrayList<>();
        if (SecurityUtils.hasRights(SecurityRole.MarkViewOwn)) {
            fileTypesParam.addAll(FileType.getMarkRelatedFileTypes());
        }
        if (SecurityUtils.hasRights(SecurityRole.PatentViewOwn)) {
            fileTypesParam.addAll(FileType.getPatentRelatedFileTypes());
        }
        if (CollectionUtils.isEmpty(fileTypesParam)) {
            return null;
        }
        return findAllByFileTypInOrderByFileTypeName(fileTypesParam);
    }

    @Override
    public Map<String, String> findAllByFileTypInOrderByFileTypeName(List<String> fileTypes) {
        List<CfFileType> allByFileTypInOrOrderByFileTypeName = cfFileTypeRepository.findAllByFileTypInOrderByFileTypeName(fileTypes);
        if (CollectionUtils.isEmpty(allByFileTypInOrOrderByFileTypeName))
            return null;

        Map<String, String> map = new LinkedHashMap<String, String>();
        allByFileTypInOrOrderByFileTypeName.stream().forEach(r -> {
            map.put(r.getFileTyp(), r.getFileTypeName());
        });

        return map;
    }


}