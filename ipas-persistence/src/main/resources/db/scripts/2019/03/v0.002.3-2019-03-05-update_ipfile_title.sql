CREATE TRIGGER [IPASPROD].[SOLICITUD_UPDATE_AFTER_UPDATE] ON [IPASPROD].[IP_PATENT]
AFTER UPDATE
AS
	SET NOCOUNT ON
	DECLARE @error_store int
	DECLARE
		@column_new_value$2 varchar(2),
		@column_new_value$3 varchar(1),
		@column_new_value$4 numeric(4, 0),
		@column_new_value$5 numeric(8, 0),
		@column_new_value$10 datetime,
		@column_new_value$11 datetime,
		@column_new_value$14 datetime,
		@column_new_value$15 varchar(8),
		@column_new_value$16 numeric(5, 0),
		@column_new_value$21 numeric(8, 0),
		@column_new_value$22 datetime,
		@column_new_value$23 varchar(1),
		@column_new_value$24 datetime,
		@column_new_value$25 datetime,
		@column_new_value$26 nvarchar(4000),
		@column_new_value$27 varchar(3),
		@column_new_value$28 varchar(2),
		@column_new_value$32 numeric(8, 0),
		@column_new_value$33 numeric(4, 0),
		@column_new_value$34 numeric(8, 0),
		@column_new_value$35 numeric(4, 0),
		@column_new_value$42 numeric(5, 0),
		@column_new_value$43 datetime,
		@column_new_value$56 datetime,
		@column_new_value$63 varchar(5),
		@column_new_value$64 numeric(4, 0),
		@column_new_value$65 varchar(1),
		@column_new_value$66 numeric(10, 0),
		@column_new_value$67 varchar(10),
		@column_new_value$68 varchar(5),
		@column_new_value$90 nvarchar(4000)


	DECLARE ForEachInsertedRowTriggerCursor CURSOR LOCAL FORWARD_ONLY READ_ONLY FOR
	SELECT FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, FILING_DATE, FIRST_PRIORITY_DATE, PUBLICATION_DATE, JOURNAL_CODE, LAW_CODE, REGISTRATION_NBR, REGISTRATION_DATE, IND_REGISTERED, ENTITLEMENT_DATE, EXPIRATION_DATE, TITLE, APPL_TYP, APPL_SUBTYP, MAIN_OWNER_PERSON_NBR, MAIN_OWNER_ADDR_NBR, SERVICE_PERSON_NBR, SERVICE_ADDR_NBR, CAPTURE_USER_ID, CAPTURE_DATE, SPECIAL_PUBL_DATE,  REGISTRATION_TYP, REGISTRATION_SER, REGISTRATION_DUP, PUBLICATION_NBR, PUBLICATION_SER, PUBLICATION_TYP, ENGLISH_TITLE FROM inserted

	OPEN ForEachInsertedRowTriggerCursor
	FETCH NEXT FROM ForEachInsertedRowTriggerCursor INTO @column_new_value$2, @column_new_value$3, @column_new_value$4, @column_new_value$5, @column_new_value$10, @column_new_value$11, @column_new_value$14, @column_new_value$15, @column_new_value$16, @column_new_value$21, @column_new_value$22, @column_new_value$23, @column_new_value$24, @column_new_value$25, @column_new_value$26, @column_new_value$27, @column_new_value$28, @column_new_value$32, @column_new_value$33, @column_new_value$34, @column_new_value$35, @column_new_value$42, @column_new_value$43, @column_new_value$56, @column_new_value$63, @column_new_value$64, @column_new_value$65, @column_new_value$66, @column_new_value$67, @column_new_value$68, @column_new_value$90

	WHILE @@fetch_status = 0
	BEGIN
		BEGIN
			UPDATE IPASPROD.IP_FILE
			SET
			IPASPROD.IP_FILE.JOURNAL_CODE = @column_new_value$15,
			IPASPROD.IP_FILE.LAW_CODE = @column_new_value$16,
			IPASPROD.IP_FILE.SERVICE_PERSON_NBR = @column_new_value$34,
			IPASPROD.IP_FILE.MAIN_OWNER_PERSON_NBR = @column_new_value$32,
			IPASPROD.IP_FILE.FIRST_PRIORITY_DATE = @column_new_value$11,
			IPASPROD.IP_FILE.CAPTURE_DATE = @column_new_value$43,
			IPASPROD.IP_FILE.REGISTRATION_DATE = @column_new_value$22,
			IPASPROD.IP_FILE.FILING_DATE = @column_new_value$10,
			IPASPROD.IP_FILE.PUBLICATION_DATE = @column_new_value$14,
			IPASPROD.IP_FILE.EXPIRATION_DATE = @column_new_value$25,
			IPASPROD.IP_FILE.ENTITLEMENT_DATE = @column_new_value$24,
			IPASPROD.IP_FILE.IND_REGISTERED = @column_new_value$23,
			IPASPROD.IP_FILE.REGISTRATION_NBR = @column_new_value$21,
			IPASPROD.IP_FILE.REGISTRATION_DUP = @column_new_value$65,
			IPASPROD.IP_FILE.REGISTRATION_TYP = @column_new_value$63,
			IPASPROD.IP_FILE.REGISTRATION_SER = @column_new_value$64,
			IPASPROD.IP_FILE.PUBLICATION_NBR = @column_new_value$66,
			IPASPROD.IP_FILE.PUBLICATION_SER = @column_new_value$67,
			IPASPROD.IP_FILE.PUBLICATION_TYP = @column_new_value$68,
			IPASPROD.IP_FILE.SERVICE_ADDR_NBR = @column_new_value$35,
			IPASPROD.IP_FILE.MAIN_OWNER_ADDR_NBR = @column_new_value$33,
			IPASPROD.IP_FILE.APPL_SUBTYP = @column_new_value$28,
			IPASPROD.IP_FILE.APPL_TYP = @column_new_value$27,
			IPASPROD.IP_FILE.CAPTURE_USER_ID = @column_new_value$42,
			IPASPROD.IP_FILE.SPECIAL_PUBL_DATE = @column_new_value$56,
			IPASPROD.IP_FILE.TITLE = @column_new_value$26,
			IPASPROD.IP_FILE.TITLE_LANG2 = @column_new_value$90
			WHERE ((IPASPROD.IP_FILE.FILE_SEQ = @column_new_value$2) AND
					(IPASPROD.IP_FILE.FILE_TYP = @column_new_value$3) AND
					(IPASPROD.IP_FILE.FILE_SER = @column_new_value$4) AND
					(IPASPROD.IP_FILE.FILE_NBR = @column_new_value$5))
		END
		BEGIN
			UPDATE IPASPROD.IP_DOC
			SET
			IPASPROD.IP_DOC.APPL_SUBTYP = @column_new_value$28,
			IPASPROD.IP_DOC.APPL_TYP = @column_new_value$27
			WHERE ((IPASPROD.IP_DOC.FILE_SEQ = @column_new_value$2) AND
					(IPASPROD.IP_DOC.FILE_TYP = @column_new_value$3) AND
					(IPASPROD.IP_DOC.FILE_SER = @column_new_value$4) AND
					(IPASPROD.IP_DOC.FILE_NBR = @column_new_value$5))
		END
		FETCH NEXT FROM ForEachInsertedRowTriggerCursor INTO @column_new_value$2, @column_new_value$3, @column_new_value$4, @column_new_value$5, @column_new_value$10, @column_new_value$11, @column_new_value$14, @column_new_value$15, @column_new_value$16, @column_new_value$21, @column_new_value$22, @column_new_value$23, @column_new_value$24, @column_new_value$25, @column_new_value$26, @column_new_value$27, @column_new_value$28, @column_new_value$32, @column_new_value$33, @column_new_value$34, @column_new_value$35, @column_new_value$42, @column_new_value$43, @column_new_value$56, @column_new_value$63, @column_new_value$64, @column_new_value$65, @column_new_value$66, @column_new_value$67, @column_new_value$68, @column_new_value$90
	END
	CLOSE ForEachInsertedRowTriggerCursor
	DEALLOCATE ForEachInsertedRowTriggerCursor  ;