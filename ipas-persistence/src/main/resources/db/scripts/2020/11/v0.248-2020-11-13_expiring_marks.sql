--liquibase formatted sql

--changeset dveizov:248
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('ipobjects.expiring-marks-notifications','Марки: Уведомления за изтичащи марки');

INSERT INTO IPASPROD.EXT_CONFIG_PARAM (CONFIG_CODE, VALUE) VALUES ('EXPIRING_MARK_NOTIFICATION_TEMPLATE', 'ТМ Уедомление изтичане.doc');
