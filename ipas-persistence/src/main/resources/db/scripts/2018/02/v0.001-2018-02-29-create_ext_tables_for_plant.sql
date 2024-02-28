----------------------------------------------------------------------------------------
-- DROP TABLES

-- ALTER TABLE [EXT_CORE].[PLANT] DROP CONSTRAINT [FK_PLANT_PLANT_TAXON_NOMENCLATURE]
-- GO

-- DROP TABLE [EXT_CORE].[PLANT]
-- GO

-- DROP TABLE [EXT_CORE].[PLANT_TAXON_NOMENCLATURE]
-- GO

-----------------------------------------------------------------------------------------
-- PLANT_TAXON_NOMENCLATURE
--2020.04.28 - removing the create schema statement, because the schema is created in the production's database, so it should not be created in the new IPAS
--CREATE SCHEMA EXT_CORE;
--GO

SET ANSI_NULLS ON;

SET QUOTED_IDENTIFIER ON;

CREATE TABLE [EXT_CORE].[PLANT_TAXON_NOMENCLATURE](
	[ID] [int] NOT NULL,
	[TAXON_CODE] [varchar](30) NULL,
	[COMMON_CLASSIFY_BUL] [varchar](255) NULL,
	[COMMON_CLASSIFY_ENG] [varchar](255) NULL,
	[LATIN_CLASSIFY] [varchar](255) NULL,
 CONSTRAINT [PK_PLANT_TAXON_NOMENCLATURE] PRIMARY KEY CLUSTERED
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY];

INSERT INTO [EXT_CORE].[PLANT_TAXON_NOMENCLATURE] (
      [ID]
      ,[TAXON_CODE]
      ,[COMMON_CLASSIFY_BUL]
      ,[COMMON_CLASSIFY_ENG]
      ,[LATIN_CLASSIFY]
	  )
 SELECT
      CAST(PARSENAME([OPTION_NBR], 1) AS int) AS [ID]                            -- int
      ,PARSENAME([OPTION_NBR], 2) AS [TAXON_CODE]                                -- character varying(30) in BPO it is 50 symbols
      ,[OPTION_NAME] AS [COMMON_CLASSIFY_BUL]                                    -- character varying(255)
      ,[OPTION_LONG_NAME] AS [COMMON_CLASSIFY_ENG]                               -- character varying(255)
      ,[OPTION_MEDIUM_NAME] AS [LATIN_CLASSIFY]                                  -- character varying(255)
  FROM [IPASPROD].[CF_LIST_OPTIONS]
  ORDER BY id;

-----------------------------------------------------------------------------------------------
-- PLANT

SET ANSI_NULLS ON;

SET QUOTED_IDENTIFIER ON;

CREATE TABLE [EXT_CORE].[PLANT](
	[FILE_SEQ] [varchar](2) NOT NULL,
	[FILE_TYP] [varchar](1) NOT NULL,
	[FILE_SER] [numeric](4, 0) NOT NULL,
	[FILE_NBR] [numeric](10, 0) NOT NULL,
	[PLANT_NUMENCLATURE_ID] [int] NULL,
	[PROPOSED_DENOMINATION] [varchar](255) NULL,
	[PROPOSED_DENOMINATION_ENG] [varchar](255) NULL,
	[PUBL_DENOMINATION] [varchar](255) NULL,
	[PUBL_DENOMINATION_ENG] [varchar](255) NULL,
	[APPR_DENOMINATION] [varchar](255) NULL,
	[APPR_DENOMINATION_ENG] [varchar](255) NULL,
	[REJ_DENOMINATION] [varchar](255) NULL,
	[REJ_DENOMINATION_ENG] [varchar](255) NULL,
	[FEATURES] [text] NULL,
	[STABILITY] [text] NULL,
	[TESTING] [text] NULL,
 CONSTRAINT [PK_PLANT] PRIMARY KEY CLUSTERED
(
	[FILE_SEQ] ASC,
	[FILE_TYP] ASC,
	[FILE_SER] ASC,
	[FILE_NBR] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY];

ALTER TABLE [EXT_CORE].[PLANT]  WITH CHECK ADD  CONSTRAINT [FK_PLANT_PLANT_TAXON_NOMENCLATURE] FOREIGN KEY([PLANT_NUMENCLATURE_ID])
REFERENCES [EXT_CORE].[PLANT_TAXON_NOMENCLATURE] ([ID]);

ALTER TABLE [EXT_CORE].[PLANT] CHECK CONSTRAINT [FK_PLANT_PLANT_TAXON_NOMENCLATURE];

/*2020.06.25 - this SQL is not working properly, so it's disabled! */
/*INSERT INTO [EXT_CORE].[PLANT] (
      [FILE_SEQ]
      ,[FILE_TYP]
      ,[FILE_SER]
      ,[FILE_NBR]
      ,[PLANT_NUMENCLATURE_ID]
      ,[PROPOSED_DENOMINATION]
      ,[PROPOSED_DENOMINATION_ENG]
      ,[PUBL_DENOMINATION]
      ,[PUBL_DENOMINATION_ENG]
      ,[APPR_DENOMINATION]
      ,[APPR_DENOMINATION_ENG]
      ,[REJ_DENOMINATION]
      ,[REJ_DENOMINATION_ENG]
      ,[FEATURES]
      ,[STABILITY]
      ,[TESTING]
	  )
SELECT
      pt.FILE_SEQ
      ,pt.FILE_TYP
      ,pt.FILE_SER
      ,pt.FILE_NBR
      ,CAST(PARSENAME(ao.OPTION_NBR, 1) AS int)		AS ID                      -- int
      ,CAST(ac1.NOTES1 as varchar(255))				AS PROPOSED_DENOMINATION
      ,ac1.NOTES2									AS PROPOSED_DENOMINATION_ENG
      ,ac2.NOTES1									AS PUBL_DENOMINATION
      ,ac2.NOTES2									AS PUBL_DENOMINATION_ENG
      ,ac3.NOTES1									AS APPR_DENOMINATION
      ,ac3.NOTES2									AS APPR_DENOMINATION_ENG
      ,ac4.NOTES1									AS REJ_DENOMINATION
      ,ac4.NOTES2									AS REJ_DENOMINATION_ENG
      ,pt.EXAM_IPC_USED								AS FEATURES
      ,pt.EXAM_KEYWORDS_USED						AS STABILITY
      ,pt.EXAM_RESULT								AS TESTING
  FROM [IPASPROD].[IP_PATENT] pt
      LEFT JOIN [IPASPROD].[IP_PROC] p ON pt.FILE_NBR = p.FILE_NBR AND pt.FILE_SEQ = p.FILE_SEQ AND pt.FILE_SER = p.FILE_SER AND pt.FILE_TYP = p.FILE_TYP
      LEFT JOIN [IPASPROD].[IP_ACTION] ac ON p.PROC_NBR = ac.PROC_NBR AND p.PROC_TYP = ac.PROC_TYP AND ( ac.ACTION_TYP = '644' OR ac.ACTION_TYP = '645' OR ac.ACTION_TYP = '1220' )
      LEFT JOIN [IPASPROD].[IP_ACTION_OPTIONS] ao ON ac.PROC_TYP = ao.PROC_TYP AND ac.PROC_NBR = ao.PROC_NBR AND ac.ACTION_NBR = ao.ACTION_NBR

      LEFT JOIN [IPASPROD].[IP_ACTION] ac1 ON p.PROC_NBR = ac1.PROC_NBR AND p.PROC_TYP = ac1.PROC_TYP AND ac1.ACTION_TYP = '057'
      LEFT JOIN [IPASPROD].[IP_ACTION] ac2 ON p.PROC_NBR = ac2.PROC_NBR AND p.PROC_TYP = ac2.PROC_TYP AND ac2.ACTION_TYP = '058'
      LEFT JOIN [IPASPROD].[IP_ACTION] ac3 ON p.PROC_NBR = ac3.PROC_NBR AND p.PROC_TYP = ac3.PROC_TYP AND ac3.ACTION_TYP = '059'
      LEFT JOIN [IPASPROD].[IP_ACTION] ac4 ON p.PROC_NBR = ac4.PROC_NBR AND p.PROC_TYP = ac4.PROC_TYP AND ac4.ACTION_TYP = '060'
  WHERE pt.FILE_TYP = 'С'
  ORDER BY pt.FILE_SEQ, pt.FILE_TYP, pt.FILE_SER, pt.FILE_NBR;*/



/*
SELECT query
-- EXT NOMENCLATURE

 SELECT
      CAST(PARSENAME([OPTION_NBR], 1) AS int) AS id                            -- int
      ,PARSENAME([OPTION_NBR], 2) AS taxon_code                                -- character varying(30) in BPO it is 50 symbols
      ,[OPTION_NAME] AS common_classify_bul                                    -- character varying(255)
      ,[OPTION_LONG_NAME] AS common_classify_eng                               -- character varying(255)
      ,[OPTION_MEDIUM_NAME] AS latin_classify                                  -- character varying(255)
  FROM [IPASPROD].[CF_LIST_OPTIONS]
  ORDER BY id;


-- EXT PLANT
 SELECT
      pt.FILE_SEQ
      ,pt.FILE_TYP
      ,pt.FILE_SER
      ,pt.FILE_NBR
      ,CAST(PARSENAME(ao.OPTION_NBR, 1) AS int)		AS id                      -- int
      ,PARSENAME(ao.OPTION_NBR, 2)					AS taxon_code              -- character varying(30) in BPO it is 50 symbols
      ,ac1.NOTES1									AS PROPOSED_DENOMINATION
      ,ac1.NOTES2									AS PROPOSED_DENOMINATION_ENG
      ,ac2.NOTES1									AS PUBL_DENOMINATION
      ,ac2.NOTES2									AS PUBL_DENOMINATION_ENG
      ,ac3.NOTES1									AS APPR_DENOMINATION
      ,ac3.NOTES2									AS APPR_DENOMINATION_ENG
      ,ac4.NOTES1									AS REJ_DENOMINATION
      ,ac4.NOTES2									AS REJ_DENOMINATION_ENG
      ,pt.EXAM_IPC_USED								AS FEATURES
      ,pt.EXAM_KEYWORDS_USED						AS STABILITY
      ,pt.EXAM_RESULT								AS TESTING
  FROM [IPASPROD].[IP_PATENT] pt
      LEFT JOIN [IPASPROD].[IP_PROC] p ON pt.FILE_NBR = p.FILE_NBR AND pt.FILE_SEQ = p.FILE_SEQ AND pt.FILE_SER = p.FILE_SER AND pt.FILE_TYP = p.FILE_TYP
      LEFT JOIN [IPASPROD].[IP_ACTION] ac ON p.PROC_NBR = ac.PROC_NBR AND p.PROC_TYP = ac.PROC_TYP AND ( ac.ACTION_TYP = '644' OR ac.ACTION_TYP = '645' OR ac.ACTION_TYP = '1220' )
      LEFT JOIN [IPASPROD].[IP_ACTION_OPTIONS] ao ON ac.PROC_TYP = ao.PROC_TYP AND ac.PROC_NBR = ao.PROC_NBR AND ac.ACTION_NBR = ao.ACTION_NBR

      LEFT JOIN [IPASPROD].[IP_ACTION] ac1 ON p.PROC_NBR = ac1.PROC_NBR AND p.PROC_TYP = ac1.PROC_TYP AND ac1.ACTION_TYP = '057'
      LEFT JOIN [IPASPROD].[IP_ACTION] ac2 ON p.PROC_NBR = ac2.PROC_NBR AND p.PROC_TYP = ac2.PROC_TYP AND ac2.ACTION_TYP = '058'
      LEFT JOIN [IPASPROD].[IP_ACTION] ac3 ON p.PROC_NBR = ac3.PROC_NBR AND p.PROC_TYP = ac3.PROC_TYP AND ac3.ACTION_TYP = '059'
      LEFT JOIN [IPASPROD].[IP_ACTION] ac4 ON p.PROC_NBR = ac4.PROC_NBR AND p.PROC_TYP = ac4.PROC_TYP AND ac4.ACTION_TYP = '060'
  WHERE pt.FILE_TYP = 'С' 
  ORDER BY pt.FILE_SEQ, pt.FILE_TYP, pt.FILE_SER, pt.FILE_NBR;
*/