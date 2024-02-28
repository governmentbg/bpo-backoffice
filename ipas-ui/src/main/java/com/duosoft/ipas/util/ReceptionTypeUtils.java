package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.reception.CReceptionType;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.ReceptionType;
import com.duosoft.ipas.webmodel.ReceptionForm;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public class ReceptionTypeUtils {

    public static boolean isEuPatent(ReceptionForm receptionForm) {
        return receptionForm.getReceptionType().equals(ReceptionType.EU_PATENT.code());
    }

    public static boolean isUserdoc(ReceptionForm receptionForm) {
        return receptionForm.getReceptionType().equals(ReceptionType.USERDOC.code());
    }

    public static boolean isMark(ReceptionForm receptionForm) {
        return receptionForm.getReceptionType().equals(ReceptionType.MARK.code());
    }

    public static ReceptionType selectReceptionTypeByFileType(FileType fileType) {
        switch (fileType) {
            case MARK:
            case DIVISIONAL_MARK:
                return ReceptionType.MARK;
            case PATENT:
                return ReceptionType.PATENT;
            case DESIGN:
                return ReceptionType.DESIGN;
            case SPC:
                return ReceptionType.SPC;
            case EU_PATENT:
                return ReceptionType.EU_PATENT;
            case UTILITY_MODEL:
                return ReceptionType.UTILITY_MODEL;
            case GEOGRAPHICAL_INDICATIONS:
                return ReceptionType.GEOGRAPHICAL_INDICATION;
            case PLANTS_AND_BREEDS:
                return ReceptionType.SORT_AND_BREEDS;
            case USERDOC:
                return ReceptionType.USERDOC;
            default:
                throw new RuntimeException("Cannot find Reception type for file type = " + fileType.toString());
        }
    }

    public static FileType selectFileTypeByReceptionType(ReceptionType receptionType) {
        switch (receptionType) {
            case MARK:
                return FileType.MARK;
            case PATENT:
                return FileType.PATENT;
            case SPC:
                return FileType.SPC;
            case UTILITY_MODEL:
                return FileType.UTILITY_MODEL;
            case GEOGRAPHICAL_INDICATION:
                return FileType.GEOGRAPHICAL_INDICATIONS;
            case DESIGN:
                return FileType.DESIGN;
            case SORT_AND_BREEDS:
                return FileType.PLANTS_AND_BREEDS;
            case EU_PATENT:
                return FileType.EU_PATENT;
            case USERDOC:
                return FileType.USERDOC;
            default:
                throw new RuntimeException("Cannot find File type for reception type = " + receptionType.name());
        }
    }


    public static String selectReceptionTypeName(MessageSource messageSource, Integer receptionType) {
        ReceptionType receptionTypeEnum = ReceptionType.selectByCode(receptionType);
        switch (receptionTypeEnum) {
            case MARK:
                return messageSource.getMessage("reception.form.ipobject.label.MARK", null, LocaleContextHolder.getLocale());
            case PATENT:
                return messageSource.getMessage("reception.form.ipobject.label.PATENT", null, LocaleContextHolder.getLocale());
            case SPC:
                return messageSource.getMessage("reception.form.ipobject.label.name.SPC", null, LocaleContextHolder.getLocale());
            case UTILITY_MODEL:
                return messageSource.getMessage("reception.form.ipobject.label.UTILITY_MODEL", null, LocaleContextHolder.getLocale());
            case GEOGRAPHICAL_INDICATION:
                return messageSource.getMessage("reception.form.ipobject.label.GEOGRAPHICAL_INDICATION", null, LocaleContextHolder.getLocale());
            case DESIGN:
                return messageSource.getMessage("reception.form.ipobject.label.DESIGN", null, LocaleContextHolder.getLocale());
            case SORT_AND_BREEDS:
                return messageSource.getMessage("reception.form.ipobject.label.PLANTS_AND_BREEDS", null, LocaleContextHolder.getLocale());
            case EU_PATENT:
                return messageSource.getMessage("reception.form.ipobject.label.EU_PATENT", null, LocaleContextHolder.getLocale());
        }
        return messageSource.getMessage("reception.form.ipobject.label.OTHER", null, LocaleContextHolder.getLocale());
    }

    public static String selectReceptionTypeShortTitle(List<CReceptionType> receptionTypes, ReceptionType receptionTypeEnum) {
        if (CollectionUtils.isEmpty(receptionTypes) || Objects.isNull(receptionTypeEnum))
            return null;

        try {
            FileType fileType = selectFileTypeByReceptionType(receptionTypeEnum);
            CReceptionType result = receptionTypes.stream()
                    .filter(cReceptionType -> cReceptionType.getFileType().equals(fileType.code()))
                    .findFirst()
                    .orElse(null);
            return Objects.isNull(result) ? null : result.getShortTitle();
        } catch (Exception e) {
            return null;
        }
    }


    public static CReceptionType selectReceptionTypeById(List<CReceptionType> receptionTypes, Integer receptionTypeId) {
        if (CollectionUtils.isEmpty(receptionTypes) || Objects.isNull(receptionTypeId))
            return null;

        return receptionTypes.stream()
                .filter(cReceptionType -> cReceptionType.getId().equals(receptionTypeId))
                .findFirst()
                .orElse(null);
    }

}
