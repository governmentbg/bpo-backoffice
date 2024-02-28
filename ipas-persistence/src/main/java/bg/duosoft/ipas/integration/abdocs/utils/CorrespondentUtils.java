package bg.duosoft.ipas.integration.abdocs.utils;

import bg.duosoft.abdocs.model.Correspondent;
import bg.duosoft.abdocs.model.CorrespondentContact;
import bg.duosoft.abdocs.model.CorrespondentType;
import bg.duosoft.abdocs.model.DocCorrespondents;
import bg.duosoft.ipas.util.DefaultValue;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CorrespondentUtils {

    public static CorrespondentType generateCorrespondentType(String countryCode, boolean indCompany) {
        if (countryCode.equals(DefaultValue.BULGARIA_CODE)) {
            if (!indCompany)
                return CorrespondentType.BulgarianCitizen;
            else
                return CorrespondentType.LegalEntity;
        } else {
            if (!indCompany)
                return CorrespondentType.Foreigner;
            else
                return CorrespondentType.ForeignLegalEntity;
        }
    }

    public static boolean isCorrespondentLegalEntity(CorrespondentType correspondentType) {
        if (Objects.isNull(correspondentType)) {
            return false;
        }
        switch (correspondentType) {
            case LegalEntity:
            case ForeignLegalEntity:
                return true;
            default:
                return false;
        }
    }

    public static void fillNameParts(Correspondent res, Boolean indCompany, String personName) {
        if (!indCompany) {
            NameParts nameParts = generateNameParts(false, personName);
            res.setFirstName(nameParts.getFirstName());
            res.setMiddleName(nameParts.getMiddleName());
            res.setLastName(nameParts.getLastName());
        }
    }


    public static NameParts generateNameParts(boolean isCompany, String fullName) {
        String fname = null;
        String sname = null;
        String lname = null;

        if (fullName != null) {
            if (isCompany) {
                lname = fullName;
            } else {
                // the name of a physical person looks one of these ways:
                // firstName lastName
                // firstName secondName lastName
                // title firstName secondName
                // title firstName secondName lastName
                // the title is one (or combination) of these words: юр./д-р/инж./доц./проф./
                String[] names = fullName.split(" ");
                if (names.length >= 3) {
                    lname = names[names.length - 1];
                    sname = names[names.length - 2];
                    fname = "";
                    for (int i = 0; i < names.length - 2; i++) {
                        String part = names[i];
                        fname += part + " ";
                    }

                    String s = fname.toLowerCase()
                            .replaceAll("арх\\. ", "")
                            .replaceAll("акад\\.? ", "")
                            .replaceAll("проф\\.? ", "")
                            .replaceAll("доц\\.? ", "")
                            .replaceAll("д-р\\.? ", "")
                            .replaceAll("юр\\. ", "")
                            .replaceAll("адв\\.? ", "")
                            .replaceAll("инж\\.? ", "")
                            .replaceAll("д\\.?м\\.?н\\.? ", "")
                            .replaceAll("д\\.?м\\.?т\\.? ", "");
                    if ("".equals(s.trim())) {//ako fname se systoi samo ot titli, to kym fname se dobavq i sname, a sname stava null
                        fname += sname;
                        sname = null;
                    } else {
                        fname = fname.substring(0, fname.length() - 1);
                    }
                } else if (names.length == 1) {
                    fname = fullName;
                } else {
                    fname = names[names.length - 2];
                    sname = null;
                    lname = names[names.length - 1];
                }
            }
        }
        return new NameParts(fname, sname, lname);
    }


    public static String getFirstEmailAddress(List<DocCorrespondents> correspondentList) {
        if (!CollectionUtils.isEmpty(correspondentList)) {
            DocCorrespondents docCorrespondent = correspondentList.get(0);
            Correspondent correspondent = docCorrespondent.getCorrespondent();
            if (Objects.nonNull(correspondent)) {
                List<CorrespondentContact> correspondentContacts = correspondent.getCorrespondentContacts();
                if (!CollectionUtils.isEmpty(correspondentContacts)) {
                    return correspondentContacts.get(0).getEmail();
                }
            }
        }
        return null;
    }

}
