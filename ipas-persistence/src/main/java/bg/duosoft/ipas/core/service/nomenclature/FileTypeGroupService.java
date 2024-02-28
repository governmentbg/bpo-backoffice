package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.CFileTypeGroup;

import java.util.List;
import java.util.Map;

public interface FileTypeGroupService {

    public CFileTypeGroup getFileTypeGroup(String groupCode);

    public Map<String, String> getFileTypeGroupNamesMap();

    public Map<String, String> getUserdocFileTypesGroupNamesMap();

    public String getDefaultFileTypeByGroup(String groupCode);

    public List<String> getFileTypesByGroup(String groupCode);

    public String getFileTypeGroupByFileType(String fileType);
}