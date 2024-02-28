--liquibase formatted sql

--changeset dveizov:186
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Административно-наказателна дейност: Преглед' WHERE ROLE_NAME = 'court.decision.view'
