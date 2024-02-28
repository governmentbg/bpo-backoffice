package bg.duosoft.ipas.core.service.impl;

import bg.duosoft.ipas.core.model.util.CPdfBookmark;
import bg.duosoft.ipas.core.service.PdfBookmarkService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PageMode;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitWidthDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 23.11.2023
 * Time: 15:49
 */
@Service
public class PdfBookmarkServiceImpl implements PdfBookmarkService {
    public List<CPdfBookmark> getPdfBookmarks(byte[] content) {
        try {
            List<CPdfBookmark> res = new ArrayList<>();
            try (PDDocument document = Loader.loadPDF(content)) {
                readBookmarks(document, document.getDocumentCatalog().getDocumentOutline(), res);
            }
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] addPdfBookmarks(byte[] content, List<CPdfBookmark> bookmarks) {
        try {
            try (PDDocument document = Loader.loadPDF(content)) {
                PDDocumentOutline documentOutline = new PDDocumentOutline();
                if (!CollectionUtils.isEmpty(bookmarks)) {
                    document.getDocumentCatalog().setDocumentOutline(documentOutline);
                    for (CPdfBookmark b : bookmarks) {
                        PDPageDestination pageDestination = new PDPageFitWidthDestination();
                        pageDestination.setPage(document.getPage(b.getPageNumber() - 1));

                        PDOutlineItem bookmark = new PDOutlineItem();
                        bookmark.setDestination(pageDestination);
                        bookmark.setTitle(b.getBookmarkName());
                        documentOutline.addLast(bookmark);
                    }
                    document.getDocumentCatalog().setPageMode(PageMode.USE_OUTLINES);
                } else {
                    document.getDocumentCatalog().setDocumentOutline(null);
                    document.getDocumentCatalog().setPageMode(PageMode.USE_NONE);
                }

                ByteArrayOutputStream res = new ByteArrayOutputStream();
                document.save(res);
                return res.toByteArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public Integer getPagesCount(byte[] content) {
        try (PDDocument document = Loader.loadPDF(content)) {
            return document.getNumberOfPages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void readBookmarks(PDDocument document, PDOutlineNode bookmark, List<CPdfBookmark> result) throws IOException {
        PDOutlineItem current = bookmark == null ? null : bookmark.getFirstChild();
        while (current != null) {
            PDPage currentPage = current.findDestinationPage(document);
            Integer currentPageIndex = document.getPages().indexOf(currentPage);
            result.add(new CPdfBookmark(current.getTitle(), currentPageIndex + 1));
            readBookmarks(document, current, result);
            current = current.getNextSibling();
        }
    }
}
