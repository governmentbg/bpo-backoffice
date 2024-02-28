package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.enums.FileType;

public class IdentityPanelUtils {

    public static boolean showFilingDateFragment(CFileId id) {
        FileType fileType = FileType.selectByCode(id.getFileType());
        switch (fileType) {
            case ACP:
                return false;
            default:
                return true;
        }
    }

    public static boolean showSubmissionDateFragment(CFileId id) {
        FileType fileType = FileType.selectByCode(id.getFileType());
        switch (fileType) {
            case ACP:
                return true;
            default:
                return false;
        }
    }


    public static boolean showEntitlementDateFragment(CFileId id) {
        FileType fileType = FileType.selectByCode(id.getFileType());
        switch (fileType) {
            case ACP:
                return false;
            default:
                return true;
        }
    }

    public static boolean showExpirationDateFragment(CFileId id) {
        FileType fileType = FileType.selectByCode(id.getFileType());
        switch (fileType) {
            case ACP:
                return false;
            default:
                return true;
        }
    }

    public static boolean showRegistrationDateFragment(CFileId id) {
        FileType fileType = FileType.selectByCode(id.getFileType());
        switch (fileType) {
            case ACP:
                return false;
            default:
                return true;
        }
    }


    public static boolean showRegistrationNumberFragment(CFileId id) {
        FileType fileType = FileType.selectByCode(id.getFileType());
        switch (fileType) {
            case ACP:
                return false;
            default:
                return true;
        }
    }


    public static boolean showRegistrationDupFragment(CFileId id) {
        FileType fileType = FileType.selectByCode(id.getFileType());
        switch (fileType) {
            case GEOGRAPHICAL_INDICATIONS:
            case INTERNATIONAL_MARK_R:
            case MARK:
            case INTERNATIONAL_MARK_I:
            case DIVISIONAL_MARK:
            case INTERNATIONAL_MARK_B:
                return true;
            default:
                return false;
        }
    }

    public static boolean showReceptionRequestFragment(CFileId id) {
        FileType fileType = FileType.selectByCode(id.getFileType());
        switch (fileType) {
            case ACP:
                return false;
            default:
                return true;
        }
    }

    public static boolean showPublicationTypeFragment(CFileId id) {
        FileType fileType = FileType.selectByCode(id.getFileType());
        switch (fileType) {
            case ACP:
                return false;
            default:
                return true;
        }
    }


    public static boolean showIndFaxReceptionFragment(CFileId id) {
        FileType fileType = FileType.selectByCode(id.getFileType());
        switch (fileType) {
            case ACP:
                return false;
            default:
                return true;
        }
    }
}
