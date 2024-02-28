package bg.duosoft.ipas.util.mark;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CInternationalNiceClass;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.parser.IntegerUtils;
import org.springframework.data.util.Pair;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class InternationalMarkUtils {

    public static boolean isInternationalMark(CFileId fileId) {
        if (Objects.isNull(fileId)) {
            return false;
        }

        return FileType.INTERNATIONAL_MARK_I.code().equals(fileId.getFileType()) || FileType.INTERNATIONAL_MARK_R.code().equals(fileId.getFileType()) || FileType.INTERNATIONAL_MARK_B.code().equals(fileId.getFileType());
    }

    public static List<CInternationalNiceClass> filterNiceClassesByTagCode(List<CInternationalNiceClass> internationalNiceClasses, String tagCode) {
        List<CInternationalNiceClass> filteredNiceClasses = new ArrayList<>();

        for (CInternationalNiceClass niceClass : internationalNiceClasses) {
            if (niceClass.getTagCode().equals(tagCode)) {
                filteredNiceClasses.add(niceClass);
            }
        }
        return filteredNiceClasses;
    }

    public static Pair<Optional<Integer>, Optional<String>> separateRegistrationNumberAndDup(String registrationNumberWithDup) {
        if (!StringUtils.hasText(registrationNumberWithDup)) {
            return null;
        }

        String registrationNbrString = registrationNumberWithDup.replaceAll("[A-Za-z]+$", "");
        Integer registrationNumber = IntegerUtils.tryParseInt(registrationNbrString, null);

        String registrationDup = registrationNumberWithDup.replaceAll("^[0-9]+", "");
        if (!StringUtils.hasText(registrationDup)) {
            registrationDup = null;
        }

        return Pair.of(Optional.ofNullable(registrationNumber), Optional.ofNullable(registrationDup));
    }
}
