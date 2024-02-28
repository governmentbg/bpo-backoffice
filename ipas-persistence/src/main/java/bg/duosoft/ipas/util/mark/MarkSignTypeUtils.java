package bg.duosoft.ipas.util.mark;

import bg.duosoft.ipas.enums.MarkSignType;

import java.util.Objects;

public class MarkSignTypeUtils {

    public static boolean isMarkContainName(MarkSignType signType) {
        if (Objects.isNull(signType))
            return false;

        switch (signType) {
            case WORD:
            case COMBINED:
            case OTHER:
            case THREE_D:
                return true;
            default:
                return false;
        }
    }

    public static boolean isMarkContainOneImage(MarkSignType signType) {
        if (Objects.isNull(signType))
            return false;

        switch (signType) {
            case COMBINED:
            case FIGURATIVE:
            case THREE_D:
            case POSITION:
            case PATTERN:
            case COLOUR:
            case SOUND:
            case HOLOGRAM:
            case OTHER:
            case MOTION:
                return true;
            default:
                return false;
        }
    }

    public static boolean isMarkContainAudio(MarkSignType signType) {
        if (Objects.isNull(signType))
            return false;

        return signType == MarkSignType.SOUND;
    }

    public static boolean isMarkContainVideo(MarkSignType signType) {
        if (Objects.isNull(signType))
            return false;

        switch (signType) {
            case MOTION:
            case HOLOGRAM:
            case MULTIMEDIA:
                return true;
            default:
                return false;
        }
    }

    public static boolean isMarkContainOneSound(MarkSignType signType) {
        if (Objects.isNull(signType))
            return false;

        switch (signType) {
            case SOUND:
                return true;
            default:
                return false;
        }
    }

    public static boolean isMarkContainOneVideo(MarkSignType signType) {
        if (Objects.isNull(signType))
            return false;

        switch (signType) {
            case MOTION:
            case HOLOGRAM:
            case MULTIMEDIA:
                return true;
            default:
                return false;
        }
    }

    public static boolean isMarkContainImages(MarkSignType signType) {
        if (Objects.isNull(signType))
            throw new RuntimeException("Sign type is empty !");

        switch (signType) {
            case COMBINED:
            case FIGURATIVE:
            case THREE_D:
            case POSITION:
            case PATTERN:
            case COLOUR:
            case SOUND:
            case HOLOGRAM:
            case OTHER:
            case MOTION:
                return true;
            default:
                return false;
        }
    }

    public static boolean isMarkContainAttachments(MarkSignType signType) {
        if (Objects.isNull(signType))
            throw new RuntimeException("Sign type is empty !");

        switch (signType) {
            case WORD:
                return false;
            default:
                return true;
        }

    }

}
