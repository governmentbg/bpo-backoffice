--liquibase formatted sql

--changeset mmurlev:300.1
IF EXISTS(SELECT * FROM sys.triggers WHERE type = 'TR' AND name = 'decision_publication')
DROP TRIGGER [EXT_CORE].[decision_publication];

--changeset mmurlev:300.2 splitStatements:false
CREATE TRIGGER [EXT_CORE].[decision_publication]
   ON  EXT_CORE.IP_OFFIDOC_ABDOCS_DOCUMENT
   AFTER UPDATE
AS
BEGIN
	declare @notif_date date;
	declare @offidoc_ori varchar(4);
	declare @offidoc_ser numeric(4);
	declare @offidoc_nbr numeric(7);

	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	select @offidoc_ori=offidoc_ori, @offidoc_ser=offidoc_ser, @offidoc_nbr=offidoc_nbr, @notif_date=NOTIFICATION_FINISHED_DATE from inserted;

	IF @notif_date IS NULL
		return;

	update ext_core.IP_OFFIDOC_PUBLISHED_DECISION set decision_date = @notif_date
		where DECISION_DATE is null and @offidoc_ori=offidoc_ori and @offidoc_ser=offidoc_ser and @offidoc_nbr=offidoc_nbr

END;

ALTER TABLE EXT_CORE.IP_OFFIDOC_ABDOCS_DOCUMENT ENABLE TRIGGER decision_publication;
