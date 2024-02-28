package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.mapper.file.RelationshipExtendedMapper;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.model.file.CRelationshipExtended;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.PatentRelationshipExtApplType;
import bg.duosoft.ipas.enums.RelationshipDirection;
import bg.duosoft.ipas.enums.RelationshipType;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationship;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationshipExtended;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationshipPK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfRelationshipType;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class PatentRelationshipsMapperHelper {

    @Autowired
    private RelationshipExtendedMapper relationshipExtendedMapper;

    public void extendedRelationshipToFileRelationship(CRelationshipExtended relationshipExtended,IpPatent ipPatent, FileService fileService){
        IpFileRelationshipExtended ipFileRelationshipExtended = relationshipExtendedMapper.toEntity(relationshipExtended);
        extendedRelationshipToFileRelationship(ipFileRelationshipExtended,ipPatent,fileService);
    }

    public void extendedRelationshipToFileRelationship(IpPatent ipPatent, FileService fileService){
        IpFileRelationshipExtended relationshipExtended = ipPatent.getRelationshipExtended();
        extendedRelationshipToFileRelationship(relationshipExtended,ipPatent,fileService);
    }

    private void extendedRelationshipToFileRelationship(IpFileRelationshipExtended relationshipExtended,IpPatent ipPatent, FileService fileService){
        if (Objects.nonNull(relationshipExtended)){
            if (relationshipExtended.getApplicationType().equals(PatentRelationshipExtApplType.EUROPEAN_PATENT.code())
                    && (relationshipExtended.getRelationshipType().getRelationshipTyp().equals(RelationshipType.PARALLEL_PATENT_TYPE) || relationshipExtended.getRelationshipType().getRelationshipTyp().equals(RelationshipType.TRANSFORMED_NATIONAL_PATENT_TYPE))){

                ipPatent.setRelationshipExtended(null);

                List<IpFileRelationship> ipFileRelationships2 = ipPatent.getFile().getIpFileRelationships2();
                if (CollectionUtils.isEmpty(ipFileRelationships2)){
                    ipFileRelationships2=new ArrayList<>();
                }
                IpFileRelationship newRelationship2=new IpFileRelationship();
                newRelationship2.setRelationshipType(new CfRelationshipType());
                newRelationship2.setRowVersion(1);

                newRelationship2.getRelationshipType().setRelationshipTyp(relationshipExtended.getRelationshipType().getRelationshipTyp());

                List<CFile> allByFileNbrAndFileType = fileService.findAllByFileNbrAndFileType(Integer.valueOf(relationshipExtended.getFilingNumber()), Arrays.asList(FileType.EU_PATENT.code()));
                CFile euPatentId = allByFileNbrAndFileType.get(0);
                newRelationship2.setPk(new IpFileRelationshipPK(euPatentId.getFileId().getFileSeq(),euPatentId.getFileId().getFileType(),euPatentId.getFileId().getFileSeries(),
                        euPatentId.getFileId().getFileNbr(),ipPatent.getPk().getFileSeq(),ipPatent.getPk().getFileTyp(),ipPatent.getPk().getFileSer(),ipPatent.getPk().getFileNbr(),relationshipExtended.getRelationshipType().getRelationshipTyp()));
                ipFileRelationships2.add(newRelationship2);
                ipPatent.getFile().setIpFileRelationships2(ipFileRelationships2);

            }
        }
    }

    public void fileRelationshipToExtendedRelationship(CPatent patent,FileService fileService){
        List<CRelationship> relationshipList = patent.getFile().getRelationshipList();
        if (!CollectionUtils.isEmpty(relationshipList)){

            CRelationship epoPatentParallelOrTransformed = relationshipList.stream().filter(r->r.getRelationshipRole().equals(RelationshipDirection.RELATIONSHIP_CONTAINS_SUPER_OBJECTS_ROLE)
                    && r.getFileId().getFileType().equals(FileType.EU_PATENT.code())
                    && (r.getRelationshipType().equals(RelationshipType.PARALLEL_PATENT_TYPE)
                    || r.getRelationshipType().equals(RelationshipType.TRANSFORMED_NATIONAL_PATENT_TYPE))).findFirst().orElse(null);

            if (Objects.nonNull(epoPatentParallelOrTransformed)){
                relationshipList.remove(epoPatentParallelOrTransformed);

                CRelationshipExtended cRelationshipExtended = new CRelationshipExtended();
                cRelationshipExtended.setApplicationType(PatentRelationshipExtApplType.EUROPEAN_PATENT.code());
                cRelationshipExtended.setRelationshipType(epoPatentParallelOrTransformed.getRelationshipType());
                cRelationshipExtended.setFilingNumber(epoPatentParallelOrTransformed.getFileId().getFileNbr().toString());
                cRelationshipExtended.setFilingDate(fileService.findFilingDateById(epoPatentParallelOrTransformed.getFileId()));
                patent.setRelationshipExtended(cRelationshipExtended);
            }
        }
    }
}
