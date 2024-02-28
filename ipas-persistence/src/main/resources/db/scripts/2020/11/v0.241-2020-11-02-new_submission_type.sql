--liquibase formatted sql

--changeset dveizov:241
INSERT INTO EXT_RECEPTION.CF_SUBMISSION_TYPE (ID, NAME, NAME_EN) VALUES (10, 'Сигурно електронно връчване', 'Secure electronic serve');
