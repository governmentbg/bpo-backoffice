package bg.duosoft.ipas.persistence.model.nonentity;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PersonUsageData {
    private Integer personNumber;
    private Integer addressNumber;
    private int ipMarkOwnersCount;
    private int ipMarkRepresentativesCount;
    private int ipMarkMainOwnerCount;
    private int ipMarkServicePersonCount;
    private int ipPatentOwnersCount;
    private int ipPatentRepresentativesCount;
    private int ipPatentInventorsCount;
    private int ipPatentMainOwnerCount;
    private int ipPatentServicePersonCount;
    private int ipFileMainOwnerCount;
    private int ipFileServicePersonCount;
    private int ipUserdocApplicantCount;
    private int ipUserdocPayeeCount;
    private int ipUserdocPayerCount;
    private int ipUserdocGranteeCount;
    private int ipUserdocGrantorCount;
    private int ipUserdocOldOwnersCount;
    private int ipUserdocNewOwnersCount;
    private int ipUserdocRepresentativeCount;
    private int ipUserdocPersonCount;
    private int ipPersonAbdocsSyncCount;
    private int extReceptionCorrespondentCount;
    private int extReceptionUserdocCorrespondentCount;
    private int agentHistoryCount;
    private int extendedAgentCount;
    private int extendedPersonAddressesCount;
    private int extendedPartnershipCount;
    private int partnershipAgentAgentsCount;
    private int partnershipAgentPartnershipsCount;
    private int partnershipHistoryCount;
    private int acpRepresentativesCount;
    private int acpInfringerCount;
    private int acpServicePersonCount;

    public int getTotal() {
        return this.ipMarkOwnersCount
                + this.ipMarkRepresentativesCount
                + this.ipMarkMainOwnerCount
                + this.ipMarkServicePersonCount
                + this.ipPatentOwnersCount
                + this.ipPatentRepresentativesCount
                + this.ipPatentInventorsCount
                + this.ipPatentMainOwnerCount
                + this.ipPatentServicePersonCount
                + this.ipFileMainOwnerCount
                + this.ipFileServicePersonCount
                + this.ipUserdocApplicantCount
                + this.ipUserdocPayeeCount
                + this.ipUserdocPayerCount
                + this.ipUserdocGranteeCount
                + this.ipUserdocGrantorCount
                + this.ipUserdocOldOwnersCount
                + this.ipUserdocNewOwnersCount
                + this.ipUserdocRepresentativeCount
                + this.ipUserdocPersonCount
                + this.ipPersonAbdocsSyncCount
                + this.extReceptionCorrespondentCount
                + this.extReceptionUserdocCorrespondentCount
                + this.agentHistoryCount
                + this.extendedAgentCount
                + this.extendedPersonAddressesCount
                + this.extendedPartnershipCount
                + this.partnershipAgentAgentsCount
                + this.partnershipAgentPartnershipsCount
                + this.partnershipHistoryCount
                + this.acpRepresentativesCount
                + this.acpInfringerCount
                + this.acpServicePersonCount;
    }

    public Map<String, Integer> getTablesInfo() {
        Map<String, Integer> usedTables = new HashMap<>();

        if(ipMarkOwnersCount > 0) {
            usedTables.put("IPASPROD.IP_MARK_OWNERS", ipMarkOwnersCount);
        }
        if(ipMarkRepresentativesCount > 0) {
            usedTables.put("IPASPROD.IP_MARK_REPRS", ipMarkRepresentativesCount);
        }
        if(ipMarkMainOwnerCount > 0 || ipMarkServicePersonCount > 0) {
            usedTables.put("IPASPROD.IP_MARK", ipMarkOwnersCount+ipMarkServicePersonCount);
        }
        if (ipPatentOwnersCount > 0) {
            usedTables.put("IPASPROD.IP_PATENT_OWNERS ", ipPatentOwnersCount);
        }
        if(ipPatentRepresentativesCount > 0) {
            usedTables.put("IPASPROD.IP_PATENT_REPRS", ipPatentRepresentativesCount);
        }
        if(ipPatentInventorsCount > 0) {
            usedTables.put("IPASPROD.IP_PATENT_INVENTORS", ipPatentInventorsCount);
        }
        if(ipPatentMainOwnerCount > 0 || ipPatentServicePersonCount > 0) {
            usedTables.put("IPASPROD.IP_PATENT", ipPatentMainOwnerCount+ipPatentServicePersonCount);
        }
        if(ipFileMainOwnerCount > 0 || ipFileServicePersonCount > 0) {
            usedTables.put("IPASPROD.IP_FILE", ipFileMainOwnerCount+ipFileServicePersonCount);
        }
        if(ipUserdocApplicantCount > 0 || ipUserdocPayeeCount > 0 || ipUserdocPayerCount > 0 || ipUserdocGranteeCount > 0 || ipUserdocGrantorCount > 0) {
            usedTables.put("IPASPROD.IP_USERDOC", ipUserdocApplicantCount+ipUserdocPayeeCount+ipUserdocPayerCount+ipUserdocGranteeCount+ipUserdocGrantorCount);
        }
        if(ipUserdocOldOwnersCount > 0) {
            usedTables.put("IPASPROD.IP_USERDOC_OLD_OWNERS", ipUserdocOldOwnersCount);
        }
        if(ipUserdocNewOwnersCount > 0) {
            usedTables.put("IPASPROD.IP_USERDOC_NEW_OWNERS", ipUserdocNewOwnersCount);
        }
        if(ipUserdocRepresentativeCount > 0) {
            usedTables.put("IPASPROD.IP_USERDOC_REPRS", ipUserdocRepresentativeCount);
        }
        if(ipUserdocPersonCount > 0) {
            usedTables.put("EXT_CORE.IP_USERDOC_PERSON", ipUserdocPersonCount);
        }
        if(ipPersonAbdocsSyncCount > 0) {
            usedTables.put("EXT_CORE.IP_PERSON_ABDOCS_SYNC", ipPersonAbdocsSyncCount);
        }
        if(extReceptionCorrespondentCount > 0) {
            usedTables.put("EXT_RECEPTION.CORRESPONDENT", extReceptionCorrespondentCount);
        }
        if(extReceptionUserdocCorrespondentCount > 0) {
            usedTables.put("EXT_RECEPTION.USERDOC_CORRESPONDENT", extReceptionUserdocCorrespondentCount);
        }
        if(agentHistoryCount > 0) {
            usedTables.put("EXT_AGENT.AGENT_HISTORY", agentHistoryCount);
        }
        if(extendedAgentCount > 0) {
            usedTables.put("EXT_AGENT.EXTENDED_AGENT", extendedAgentCount);
        }
        if(extendedPersonAddressesCount > 0) {
            usedTables.put("EXT_AGENT.EXTENDED_PERSON_ADDRESSES", extendedPersonAddressesCount);
        }
        if(extendedPartnershipCount > 0) {
            usedTables.put("EXT_AGENT.EXTENDED_PARTNERSHIP", extendedPartnershipCount);
        }
        if(partnershipAgentAgentsCount > 0 || partnershipAgentPartnershipsCount > 0) {
            usedTables.put("EXT_AGENT.PARTNERSHIP_AGENT", partnershipAgentAgentsCount+partnershipAgentPartnershipsCount);
        }
        if(partnershipHistoryCount > 0) {
            usedTables.put("EXT_AGENT.PARTNERSHIP_HISTORY", partnershipHistoryCount);
        }

        if(acpRepresentativesCount > 0) {
            usedTables.put("ext_core.ACP_REPRS", acpRepresentativesCount);
        }
        if(acpInfringerCount > 0) {
            usedTables.put("ACP_INFRINGER", acpInfringerCount);
        }
        if(acpServicePersonCount > 0) {
            usedTables.put("EXT_CORE.ACP_SERVICE_PERSON", acpServicePersonCount);
        }

        return usedTables;
    }
}
