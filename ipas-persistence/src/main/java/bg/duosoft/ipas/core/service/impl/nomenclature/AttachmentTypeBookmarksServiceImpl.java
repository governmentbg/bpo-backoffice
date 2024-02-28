package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.patent.AttachmentTypeBookmarksMapper;
import bg.duosoft.ipas.core.model.patent.CAttachmentTypeBookmarks;
import bg.duosoft.ipas.core.service.nomenclature.AttachmentTypeBookmarksService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.AttachmentTypeBookmarksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentTypeBookmarksServiceImpl implements AttachmentTypeBookmarksService {
    @Autowired
    private AttachmentTypeBookmarksRepository attachmentTypeBookmarksRepository;
    @Autowired
    private AttachmentTypeBookmarksMapper attachmentTypeBookmarksMapper;

    @Override
    public List<CAttachmentTypeBookmarks> selectAllByAttachmentTypeId(Integer attachmentTypeId) {
        return attachmentTypeBookmarksMapper.toCoreList(attachmentTypeBookmarksRepository.selectAllByAttachmentTypeId(attachmentTypeId));
    }
}
