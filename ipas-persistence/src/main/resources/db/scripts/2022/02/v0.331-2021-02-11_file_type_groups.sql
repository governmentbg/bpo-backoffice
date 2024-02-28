--liquibase formatted sql

--changeset ggeorgiev:331.1
create table EXT_CORE.CF_FILE_TYPE_GROUPS (
                                              GROUP_CODE varchar(1) not null,
                                              GROUP_NAME varchar(255) not null,
                                              FILE_TYPES varchar(255) not null
                                                  CONSTRAINT [FILE_TYPE_GROUP_CODE_PK] PRIMARY KEY CLUSTERED
                                                  (
                                                  [GROUP_CODE] ASC
                                                  )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY];


--changeset ggeorgiev:331.2
INSERT INTO EXT_CORE.CF_FILE_TYPE_GROUPS (GROUP_CODE, GROUP_NAME, FILE_TYPES)
select FILE_TYP, FILE_TYPE_NAME, FILE_TYP from cf_file_Type where file_typ not in ('N', 'D', 'I', 'R', 'B', 'Е', 'У');
INSERT INTO EXT_CORE.CF_FILE_TYPE_GROUPS (GROUP_CODE, GROUP_NAME, FILE_TYPES) VALUES ('N', 'Марка', 'N,D');
INSERT INTO EXT_CORE.CF_FILE_TYPE_GROUPS (GROUP_CODE, GROUP_NAME, FILE_TYPES) VALUES ('I', 'Международна регистрация', 'I,R,B');