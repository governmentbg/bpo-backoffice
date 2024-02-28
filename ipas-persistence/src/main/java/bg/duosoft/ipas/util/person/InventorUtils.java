package bg.duosoft.ipas.util.person;

import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.core.model.person.CAuthor;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InventorUtils {

    public static List<CAuthor> sortBySequenceNumber(List<CAuthor> authors) {
        if (CollectionUtils.isEmpty(authors))
            return authors;

        List<CAuthor> newAuthors = new ArrayList<>(authors);
        newAuthors.sort(Comparator.comparing(CAuthor::getAuthorSeq));
        return newAuthors;
    }

    public static Long getMaxSequenceNumber(List<CAuthor> authors) {
        if (CollectionUtils.isEmpty(authors))
            return null;

        return authors.stream()
                .map(CAuthor::getAuthorSeq)
                .filter(Objects::nonNull)
                .max(Comparator.comparing(Long::longValue))
                .orElse(0L);
    }

    public static Long getMinSequenceNumber(List<CAuthor> authors) {
        if (CollectionUtils.isEmpty(authors))
            return null;

        return authors.stream()
                .map(CAuthor::getAuthorSeq)
                .filter(Objects::nonNull)
                .min(Comparator.comparing(Long::longValue))
                .orElse(0L);
    }

    public static List<CPerson> convertToCPersonList(CAuthorshipData authorshipData) {
        if (Objects.isNull(authorshipData)) {
            return null;
        }

        List<CAuthor> authorList = authorshipData.getAuthorList();
        if (CollectionUtils.isEmpty(authorList)) {
            return null;
        }

        return authorList.stream().map(CAuthor::getPerson).collect(Collectors.toList());
    }

}
