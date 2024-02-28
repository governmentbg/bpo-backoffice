package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.miscellaneous.FileTypeGroupMapper;
import bg.duosoft.ipas.core.model.CFileTypeGroup;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeGroupService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfFileTypeGroup;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfFileTypeGroupsRepository;
import org.infinispan.cache.impl.EncoderCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 15.02.2022
 * Time: 11:12
 */
@Service("fileTypeGroupService")
public class FileTypeGroupServiceImpl implements FileTypeGroupService {
    @Autowired
    private FileTypeGroupMapper fileTypeGroupMapper;
    @Autowired
    private CfFileTypeGroupsRepository cfFileTypeGroupsRepository;
    @Autowired
    private CacheManager cacheManager;

    @Override
    @Cacheable(value = "fileTypeGroupsMap", key = "{'name','all'}")
    public Map<String, String> getFileTypeGroupNamesMap() {
        return fileTypeGroupsToNameMap(getFileTypeGroups().values(), r -> true);
    }

    //caching the file types with code, because the method is private, but it's called from all the other methods in the service!
    private Map<String, CFileTypeGroup> getFileTypeGroups() {
        Cache cache = cacheManager.getCache("fileTypeGroupsMap");
        if (cache.get("all") == null || cache.get("all").get() == null) {
            synchronized (this) {
                List<CfFileTypeGroup> recs = cfFileTypeGroupsRepository.findAll();
                LinkedHashMap<String, CFileTypeGroup> res = recs.stream().map(r -> fileTypeGroupMapper.toCore(r)).collect(Collectors.toMap(CFileTypeGroup::getGroupCode, Function.identity(), (a, b) -> a, LinkedHashMap::new));
                cache.put("all", res);
            }
        }
        return (Map<String, CFileTypeGroup>) cache.get("all").get();
    }

    @Cacheable(value = "fileTypeGroupsMap", key = "{'name', 'userdoc'}")
    public Map<String, String> getUserdocFileTypesGroupNamesMap() {
        return fileTypeGroupsToNameMap(getFileTypeGroups().values(), r -> r.isUserdocFlag());
    }


    protected Map<String, String> fileTypeGroupsToNameMap(Collection<CFileTypeGroup> recs, Predicate<CFileTypeGroup> filter) {
        if (CollectionUtils.isEmpty(recs)) {
            return null;
        }
        return recs.stream().filter(filter).sorted(Comparator.comparing(CFileTypeGroup::getGroupName)).collect(Collectors.toMap(CFileTypeGroup::getGroupCode, CFileTypeGroup::getGroupName, (o, n) -> o, LinkedHashMap::new));
    }

    @Override
    @Cacheable(value = "fileTypeGroup")
    public CFileTypeGroup getFileTypeGroup(String groupCode) {
        return getFileTypeGroups().get(groupCode);
    }

    @Override
    @Cacheable(value = "fileTypeGroup", key = "{'default', #groupCode}")
    public String getDefaultFileTypeByGroup(String groupCode) {
        List<String> res = getFileTypesByGroup(groupCode);
        return CollectionUtils.isEmpty(res) ? null : res.get(0);
    }

    @Override
    @Cacheable(value = "fileTypesByGroup")
    public List<String> getFileTypesByGroup(String groupCode) {
        return getFileTypeGroups().values().stream().filter(r -> r.getGroupCode().equals(groupCode)).findFirst().map(r -> r.getFileTypes()).orElse(null);
    }

    @Cacheable("groupTypeByFileType")
    public String getFileTypeGroupByFileType(String fileType) {
        return fileType == null ? null : getFileTypeGroups().values().stream().filter(r -> r.getFileTypes().contains(fileType)).findFirst().map(r -> r.getGroupCode()).orElse(null);
    }
}
