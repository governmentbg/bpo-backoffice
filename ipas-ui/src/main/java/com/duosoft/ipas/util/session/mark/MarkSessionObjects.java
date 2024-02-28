package com.duosoft.ipas.util.session.mark;

public interface MarkSessionObjects {

    String SESSION_MARK = "sessionMark";
    String SESSION_MARK_OWNERS = "sessionMarkOwners";
    String SESSION_MARK_REPRESENTATIVES = "sessionMarkRepresentatives";
    String SESSION_MARK_SERVICE_PERSON = "sessionMarkServicePerson";
    String SESSION_MARK_ATTORNEY_DATA = "sessionMarkAttorneyData";
    String SESSION_ACP_SERVICE_PERSON = "sessionAcpServicePerson";
    String SESSION_ACP_INFRINGER = "sessionAcpInfringer";
    String SESSION_ACP_REPRESENTATIVES = "sessionAcpRepresentatives";
    String SESSION_MARK_NICE_CLASSES = "sessionMarkNiceClasses";
    String SESSION_MARK_INTL_REPLACEMENT_NICE_CLASSES = "sessionMarkIntlReplacementNiceClasses";
    String SESSION_MARK_ACP_AFFECTED_OBJECTS = "sessionMarkAcpAffectedObjects";
    String SESSION_MARK_ACP_VIOLATION_PLACES = "sessionMarkAcpViolationPlaces";
    String SESSION_MARK_ACP_TAKEN_ITEMS = "sessionMarkAcpTakenItems";
    String SESSION_MARK_ACP_CHECK_DATA = "sessionMarkAcpCheckData";
    String SESSION_MARK_PRIORITIES = "sessionMarkPriorities";
    String SESSION_MARK_ATTACHMENTS = "sessionMarkAttachments";
    String SESSION_MARK_USAGE_RULES="sessionMarkUsageRules";
    String SESSION_MARK_NEW_USAGE_RULE="sessionMarkNewUsageRule";

    static String[] getAllSessionObjectNames() {
        return new String[]{
                SESSION_MARK,
                SESSION_MARK_OWNERS,
                SESSION_MARK_REPRESENTATIVES,
                SESSION_MARK_SERVICE_PERSON,
                SESSION_MARK_ATTORNEY_DATA,
                SESSION_ACP_SERVICE_PERSON,
                SESSION_ACP_INFRINGER,
                SESSION_ACP_REPRESENTATIVES,
                SESSION_MARK_NICE_CLASSES,
                SESSION_MARK_INTL_REPLACEMENT_NICE_CLASSES,
                SESSION_MARK_ACP_AFFECTED_OBJECTS,
                SESSION_MARK_ACP_VIOLATION_PLACES,
                SESSION_MARK_ACP_TAKEN_ITEMS,
                SESSION_MARK_ACP_CHECK_DATA,
                SESSION_MARK_ATTACHMENTS,
                SESSION_MARK_USAGE_RULES,
                SESSION_MARK_NEW_USAGE_RULE,
                SESSION_MARK_PRIORITIES};
    }

    static String[] getPersonSessionObjectNames() {
        return new String[]{
                SESSION_MARK_OWNERS,
                SESSION_MARK_REPRESENTATIVES,
                SESSION_MARK_SERVICE_PERSON,
                SESSION_ACP_SERVICE_PERSON,
                SESSION_ACP_INFRINGER,
                SESSION_ACP_REPRESENTATIVES
        };
    }
}
