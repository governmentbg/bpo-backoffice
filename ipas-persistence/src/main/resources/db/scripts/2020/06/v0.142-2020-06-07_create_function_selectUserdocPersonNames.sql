--liquibase formatted sql

--changeset dveizov:142
CREATE FUNCTION [IPASPROD].[selectUserdocPersonNames](@docOri varchar(4),
                                                      @docLog varchar(1),
                                                      @docSer NUMERIC(4, 0),
                                                      @docNbr NUMERIC(15, 0),
                                                      @role varchar(255))
    RETURNS varchar(max)
AS
BEGIN
    DECLARE @persons varchar(max)

    SELECT @persons = COALESCE(@persons + '; ', '') + p.PERSON_NAME
        FROM EXT_CORE.IP_USERDOC_PERSON oo
            join ipasprod.ip_person p on p.PERSON_NBR = oo.PERSON_NBR
    where oo.DOC_ORI = @docOri AND oo.DOC_LOG = @docLog AND oo.DOC_SER = @docSer AND oo.DOC_NBR = @docNbr AND oo.ROLE = @role

    return @persons
END;