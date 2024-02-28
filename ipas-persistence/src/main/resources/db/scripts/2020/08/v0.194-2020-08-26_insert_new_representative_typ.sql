--liquibase formatted sql

--changeset mmurlev:194
INSERT INTO IPASPROD.CF_REPRESENTATIVE_TYPE (ROW_VERSION, REPRESENTATIVE_TYP, REPRESENTATIVE_TYPE_DESC, IND_AGENT, IND_LEGAL_ADDRESS) values (1,'IR','Международен представител','N','N')


