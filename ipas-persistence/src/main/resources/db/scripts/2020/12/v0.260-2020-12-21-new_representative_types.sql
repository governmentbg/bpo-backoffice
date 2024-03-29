--liquibase formatted sql

--changeset dveizov:260.2
create table EXT_AGENT.CF_PARTNERSHIP_TYPE
(
    ID      varchar(50) not null
        constraint CF_PARTNERSHIP_TYPE_pk
            primary key nonclustered,
    NAME    varchar(150),
    NAME_EN varchar(150),
);

INSERT INTO EXT_AGENT.CF_PARTNERSHIP_TYPE (ID, NAME, NAME_EN) VALUES ('PARTNERSHIP', 'Съдружие', 'Partnership');
INSERT INTO EXT_AGENT.CF_PARTNERSHIP_TYPE (ID, NAME, NAME_EN) VALUES ('COMPANY', 'Дружество', 'Company');

ALTER TABLE EXT_AGENT.EXTENDED_PARTNERSHIP ADD PARTNERSHIP_TYPE varchar(50);
ALTER TABLE EXT_AGENT.EXTENDED_PARTNERSHIP ADD CONSTRAINT EXTENDED_PARTNERSHIP_PARTNERSHIP_TYPE_FK FOREIGN KEY (PARTNERSHIP_TYPE) REFERENCES EXT_AGENT.CF_PARTNERSHIP_TYPE (ID);
UPDATE EXT_AGENT.EXTENDED_PARTNERSHIP SET PARTNERSHIP_TYPE = 'PARTNERSHIP' where 1 = 1;
ALTER TABLE EXT_AGENT.EXTENDED_PARTNERSHIP ALTER COLUMN PARTNERSHIP_TYPE varchar(50) NOT NULL;
