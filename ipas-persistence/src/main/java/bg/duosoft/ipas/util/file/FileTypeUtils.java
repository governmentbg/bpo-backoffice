package bg.duosoft.ipas.util.file;

import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.DefaultValue;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileTypeUtils {

    public static String selectIpObjectTypeByFileType(String fileType) {
        FileType fileTypeEnum = FileType.selectByCode(fileType);
        switch (fileTypeEnum) {
            case MARK:
            case INTERNATIONAL_MARK_I:
            case INTERNATIONAL_MARK_R:
            case INTERNATIONAL_MARK_B:
            case INTERNATIONAL_MARK:
            case INTERNATIONAL_NP:
            case DIVISIONAL_MARK:
            case GEOGRAPHICAL_INDICATIONS:
            case ACP:
            case GEOGRAPHICAL_INDICATIONS_V:
                return DefaultValue.MARK_OBJECT_INDICATION;
            case UTILITY_MODEL:
            case PATENT:
            case SPC:
            case DESIGN:
            case PLANTS_AND_BREEDS:
            case EU_PATENT:
            case SINGLE_DESIGN:
            case DIVISIONAL_DESIGN:
            case INTERNATIONAL_DESIGN:
            case INTERNATIONAL_SINGLE_DESIGN:
                return DefaultValue.PATENT_OBJECT_INDICATION;
            default:
                throw new RuntimeException("Cannot find ipobject type for file type " + fileType);
        }
    }

    public static boolean isPatentFileType(String fileType) {
        return selectIpObjectTypeByFileType(fileType).equals(DefaultValue.PATENT_OBJECT_INDICATION);
    }

    public static boolean isMarkFileType(String fileType) {
        return selectIpObjectTypeByFileType(fileType).equals(DefaultValue.MARK_OBJECT_INDICATION);
    }

    public static Map<String, String> getFileTypesOptionsMap(MessageSource messageSource){
        Map<String, String> theMap = new HashMap<>();
        Arrays.stream(FileType.values()).filter(val -> !val.equals(FileType.INTERNATIONAL_MARK) && !val.equals(FileType.DIVISIONAL_DESIGN))
                .forEach(val -> theMap.put(val.code(), messageSource.getMessage("file.type."+val.name(), null, LocaleContextHolder.getLocale())));
        return theMap;
    }
}
