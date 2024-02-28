--liquibase formatted sql

--changeset vnikolov:25.1

/****** Object:  Trigger [IPASPROD].[FILE_INDEX_AFTER]    Script Date: 25/09/2019 9:17:27 AM ******/
SET ANSI_NULLS ON;

SET QUOTED_IDENTIFIER ON;





--changeset vnikolov:25.2 splitStatements:false
/*************************************************** [IPASPROD].[FILE_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[FILE_INDEX_AFTER] 
   ON [IPASPROD].[IP_FILE]
   AFTER INSERT, UPDATE, DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	
	DECLARE @Activity  NVARCHAR (1)

	-- update
	IF EXISTS (SELECT * FROM inserted) AND EXISTS (SELECT * FROM deleted)
	BEGIN
		SET @Activity = 'U'
	END

	-- insert
	IF EXISTS (SELECT * FROM inserted) AND NOT EXISTS(SELECT * FROM deleted)
	BEGIN
		SET @Activity = 'I'
	END

	-- delete
	IF EXISTS (SELECT * FROM deleted) AND NOT EXISTS(SELECT * FROM inserted)
	BEGIN
		SET @Activity = 'D';
	END
	
	IF NOT EXISTS (SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]		
			WHERE (iq.[TYPE] = '1' OR iq.[TYPE] = '2') AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]		
			WHERE (iq.[TYPE] = '1' OR iq.[TYPE] = '2') AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
		AND EXISTS ( SELECT m.FILE_NBR 
			FROM [IPASPROD].[IP_MARK] m
			JOIN inserted i ON m.[FILE_SEQ] = i.[FILE_SEQ] AND m.[FILE_TYP] = i.[FILE_TYP] AND m.[FILE_SER] = i.[FILE_SER] AND m.[FILE_NBR] = i.[FILE_NBR]
			UNION SELECT  p.FILE_NBR   
			FROM [IPASPROD].[IP_PATENT] p
			JOIN inserted i ON p.[FILE_SEQ] = i.[FILE_SEQ] AND p.[FILE_TYP] = i.[FILE_TYP] AND p.[FILE_SER] = i.[FILE_SER] AND p.[FILE_NBR] = i.[FILE_NBR])
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT 
			CASE   
				WHEN EXISTS (SELECT * 
					FROM [IPASPROD].[IP_PATENT] p
					JOIN inserted i ON p.[FILE_SEQ] = i.[FILE_SEQ] AND p.[FILE_TYP] = i.[FILE_TYP] AND p.[FILE_SER] = i.[FILE_SER] AND p.[FILE_NBR] = i.[FILE_NBR]
					WHERE p.FILE_SEQ = ii.FILE_SEQ and p.FILE_TYP = ii.FILE_TYP and p.FILE_SER = ii.FILE_SER and p.FILE_NBR = ii.FILE_NBR) THEN '1'
				WHEN EXISTS (SELECT * 
					FROM [IPASPROD].[IP_MARK] m
					JOIN inserted i ON m.[FILE_SEQ] = i.[FILE_SEQ] AND m.[FILE_TYP] = i.[FILE_TYP] AND m.[FILE_SER] = i.[FILE_SER] AND m.[FILE_NBR] = i.[FILE_NBR]
					WHERE m.FILE_SEQ = ii.FILE_SEQ and m.FILE_TYP = ii.FILE_TYP and m.FILE_SER = ii.FILE_SER and m.FILE_NBR = ii.FILE_NBR) THEN '2'  
				ELSE '0'  
			END,
			ii.FILE_SEQ,
			ii.FILE_TYP,
			ii.FILE_SER,
			ii.FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted ii
		UNION 
		SELECT 
			CASE   
				WHEN EXISTS (SELECT * 
					FROM [IPASPROD].[IP_PATENT] p
					JOIN deleted d ON p.[FILE_SEQ] = d.[FILE_SEQ] AND p.[FILE_TYP] = d.[FILE_TYP] AND p.[FILE_SER] = d.[FILE_SER] AND p.[FILE_NBR] = d.[FILE_NBR]
					WHERE p.FILE_SEQ = dd.FILE_SEQ and p.FILE_TYP = dd.FILE_TYP and p.FILE_SER = dd.FILE_SER and p.FILE_NBR = dd.FILE_NBR) THEN '1'
				WHEN EXISTS (SELECT * 
					FROM [IPASPROD].[IP_MARK] m
					JOIN deleted d ON m.[FILE_SEQ] = d.[FILE_SEQ] AND m.[FILE_TYP] = d.[FILE_TYP] AND m.[FILE_SER] = d.[FILE_SER] AND m.[FILE_NBR] = d.[FILE_NBR]
					WHERE m.FILE_SEQ = dd.FILE_SEQ and m.FILE_TYP = dd.FILE_TYP and m.FILE_SER = dd.FILE_SER and m.FILE_NBR = dd.FILE_NBR) THEN '2'  
				ELSE '0'  
			END,
			dd.FILE_SEQ,
			dd.FILE_TYP,
			dd.FILE_SER,
			dd.FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted dd;
	END
END;


