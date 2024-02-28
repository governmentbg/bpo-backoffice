package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.util.filter.UserdocTypesFilter;

import java.util.List;
import java.util.Map;

public interface UserdocTypesService {

    CUserdocType selectUserdocTypeByDocId(CDocumentId documentId);

    Map<String, String> selectUserdocTypesMap();

    CUserdocType selectUserdocTypeById(String userdocType);

    Map<String, String> selectAllowedUserdocTypesForChange(CUserdoc userdoc);

    Map<String, String> selectUserdocTypesByUserdocName(String userdocName);

    List<String> getAllProcTypes();

    Map<String, String> selectDistinctUserdocTypeMapForUserdocsByUsersAndUserdocGroupName(List<Integer>users, String userdocGroupName);

    Map<String, String> selectDistinctUserdocTypeMapForUserdocsByResponsibleUser(Integer responsibleUserId);

    Map<String, String> findAllByUserdocTypInOrderByUserdocName(List<String> userdocTypes);

    List<CUserdocType> selectUserdocTypesMapByUserdocParentType(String mainType);

    List<CUserdocType> selectUserdocTypes(UserdocTypesFilter filter);

    Integer selectUserdocTypesCount(UserdocTypesFilter filter);

    Map<String, String> selectInvalidatedUserdocNames(List<String> invalidatedUserdocTypes);

    List<CUserdocType> selectAutocompleteUserdocTypesForInvalidation(String invalidateTypeTerm, String currentUserdocType);

    List<String> selectUserdocTypesForInvalidation(String currentUserdocType);

    CUserdocType addInvalidatedUserdocType(String userdocType, String invalidateUserdocType);

    CUserdocType deleteInvalidatedUserdocType(String userdocType, String invalidateUserdocType);

    CUserdocType saveUserdocType(CUserdocType userdocType);

    List<String> selectUserdocsInvalidatingCurrentUserdoc(String currentUserdocType);

    CUserdocType addPanelForUserdocType(String userdocType, String panel);

    CUserdocType deletePanelForUserdocType(String userdocType, String panel);

    CUserdocType addRoleForUserdocType(String userdocType, UserdocPersonRole role);

    CUserdocType deleteRoleForUserdocType(String userdocType, UserdocPersonRole role);

    List<String> selectUserdocTypesByListCode(String userdocListCode);

}
