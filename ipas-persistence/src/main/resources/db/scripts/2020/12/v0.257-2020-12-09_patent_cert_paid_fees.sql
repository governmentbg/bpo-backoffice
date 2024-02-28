--liquibase formatted sql

--changeset dveizov:257
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('ipobjects.certificates-for-paid-patent-fees','Патенти: Удостоверения за платени такси');

INSERT INTO IPASPROD.EXT_CONFIG_PARAM (CONFIG_CODE, VALUE) VALUES ('PATENT_CERTIFICATE_PAID_FEES_TEMPLATE', 'ПТ Удостоверения за плащане.doc');
