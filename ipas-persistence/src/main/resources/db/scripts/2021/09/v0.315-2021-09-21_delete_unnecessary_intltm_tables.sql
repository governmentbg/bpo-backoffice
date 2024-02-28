--liquibase formatted sql

--changeset mmihova:315.1
drop table ext_core.cf_international_mark_status_change

--changeset mmihova:315.2
drop table ext_core.cf_international_responsible_user_change