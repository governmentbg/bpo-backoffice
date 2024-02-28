--liquibase formatted sql

--changeset vnikolov:231
INSERT INTO [EXT_CORE].[CF_IMAGE_VIEW_TYPE]
           ([VIEW_TYPE_ID]
           ,[VIEW_TYPE_NAME])
     VALUES
           (10, 'Изглед в разрез')