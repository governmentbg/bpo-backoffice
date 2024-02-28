package com.duosoft.ipas.util;

import bg.duosoft.ipas.persistence.model.nonentity.SimplePersonAddressResult;

import java.util.*;

public class PersonAdministrationUtils {

    public static final String DUPLICATE_PERSONS_REMOVE_CHARACTERS_REGEX = "[-:/.', \"\t№]";

    public static Map<String, List<SimplePersonAddressResult>> createDuplicatedPersonsMap(List<SimplePersonAddressResult> persons) {
        Map<String, List<SimplePersonAddressResult>> duplicateRecords = new LinkedHashMap<>();
        for (SimplePersonAddressResult person : persons) {
            String checkedText = person.getCheckText();
            boolean isExist = duplicateRecords.containsKey(checkedText);
            if (isExist) {
                List<SimplePersonAddressResult> duplicates = duplicateRecords.get(checkedText);
                duplicates.add(person);
            } else {
                List<SimplePersonAddressResult> alternatives = new LinkedList<>();
                alternatives.add(person);
                duplicateRecords.put(checkedText, alternatives);
            }
        }
        removeRecordsWithoutDuplicates(duplicateRecords);
        return duplicateRecords;
    }

    public static void removeRecordsWithoutDuplicates(Map<String, List<SimplePersonAddressResult>> duplicateRecords) {
        List<String> removeKeys = new ArrayList<>();
        Set<String> strings = duplicateRecords.keySet();
        for (String string : strings) {
            List<SimplePersonAddressResult> value = duplicateRecords.get(string);
            if (value.size() < 2) {
                removeKeys.add(string);
            }
        }
        for (String removeKey : removeKeys) {
            duplicateRecords.remove(removeKey);
        }
    }

}
