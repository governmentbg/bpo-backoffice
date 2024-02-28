package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.design.CImageViewType;

import java.util.List;


public interface ImageViewTypeService {

    List<CImageViewType> getAllImageViewTypes();

    CImageViewType getImageViewTypeById(Integer viewTypeId);

}
