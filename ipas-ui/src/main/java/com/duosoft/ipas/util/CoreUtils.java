package com.duosoft.ipas.util;


import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CViennaClass;
import bg.duosoft.ipas.enums.FileType;
import com.duosoft.ipas.controller.ipobjects.marklike.mark.MarkViennaController;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

public class CoreUtils {
    public static final int HOME_PANEL_MAX_TEXT_LENGTH = 80;
    public static final int HOME_PANEL_MAX_TEXT_LENGTH_50 = 50;
    public static final String FILING_NUMBER_DELIMITER = "/";
    public static final int FIRST_CHAR_INDEX = 0;
    private static final String CONTINUE_TEXT_SIGN = "...";
    private static final String ZERO_CHAR_APPENDER = "0";
    private static final int TWO_DIGITS_NUMBER_LENGTH = 2;

    public static CFileId createCFileId(String filingNumber) {
        if (StringUtils.isEmpty(filingNumber))
            return null;

        String[] parts = filingNumber.split("/");

        CFileId cFileId = new CFileId();
        cFileId.setFileSeq(parts[0]);
        cFileId.setFileType(parts[1]);
        cFileId.setFileSeries(Integer.valueOf(parts[2]));
        cFileId.setFileNbr(Integer.valueOf(parts[3]));
        return cFileId;
    }

    public static boolean isEmptyCFileId(CFileId fileId) {
        return fileId == null || fileId.getFileNbr() == null || fileId.getFileSeries() == null || StringUtils.isEmpty(fileId.getFileType()) || StringUtils.isEmpty(fileId.getFileSeq());
    }

    public static String createFilingNumber(CFileId id, boolean isFileNbrEightDigits) {
        if (Objects.isNull(id))
            return null;

        return createFilingNumber(id.getFileSeq(), id.getFileType(), id.getFileSeries(), id.getFileNbr(), isFileNbrEightDigits);
    }

    public static String createFullRegistrationNumber(Long registrationNbr,String registrationDup,String fileType) {
        if (Objects.nonNull(registrationNbr) && Objects.nonNull(fileType) && fileType.equals(FileType.EU_PATENT.code())) {
            return registrationNbr.toString();
        }

        if (Objects.nonNull(registrationNbr) && Objects.nonNull(registrationDup)) {
            return registrationNbr.toString().concat(registrationDup);
        }
        if (Objects.nonNull(registrationNbr)) {
            return registrationNbr.toString();
        }
        return null;
    }

    public static String createFilingNumber(String fileSeq, String fileTyp, Integer fileSer, Integer fileNumber, boolean isFileNbrEightDigits) {
        StringBuilder fileNbr = Optional.ofNullable(String.valueOf(fileNumber))
                .map(StringBuilder::new)
                .orElse(null);
        if (Objects.isNull(fileNbr))
            return null;

        if (isFileNbrEightDigits)
            while (fileNbr.length() < 8)
                fileNbr.insert(FIRST_CHAR_INDEX, ZERO_CHAR_APPENDER);

        return fileSeq + FILING_NUMBER_DELIMITER + fileTyp + FILING_NUMBER_DELIMITER + fileSer + FILING_NUMBER_DELIMITER + fileNbr;
    }

    public static String formatViennaClass(CViennaClass cViennaClass) {
        String viennaCategory = String.valueOf(cViennaClass.getViennaCategory());
        String viennaDivision = String.valueOf(cViennaClass.getViennaDivision());
        String viennaSection = String.valueOf(cViennaClass.getViennaSection());
        return toTwoDigits(viennaCategory) + MarkViennaController.VIENNA_CLASS_DELIMITER + toTwoDigits(viennaDivision) + MarkViennaController.VIENNA_CLASS_DELIMITER + toTwoDigits(viennaSection);
    }

    public static String getUrlPrefixByFileType(String fileType) {
        return getUrlPrefixByFileType(FileType.selectByCode(fileType));
    }

    public static String getUrlPrefixByFileType(FileType fileType) {
        switch (fileType) {
            case INTERNATIONAL_MARK_I:
            case INTERNATIONAL_MARK_R:
            case INTERNATIONAL_MARK_B:
                return "international_mark";
            case MARK:
            case DIVISIONAL_MARK:
                return "mark";
            case ACP:
                return "acp";
            case PATENT:
                return "patent";
            case EU_PATENT:
                return "eupatent";
            case DESIGN:
            case INTERNATIONAL_DESIGN:
                return "design";
            case UTILITY_MODEL:
                return "utility_model";
            case GEOGRAPHICAL_INDICATIONS:
                return "geographical_indications";
            case PLANTS_AND_BREEDS:
                return "plants_and_breeds";
            case SPC:
                return "spc";
            case USERDOC:
                return "userdoc";
            default:
                throw new RuntimeException("Unknown prefix for fileType: " + fileType);
        }
    }

    public static FileType getFileTypeByFilingNumber(String filingNumber) {
        if (StringUtils.isEmpty(filingNumber))
            return null;
        String[] split = filingNumber.split("/");
        String fileType = split[1];


        for (FileType e : FileType.values()) {
            if (e.code().equals(fileType))
                return e;
        }

        return null;

    }

    private static String toTwoDigits(String value) {
        if (value.length() < TWO_DIGITS_NUMBER_LENGTH) {
            while (value.length() < TWO_DIGITS_NUMBER_LENGTH)
                value = ZERO_CHAR_APPENDER + value;
        }
        return value;
    }

    public static String getShortNameForHomePagePanel(String value) {
        if (StringUtils.isEmpty(value) || value.length() < HOME_PANEL_MAX_TEXT_LENGTH)
            return value;

        return value.substring(FIRST_CHAR_INDEX, HOME_PANEL_MAX_TEXT_LENGTH) + CONTINUE_TEXT_SIGN;
    }


    public static String generateContextUrl(HttpServletRequest request) {
        String serverName = request.getServerName().toLowerCase();
        String scheme = request.getScheme();
        int port = request.getServerPort();

        String val = scheme + "://" + serverName;
        if (("http".equals(scheme) && port == 80) || ("https".equals(scheme) && port == 443)) {
            //do nothing, else add port
        } else {
            val += ":" + port;
        }
        val += request.getContextPath();
        return val;
    }

    public static String toSixSymbols(String text) {
        StringBuilder resultText = Optional.ofNullable(String.valueOf(text))
                .map(StringBuilder::new)
                .orElse(null);

        if (Objects.isNull(resultText))
            return null;

        while (resultText.length() < 6)
            resultText.insert(FIRST_CHAR_INDEX, ZERO_CHAR_APPENDER);

        return resultText.toString();
    }


    public static boolean isBigDecimal(String searchText) {
        boolean isSearchTextNumber = true;

        try {
            new BigDecimal(searchText);
        } catch (Exception e) {
            isSearchTextNumber = false;
        }
        return isSearchTextNumber;
    }


    public static boolean isNumber(String searchText) {
        boolean isSearchTextNumber = true;

        try {
            Integer.valueOf(searchText);
        } catch (Exception e) {
            isSearchTextNumber = false;
        }
        return isSearchTextNumber;
    }

    public static String getRequestParametersAsString(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();

        if (parameterMap.size() == 0)
            return null;

        StringBuilder builder = new StringBuilder();
        Set<String> keySet = parameterMap.keySet();
        List<String> list = new ArrayList<>(keySet);
        for (int i = 0; i < list.size(); i++) {
            String key = list.get(i);
            String[] value = parameterMap.get(key);
            if (i == 0)
                builder.append("?").append(key).append("=").append(value[0]);
            else
                builder.append("&").append(key).append("=").append(value[0]);
        }

        return builder.toString();
    }

    public static String replaceForwardSlashWithUnderscore(String text) {
        if (StringUtils.isEmpty(text)) {
            return text;
        }

        return text.replaceAll("/", "_");
    }

}
