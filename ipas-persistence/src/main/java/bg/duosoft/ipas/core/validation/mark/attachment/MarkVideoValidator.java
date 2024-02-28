package bg.duosoft.ipas.core.validation.mark.attachment;

import bg.duosoft.ipas.core.model.mark.CMarkAttachment;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.VideoFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class MarkVideoValidator implements IpasValidator<CMarkAttachment> {

    @Override
    public List<ValidationError> validate(CMarkAttachment obj, Object... additionalArgs) {
        Integer attachmentIndex = Objects.isNull(additionalArgs) ? null : (Integer) additionalArgs[0];
        Integer maxFileSize = Objects.isNull(additionalArgs) ? null : (Integer) additionalArgs[1];

        List<VideoFormat> supportedFormats = List.of(VideoFormat.MP4, VideoFormat.QUICKTIME);
        VideoValidationResult result = isValid(obj, supportedFormats, maxFileSize);
        List<ValidationError> errors = new ArrayList<>();
        if (!result.isValidType())
            errors.add(ValidationError.builder().pointer("videoFile-" + attachmentIndex).messageCode("invalid.video.data").invalidValue(result.getFormatType()).build());
        if (!result.isValidSize())
            errors.add(ValidationError.builder().pointer("videoFile-" + attachmentIndex).messageCode("invalid.video.size").invalidValue(result.getSize()).build());
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }

    protected VideoValidationResult isValid(CMarkAttachment video, List<VideoFormat> supportedFormats, Integer maxFileSize) {
        int length = video.getData().length;
        boolean isValidSize = true;
        if (Objects.nonNull(maxFileSize) && (length > maxFileSize))
            isValidSize = false;

        try {
            String fileMimeType = video.getMimeType();
            boolean validType = false;
            for (VideoFormat videoType : supportedFormats)
                if (videoType.getMimeType().equals(fileMimeType))
                    validType = true;

            return new VideoValidationResult(validType, video.getMimeType(), isValidSize, length);
        } catch (Exception e) {
            log.error("Video validation exception...", e);
            return new VideoValidationResult(false, null, isValidSize, length);
        }
    }

    @AllArgsConstructor
    @Data
    private static class VideoValidationResult {
        private boolean validType;
        private String formatType;
        private boolean validSize;
        private Integer size;
    }

}
