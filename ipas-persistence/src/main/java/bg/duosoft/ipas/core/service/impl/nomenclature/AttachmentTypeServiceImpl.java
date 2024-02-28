package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.patent.AttachmentTypeMapper;
import bg.duosoft.ipas.core.model.patent.CAttachmentType;
import bg.duosoft.ipas.core.model.reception.CUserdocReceptionRelation;
import bg.duosoft.ipas.core.service.nomenclature.AttachmentTypeService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAttachmentType;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfAttachmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttachmentTypeServiceImpl implements AttachmentTypeService {
    @Autowired
    private CfAttachmentTypeRepository cfAttachmentTypeRepository;
    @Autowired
    private AttachmentTypeMapper attachmentTypeMapper;

    @Override
    public CAttachmentType findById(Integer id) {
        return attachmentTypeMapper.toCore(cfAttachmentTypeRepository.findById(id).orElse(null));
    }

    @Override
    public List<CAttachmentType> findAllAndOrderByName() {
        List<CAttachmentType> attachmentTypes = cfAttachmentTypeRepository.findAll().stream().map(r -> attachmentTypeMapper.toCore(r)).collect(Collectors.toList());
        attachmentTypes.sort(Comparator.comparing(CAttachmentType::getName));
        return attachmentTypes;
    }

    @Override
    public List<CAttachmentType> findAllByTypeAndOrder(String fileType) {
        List<CAttachmentType> result = new ArrayList<>();
        List<CAttachmentType> attachmentTypes = cfAttachmentTypeRepository.findAll().stream().map(r -> attachmentTypeMapper.toCore(r)).collect(Collectors.toList());
        attachmentTypes.sort(Comparator.comparing(CAttachmentType::getAttachmentOrder));

        for (CAttachmentType attachmentType : attachmentTypes) {
            if (attachmentType.getAttachmentFileTypes().contains(fileType)) {
                result.add(attachmentType);
            }
        }

        return result;
    }
}
