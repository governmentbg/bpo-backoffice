package bg.duosoft.ipas.core.service.impl.file;

import bg.duosoft.ipas.core.mapper.file.Relationship1Mapper;
import bg.duosoft.ipas.core.mapper.file.Relationship2Mapper;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.model.file.CRelationshipExtended;
import bg.duosoft.ipas.core.service.file.FileRelationshipsService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.PatentRelationshipExtApplType;
import bg.duosoft.ipas.enums.RelationshipDirection;
import bg.duosoft.ipas.enums.RelationshipType;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationship;
import bg.duosoft.ipas.persistence.repository.entity.file.IpFileRelationshipsRepository;
import bg.duosoft.ipas.util.DataConverter;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.CollectionTable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@LogExecutionTime
public class FileRelationshipsServiceImpl implements FileRelationshipsService {

    @Autowired
    private IpFileRelationshipsRepository ipFileRelationshipsRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private Relationship2Mapper relationship2Mapper;

    @Autowired
    private Relationship1Mapper relationship1Mapper;

    @Override
    public Integer findSingleDesignMaxNumberById1(String fileSeq, String fileType, Integer fileSer, Integer fileNbr, String relationshipType) {
        return ipFileRelationshipsRepository.findSingleDesignMaxNumberById1(fileSeq, fileType, fileSer, fileNbr, relationshipType);
    }


    @Override
    public long count() {
        return ipFileRelationshipsRepository.count();
    }

    @Override
    public Date getPatentLikeObjectEntitlementDateFromRelationships(List<CRelationship> relationshipList, CRelationshipExtended relationshipExtended, Date defaultEntitlementDate) {

        if (!CollectionUtils.isEmpty(relationshipList)) {
            CRelationship relationship = relationshipList.stream().filter(r -> r.getRelationshipRole().equals(RelationshipDirection.RELATIONSHIP_CONTAINS_SUPER_OBJECTS_ROLE) &&
                    (r.getRelationshipType().equals(RelationshipType.DIVISIONAL_PATENT_TYPE) || r.getRelationshipType().equals(RelationshipType.DIVISIONAL_UTILITY_MODEL_TYPE)
                            || r.getRelationshipType().equals(RelationshipType.PARALLEL_PATENT_TYPE) || r.getRelationshipType().equals(RelationshipType.DIVISIONAL_EUROPEAN_PATENT_TYPE))).findFirst().orElse(null);
            if (Objects.nonNull(relationship)) {
                CFile fileById = fileService.findById(relationship.getFileId());
                if (Objects.nonNull(fileById) && Objects.nonNull(fileById.getRegistrationData()) && Objects.nonNull(fileById.getRegistrationData().getEntitlementDate())){
                    return fileById.getRegistrationData().getEntitlementDate();
                }
            }
        }

        if (Objects.nonNull(relationshipExtended)) {
            if (relationshipExtended.getApplicationType().equals(PatentRelationshipExtApplType.INTERNATIONAL_PATENT.code())) {
                return relationshipExtended.getFilingDate();
            } else {
                if (Objects.nonNull(relationshipExtended.getFilingDate())) {
                    Integer fileSeries = DateUtils.convertToLocalDatTime(relationshipExtended.getFilingDate()).getYear();
                    CFileId fileId = new CFileId();
                    fileId.setFileSeq(DefaultValue.BULGARIA_CODE);
                    fileId.setFileSeries(fileSeries);
                    fileId.setFileNbr(DataConverter.parseInteger(relationshipExtended.getFilingNumber(), 0));
                    fileId.setFileType(FileType.EU_PATENT.code());
                    CFile file = fileService.findById(fileId);
                    if (Objects.nonNull(file) && Objects.nonNull(file.getRegistrationData())) {
                        return file.getRegistrationData().getEntitlementDate();
                    }
                }
            }
        }
        return defaultEntitlementDate;
    }

    @Override
    public List<CRelationship> findRelationships1ForFile(CFileId fileId, String relationshipType) {
        List<IpFileRelationship> relationships = ipFileRelationshipsRepository.findRelationships1ForFile(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr(), relationshipType);
        if(relationships == null){
            return new ArrayList<>();
        }

        List<CRelationship> parentsRels = relationships.stream().map(rel -> relationship2Mapper.toCore(rel)).collect(Collectors.toList());
        return parentsRels;
    }

    @Override
    public List<CRelationship> findRelationships2ForFile(CFileId fileId, String relationshipType) {
        List<IpFileRelationship> relationships = ipFileRelationshipsRepository.findRelationships2ForFile(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr(), relationshipType);
        if(relationships == null){
            return new ArrayList<>();
        }

        List<CRelationship> childRels = relationships.stream().map(rel -> relationship1Mapper.toCore(rel)).collect(Collectors.toList());
        return childRels;
    }
}
