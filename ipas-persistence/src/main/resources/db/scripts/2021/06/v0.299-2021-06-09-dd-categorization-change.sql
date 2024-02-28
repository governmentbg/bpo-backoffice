--liquibase formatted sql
--changeset raneva:299.1 splitStatements:false
update EXT_CORE.CF_DD_CATEGORIZATION_TAGS set categories='Application,Application_AG' where id=1;