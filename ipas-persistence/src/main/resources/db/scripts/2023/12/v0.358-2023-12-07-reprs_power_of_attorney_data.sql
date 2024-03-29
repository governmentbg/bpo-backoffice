--liquibase formatted sql

--changeset murlev:358
ALTER TABLE IPASPROD.IP_MARK_REPRS ADD ATTORNEY_POWER_TERM date;
ALTER TABLE IPASPROD.IP_MARK_REPRS ADD REAUTHORIZATION_RIGHT   bit default 0;
ALTER TABLE IPASPROD.IP_MARK_REPRS ADD AUTHORIZATION_CONDITION nvarchar(max);
ALTER TABLE IPASPROD.IP_PATENT_REPRS ADD ATTORNEY_POWER_TERM date;
ALTER TABLE IPASPROD.IP_PATENT_REPRS ADD REAUTHORIZATION_RIGHT   bit default 0;
ALTER TABLE IPASPROD.IP_PATENT_REPRS ADD AUTHORIZATION_CONDITION nvarchar(max);
ALTER TABLE EXT_CORE.IP_USERDOC_PERSON ADD ATTORNEY_POWER_TERM date;
ALTER TABLE EXT_CORE.IP_USERDOC_PERSON ADD REAUTHORIZATION_RIGHT   bit default 0;
ALTER TABLE EXT_CORE.IP_USERDOC_PERSON ADD AUTHORIZATION_CONDITION nvarchar(max);
