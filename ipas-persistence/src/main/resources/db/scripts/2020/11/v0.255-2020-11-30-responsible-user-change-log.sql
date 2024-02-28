--liquibase formatted sql

--changeset ggeorgiev:255.1 splitStatements:false
UPDATE r
    set r.status = 1
    FROM ext_core.IP_PROC_RESPONSIBLE_USER_CHANGES r
    join (select PROC_NBR, PROC_TYP, max(change_nbr) CHANGE_NBR from ext_core.IP_PROC_RESPONSIBLE_USER_CHANGES group by PROC_NBR, PROC_TYP) c on (c.PROC_NBR = r.PROC_NBR and c.PROC_TYP = r.PROC_TYP and r.CHANGE_NBR != c.CHANGE_NBR)


--changeset ggeorgiev:255.2 splitStatements:false
CREATE TRIGGER EXT_CORE.UPDATE_RESPONSIBLE_USER_CHANGES_STATUS_FLAG
    ON EXT_CORE.IP_PROC_RESPONSIBLE_USER_CHANGES
    AFTER INSERT
    AS
BEGIN
    SET NOCOUNT ON;
    declare @status int;

    SELECT @status = i.status from inserted i;
    IF @status = 0
        BEGIN
            UPDATE c
            SET c.status = 1
            FROM EXT_CORE.IP_PROC_RESPONSIBLE_USER_CHANGES c
            JOIN inserted i on i.PROC_TYP = c.PROC_TYP and i.PROC_NBR = c.PROC_NBR
            WHERE c.CHANGE_NBR != i.CHANGE_NBR;
        END;
END;



ALTER TABLE EXT_CORE.IP_PROC_RESPONSIBLE_USER_CHANGES ENABLE TRIGGER UPDATE_RESPONSIBLE_USER_CHANGES_STATUS_FLAG;