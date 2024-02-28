package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.design.ImageViewTypeMapper;
import bg.duosoft.ipas.core.model.design.CImageViewType;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.service.nomenclature.ImageViewTypeService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfImageViewTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ImageViewTypeServiceImpl implements ImageViewTypeService {

    @Autowired
    private CfImageViewTypeRepository cfImageViewTypeRepository;

    @Autowired
    private ImageViewTypeMapper imageViewTypeMapper;

    @Override
    public List<CImageViewType> getAllImageViewTypes() {
        List<CImageViewType> viewTypes= cfImageViewTypeRepository.findAll().stream().map(r->imageViewTypeMapper.toCore(r)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(viewTypes)){
            viewTypes.sort(Comparator.comparing(CImageViewType::getViewTypeName));
        }
        return viewTypes;
    }

    @Override
    public CImageViewType getImageViewTypeById(Integer viewTypeId) {
        return imageViewTypeMapper.toCore(cfImageViewTypeRepository.findById(viewTypeId).orElse(null));
    }

}
