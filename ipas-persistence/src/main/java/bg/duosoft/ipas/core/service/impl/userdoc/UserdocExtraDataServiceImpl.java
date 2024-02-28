package bg.duosoft.ipas.core.service.impl.userdoc;

import bg.duosoft.ipas.core.mapper.userdoc.UserdocExtraDataMapper;
import bg.duosoft.ipas.core.mapper.userdoc.UserdocExtraDataTypeMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraData;
import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraDataType;
import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraDataValue;
import bg.duosoft.ipas.core.service.UserdocExtraDataService;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocExtraDataType;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocExtraData;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocExtraDataPK;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfUserdocExtraDataTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocExtraDataRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocRepository;
import bg.duosoft.ipas.util.userdoc.UserdocExtraDataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UserdocExtraDataServiceImpl implements UserdocExtraDataService {

    private final UserdocExtraDataMapper userdocExtraDataMapper;
    private final UserdocExtraDataTypeMapper userdocExtraDataTypeMapper;
    private final IpUserdocExtraDataRepository ipUserdocExtraDataRepository;
    private final CfUserdocExtraDataTypeRepository cfUserdocExtraDataTypeRepository;
    private final IpUserdocRepository ipUserdocRepository;

    @Override
    public CUserdocExtraData save(CUserdocExtraData userdocExtraData) {
        CDocumentId documentId = userdocExtraData.getDocumentId();
        ipUserdocRepository.updateRowVersion(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr());

        IpUserdocExtraData entity = userdocExtraDataMapper.toEntity(userdocExtraData);
        IpUserdocExtraData result = ipUserdocExtraDataRepository.save(entity);
        return userdocExtraDataMapper.toCore(result);
    }

    @Override
    public CUserdocExtraData save(CDocumentId documentId, String type, CUserdocExtraDataValue value) {
        if (Objects.isNull(documentId) || Objects.isNull(type) || Objects.isNull(value)) {
            return null;
        }

        CUserdocExtraDataType typeObject = selectExtraDataType(type);
        if (Objects.isNull(typeObject)) {
            return null;
        }

        return save(UserdocExtraDataUtils.buildExtraDataObject(documentId, type, value, typeObject));
    }

    @Override
    public CUserdocExtraDataType selectExtraDataType(String code) {
        CfUserdocExtraDataType entity = cfUserdocExtraDataTypeRepository.findById(code).orElse(null);
        if (Objects.isNull(entity))
            return null;

        return userdocExtraDataTypeMapper.toCore(entity);
    }

    @Override
    public CUserdocExtraData selectById(CDocumentId documentId, String type) {
        IpUserdocExtraDataPK pk = new IpUserdocExtraDataPK();
        pk.setCode(type);
        pk.setDocOri(documentId.getDocOrigin());
        pk.setDocLog(documentId.getDocLog());
        pk.setDocSer(documentId.getDocSeries());
        pk.setDocNbr(documentId.getDocNbr());
        IpUserdocExtraData result = ipUserdocExtraDataRepository.findById(pk).orElse(null);
        if(Objects.isNull(result))
            return null;

        return userdocExtraDataMapper.toCore(result);
    }

}