package bg.duosoft.ipas.core.validation.patent.attachments;

import bg.duosoft.ipas.core.model.util.CPdfAttachmentBookmark;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class AttachmentBookmarksValidator implements IpasTwoArgsValidator<Integer, List<CPdfAttachmentBookmark>> {
    @Override
    public List<ValidationError> validate(Integer documentPages, List<CPdfAttachmentBookmark> bookmarks, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        for (CPdfAttachmentBookmark bookmark : bookmarks) {
            if (Objects.nonNull(bookmark.getBookmarkRequired()) && bookmark.getBookmarkRequired() && Objects.isNull(bookmark.getPageNumber())) {
                errors.add(ValidationError.builder().pointer("bookmark."+bookmark.getBookmarkName()).messageCode("required.field").build());
            }

            if (Objects.nonNull(bookmark.getPageNumber()) && bookmark.getPageNumber()>documentPages){
                errors.add(ValidationError.builder().pointer("bookmark."+bookmark.getBookmarkName()).messageCode("document.invalid.page").build());
            }

        }

        return errors.size() == 0 ? null : errors;

    }
}
