package bg.duosoft.ipas.enums;

public enum ImageFormat {
    TIFF("image/tiff"),
    JPEG("image/jpeg"),
    BMP("image/bmp"),
    PNG("image/png"),
    GIF("image/gif");

    private final String mimeType;

    ImageFormat(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
