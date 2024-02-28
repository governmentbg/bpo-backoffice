package bg.duosoft.ipas.core.validation.person;

import bg.duosoft.ipas.core.model.search.CCriteriaPerson;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.PersonIdType;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.person.SearchPersonUtils;
import bg.duosoft.ipas.util.regex.RegexUtils;
import bg.duosoft.ipas.util.validation.PersonValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PersonValidator implements IpasValidator<CPerson> {

    @Autowired
    private PersonService personService;

    @Override
    public List<ValidationError> validate(CPerson person, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        List<String> excludeValidationFields = null;
        if (additionalArgs != null && additionalArgs.length > 0) {
            excludeValidationFields = (List<String>) additionalArgs[0];
        }

        if (StringUtils.isEmpty(person.getNationalityCountryCode())){
            errors.add(ValidationError.builder().pointer("nationalityCountryCode").messageCode("person.empty.nationality.code").build());
        }
        if (StringUtils.isEmpty(person.getResidenceCountryCode())){
            errors.add(ValidationError.builder().pointer("residenceCountryCode").messageCode("person.empty.residence.code").build());
        }

        if (!CollectionUtils.isEmpty(errors)){
            return errors;
        }

        if (StringUtils.isEmpty(person.getPersonName()))
            errors.add(ValidationError.builder().pointer("personName").messageCode("required.field").build());
        else
            validatePhysicalPersonName(errors, person);

        if (!(!CollectionUtils.isEmpty(excludeValidationFields) && excludeValidationFields.contains(PersonValidationUtils.ADDRESS_STREET))) {
            rejectIfEmptyString(errors, person.getAddressStreet(), "addressStreet");
        }

        if (!(!CollectionUtils.isEmpty(excludeValidationFields) && excludeValidationFields.contains(PersonValidationUtils.CITY))) {
            rejectIfEmptyString(errors, person.getCityName(), "cityName");
        }

        String email = person.getEmail();
        if (!StringUtils.isEmpty(email)) {
            rejectIfNotMatchRegex(errors, email, RegexUtils.EMAIL_REGEX, "email", "email.invalid");
        }

        String individualIdTxt = person.getIndividualIdTxt();
        String individualIdType = person.getIndividualIdType();
        if (StringUtils.hasText(individualIdTxt)) {
            rejectIfEmptyString(errors, individualIdType, "individualIdTxt", "person.individualType.not.selected");
            if (CollectionUtils.isEmpty(errors)) {
                PersonIdType personIdType = PersonIdType.valueOf(individualIdType);
                switch (personIdType) {
                    case EIK:
                        rejectIfFalse(errors, person.isCompany() && person.isBulgarianNatinality(), "individualIdTxt", "person.individualType.eik.company");
                        break;
                    case LNCH:
                        rejectIfFalse(errors, person.isPhysicalPerson() && person.isForeigner(), "individualIdTxt", "person.individualType.lnch.company");
                        rejectIfFalse(errors, validateLNCH(individualIdTxt), "individualIdTxt", "person.invalid.LNCH");
                        break;
                    case EGN:
                        rejectIfFalse(errors, person.isPhysicalPerson() && person.isBulgarianNatinality(), "individualIdTxt", "person.individualType.egn.company");
                        rejectIfFalse(errors, validateEGN(individualIdTxt), "individualIdTxt", "person.invalid.EGN");
                        break;
                }
            }
        }


        return errors;
    }

    private void validatePhysicalPersonName(List<ValidationError> errors, CPerson person) {
        Boolean indCompany = person.getIndCompany();
        String nationalityCountryCode = person.getNationalityCountryCode();
        if (Objects.nonNull(indCompany) && !indCompany && DefaultValue.BULGARIA_CODE.equalsIgnoreCase(nationalityCountryCode)) {
            String personName = person.getPersonName().toLowerCase();

            if (!personName.trim().matches("(?u)^(?<title>(?i)(?:арх\\. |акад\\.? |проф\\.? |доц\\.? |д-р\\.? |юр\\.? |адв\\.? |инж\\.? |д\\.?м\\.?н\\.? |д\\.?т\\.?н\\.? )*)(?<firstName>[a-zA-Zа-яA-Я\\-]+) (?:(?<secondName>[a-zA-Zа-яA-Я\\-]+) )?(?<lastName>[a-zA-Zа-яA-Я\\-]+)$"))
                errors.add(ValidationError.builder().pointer("personName").messageCode("person.name.invalid.format").build());
        }
    }

    private void validateExistingPersonsResult(List<ValidationError> errors, CPerson cPerson) {
        if (CollectionUtils.isEmpty(errors)) {
            CCriteriaPerson cCriteriaPerson = SearchPersonUtils.createPersonSearchCriteria(cPerson);
            List<CPerson> persons = personService.findPersons(cCriteriaPerson);
            if (!CollectionUtils.isEmpty(persons)) {
                if (persons.size() != DefaultValue.ONE_RESULT_SIZE) {
                    errors.add(ValidationError.builder().pointer("multipleResultForSearchPerson").messageCode("multiple.persons.exist.on.insert").build());
                }
            }
        }
    }

    public static boolean validateEGN(String input) {
        if (input == null) {
            return false;
        } else if (input.length() != 10) {
            return false;
        } else {

            int[] digits = new int[10];
            int[] coeffs = {2, 4, 8, 5, 10, 9, 7, 3, 6};
            int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};


            for (int i = 0; i < input.length(); i++) {
                Integer digit = parseInteger(input.charAt(i) + "", null);
                if (digit == null) {
                    break;
                }
                digits[i] = digit;
            }

            if (10 != digits.length) {
                return false;
            }

            int dd = digits[4] * 10 + digits[5];
            int mm = digits[2] * 10 + digits[3];
            int yy = digits[0] * 10 + digits[1];
            Integer yyyy = null;

            if (mm >= 1 && mm <= 12) {
                yyyy = 1900 + yy;
            } else if (mm >= 21 && mm <= 32) {
                mm -= 20;
                yyyy = 1800 + yy;
            } else if (mm >= 41 && mm <= 52) {
                mm -= 40;
                yyyy = 2000 + yy;
            } else {
                return false;
            }

            days[1] += isLeapYear(yyyy) ? 1 : 0;

            if (!(dd >= 1 && dd <= days[mm - 1])) {
                return false;
            }

            // Gregorian calendar adoption. 31 Mar 1916 was followed by 14 Apr 1916.
            if (yyyy == 1916 && mm == 4 && (dd >= 1 && dd < 14)) {
                return false;
            }

            int checksum = 0;

            for (int j = 0; j < coeffs.length; j++) {
                checksum = checksum + (digits[j] * coeffs[j]);
            }
            checksum %= 11;
            if (10 == checksum) {
                checksum = 0;
            }

            if (digits[9] != checksum) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateLNCH(String input) {
        if (input == null) {
            return false;
        } else if (input.length() != 10) {
            return false;
        } else {

            int[] digits = new int[10];
            int[] coeffs = {21, 19, 17, 13, 11, 9, 7, 3, 1};
            for (int i = 0; i < input.length(); i++) {
                Integer digit = parseInteger(input.charAt(i) + "", null);
                if (digit == null) {
                    break;
                }
                digits[i] = digit;
            }

            if (10 != digits.length) {
                return false;
            }

            int checksum = 0;

            for (int j = 0; j < coeffs.length; j++) {
                checksum = checksum + (digits[j] * coeffs[j]);
            }
            checksum %= 10;

            if (digits[9] != checksum) {
                return false;
            }
        }
        return true;
    }

    private static boolean isLeapYear(int yyyy) {
        if (yyyy % 400 == 0) {
            return true;
        }
        if (yyyy % 100 == 0) {
            return false;
        }
        if (yyyy % 4 == 0) {
            return true;
        }

        return false;
    }

    public static Integer parseInteger(String value, Integer defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static void main(String[] args) {
        String name = "Иванка Пакиданска и Илияна Маринова";
        name = org.apache.commons.lang3.StringUtils.normalizeSpace(name);
        String regex = "(?u)^(?<title>(?i)(?:арх\\. |акад\\.? |проф\\.? |доц\\.? |д-р\\.? |юр\\.? |адв\\.? |инж\\.? |д\\.?м\\.?н\\.? |д\\.?т\\.?н\\.? )*)(?<firstName>[a-zA-Zа-яA-Я\\-]+) (?:(?<secondName>[a-zA-Zа-яA-Я\\-]+) )?(?<lastName>[a-zA-Zа-яA-Я\\-]+)$";
        Pattern p = Pattern.compile(regex);
//        String regex = "^[\\wа-яA-Я\\-]+ ([\\wа-яA-Я\\-]+ )?[\\wа-яA-Я\\-]+$";
//        Matcher m = p.matcher("АНГЕЛ АТАНАСОВ ВАНГЕЛОВ");
        Matcher m = p.matcher(name);
        if (m.find()) {

            System.out.println("Inside matches....");
            System.out.println("Title:" + m.group("title"));
            System.out.println("FirstName:" + m.group("firstName"));
            System.out.println("SecondName:" + m.group("secondName"));
            System.out.println("LastName:" + m.group("lastName"));
        }
//        System.out.println("АЛА".matches("(?i)(?u)ала"));
//        System.out.println(p.matcher().matches());
//        System.out.println("проф. дтн АНГЕЛ АТАНАСОВ ВАНГЕЛОВ".matches(regex));
    }
}
