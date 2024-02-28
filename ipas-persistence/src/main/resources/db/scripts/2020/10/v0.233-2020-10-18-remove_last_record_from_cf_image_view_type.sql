--liquibase formatted sql

--changeset dveizov:233
DELETE FROM EXT_CORE.CF_IMAGE_VIEW_TYPE WHERE VIEW_TYPE_ID = 10;