package bg.duosoft.ipas.enums;

public enum AudioFormat {
    WAV("audio/vnd.wave"),
    MP3("audio/mpeg");

    private final String mimeType;

    AudioFormat(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
