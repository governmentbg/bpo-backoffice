package bg.duosoft.ipas.integration.ebddownload.mapper;

import bg.bpo.ebd.ebddpersistence.entity.EbdDInventor;
import bg.bpo.ebd.ebddpersistence.entity.EbdDOwner;
import bg.bpo.ebd.ebddpersistence.entity.EbdDPatent;
import bg.bpo.ebd.ebddpersistence.service.EbdStatusService;
import bg.duosoft.ipas.core.model.ebddownload.CEbdPatent;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.util.eupatent.EuPatentUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {
        EbdPersonBaseMapper.class,
        EbdPctApplicationMapper.class,
        EbdPriorityMapper.class
})
public abstract class EbdPatentMapper {

    @Autowired
    private EbdStatusService ebdStatusService;

    @Autowired
    private StatusService statusService;
    @Autowired
    private EbdAuthorMapper ebdAuthorMapper;

    private static final Short WITHDRAWN_STATUS_CODE = 660;

    @Mapping(target = "filingNumber", source = "idappli")
    @Mapping(target = "filingDate", source = "dtappli")
    @Mapping(target = "registrationNumber", source = "idpatent")
    @Mapping(target = "registrationDate", source = "dtgrant")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "statusCode", source = "lgstappli")
    @Mapping(target = "backofficeFileNbr", source = "fileNbr")
    @Mapping(target = "owners", source = "owners")
    @Mapping(target = "pctApplicationData", source = "pct")
    @Mapping(target = "parisPriorities", source = "prioritiesList")
    @BeanMapping(ignoreByDefault = true)
    public abstract CEbdPatent toCore(EbdDPatent ebdDPatent);

    @AfterMapping
    protected void afterToCore(EbdDPatent source, @MappingTarget CEbdPatent target) {
        target.setWithdrawn(WITHDRAWN_STATUS_CODE.equals(source.getLgstappli()));
        setStatus(source, target);
        setInventors(source, target);
        setOwnerNames(source, target);
    }

    private void setStatus(EbdDPatent source, @MappingTarget CEbdPatent target) {
        String status = ebdStatusService.selectStatusFromPermitVal(source.getLgstappli().intValue());
        target.setStatus(status);

        Long fileNbr = source.getFileNbr();
        if (Objects.nonNull(fileNbr)) {
            CFileId cFileId = EuPatentUtils.generateEUPatentCFileId(target);
            CStatus ipasStatus = statusService.getStatus(cFileId.getFileSeq(), cFileId.getFileType(), cFileId.getFileSeries(), cFileId.getFileNbr());
            if (Objects.nonNull(ipasStatus))
                target.setIpasStatus(ipasStatus.getStatusName());
        }

    }
    private void setInventors(EbdDPatent source, CEbdPatent target) {
        if (source.getInventors() != null) {
            CAuthorshipData authorshipData = new CAuthorshipData();
            authorshipData.setAuthorList(new ArrayList<>());
            for (EbdDInventor i : source.getInventors()) {
                authorshipData.getAuthorList().add(ebdAuthorMapper.toCore(i));
            }
            target.setAuthorshipData(authorshipData);
        }
    }
    private void setOwnerNames(EbdDPatent source, @MappingTarget CEbdPatent target) {
        Set<EbdDOwner> owners = source.getOwners();
        if (!CollectionUtils.isEmpty(owners)) {
            List<String> collect = owners.stream()
                    .map(EbdDOwner::getFullName)
                    .collect(Collectors.toList());
            target.setOwnerNames(String.join(",", collect));
        }
    }
}
