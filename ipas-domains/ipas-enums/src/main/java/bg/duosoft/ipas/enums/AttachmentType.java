package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum AttachmentType {
    VIDEO(Arrays.asList(
            VideoFormat.MP4.getMimeType(),
            VideoFormat.QUICKTIME.getMimeType()
    )),
    AUDIO(Arrays.asList(
            AudioFormat.WAV.getMimeType(),
            AudioFormat.MP3.getMimeType()
    )),
    IMAGE(Arrays.asList(
            ImageFormat.TIFF.getMimeType(),
            ImageFormat.JPEG.getMimeType(),
            ImageFormat.BMP.getMimeType(),
            ImageFormat.PNG.getMimeType(),
            ImageFormat.GIF.getMimeType()
    ));

    AttachmentType(List<String> mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    private List<String> mimeTypes;

    public static AttachmentType selectByMimeType(String mimeType) {
        AttachmentType markSignType = Arrays.stream(AttachmentType.values())
                .filter(attachmentType -> attachmentType.mimeTypes.contains(mimeType))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(markSignType))
            throw new RuntimeException("Cannot find AttachmentType with mime type: " + mimeType);

        return markSignType;
    }
}
