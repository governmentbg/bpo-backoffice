package bg.duosoft.ipas.core.validation.image;

import bg.duosoft.ipas.enums.ImageFormat;
import bg.duosoft.ipas.util.attachment.AttachmentUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Set;

/**
 * User: ggeorgiev
 * Date: 11.3.2019 г.
 * Time: 17:57
 */
@Slf4j
public class ImageValidatorBase {
    protected ImageValidationResult isValid(byte[] content, Set<ImageFormat> supportedFormats, Integer maxFileSize) {
        int length = content.length;
        boolean isValidSize = true;
        if (Objects.nonNull(maxFileSize) && (length > maxFileSize))
            isValidSize = false;

        try {
            String contentType = AttachmentUtils.getContentType(content);
            boolean imageFormatValid = isImageFormatValid(supportedFormats, contentType);
            return new ImageValidationResult(imageFormatValid, contentType, isValidSize, length);
        } catch (Exception e) {
            log.error("Cannot get image info...", e);
            return new ImageValidationResult(false, null, isValidSize, length);
        }
    }

    private boolean isImageFormatValid(Set<ImageFormat> supportedFormats, String contentType) {
        boolean isImageFormatValid = false;
        for (ImageFormat imageFormat : supportedFormats)
            if (imageFormat.getMimeType().equals(contentType))
                isImageFormatValid = true;

        return isImageFormatValid;
    }

    @AllArgsConstructor
    @Data
    protected static class ImageValidationResult {
        private boolean validType;
        private String formatType;
        private boolean validSize;
        private Integer size;
    }
}
