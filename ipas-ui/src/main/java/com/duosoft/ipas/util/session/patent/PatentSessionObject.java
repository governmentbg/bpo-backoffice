package com.duosoft.ipas.util.session.patent;

public interface PatentSessionObject {

    String SESSION_PATENT = "sessionPatent";
    String SESSION_PATENT_HASH = "sessionPatentHash";
    String SESSION_SINGLE_DESIGNS ="sessionSingleDesigns";
    String SESSION_SINGLE_DESIGNS_ON_EDIT ="sessionSingleDesignsOnEdit";
    String SESSION_PATENT_PRIORITIES = "sessionPatentPriorities";
    String SESSION_PATENT_CLAIMS="sessionPatentClaims";
    String SESSION_PATENT_CITATIONS="sessionPatentCitations";
    String SESSION_PATENT_DRAWINGS="sessionPatentDrawings";
    String SESSION_PATENT_NEW_DRAWING="sessionPatentDrawing";
    String SESSION_PATENT_OWNERS = "sessionPatentOwners";
    String SESSION_PATENT_IPCS = "sessionPatenIpcs";
    String SESSION_PATENT_CPCS = "sessionPatenCpcs";
    String SESSION_PATENT_REPRESENTATIVES = "sessionPatentRepresentatives";
    String SESSION_PATENT_INVENTORS = "sessionPatentInventors";
    String SESSION_PATENT_SERVICE_PERSON = "sessionPatentServicePerson";
    String SESSION_PATENT_ATTORNEY_DATA = "sessionPatentAttorneyData";
    String SESSION_PATENT_ATTACHMENTS="sessionPatentAttachments";
    String SESSION_PATENT_NEW_ATTACHMENT="sessionPatentAttachment";


    static String[] getAllSessionObjectNames() {
        return new String[]{
                SESSION_PATENT_IPCS,
                SESSION_PATENT_CPCS,
                SESSION_SINGLE_DESIGNS,
                SESSION_SINGLE_DESIGNS_ON_EDIT,
                SESSION_PATENT_PRIORITIES,
                SESSION_PATENT_INVENTORS,
                SESSION_PATENT_OWNERS,
                SESSION_PATENT_REPRESENTATIVES,
                SESSION_PATENT_SERVICE_PERSON,
                SESSION_PATENT_ATTORNEY_DATA,
                SESSION_PATENT,
                SESSION_PATENT_HASH,
                SESSION_PATENT_CLAIMS,
                SESSION_PATENT_DRAWINGS,
                SESSION_PATENT_NEW_DRAWING,
                SESSION_PATENT_ATTACHMENTS,
                SESSION_PATENT_NEW_ATTACHMENT

        };
    }

    static String[] getPersonSessionObjectNames() {
        return new String[]{
                SESSION_PATENT_OWNERS,
                SESSION_PATENT_REPRESENTATIVES,
                SESSION_PATENT_SERVICE_PERSON,
                SESSION_PATENT_INVENTORS
        };
    }
}
