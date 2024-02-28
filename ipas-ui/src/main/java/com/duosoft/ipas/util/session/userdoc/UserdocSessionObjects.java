package com.duosoft.ipas.util.session.userdoc;

public interface UserdocSessionObjects {

    String SESSION_USERDOC = "sessionUserDocument";
    String SESSION_USERDOC_PROCESS_PARENT_DATA = "sessionUserdocProcessParentData";
    String SESSION_USERDOC_NICE_CLASSES = "sessionUserdocNiceClasses";
    String SESSION_USERDOC_SINGLE_DESIGNS = "sessionUserdocSingleDesigns";
    String SESSION_USERDOC_APPROVED_NICE_CLASSES = "sessionUserdocApprovedNiceClasses";
    String SESSION_USERDOC_APPLICANTS = "sessionUserdocApplicants";
    String SESSION_USERDOC_REPRESENTATIVES = "sessionUserdocRepresentatives";
    String SESSION_USERDOC_SERVICE_PERSON = "sessionUserdocServicePerson";
    String SESSION_USERDOC_ATTORNEY_DATA = "sessionUserdocAttorneyData";
    String SESSION_USERDOC_GRANTORS = "sessionUserdocGrantors";
    String SESSION_USERDOC_GRANTEES = "sessionUserdocGrantees";
    String SESSION_USERDOC_PAYEE = "sessionUserdocPayee";
    String SESSION_USERDOC_PAYER = "sessionUserdocPayer";
    String SESSION_USERDOC_PLEDGER = "sessionUserdocPledger";
    String SESSION_USERDOC_CREDITOR = "sessionUserdocCreditor";
    String SESSION_USERDOC_NEW_OWNERS = "sessionUserdocNewOwners";
    String SESSION_USERDOC_OLD_OWNERS = "sessionUserdocOldOwners";
    String SESSION_USERDOC_ROOT_GROUNDS = "sessionUserdocRootGrounds";
    String SESSION_USERDOC_COURT_APPEALS = "sessionUserdocCourtAppeals";
    String SESSION_USERDOC_REVIEWER_SQUAD = "sessionUserdocReviewerSquad";
    String SESSION_USERDOC_ROOT_GROUND_UPLOADED_IMAGE = "sessionUserdocRootGroundUploadedImage";
    String SESSION_USERDOC_NEW_REPRESENTATIVES = "sessionUserdocNewRepresentatives";
    String SESSION_USERDOC_OLD_REPRESENTATIVES = "sessionUserdocOldRepresentatives";
    String SESSION_USERDOC_REPRESENTATIVES_OF_THE_OWNER = "sessionUserdocRepresentativesOfTheOwner";
    String SESSION_USERDOC_OLD_CORRESPONDENCE_ADDRESS = "sessionUserdocOldCorrespondenceAddress";
    String SESSION_USERDOC_NEW_CORRESPONDENCE_ADDRESS = "sessionUserdocNewCorrespondenceAddress";
    String SESSION_USERDOC_AFFECTED_INVENTOR = "sessionUserdocAffectedInventor";

    static String[] getAllSessionObjectNames() {
        return new String[]{
                SESSION_USERDOC,
                SESSION_USERDOC_PROCESS_PARENT_DATA,
                SESSION_USERDOC_ROOT_GROUNDS,
                SESSION_USERDOC_COURT_APPEALS,
                SESSION_USERDOC_REVIEWER_SQUAD,
                SESSION_USERDOC_ROOT_GROUND_UPLOADED_IMAGE,
                SESSION_USERDOC_NICE_CLASSES,
                SESSION_USERDOC_SINGLE_DESIGNS,
                SESSION_USERDOC_APPROVED_NICE_CLASSES,
                SESSION_USERDOC_APPLICANTS,
                SESSION_USERDOC_REPRESENTATIVES,
                SESSION_USERDOC_SERVICE_PERSON,
                SESSION_USERDOC_ATTORNEY_DATA,
                SESSION_USERDOC_GRANTORS,
                SESSION_USERDOC_GRANTEES,
                SESSION_USERDOC_PAYEE,
                SESSION_USERDOC_PAYER,
                SESSION_USERDOC_PLEDGER,
                SESSION_USERDOC_CREDITOR,
                SESSION_USERDOC_NEW_REPRESENTATIVES,
                SESSION_USERDOC_OLD_REPRESENTATIVES,
                SESSION_USERDOC_REPRESENTATIVES_OF_THE_OWNER,
                SESSION_USERDOC_NEW_CORRESPONDENCE_ADDRESS,
                SESSION_USERDOC_OLD_CORRESPONDENCE_ADDRESS,
                SESSION_USERDOC_NEW_OWNERS,
                SESSION_USERDOC_OLD_OWNERS,
                SESSION_USERDOC_AFFECTED_INVENTOR
        };
    }

    static String[] getPersonSessionObjectNames() {
        return new String[]{
                SESSION_USERDOC_APPLICANTS,
                SESSION_USERDOC_REPRESENTATIVES,
                SESSION_USERDOC_SERVICE_PERSON,
                SESSION_USERDOC_GRANTORS,
                SESSION_USERDOC_GRANTEES,
                SESSION_USERDOC_PAYEE,
                SESSION_USERDOC_PAYER,
                SESSION_USERDOC_PLEDGER,
                SESSION_USERDOC_CREDITOR,
                SESSION_USERDOC_NEW_REPRESENTATIVES,
                SESSION_USERDOC_OLD_REPRESENTATIVES,
                SESSION_USERDOC_NEW_CORRESPONDENCE_ADDRESS,
                SESSION_USERDOC_OLD_CORRESPONDENCE_ADDRESS,
                SESSION_USERDOC_REPRESENTATIVES_OF_THE_OWNER,
                SESSION_USERDOC_NEW_OWNERS,
                SESSION_USERDOC_OLD_OWNERS,
                SESSION_USERDOC_AFFECTED_INVENTOR
        };
    }

}
