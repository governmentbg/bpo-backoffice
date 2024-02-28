package bg.duosoft.ipas.core.service.application;

import bg.duosoft.ipas.core.model.IpasApplicationSearchResult;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.enums.ApplicationSearchType;
import bg.duosoft.ipas.enums.FileType;

import java.util.List;

public interface SearchApplicationService {

    List<IpasApplicationSearchResult> selectApplications(List<FileType> fileTypes, String searchText, ApplicationSearchType applicationSearchType, boolean includeReceptions);

    IpasApplicationSearchResult selectApplication(CFileId id);
}
