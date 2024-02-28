package bg.duosoft.ipas.core.service.impl.patent;

import bg.duosoft.ipas.core.mapper.patent.PatentAttachmentMapper;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.patent.CAttachmentTypeBookmarks;
import bg.duosoft.ipas.core.model.patent.CPatentAttachment;
import bg.duosoft.ipas.core.model.util.CPdfAttachmentBookmark;
import bg.duosoft.ipas.core.model.util.CPdfBookmark;
import bg.duosoft.ipas.core.service.PdfBookmarkService;
import bg.duosoft.ipas.core.service.patent.PatentAttachmentService;
import bg.duosoft.ipas.enums.AttachmentTypeBookmark;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentAttachmentPK;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentAttachmentRepository;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
@LogExecutionTime
public class PatentAttachmentServiceImpl implements PatentAttachmentService {

    @Autowired
    private IpPatentAttachmentRepository ipPatentAttachmentRepository;

    @Autowired
    private PatentAttachmentMapper patentAttachmentMapper;

    @Autowired
    private PdfBookmarkService pdfBookmarkService;


    @Override
    public CPatentAttachment findPatentAttachment(Integer id, Integer attachmentType, CFileId fileId, boolean loadContent) {
        IpPatentAttachmentPK pk = new IpPatentAttachmentPK();
        pk.setId(id);
        pk.setAttachmentTypeId(attachmentType);
        pk.setFileNbr(fileId.getFileNbr());
        pk.setFileSeq(fileId.getFileSeq());
        pk.setFileSer(fileId.getFileSeries());
        pk.setFileTyp(fileId.getFileType());
        return patentAttachmentMapper.toCore(ipPatentAttachmentRepository.findById(pk).orElse(null), loadContent);
    }

    @Override
    public Integer getMaxIdByFileIdAndAttachmentType(CFileId fileId, Integer attachmentType) {
        Integer maxId = ipPatentAttachmentRepository.getMaxIdByFileIdAndAttachmentType(fileId.getFileSeq(), fileId.getFileType(),
                fileId.getFileSeries(), fileId.getFileNbr(), attachmentType);
        if (Objects.isNull(maxId)) {
            return DefaultValue.INCREMENT_VALUE;
        }
        return maxId + DefaultValue.INCREMENT_VALUE;
    }

    @Override
    public List<CPdfAttachmentBookmark> initAttachmentBookmarks(CPatentAttachment patentAttachment, CFileId fileId) {
        List<CPdfAttachmentBookmark> bookmarks = new ArrayList<>();

        if (CollectionUtils.isEmpty(patentAttachment.getAttachmentType().getBookmarks())) {
            throw new RuntimeException("No configuration related to bookmarks");
        }

        if (Objects.nonNull(patentAttachment.getHasBookmarks()) && patentAttachment.getHasBookmarks()) {
            List<CPdfAttachmentBookmark> existedBookmarks = getExistedBookmarks(patentAttachment, fileId, patentAttachment.getAttachmentType().getBookmarks());
            if (!CollectionUtils.isEmpty(existedBookmarks)) {
                bookmarks.addAll(existedBookmarks);
            }
        }
        bookmarks.addAll(getNotFilledBookMarks(bookmarks, patentAttachment.getAttachmentType().getBookmarks()));
        bookmarks.sort(Comparator.comparing(CPdfAttachmentBookmark::getBookmarkOrder));
        return bookmarks;
    }

    @Override
    public void updateBookmarks(CFileId fileId, CPatentAttachment patentAttachment, List<CPdfAttachmentBookmark> attachmentBookmarks) {
        attachmentBookmarks.sort(Comparator.comparing(CPdfAttachmentBookmark::getBookmarkOrder));
        attachmentBookmarks.removeIf(r -> (Objects.isNull(r.getBookmarkRequired()) || !r.getBookmarkRequired()) && Objects.isNull(r.getPageNumber()));
        List<CPdfBookmark> bookmarks = new ArrayList<>();
        attachmentBookmarks.forEach(r -> bookmarks.add(new CPdfBookmark(AttachmentTypeBookmark.selectByCode(r.getBookmarkName()).description(), r.getPageNumber())));
        patentAttachment.setAttachmentContent(pdfBookmarkService.addPdfBookmarks(getAttachmentContent(patentAttachment, fileId), bookmarks));
        patentAttachment.setHasBookmarks(!CollectionUtils.isEmpty(attachmentBookmarks));
        patentAttachment.setContentLoaded(true);
    }

    @Override
    public byte[] getAttachmentContent(CPatentAttachment patentAttachment, CFileId fileId) {
        byte[] content = null;
        if (patentAttachment.isContentLoaded()) {
            content = patentAttachment.getAttachmentContent();
        } else {
            CPatentAttachment dbPatentAttachment = findPatentAttachment(patentAttachment.getId(), patentAttachment.getAttachmentType().getId(), fileId, true);
            content = dbPatentAttachment.getAttachmentContent();
        }
        return content;
    }


    private List<CPdfAttachmentBookmark> getNotFilledBookMarks(List<CPdfAttachmentBookmark> bookmarks, List<CAttachmentTypeBookmarks> attachmentTypeBookmarks) {
        List<CPdfAttachmentBookmark> notFilledBookmarks = new ArrayList<>();
        for (CAttachmentTypeBookmarks attachmentTypeBookmark : attachmentTypeBookmarks) {
            CPdfAttachmentBookmark cPdfAttachmentBookmark = bookmarks.stream().filter(r -> r.getBookmarkName().equals(attachmentTypeBookmark.getBookmarkName())).findFirst().orElse(null);
            if (Objects.isNull(cPdfAttachmentBookmark)) {
                notFilledBookmarks.add(new CPdfAttachmentBookmark(attachmentTypeBookmark.getBookmarkName(), null, attachmentTypeBookmark.getBookmarkRequired(), attachmentTypeBookmark.getBookmarkOrder()));
            }
        }
        return notFilledBookmarks;
    }

    private List<CPdfAttachmentBookmark> getExistedBookmarks(CPatentAttachment patentAttachment, CFileId fileId, List<CAttachmentTypeBookmarks> attachmentTypeBookmarks) {
        List<CPdfAttachmentBookmark> existedAttachmentBookmarks = new ArrayList<>();
        byte[] content = getAttachmentContent(patentAttachment, fileId);
        List<CPdfBookmark> pdfBookmarks = pdfBookmarkService.getPdfBookmarks(content);
        for (CPdfBookmark pdfBookmark : pdfBookmarks) {
            pdfBookmark.setBookmarkName(AttachmentTypeBookmark.selectByDescription(pdfBookmark.getBookmarkName()).code());
            CAttachmentTypeBookmarks bookmarkConfig = attachmentTypeBookmarks.stream().filter(r -> pdfBookmark.getBookmarkName().equals(r.getBookmarkName())).findFirst().orElse(null);
            if (Objects.isNull(bookmarkConfig)) {
                throw new RuntimeException("Config not found for existed bookmark!");
            }
            existedAttachmentBookmarks.add(new CPdfAttachmentBookmark(pdfBookmark.getBookmarkName(), pdfBookmark.getPageNumber(), bookmarkConfig.getBookmarkRequired(),bookmarkConfig.getBookmarkOrder()));
        }

        return existedAttachmentBookmarks;
    }

}
