--liquibase formatted sql

--changeset ggeorgiev:12.1
alter table EXT_RECEPTION.CF_SUBMISSION_TYPE add NAME_EN varchar(255);

--changeset ggeorgiev:12.2
update EXT_RECEPTION.CF_SUBMISSION_TYPE set CF_SUBMISSION_TYPE.NAME_EN = 'Internet' where ID = 0;
update EXT_RECEPTION.CF_SUBMISSION_TYPE set CF_SUBMISSION_TYPE.NAME_EN = 'Counter' where ID = 1;
update EXT_RECEPTION.CF_SUBMISSION_TYPE set CF_SUBMISSION_TYPE.NAME_EN = 'Fax' where ID = 3;
update EXT_RECEPTION.CF_SUBMISSION_TYPE set CF_SUBMISSION_TYPE.NAME_EN = 'Mail' where ID = 4;
update EXT_RECEPTION.CF_SUBMISSION_TYPE set CF_SUBMISSION_TYPE.NAME_EN = 'Email' where ID = 5;

