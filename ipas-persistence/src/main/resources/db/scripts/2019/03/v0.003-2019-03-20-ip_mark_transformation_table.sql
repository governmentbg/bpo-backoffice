-- ALTER TABLE [EXT_CORE].[IP_MARK_TRANSFORMATION] DROP CONSTRAINT [MARK_TRANSFORMATION_FK]
-- GO

/****** Object:  Table [EXT_CORE].[IP_MARK_TRANSFORMATION]    Script Date: 27.3.2019 г. 12:44:12 ******/
-- DROP TABLE [EXT_CORE].[IP_MARK_TRANSFORMATION]
-- GO

/****** Object:  Table [EXT_CORE].[IP_MARK_TRANSFORMATION]    Script Date: 20.3.2019 г. 17:36:37 ******/
SET ANSI_NULLS ON;

SET QUOTED_IDENTIFIER ON;

CREATE TABLE [EXT_CORE].[IP_MARK_TRANSFORMATION](
	[FILE_SEQ] [varchar](2) NOT NULL,
	[FILE_TYP] [varchar](1) NOT NULL,
	[FILE_SER] [numeric](4, 0) NOT NULL,
	[FILE_NBR] [numeric](10, 0) NOT NULL,
	[TRANSFORM_TYPE] [varchar](2) NULL,
	[REGISTRATION_DATE] [datetime] NULL,
	[FILING_DATE] [datetime] NULL,
	[PRIORITY_DATE] [datetime] NULL,
	[CANCELLATION_DATE] [datetime] NULL,
	[TRANSFORM_NUMBER] [varchar](20) NULL,
 CONSTRAINT [PK_IP_MARK_TRANSFORMATION] PRIMARY KEY CLUSTERED
(
	[FILE_SEQ] ASC,
	[FILE_TYP] ASC,
	[FILE_SER] ASC,
	[FILE_NBR] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY];


ALTER TABLE [EXT_CORE].[IP_MARK_TRANSFORMATION]  WITH CHECK ADD  CONSTRAINT [MARK_TRANSFORMATION_FK] FOREIGN KEY([FILE_NBR], [FILE_SEQ], [FILE_TYP], [FILE_SER])
REFERENCES [IP_MARK] ([FILE_NBR], [FILE_SEQ], [FILE_TYP], [FILE_SER])
ON DELETE CASCADE;

ALTER TABLE [EXT_CORE].[IP_MARK_TRANSFORMATION] CHECK CONSTRAINT [MARK_TRANSFORMATION_FK];
