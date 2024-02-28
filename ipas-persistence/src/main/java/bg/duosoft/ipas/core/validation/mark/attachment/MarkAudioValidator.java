package bg.duosoft.ipas.core.validation.mark.attachment;

import bg.duosoft.ipas.core.model.mark.CMarkAttachment;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.AudioFormat;
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
public class MarkAudioValidator implements IpasValidator<CMarkAttachment> {

    @Override
    public List<ValidationError> validate(CMarkAttachment obj, Object... additionalArgs) {
        Integer attachmentIndex = Objects.isNull(additionalArgs) ? null : (Integer) additionalArgs[0];
        Integer maxFileSize = Objects.isNull(additionalArgs) ? null : (Integer) additionalArgs[1];

        List<AudioFormat> supportedFormats = List.of(AudioFormat.MP3);
        SoundValidationResult result = isValid(obj, supportedFormats, maxFileSize);
        List<ValidationError> errors = new ArrayList<>();
        if (!result.isValidType())
            errors.add(ValidationError.builder().pointer("audioFile-" + attachmentIndex).messageCode("invalid.audio.data").invalidValue(result.getFormatType()).build());
        if (!result.isValidSize())
            errors.add(ValidationError.builder().pointer("audioFile-" + attachmentIndex).messageCode("invalid.audio.size").invalidValue(result.getSize()).build());
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }

    protected SoundValidationResult isValid(CMarkAttachment sound, List<AudioFormat> supportedFormats, Integer maxFileSize) {
        int length = sound.getData().length;
        boolean isValidSize = true;
        if (Objects.nonNull(maxFileSize) && (length > maxFileSize))
            isValidSize = false;

        try {
            String fileMimeType = sound.getMimeType();
            boolean validType = false;
            for (AudioFormat soundType : supportedFormats)
                if (soundType.getMimeType().equals(fileMimeType))
                    validType = true;

            return new SoundValidationResult(validType, sound.getMimeType(), isValidSize, length);
        } catch (Exception e) {
            log.error("Sound validation exception...", e);
            return new SoundValidationResult(false, null, isValidSize, length);
        }
    }

    @AllArgsConstructor
    @Data
    private static class SoundValidationResult {
        private boolean validType;
        private String formatType;
        private boolean validSize;
        private Integer size;
    }

}
