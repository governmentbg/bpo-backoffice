update ipasunittests.IPASPROD.ip_PATENT set ENTITLEMENT_DATE = ipasunittests.IPASPROD.getPatEntitlementDate(FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR) where ENTITLEMENT_DATE IS NULL;

update ipasunittests.IPASPROD.IP_PATENT set EXPIRATION_DATE = dateadd(year, 20, ENTITLEMENT_DATE) where EXPIRATION_DATE is null;

update ipasunittests.IPASPROD.IP_MARK set ENTITLEMENT_DATE = FILING_DATE where ENTITLEMENT_DATE is null;

update ipasunittests.IPASPROD.IP_MARK set EXPIRATION_DATE = dateadd(year, 10, ENTITLEMENT_DATE) where EXPIRATION_DATE is null;



update ipasunittests.IPASPROD.ip_mark set SERVICE_PERSON_NBR = (select top 1 PERSON_NBR FROM ipasunittests.IPASPROD.IP_MARK_OWNERS mo WHERE mo.FILE_NBR = IP_MARK.FILE_NBR AND
        mo.FILE_SEQ = IP_MARK.FILE_SEQ AND
        mo.FILE_SER = IP_MARK.FILE_SER AND
        mo.FILE_TYP = IP_MARK.FILE_TYP)
where SERVICE_PERSON_NBR is null;


update ipasunittests.IPASPROD.ip_mark set SERVICE_ADDR_NBR = (select top 1 ADDR_NBR FROM ipasunittests.IPASPROD.IP_MARK_OWNERS mo WHERE mo.FILE_NBR = IP_MARK.FILE_NBR AND
        mo.FILE_SEQ = IP_MARK.FILE_SEQ AND
        mo.FILE_SER = IP_MARK.FILE_SER AND
        mo.FILE_TYP = IP_MARK.FILE_TYP AND
        mo.PERSON_NBR = IP_MARK.SERVICE_PERSON_NBR)
where SERVICE_ADDR_NBR is null;




update ipasunittests.IPASPROD.IP_PATENT set SERVICE_PERSON_NBR = (select top 1 PERSON_NBR FROM ipasunittests.IPASPROD.IP_PATENT_OWNERS po WHERE po.FILE_NBR = IP_PATENT.FILE_NBR AND
        po.FILE_SEQ = IP_PATENT.FILE_SEQ AND
        po.FILE_SER = IP_PATENT.FILE_SER AND
        po.FILE_TYP = IP_PATENT.FILE_TYP)
where SERVICE_PERSON_NBR is null;


update ipasunittests.IPASPROD.IP_PATENT set SERVICE_ADDR_NBR = (select top 1 ADDR_NBR FROM ipasunittests.IPASPROD.IP_PATENT_OWNERS po WHERE po.FILE_NBR = IP_PATENT.FILE_NBR AND
        po.FILE_SEQ = IP_PATENT.FILE_SEQ AND
        po.FILE_SER = IP_PATENT.FILE_SER AND
        po.FILE_TYP = IP_PATENT.FILE_TYP AND
        po.PERSON_NBR = IP_PATENT.SERVICE_PERSON_NBR)
where SERVICE_ADDR_NBR is null;