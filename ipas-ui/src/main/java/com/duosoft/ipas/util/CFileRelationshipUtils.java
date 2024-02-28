package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.model.file.CRelationshipExtended;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.search.CSearchParam;
import bg.duosoft.ipas.core.model.search.CSearchResult;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.search.SearchService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.MarkTransformationType;
import bg.duosoft.ipas.enums.PatentRelationshipExtApplType;
import bg.duosoft.ipas.enums.RelationshipType;
import bg.duosoft.ipas.core.model.search.SearchPage;
import com.duosoft.ipas.util.json.RelationshipData;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static bg.duosoft.ipas.enums.RelationshipDirection.RELATIONSHIP_CONTAINS_DIVIDED_OBJECTS_ROLE;
import static bg.duosoft.ipas.enums.RelationshipDirection.RELATIONSHIP_CONTAINS_SUPER_OBJECTS_ROLE;
import static bg.duosoft.ipas.enums.RelationshipType.*;

public class CFileRelationshipUtils {


    public static boolean isContainsDividedObjects(List<CRelationship> relationshipList) {
        return isContainsRelatedObjects(relationshipList, RELATIONSHIP_CONTAINS_DIVIDED_OBJECTS_ROLE, null);
    }


    public static boolean isContainsRelationshipExtendedObjects(CRelationshipExtended relationshipExtended) {
        return !Objects.isNull(relationshipExtended) && !Objects.isNull(relationshipExtended.getFilingDate()) && !Objects.isNull(relationshipExtended.getFilingNumber());
    }

    public static String getRelationshipExtendedApplicationTypeTranslation(MessageSource messageSource, CRelationshipExtended relationshipExtended) {
        if (isContainsRelationshipExtendedObjects(relationshipExtended)) {

            if (relationshipExtended.getRelationshipType().equals(RelationshipType.PARALLEL_PATENT_TYPE)
                    || relationshipExtended.getRelationshipType().equals(TRANSFORMED_NATIONAL_PATENT_TYPE)) {
                if (relationshipExtended.getApplicationType().equals(PatentRelationshipExtApplType.EUROPEAN_PATENT.code())) {
                    return messageSource.getMessage("patent.rights.WO", null, LocaleContextHolder.getLocale());
                }
                if (relationshipExtended.getApplicationType().equals(PatentRelationshipExtApplType.INTERNATIONAL_PATENT.code())) {
                    return messageSource.getMessage("patent.rights.EM", null, LocaleContextHolder.getLocale());
                }
            }

            if (relationshipExtended.getRelationshipType().equals(TRANSFORMED_MARK_TYPE)) {
                if (relationshipExtended.getApplicationType().equals(MarkTransformationType.EUROPEAN_MARK.code())) {
                    return messageSource.getMessage("mark.rights.WO", null, LocaleContextHolder.getLocale());
                }
                if (relationshipExtended.getApplicationType().equals(MarkTransformationType.INTERNATIONAL_MARK.code())) {
                    return messageSource.getMessage("mark.rights.EM", null, LocaleContextHolder.getLocale());
                }
            }


        }
        return null;
    }

    public static boolean isContainsSuperObjects(List<CRelationship> relationshipList) {
        return isContainsRelatedObjects(relationshipList, RELATIONSHIP_CONTAINS_SUPER_OBJECTS_ROLE, null);
    }

    public static boolean isContainsSuperObjects(List<CRelationship> relationshipList, String divisionalType) {
        return isContainsRelatedObjects(relationshipList, RELATIONSHIP_CONTAINS_SUPER_OBJECTS_ROLE, divisionalType);
    }


    public static List<CRelationship> findDividedObjects(List<CRelationship> relationshipList, String relationshipType) {
        return !relationshipType.equals(RelationshipType.DESIGN_TYPE) ? findRelatedObjects(relationshipList, RELATIONSHIP_CONTAINS_DIVIDED_OBJECTS_ROLE, relationshipType) : null;
    }

    public static List<CRelationship> findSuperObjects(List<CRelationship> relationshipList, String relationshipType) {
        return findRelatedObjects(relationshipList, RELATIONSHIP_CONTAINS_SUPER_OBJECTS_ROLE, relationshipType);
    }

    public static Set<String> findDividedObjectsUniqueTypes(List<CRelationship> relationshipList) {
        return getRelationalObjectUniqueTypes(relationshipList, RELATIONSHIP_CONTAINS_DIVIDED_OBJECTS_ROLE);
    }

    public static List<CRelationship> constructRelationshipListWithExistingObjects(FileService fileService, List<CRelationship> relationshipList) {
        List<CRelationship> relationshipResultList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(relationshipList)) {
            for (CRelationship relationship : relationshipList) {
                boolean relationshipObjectExist = fileService.isFileExist(relationship.getFileId());
                if (relationshipObjectExist) {
                    relationshipResultList.add(relationship);
                }
            }
        }
        return relationshipResultList;
    }

    public static Set<String> findSuperObjectsUniqueTypes(List<CRelationship> relationshipList) {
        return getRelationalObjectUniqueTypes(relationshipList, RELATIONSHIP_CONTAINS_SUPER_OBJECTS_ROLE);
    }

    public static CRelationship findSuperObject(List<CRelationship> relationshipList, String divisionalType) {
        List<CRelationship> superObject = findRelatedObjects(relationshipList, RELATIONSHIP_CONTAINS_SUPER_OBJECTS_ROLE, divisionalType);
        if (superObject != null && superObject.size() == 1) {
            return superObject.get(0);
        }
        return null;
    }

    public static void removeSuperObjectRelationshipType(CFile file, String relationshipType) {
        List<CRelationship> relationshipList = file.getRelationshipList();
        if (relationshipList != null) {
            relationshipList.removeIf(r -> r.getRelationshipType().equalsIgnoreCase(relationshipType) && r.getRelationshipRole().equalsIgnoreCase(RELATIONSHIP_CONTAINS_SUPER_OBJECTS_ROLE));
        }
    }

    private static boolean isContainsRelatedObjects(List<CRelationship> relationshipList, String relationshipRole, String relationshipType) {
        if (!CollectionUtils.isEmpty(relationshipList)) {
            if (StringUtils.isEmpty(relationshipType)) {
                CRelationship foundRelation = relationshipList.stream()
                        .filter(cRelationship -> cRelationship.getRelationshipRole().equalsIgnoreCase(relationshipRole))
                        .findAny()
                        .orElse(null);
                return Objects.nonNull(foundRelation);

            } else {
                CRelationship foundRelation = relationshipList.stream()
                        .filter(cRelationship -> cRelationship.getRelationshipType().equalsIgnoreCase(relationshipType) && cRelationship.getRelationshipRole().equalsIgnoreCase(relationshipRole))
                        .findAny()
                        .orElse(null);
                return Objects.nonNull(foundRelation);
            }
        }
        return false;
    }

    private static Set<String> getRelationalObjectUniqueTypes(List<CRelationship> relationshipList, String relationshipContainsSuperObjectsRole) {
        if (!CollectionUtils.isEmpty(relationshipList)) {
            List<CRelationship> result = relationshipList.stream()
                    .filter(cRelationship -> !cRelationship.getRelationshipType().equals(RelationshipType.DESIGN_TYPE)
                            && cRelationship.getRelationshipRole().equalsIgnoreCase(relationshipContainsSuperObjectsRole))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(result)) {
                return result.stream()
                        .map(CRelationship::getRelationshipType)
                        .collect(Collectors.toSet());
            }
        }

        return null;
    }

    private static List<CRelationship> findRelatedObjects(List<CRelationship> relationshipList, String relationshipRole, String relationshipType) {
        if (!CollectionUtils.isEmpty(relationshipList)) {
            List<CRelationship> result = relationshipList.stream()
                    .filter(cRelationship -> cRelationship.getRelationshipType().equalsIgnoreCase(relationshipType) && cRelationship.getRelationshipRole().equalsIgnoreCase(relationshipRole))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(result))
                return result;
        }
        return null;
    }

    public static boolean hasNationalPatentSupport(CFileId fileId) {
        FileType ft = FileType.selectByCode(fileId.getFileType());
        return ft == FileType.UTILITY_MODEL ? true : false;
    }


    public static boolean hasParallelRelationship(CPatent patent) {
        return (!Objects.isNull(patent.getRelationshipExtended())
                && patent.getRelationshipExtended().getRelationshipType().equals(PARALLEL_PATENT_TYPE));
    }

    public static boolean hasTransformation(CPatent patent) {
        return (!Objects.isNull(patent.getRelationshipExtended())
                && patent.getRelationshipExtended().getRelationshipType().equals(TRANSFORMED_NATIONAL_PATENT_TYPE));
    }

    public static boolean hasDivisionalApplicationPanelAccess(String fileType) {
        // check divisional application panel access for any objects if needed
        FileType ft = FileType.selectByCode(fileType);
        switch (ft) {
            case INTERNATIONAL_MARK_I:
            case INTERNATIONAL_MARK_R:
            case INTERNATIONAL_MARK_B:
            case EU_PATENT:
                return false;
            default:
                return true;
        }
    }

    public static boolean hasExhibitionPanelAccess(String fileType) {
        // check exhibition panel access for any objects if needed
        FileType ft = FileType.selectByCode(fileType);
        switch (ft) {
            case EU_PATENT:
            case PLANTS_AND_BREEDS:
                return false;
            default:
                return true;
        }
    }

    public static boolean hasPrioritiesPanelAccess(String fileType) {
        // check priorities panel access for any objects if needed
        FileType ft = FileType.selectByCode(fileType);
        switch (ft) {
            default:
                return true;
        }
    }

    public static boolean hasPctPanelAccess(String fileType) {
        // check pct panel access for any objects if needed
        FileType ft = FileType.selectByCode(fileType);
        switch (ft) {
            case DESIGN:
            case INTERNATIONAL_DESIGN:
            case PLANTS_AND_BREEDS:
                return false;
            default:
                return true;
        }
    }

    public static boolean hasParallelUmPanelAccess(String fileType) {
        // check transformation panel access for any objects if needed
        FileType ft = FileType.selectByCode(fileType);
        switch (ft) {
            case UTILITY_MODEL:
                return true;
            default:
                return false;
        }
    }


    public static boolean hasTransformationPanelAccess(String fileType) {
        // check transformation panel access for any objects if needed
        FileType ft = FileType.selectByCode(fileType);
        switch (ft) {
            case INTERNATIONAL_MARK_I:
            case INTERNATIONAL_MARK_R:
            case INTERNATIONAL_MARK_B:
            case EU_PATENT:
            case DESIGN:
            case INTERNATIONAL_DESIGN:
            case PLANTS_AND_BREEDS:
                return false;
            default:
                return true;
        }
    }

    public static String fileTypeToDivisionalRelationshipType(String fileType) {
        FileType ft = FileType.selectByCode(fileType);
        switch (ft) {
            case DESIGN:
            case INTERNATIONAL_DESIGN:
                return DIVISIONAL_DESIGN_TYPE;
            case MARK:
            case DIVISIONAL_MARK:
                return DIVISIONAL_MARK_TYPE;
            case PATENT:
                return DIVISIONAL_PATENT_TYPE;
            case PLANTS_AND_BREEDS:
                return DIVISIONAL_SORT_AND_BREED_TYPE;
            case UTILITY_MODEL:
                return DIVISIONAL_UTILITY_MODEL_TYPE;
            case EU_PATENT:
                return DIVISIONAL_EUROPEAN_PATENT_TYPE;
            default:
                throw new RuntimeException("Unknown relationshipType for fileType:" + ft);
        }
    }


    public static void supplyViewWithDivisionalData(SearchService searchService, Model model, CFile file) {
        CRelationship rel = findSuperObject(file.getRelationshipList(), fileTypeToDivisionalRelationshipType(file.getFileId().getFileType()));
        model.addAttribute("divisionalData", createRelationshipDataObject(searchService, rel));

    }

    public static void supplyViewWithNationalPatentTransformationData(SearchService searchService, Model model, CFile file, Object... additionalArgs) {
        String nationalPatentTransformationDataKeyName = null;
        if (additionalArgs != null && additionalArgs.length > 0) {
            nationalPatentTransformationDataKeyName = (String) additionalArgs[0];
        }
        if (hasNationalPatentSupport(file.getFileId())) {
            List<CRelationship> relations = file.getRelationshipList();
            model.addAttribute(!Objects.isNull(nationalPatentTransformationDataKeyName) ? nationalPatentTransformationDataKeyName : "nationalPatentTransformationData", createRelationshipDataObject(searchService, findSuperObject(relations, TRANSFORMED_NATIONAL_PATENT_TYPE)));
        }
    }

    public static void supplyViewWithParallelUtilityModelData(SearchService searchService, Model model, CFile file, Object... additionalArgs) {
        String nparallelUtilityModelDataKeyName = null;
        if (additionalArgs != null && additionalArgs.length > 0) {
            nparallelUtilityModelDataKeyName = (String) additionalArgs[0];
        }
        if (hasNationalPatentSupport(file.getFileId())) {
            List<CRelationship> relations = file.getRelationshipList();
            model.addAttribute(!Objects.isNull(nparallelUtilityModelDataKeyName) ? nparallelUtilityModelDataKeyName : "parallelUtilityModelData", createRelationshipDataObject(searchService, findSuperObject(relations, PARALLEL_PATENT_TYPE)));
        }
    }

    private static RelationshipData createRelationshipDataObject(SearchService searchService, CRelationship rel) {
        if (rel != null && !CoreUtils.isEmptyCFileId(rel.getFileId())) {
            Page<CSearchResult> res = searchService.search((new CSearchParam(SearchPage.create(0, 10000))
                    .fromFileNbr(rel.getFileId().getFileNbr().toString())
                    .toFileNbr(rel.getFileId().getFileNbr().toString()))
                    .fileSeq(rel.getFileId().getFileSeq())
                    .addFileType(rel.getFileId().getFileType())
                    .fromFileSer(rel.getFileId().getFileSeries().toString())
                    .toFileSer(rel.getFileId().getFileSeries().toString())
            );
            if (res.getContent().size() == 1) {
                return new RelationshipData(res.getContent().get(0));
            } else {
                return new RelationshipData(rel.getFileId().getFileSeq(), rel.getFileId().getFileType(), rel.getFileId().getFileSeries(), rel.getFileId().getFileNbr());
            }
        } else if (rel != null && rel.getFileId() != null) { //this is executed if the relation is not selected from the form's drop-down!
            return new RelationshipData(rel.getFileId().getFileSeq(), rel.getFileId().getFileType(), rel.getFileId().getFileSeries(), rel.getFileId().getFileNbr());
        } else {
            return null;
        }

    }

}
