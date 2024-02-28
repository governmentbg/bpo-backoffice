package bg.duosoft.ipas.enums;

public enum VideoFormat {
    MP4("video/mp4"),
    QUICKTIME("video/quicktime");

    private final String mimeType;

    VideoFormat(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
