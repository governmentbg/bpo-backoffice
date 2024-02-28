package bg.duosoft.ipas.core.service.impl.mark;

import bg.duosoft.ipas.core.mapper.file.FileIdMapper;
import bg.duosoft.ipas.core.model.acp.CAcpAffectedObject;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.mark.AcpAffectedObjectService;
import bg.duosoft.ipas.persistence.model.entity.ext.acp.AcpAffectedObjectIpFile;
import bg.duosoft.ipas.persistence.repository.entity.mark.AcpAffectedObjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@Slf4j
public class AcpAffectedObjectServiceImpl implements AcpAffectedObjectService {

    @Autowired
    private AcpAffectedObjectRepository acpAffectedObjectRepository;

    @Autowired
    private FileIdMapper fileIdMapper;

    @Override
    public CAcpAffectedObject constructAffectedObjectByFileId(CFileId fileId) {
        AcpAffectedObjectIpFile acpAffectedObjectIpFile = acpAffectedObjectRepository.findById(fileIdMapper.toEntity(fileId)).orElse(null);
        if (Objects.isNull(acpAffectedObjectIpFile)) {
            throw new RuntimeException("Affected acp object not found !");
        }
        return new CAcpAffectedObject(null, fileId, acpAffectedObjectIpFile.getRegistrationNbr(), acpAffectedObjectIpFile.getTitle(), null);
    }
}
