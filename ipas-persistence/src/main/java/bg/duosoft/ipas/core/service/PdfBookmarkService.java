package bg.duosoft.ipas.core.service;

import bg.duosoft.ipas.core.model.util.CPdfBookmark;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 24.11.2023
 * Time: 13:11
 */
public interface PdfBookmarkService {
    public List<CPdfBookmark> getPdfBookmarks(byte[] content);
    public byte[] addPdfBookmarks(byte[] content, List<CPdfBookmark> bookmarks);

    public Integer getPagesCount(byte[] content);
}
