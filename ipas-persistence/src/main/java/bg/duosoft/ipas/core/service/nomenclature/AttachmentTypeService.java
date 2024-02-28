package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.patent.CAttachmentType;

import java.util.List;

public interface AttachmentTypeService {

    CAttachmentType findById(Integer id);
    List<CAttachmentType> findAllAndOrderByName();
    List<CAttachmentType> findAllByTypeAndOrder(String fileType);
}
