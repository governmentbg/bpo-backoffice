--liquibase formatted sql

--changeset svtonev:212.1
update ipasprod.IP_PATENT set ENTITLEMENT_DATE=FILING_DATE where ENTITLEMENT_DATE is null and file_typ in ('P', 'U', 'Д');
update ipasprod.IP_MARK set ENTITLEMENT_DATE=FILING_DATE where ENTITLEMENT_DATE is null;