package bg.duosoft.ipas.core.service.impl.reception;

import bg.duosoft.ipas.core.mapper.miscellaneous.AbdocsDocumentTypeMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CAbdocsDocumentType;
import bg.duosoft.ipas.core.service.reception.AbdocsDocumentTypeService;
import bg.duosoft.ipas.enums.IpasObjectType;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.CfAbdocsDocumentType;
import bg.duosoft.ipas.persistence.repository.entity.reception.AbdocsDocumentTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@Transactional
public class AbdocsDocumentServiceImpl implements AbdocsDocumentTypeService {

    @Autowired
    private AbdocsDocumentTypeRepository abdocsDocumentTypeRepository;

    @Autowired
    private AbdocsDocumentTypeMapper abdocsDocumentTypeMapper;

    @Override
    public Integer selectAbdocsDocTypeIdByType(String type) {
        if (Objects.isNull(type))
            return null;

        CfAbdocsDocumentType result = abdocsDocumentTypeRepository.selectByType(type);
        if (Objects.isNull(result))
            return null;

        return result.getAbdocsDocTypeId();
    }
    public Integer getDocRegistrationType(String type) {
        if (Objects.isNull(type))
            return null;

        CfAbdocsDocumentType result = abdocsDocumentTypeRepository.selectByType(type);
        if (Objects.isNull(result))
            return null;

        return result.getDocRegistrationType();
    }

    @Override
    public String selectNameByAbdocsDocTypeId(Integer id) {
        if (Objects.isNull(id))
            return null;

        CfAbdocsDocumentType result = abdocsDocumentTypeRepository.selectByAbdocsDocTypeId(id);
        if (Objects.isNull(result))
            return null;

        return result.getName();
    }

    @Override
    public CAbdocsDocumentType selectByAbdocsDocTypeId(Integer id) {
        if (Objects.isNull(id))
            return null;

        CfAbdocsDocumentType result = abdocsDocumentTypeRepository.selectByAbdocsDocTypeId(id);
        if (Objects.isNull(result))
            return null;

        return abdocsDocumentTypeMapper.toCore(result);
    }

    @Override
    public CAbdocsDocumentType selectByType(String type) {
        if (Objects.isNull(type))
            return null;

        CfAbdocsDocumentType result = abdocsDocumentTypeRepository.selectByType(type);
        if (Objects.isNull(result))
            return null;

        return abdocsDocumentTypeMapper.toCore(result);
    }

    @Override
    public CAbdocsDocumentType saveAbdocsDocumentType(CAbdocsDocumentType cAbdocsDocumentType) {
        CfAbdocsDocumentType cfAbdocsDocumentType = abdocsDocumentTypeMapper.toEntity(cAbdocsDocumentType);
        CfAbdocsDocumentType updated = abdocsDocumentTypeRepository.save(cfAbdocsDocumentType);
        return abdocsDocumentTypeMapper.toCore(updated);
    }

    @Override
    public IpasObjectType selectIpasObjectTypeByAbdocsDocumentId(Integer docTypeId) {
        if (Objects.isNull(docTypeId))
            return null;

        String result = abdocsDocumentTypeRepository.selectIpasObjectTypeByAbdocsDocumentId(docTypeId);
        if (StringUtils.isEmpty(result))
            return null;

        return IpasObjectType.valueOf(result);
    }

}
