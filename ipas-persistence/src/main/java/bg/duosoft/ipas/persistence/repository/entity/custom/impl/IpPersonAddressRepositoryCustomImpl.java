package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddressesPK;
import bg.duosoft.ipas.persistence.model.nonentity.PersonUsageData;
import bg.duosoft.ipas.persistence.model.nonentity.SimplePersonAddressResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpPersonAddressRepositoryCustom;
import bg.duosoft.ipas.persistence.repository.entity.custom.SequenceRepository;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.filter.MergePersonFilter;
import bg.duosoft.ipas.core.model.search.CCriteriaPerson;
import bg.duosoft.ipas.util.person.TemporaryNumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static bg.duosoft.ipas.util.DefaultValue.PARTNERSHIP_PREFIX;

@Slf4j
@Repository
public class IpPersonAddressRepositoryCustomImpl extends BaseRepositoryCustomImpl implements IpPersonAddressRepositoryCustom {

    @Autowired
    private SequenceRepository sequenceRepository;

    @Override
    public IpPersonAddresses mergeOrInsertPersonAddress(IpPersonAddresses pa) {
        TemporaryNumberUtils.removeTemporaryNumbers(pa);
        Integer tempParentPersonNbr = pa.getTempParentPersonNbr();

        if (pa.getIpPerson().getIpAgent() != null) {
            return getPersonAddressByAgentCode(pa.getIpPerson().getIpAgent().getAgentCode());
        } else if (pa.getIpPerson().getExtendedPartnership() != null) {
            return getPersonAddressByPartnershipCode(pa.getIpPerson().getExtendedPartnership().getPartnershipCode());
        }

        IpPersonAddresses result = null;
        if (pa.getPk() != null && pa.getPk().getPersonNbr() != null && pa.getPk().getAddrNbr() != null) {//only merging
            em.merge(pa.getIpPerson());
            pa = em.merge(pa);//na teoriq gorniq merge, shte merge-ne i IpPersonAddress-a, ako imat ciklichna vryzka, t.e. pa.getIpPerson().getAddresses().get(0) == pa!!!
            result = pa;
        }
        if (Objects.isNull(result)) {
            if (pa.getPk() != null && pa.getPk().getPersonNbr() != null && pa.getPk().getAddrNbr() == null) {//if the address only is missing - the new addresses is getting persisted, and the person is merged!
                //TODO:It might be a good idea to check if the address already exists in the database, instead of directly creating a new one????
                generateAddressNumberAndSetToPersonAddresses(pa);
                em.merge(pa.getIpPerson());
                pa = em.merge(pa);
                result = pa;
            }
        }
        if (Objects.isNull(result)) {
            String query = "select a from IpPersonAddresses a where " +
                    " a.ipPerson.personName = :personName " +
                    " AND a.addrStreet = :street " +
                    " AND a.ipPerson.nationalityCountryCode = :nationalityCountryCode " +
                    " AND ((:cityName is null and a.cityName is null) OR a.cityName = :cityName) " +
                    " AND ((:zipCode is null and a.zipcode is null) OR a.zipcode = :zipCode) " +
                    " AND ((:gralPersonIdNumber is null and a.ipPerson.gralPersonIdNbr is null) OR a.ipPerson.gralPersonIdNbr = :gralPersonIdNumber) ";

            String indiPersonIdTxt = pa.getIpPerson().getIndiPersonIdTxt();
            if (StringUtils.hasText(indiPersonIdTxt)) {
                query += " AND a.ipPerson.indiPersonIdTxt = :indiPersonTxt ";
            }

            TypedQuery<IpPersonAddresses> q = em.createQuery(query, IpPersonAddresses.class);
            q.setParameter("personName", pa.getIpPerson().getPersonName());
            q.setParameter("nationalityCountryCode", pa.getIpPerson().getNationalityCountryCode());
            q.setParameter("street", pa.getAddrStreet());
            q.setParameter("cityName", pa.getCityName());
            q.setParameter("zipCode", pa.getZipcode());
            q.setParameter("gralPersonIdNumber", pa.getIpPerson().getGralPersonIdNbr());
            if (StringUtils.hasText(indiPersonIdTxt)) {
                q.setParameter("indiPersonTxt", indiPersonIdTxt);
            }

            List<IpPersonAddresses> res = q.getResultList();
            if (res != null && res.size() > 0) {
                result = res.get(0);
            }
        }
        if (Objects.isNull(result)) {
            Integer personNumber = sequenceRepository.getNextSequenceValue(SequenceRepository.SEQUENCE_NAME.SEQUENCE_NAME_PERSON_NBR);
            pa.getIpPerson().setPersonNbr(personNumber);
            if (pa.getPk() == null) {
                pa.setPk(new IpPersonAddressesPK());
            }
            pa.getPk().setPersonNbr(personNumber);
            pa.getPk().setAddrNbr(1);
            em.persist(pa);
            result = pa;
        }

        createOldPersonVersion(tempParentPersonNbr, result);
        return result;
    }

    private void createOldPersonVersion(Integer tempParentPersonNbr, IpPersonAddresses result) {
        if (Objects.nonNull(tempParentPersonNbr) && !(tempParentPersonNbr.equals(result.getPk().getPersonNbr()))) {// Create person old version
            IpPersonAddresses personOlderVersion = getPersonAddressByPersonNumber(tempParentPersonNbr);
            if (Objects.isNull(personOlderVersion))
                throw new RuntimeException("Cannot find older version of person !");

            personOlderVersion.getIpPerson().setGralPersonIdNbr(result.getPk().getPersonNbr());
            em.merge(personOlderVersion);
        }
    }

    public List<IpPersonAddresses> findPersons(CCriteriaPerson criteria) {
//        String sql = "select t from IpPersonAddresses t LEFT JOIN t.ipPerson.extendedPartnership left join t.ipPerson.ipAgent where 1 = 1 ";
        String sql = "SELECT pa from IpPersonAddresses pa " +
                "LEFT JOIN FETCH pa.residenceCountry " +
                "JOIN FETCH pa.ipPerson p " +
                "LEFT JOIN FETCH p.extendedPartnership  " +
                "LEFT JOIN FETCH p.ipAgent " +
                "WHERE 1 = 1 ";

        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(criteria.getAgentCode())) {
            if (criteria.getAgentCode().startsWith(PARTNERSHIP_PREFIX)) {
                sql += " AND p.extendedPartnership.partnershipCode = :partnershipCode";
                params.put("partnershipCode", criteria.getAgentCode());
            } else {
                sql += " AND p.ipAgent.agentCode = :agentCode";
                params.put("agentCode", Integer.parseInt(criteria.getAgentCode()));
            }
        }
        if (criteria.isOnlyAgent()) {
            sql += " AND (p.extendedPartnership.partnershipCode is not null or  p.ipAgent.agentCode is not null)";
            if (!StringUtils.isEmpty(criteria.getAgentCodeOrNameContainsWords())) {
                sql += " AND (lower(p.personName) like :agentCodeOrName OR CAST(p.ipAgent.agentCode as text) like :agentCodeOrName OR p.extendedPartnership.partnershipCode like :agentCodeOrName)";
                params.put("agentCodeOrName", "%" + criteria.getAgentCodeOrNameContainsWords() + "%");
            }
        }

        if (!StringUtils.isEmpty(criteria.getPersonNameContainsWords())) {
            sql += " AND lower(p.personName) like :personNameContainsWords ";
            params.put("personNameContainsWords", "%" + criteria.getPersonNameContainsWords().toLowerCase().trim() + "%");
        }

        if (!StringUtils.isEmpty(criteria.getPersonName())) {
            sql += " AND lower(p.personName) = :name ";
            params.put("name", criteria.getPersonName().toLowerCase());
        }

        if (!StringUtils.isEmpty(criteria.getStreetContainsWords())) {
            sql += " AND lower(pa.addrStreet) like :streetContainsWords ";
            params.put("streetContainsWords", "%" + criteria.getStreetContainsWords().toLowerCase().trim() + "%");
        }

        if (!StringUtils.isEmpty(criteria.getStreet())) {
            sql += " AND lower(pa.addrStreet) = :addressStreet ";
            params.put("addressStreet", criteria.getStreet().toLowerCase());
        }

        if (!StringUtils.isEmpty(criteria.getNationalityCountryCode())) {
            sql += " AND lower(p.nationalityCountryCode) = :nationalityCountryCode ";
            params.put("nationalityCountryCode", criteria.getNationalityCountryCode().toLowerCase());
        }

        if (!StringUtils.isEmpty(criteria.getCityContainsWords())) {
            sql += " AND lower(pa.cityName) like :cityContainsWords ";
            params.put("cityContainsWords", "%" + criteria.getCityContainsWords().toLowerCase().trim() + "%");
        }

        if (!StringUtils.isEmpty(criteria.getEmail())) {
            sql += " AND lower(p.email) = :email ";
            params.put("email", criteria.getEmail().toLowerCase());
        }

        if (!StringUtils.isEmpty(criteria.getEmailContainsWords())) {
            sql += " AND lower(p.email) like :emailContainsWords ";
            params.put("emailContainsWords", "%" + criteria.getEmailContainsWords().trim() + "%");
        }

        if (!StringUtils.isEmpty(criteria.getTelephoneContainsWords())) {
            sql += " AND lower(p.telephone) like :telephoneContainsWords ";
            params.put("telephoneContainsWords", "%" + criteria.getTelephoneContainsWords().trim() + "%");
        }

        if (!StringUtils.isEmpty(criteria.getCity())) {
            sql += " AND lower(pa.cityName) = :cityName ";
            params.put("cityName", criteria.getCity().toLowerCase());
        }

        if (!StringUtils.isEmpty(criteria.getZipCode())) {
            sql += " AND lower(pa.zipcode) = :zipcode ";
            params.put("zipcode", criteria.getZipCode().toLowerCase());
        }

        if (!StringUtils.isEmpty(criteria.getResidenceCountryCode())) {
            sql += " AND pa.residenceCountry.countryCode = :residenceCountryCode ";
            params.put("residenceCountryCode", criteria.getResidenceCountryCode());
        }

        if (criteria.isOnlyForeignCitizens()) {
            sql += " AND pa.residenceCountry.countryCode <> :bulgariaCode ";
            params.put("bulgariaCode", DefaultValue.BULGARIA_CODE);
        }

        if (!StringUtils.isEmpty(criteria.getIndividualIdText())) {
            sql += " AND p.indiPersonIdTxt = :indiPersonIdTxt ";
            params.put("indiPersonIdTxt", criteria.getIndividualIdText());
        }

        TypedQuery<IpPersonAddresses> q = em.createQuery(sql, IpPersonAddresses.class);
        params.forEach((k, v) -> q.setParameter(k, v));
        return q.setMaxResults(1000).getResultList();
    }


    public IpPersonAddresses getPersonAddressByAgentCode(Integer agentCode) {
        return em.createQuery("select t from IpPersonAddresses t where t.ipPerson.ipAgent.agentCode = :agentCode and t.pk.addrNbr = 1", IpPersonAddresses.class).setParameter("agentCode", agentCode).getSingleResult();
    }

    public IpPersonAddresses getPersonAddressByPartnershipCode(String partnershipCode) {
        return em.createQuery("select t from IpPersonAddresses t where t.ipPerson.extendedPartnership.partnershipCode = :partnershipCode and t.pk.addrNbr = 1", IpPersonAddresses.class).setParameter("partnershipCode", partnershipCode).getSingleResult();
    }

    public IpPersonAddresses getPersonAddressByPersonNumber(Integer personNumber) {
        return em.createQuery("select t from IpPersonAddresses t where t.pk.personNbr = :personNumber and t.pk.addrNbr = 1", IpPersonAddresses.class)
                .setParameter("personNumber", personNumber)
                .getSingleResult();
    }

    private void generateAddressNumberAndSetToPersonAddresses(IpPersonAddresses pa) {

        List<Integer> addrNbrs =
                em.createQuery("select max(a.pk.addrNbr) from IpPersonAddresses a where a.pk.personNbr = :personNbr", Integer.class)
                        .setParameter("personNbr", pa.getPk().getPersonNbr())
                        .getResultList();

        Integer addrNbr = 1;
        if (addrNbrs.size() != 0) {
            addrNbr = addrNbrs.get(0) + 1;
        }
        pa.getPk().setAddrNbr(addrNbr);
    }

    @Override
    public IpPersonAddressesPK splitIpPerson(Integer personNumber, Integer addressNumber) {
        StoredProcedureQuery query = this.em.createStoredProcedureQuery("splitIpPerson");
        query.registerStoredProcedureParameter("personNumber", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("addressNumber", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("newPersonNumber", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("newAddressNumber", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("executionStatus", Boolean.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("errorMessage", String.class, ParameterMode.OUT);

        query.setParameter("personNumber", personNumber);
        query.setParameter("addressNumber", addressNumber);

        query.execute();

        Boolean errorExists = (Boolean) query.getOutputParameterValue("executionStatus");
        String errorMessage = (String) query.getOutputParameterValue("errorMessage");
        if (Objects.nonNull(errorExists) && errorExists) {
            throw new RuntimeException(errorMessage);
        }

        IpPersonAddressesPK pk = new IpPersonAddressesPK();
        pk.setPersonNbr((Integer) query.getOutputParameterValue("newPersonNumber"));
        pk.setAddrNbr((Integer) query.getOutputParameterValue("newAddressNumber"));
        return pk;
    }

    @Override
    public PersonUsageData selectPersonUsageData(Integer personNumber, Integer addressNumber) {
        StoredProcedureQuery query = this.em.createStoredProcedureQuery("findIpPersonUse");
        query.registerStoredProcedureParameter("personNumber", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("addressNumber", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("ipMarkOwnersCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipMarkRepresentativesCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipMarkMainOwnerCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipMarkServicePersonCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipPatentOwnersCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipPatentRepresentativesCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipPatentInventorsCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipPatentMainOwnerCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipPatentServicePersonCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipFileMainOwnerCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipFileServicePersonCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipUserdocApplicantCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipUserdocPayeeCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipUserdocPayerCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipUserdocGranteeCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipUserdocGrantorCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipUserdocOldOwnersCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipUserdocNewOwnersCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipUserdocRepresentativeCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipUserdocPersonCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("ipPersonAbdocsSyncCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("extReceptionCorrespondentCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("extReceptionUserdocCorrespondentCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("agentHistoryCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("extendedAgentCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("extendedPersonAddressesCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("extendedPartnershipCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("partnershipAgentAgentsCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("partnershipAgentPartnershipsCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("partnershipHistoryCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("acpRepresentativesCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("acpInfringerCount", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("acpServicePersonCount", Integer.class, ParameterMode.OUT);

        query.setParameter("personNumber", personNumber);
        query.setParameter("addressNumber", addressNumber);
        query.execute();

        PersonUsageData result = new PersonUsageData();
        result.setPersonNumber(personNumber);
        result.setAddressNumber(addressNumber);
        result.setIpMarkOwnersCount((Integer) query.getOutputParameterValue("ipMarkOwnersCount"));
        result.setIpMarkRepresentativesCount((Integer) query.getOutputParameterValue("ipMarkRepresentativesCount"));
        result.setIpMarkMainOwnerCount((Integer) query.getOutputParameterValue("ipMarkMainOwnerCount"));
        result.setIpMarkServicePersonCount((Integer) query.getOutputParameterValue("ipMarkServicePersonCount"));
        result.setIpPatentOwnersCount((Integer) query.getOutputParameterValue("ipPatentOwnersCount"));
        result.setIpPatentRepresentativesCount((Integer) query.getOutputParameterValue("ipPatentRepresentativesCount"));
        result.setIpPatentInventorsCount((Integer) query.getOutputParameterValue("ipPatentInventorsCount"));
        result.setIpPatentMainOwnerCount((Integer) query.getOutputParameterValue("ipPatentMainOwnerCount"));
        result.setIpPatentServicePersonCount((Integer) query.getOutputParameterValue("ipPatentServicePersonCount"));
        result.setIpFileMainOwnerCount((Integer) query.getOutputParameterValue("ipFileMainOwnerCount"));
        result.setIpFileServicePersonCount((Integer) query.getOutputParameterValue("ipFileServicePersonCount"));
        result.setIpUserdocApplicantCount((Integer) query.getOutputParameterValue("ipUserdocApplicantCount"));
        result.setIpUserdocPayeeCount((Integer) query.getOutputParameterValue("ipUserdocPayeeCount"));
        result.setIpUserdocPayerCount((Integer) query.getOutputParameterValue("ipUserdocPayerCount"));
        result.setIpUserdocGranteeCount((Integer) query.getOutputParameterValue("ipUserdocGranteeCount"));
        result.setIpUserdocGrantorCount((Integer) query.getOutputParameterValue("ipUserdocGrantorCount"));
        result.setIpUserdocOldOwnersCount((Integer) query.getOutputParameterValue("ipUserdocOldOwnersCount"));
        result.setIpUserdocNewOwnersCount((Integer) query.getOutputParameterValue("ipUserdocNewOwnersCount"));
        result.setIpUserdocRepresentativeCount((Integer) query.getOutputParameterValue("ipUserdocRepresentativeCount"));
        result.setIpUserdocPersonCount((Integer) query.getOutputParameterValue("ipUserdocPersonCount"));
        result.setIpPersonAbdocsSyncCount((Integer) query.getOutputParameterValue("ipPersonAbdocsSyncCount"));
        result.setExtReceptionCorrespondentCount((Integer) query.getOutputParameterValue("extReceptionCorrespondentCount"));
        result.setExtReceptionUserdocCorrespondentCount((Integer) query.getOutputParameterValue("extReceptionUserdocCorrespondentCount"));
        result.setAgentHistoryCount((Integer) query.getOutputParameterValue("agentHistoryCount"));
        result.setExtendedAgentCount((Integer) query.getOutputParameterValue("extendedAgentCount"));
        result.setExtendedPersonAddressesCount((Integer) query.getOutputParameterValue("extendedPersonAddressesCount"));
        result.setExtendedPartnershipCount((Integer) query.getOutputParameterValue("extendedPartnershipCount"));
        result.setPartnershipAgentAgentsCount((Integer) query.getOutputParameterValue("partnershipAgentAgentsCount"));
        result.setPartnershipAgentPartnershipsCount((Integer) query.getOutputParameterValue("partnershipAgentPartnershipsCount"));
        result.setPartnershipHistoryCount((Integer) query.getOutputParameterValue("partnershipHistoryCount"));
        result.setAcpRepresentativesCount((Integer) query.getOutputParameterValue("acpRepresentativesCount"));
        result.setAcpInfringerCount((Integer) query.getOutputParameterValue("acpInfringerCount"));
        result.setAcpServicePersonCount((Integer) query.getOutputParameterValue("acpServicePersonCount"));
        return result;
    }

    @Override
    public boolean deletePerson(Integer personNumber, Integer addressNumber, boolean deleteOnlyNotUsedPerson) {
        try {
            StoredProcedureQuery query = this.em.createStoredProcedureQuery("deleteIpPerson");
            query.registerStoredProcedureParameter("personNumber", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("addressNumber", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("usageValidation", Integer.class, ParameterMode.IN);

            query.setParameter("personNumber", personNumber);
            query.setParameter("addressNumber", addressNumber);
            if (deleteOnlyNotUsedPerson) {
                query.setParameter("usageValidation", 1);
            } else {
                query.setParameter("usageValidation", 0);
            }
            query.execute();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<SimplePersonAddressResult> selectMergeSimple(MergePersonFilter filter) {
        TypedQuery<SimplePersonAddressResult> query = em.createQuery(createFilterQuery(filter), SimplePersonAddressResult.class);
        setFilterQueryParameters(filter, query);
        List<SimplePersonAddressResult> result = query.getResultList();

        return result;
    }

    @Override
    public boolean replaceIpPerson(Integer oldPersonNumber, Integer oldAddressNumber, Integer newPersonNumber, Integer newAddressNumber) {
        try {
            StoredProcedureQuery query = this.em.createStoredProcedureQuery("replaceIpPerson");
            query.registerStoredProcedureParameter("oldPersonNumber", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("oldAddressNumber", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("newPersonNumber", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("newAddressNumber", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("executionStatus", Boolean.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("errorMessage", String.class, ParameterMode.OUT);

            query.setParameter("oldPersonNumber", oldPersonNumber);
            query.setParameter("oldAddressNumber", oldAddressNumber);
            query.setParameter("newPersonNumber", newPersonNumber);
            query.setParameter("newAddressNumber", newAddressNumber);

            query.execute();

            Boolean errorExists = (Boolean) query.getOutputParameterValue("executionStatus");
            String errorMessage = (String) query.getOutputParameterValue("errorMessage");
            if (Objects.nonNull(errorExists) && errorExists) {
                throw new RuntimeException(errorMessage);
            }

            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Integer selectLastPersonVersion(Integer personNumber) {
        StoredProcedureQuery query = this.em.createStoredProcedureQuery("selectIpPersonLastVersion");
        query.registerStoredProcedureParameter("personNumber", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("ipPersonLastVersion", Integer.class, ParameterMode.OUT);
        query.setParameter("personNumber", personNumber);
        query.execute();
        return (Integer) query.getOutputParameterValue("ipPersonLastVersion");
    }

    private String createFilterQuery(MergePersonFilter filter) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT new bg.duosoft.ipas.persistence.model.nonentity.SimplePersonAddressResult(d.pk.personNbr,d.pk.addrNbr,p.personName,d.addrStreet) FROM IpPersonAddresses d JOIN IpPerson p on p.personNbr = d.ipPerson.personNbr WHERE 1=1");

        if (Objects.nonNull(filter)) {
            if (!StringUtils.isEmpty(filter.getPersonName())) {
                builder.append(" AND lower(p.personName) LIKE :personName");
            }
            if (!StringUtils.isEmpty(filter.getPersonAddress())) {
                builder.append(" AND lower(d.addrStreet) LIKE :addrStreet");
            }
        }
        builder.append(" ORDER BY d.pk.personNbr ASC, d.pk.addrNbr DESC");

        return builder.toString();
    }

    private void setFilterQueryParameters(MergePersonFilter filter, Query query) {
        if (Objects.isNull(filter)) {
            return;
        }

        if (!StringUtils.isEmpty(filter.getPersonName())) {
            query.setParameter("personName", "%" + filter.getPersonName().toLowerCase().trim() + "%");
        }
        if (!StringUtils.isEmpty(filter.getPersonAddress())) {
            query.setParameter("addrStreet", "%" + filter.getPersonAddress().toLowerCase().trim() + "%");
        }

    }
}
