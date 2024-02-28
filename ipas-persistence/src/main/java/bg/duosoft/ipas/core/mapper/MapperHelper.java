package bg.duosoft.ipas.core.mapper;

import bg.duosoft.ipas.enums.ImageFormat;
import bg.duosoft.ipas.enums.AudioFormat;

public class MapperHelper {

    public static String getBooleanAsText(Boolean b) {
        return b == null ? null : (b ? "S" : "N");
    }

    public static Boolean getTextAsBoolean(String text) {
        return text == null ? null : ("S".equals(text) ? true : false);
    }

    public static Integer getBooleanAsNumber(Boolean b) {
        return b == null ? null : (b ? 1 : 0);
    }

    public static Boolean getNumberAsBoolean(Integer number) {
        return number == null ? null : (number.equals(1) ? true : false);
    }

    public static String signWcodeToSignType(String signWcode) {
        if (signWcode == null) {
            return "N";
        }
        switch (signWcode) {
            case "D":
                return "N";
            case "F":
                return "L";
            case "M":
                return "B";
            case "T":
                return "T";
            case "S":
                return "S";
            case "O":
                return "O";
            case "C":
                return "C";
            default:
                return "N";
        }
    }

    public static String signTypeToSignWcode(String signType) {
        if (signType == null) {
            return "D";
        }
        switch (signType) {
            case "N":
                return "D";
            case "L":
                return "F";
            case "B":
                return "M";
            case "T":
                return "T";
            case "S":
                return "S";
            case "O":
                return "O";
            case "C":
                return "C";
            default:
                return "D";
        }
    }

    public static String multimediaFormatWcodeToSoundType(Long multimediaFormatWCode) {
        if (multimediaFormatWCode == null)
            return null;

        switch (multimediaFormatWCode.intValue()) {
            case 1:
                return AudioFormat.WAV.getMimeType();
            case 2:
                return AudioFormat.MP3.getMimeType();
            default:
                return null;
        }
    }

    public static Long soundTypeToMultimediaFormatWcode(String soundType) {
        if (soundType == null)
            return null;

        if (soundType.equals(AudioFormat.WAV.getMimeType()))
            return 1L;
        else if (soundType.equals(AudioFormat.MP3.getMimeType()))
            return 2L;
        else
            return null;
    }

    public static String imageFormatWCodeToLogoType(Long imageFormatWcode) {
        if (imageFormatWcode == null)
            return null;

        switch (imageFormatWcode.intValue()) {
            case 1:
                return ImageFormat.TIFF.getMimeType();
            case 2:
                return ImageFormat.JPEG.getMimeType();
            case 3:
                return ImageFormat.BMP.getMimeType();
            case 4:
                return ImageFormat.PNG.getMimeType();
            case 5:
                return ImageFormat.GIF.getMimeType();
            default:
                return null;
        }
    }

    public static Long logoTypeToFormatWCode(String logoType) {
        if (logoType == null)
            return null;

        if (ImageFormat.TIFF.getMimeType().equals(logoType))
            return 1l;
        else if (ImageFormat.JPEG.getMimeType().equals(logoType))
            return 2l;
        else if (ImageFormat.BMP.getMimeType().equals(logoType))
            return 3l;
        else if (ImageFormat.PNG.getMimeType().equals(logoType))
            return 4l;
        else if (ImageFormat.GIF.getMimeType().equals(logoType))
            return 5l;
        else
            return null;
    }
}
