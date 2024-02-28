package bg.duosoft.ipas.core.validation.mark;

import bg.duosoft.ipas.core.model.mark.*;
import bg.duosoft.ipas.core.service.mark.MarkAttachmentService;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.MarkSignType;
import bg.duosoft.ipas.util.mark.MarkSignDataAttachmentUtils;
import bg.duosoft.ipas.util.mark.MarkSignTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class MarkSignTypeValidator implements IpasValidator<CMark> {

    @Autowired
    private MarkService markService;

    @Autowired
    private MarkAttachmentService markAttachmentService;

    @Override
    public List<ValidationError> validate(CMark mark, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        CSignData signData = mark.getSignData();
        validateSignType(errors, signData);

        if (CollectionUtils.isEmpty(errors)) {
            MarkSignType markSignType = signData.getSignType();
            switch (markSignType) {
                case WORD:
                    validateWordMark(errors, mark);
                    break;
                case FIGURATIVE:
                    validateFigurativeMark(errors, mark);
                    break;
                case COMBINED:
                    validateCombinedMark(errors, mark);
                    break;
                case THREE_D:
                    validateThreeDMark(errors, mark);
                    break;
                case POSITION:
                    validatePositionMark(errors, mark);
                    break;
                case PATTERN:
                    validatePatternMark(errors, mark);
                    break;
                case COLOUR:
                    validateColourMark(errors, mark);
                    break;
                case SOUND:
                    validateSoundMark(errors, mark);
                    break;
                case MOTION:
                    validateMotionMark(errors, mark);
                    break;
                case MULTIMEDIA:
                    validateMultimediaMark(errors, mark);
                    break;
                case HOLOGRAM:
                    validateHologramMark(errors, mark);
                    break;
                case OTHER:
                    validateOtherMark(errors, mark);
                    break;
            }
        }

        return errors;
    }

    private void validateSignType(List<ValidationError> errors, CSignData signData) {
        if (Objects.isNull(signData) || Objects.isNull(signData.getSignType()))
            errors.add(ValidationError.builder().pointer("signData.signType").messageCode("required.field").build());
    }

    private void validateWordMark(List<ValidationError> errors, CMark mark) {
        if (CollectionUtils.isEmpty(errors)) {
            validateEmptyMarkName(errors, mark);
        }
    }

    private void validateSoundMark(List<ValidationError> errors, CMark mark) {
        if (CollectionUtils.isEmpty(errors)) {
            CSignData signData = mark.getSignData();
            List<CMarkAttachment> attachments = signData.getAttachments();
            List<CMarkAttachment> images = MarkSignDataAttachmentUtils.selectImagesFromAttachments(attachments);
            List<CMarkAttachment> sounds = MarkSignDataAttachmentUtils.selectAudiosFromAttachments(attachments);
            if (CollectionUtils.isEmpty(images) && CollectionUtils.isEmpty(sounds)) {
                errors.add(ValidationError.builder().pointer("singleAudio.error").messageCode("mark.audio.files").build());
            } else {
                if (!CollectionUtils.isEmpty(images)) {
                    validateImageAttachments(errors, mark, signData, images);
                }
                if (!CollectionUtils.isEmpty(sounds)) {
                    validateSoundAttachments(errors, signData, sounds);
                }
            }
        }
    }

    private void validateMotionMark(List<ValidationError> errors, CMark mark) {
        if (CollectionUtils.isEmpty(errors)) {
            imageOrVideoValidation(errors, mark);
        }
    }

    private void imageOrVideoValidation(List<ValidationError> errors, CMark mark) {
        CSignData signData = mark.getSignData();
        List<CMarkAttachment> attachments = signData.getAttachments();
        List<CMarkAttachment> images = MarkSignDataAttachmentUtils.selectImagesFromAttachments(attachments);
        List<CMarkAttachment> videos = MarkSignDataAttachmentUtils.selectVideosFromAttachments(attachments);
        if (CollectionUtils.isEmpty(images) && CollectionUtils.isEmpty(videos)) {
            errors.add(ValidationError.builder().pointer("singleVideo.error").messageCode("mark.video.files").build());
        } else {
            if (!CollectionUtils.isEmpty(images)) {
                validateImageAttachments(errors, mark, signData, images);
            }
            if (!CollectionUtils.isEmpty(videos)) {
                validateVideoAttachments(errors, signData, videos);
            }
        }
    }

    private void validateHologramMark(List<ValidationError> errors, CMark mark) {
        if (CollectionUtils.isEmpty(errors)) {
            imageOrVideoValidation(errors, mark);
        }
    }

    private void validateOtherMark(List<ValidationError> errors, CMark mark) {
        if (CollectionUtils.isEmpty(errors)) {
            CSignData signData = mark.getSignData();
            List<CMarkAttachment> attachments = signData.getAttachments();
            List<CMarkAttachment> images = MarkSignDataAttachmentUtils.selectImagesFromAttachments(attachments);

            String notes = mark.getFile().getNotes();
            if (StringUtils.isEmpty(notes)) {
                errors.add(ValidationError.builder().pointer("file.notes").messageCode("required.field").build());
            }
            if (!CollectionUtils.isEmpty(images)) {
                validateImageAttachments(errors, mark, signData, images);
            }
        }
    }

    private void validateMultimediaMark(List<ValidationError> errors, CMark mark) {
        if (CollectionUtils.isEmpty(errors)) {
            validateEmptyVideo(errors, mark);
        }
    }

    private void validateFigurativeMark(List<ValidationError> errors, CMark mark) {
        if (CollectionUtils.isEmpty(errors)) {
            validateEmptyLogo(errors, mark);
        }
    }

    private void validateThreeDMark(List<ValidationError> errors, CMark mark) {
        if (CollectionUtils.isEmpty(errors)) {
            validateEmptyLogo(errors, mark);
        }
    }

    private void validatePositionMark(List<ValidationError> errors, CMark mark) {
        if (CollectionUtils.isEmpty(errors)) {
            validateEmptyLogo(errors, mark);
        }
    }

    private void validatePatternMark(List<ValidationError> errors, CMark mark) {
        if (CollectionUtils.isEmpty(errors)) {
            validateEmptyLogo(errors, mark);
        }
    }

    private void validateColourMark(List<ValidationError> errors, CMark mark) {
        if (CollectionUtils.isEmpty(errors)) {
            validateEmptyLogo(errors, mark);
        }
    }

    private void validateCombinedMark(List<ValidationError> errors, CMark mark) {
        if (CollectionUtils.isEmpty(errors)) {
            validateEmptyMarkName(errors, mark);
            validateEmptyLogo(errors, mark);
        }
    }

    private void validateEmptyVideo(List<ValidationError> errors, CMark mark) {
        if (CollectionUtils.isEmpty(errors)) {
            CSignData signData = mark.getSignData();
            List<CMarkAttachment> attachments = signData.getAttachments();
            List<CMarkAttachment> videos = MarkSignDataAttachmentUtils.selectVideosFromAttachments(attachments);
            if (CollectionUtils.isEmpty(videos)) {
//                errors.add(ValidationError.builder().pointer("singleVideo.error").messageCode("required.field").build());
            } else {
                validateVideoAttachments(errors, signData, videos);
            }
        }
    }


    private void validateEmptyLogo(List<ValidationError> errors, CMark mark) {
        if (CollectionUtils.isEmpty(errors)) {
            CSignData signData = mark.getSignData();
            List<CMarkAttachment> attachments = signData.getAttachments();
            List<CMarkAttachment> imageAttachment = MarkSignDataAttachmentUtils.selectImagesFromAttachments(attachments);

            if (CollectionUtils.isEmpty(imageAttachment)) {
                errors.add(ValidationError.builder().pointer("singleImage.error").messageCode("required.field").build());
            } else {
                validateImageAttachments(errors, mark, signData, imageAttachment);
            }
        }
    }

    private void validateImageAttachments(List<ValidationError> errors, CMark mark, CSignData signData, List<CMarkAttachment> images) {
        if (MarkSignTypeUtils.isMarkContainOneImage(signData.getSignType())) {
            if (images.size() > 1) {
                throw new RuntimeException("Mark with type " + signData.getSignType() + " can contain only one image ! Number of images: " + images.size());
            }

            int firstImageIndex = 0;
            byte[] imageData = new byte[0];
            CMarkAttachment markAttachment = images.get(firstImageIndex);
            if (markAttachment.isLoaded()) {
                imageData = markAttachment.getData();
            } else {
                CLogo cLogo = markService.selectMarkLogo(mark.getFile().getFileId());
                if (Objects.nonNull(cLogo)) {
                    imageData = cLogo.getLogoData();
                }
            }

            if (Objects.isNull(imageData) || imageData.length < 1)
                errors.add(ValidationError.builder().pointer("imageFile-" + firstImageIndex).messageCode("required.field").build());

        } else {
            //TODO Mark with more than one images
        }
    }

    private void validateSoundAttachments(List<ValidationError> errors, CSignData signData, List<CMarkAttachment> sounds) {
        if (MarkSignTypeUtils.isMarkContainOneSound(signData.getSignType())) {
            if (sounds.size() > 1) {
                throw new RuntimeException("Mark with type " + signData.getSignType() + " can contain only one sound ! Number of sounds: " + sounds.size());
            }

            int first = 0;
            byte[] data = new byte[0];
            CMarkAttachment sound = sounds.get(first);
            if (sound.isLoaded()) {
                data = sound.getData();
            } else {
                Integer id = sound.getId();
                if (Objects.nonNull(id)) {
                    CMarkAttachment databaseSound = markAttachmentService.selectAttachmentById(id, true);
                    if (Objects.nonNull(databaseSound)) {
                        data = databaseSound.getData();
                    }
                }
            }

            if (Objects.isNull(data) || data.length < 1)
                errors.add(ValidationError.builder().pointer("audioFile-" + first).messageCode("required.field").build());

        } else {
            //TODO Mark with more than one sounds
        }
    }

    private void validateVideoAttachments(List<ValidationError> errors, CSignData signData, List<CMarkAttachment> videos) {
        if (MarkSignTypeUtils.isMarkContainOneVideo(signData.getSignType())) {
            if (videos.size() > 1) {
                throw new RuntimeException("Mark with type " + signData.getSignType() + " can contain only one video ! Number of videos: " + videos.size());
            }

            int first = 0;
            byte[] data = new byte[0];
            CMarkAttachment video = videos.get(first);
            if (video.isLoaded()) {
                data = video.getData();
            } else {
                Integer id = video.getId();
                if (Objects.nonNull(id)) {
                    CMarkAttachment databaseSound = markAttachmentService.selectAttachmentById(id, true);
                    if (Objects.nonNull(databaseSound)) {
                        data = databaseSound.getData();
                    }
                }
            }

            if (Objects.isNull(data) || data.length < 1)
                errors.add(ValidationError.builder().pointer("videoFile-" + first).messageCode("required.field").build());

        } else {
            //TODO Mark with more than one videos
        }
    }


    private void validateEmptyMarkName(List<ValidationError> errors, CMark mark) {
        CSignData signData = mark.getSignData();
        String markName = signData.getMarkName();
        if (StringUtils.isEmpty(markName))
            errors.add(ValidationError.builder().pointer("signData.markName").messageCode("required.field").build());
    }
}
