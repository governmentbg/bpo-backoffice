--liquibase formatted sql

--changeset ggeorgiev:13.1
CREATE SCHEMA EXT_USER;

--changeset ggeorgiev:13.2
CREATE TABLE EXT_USER.CF_SECURITY_ROLES (
                                            ROLE_NAME VARCHAR(255) NOT NULL,
                                            DESCRIPTION TEXT NOT NULL,
                                            DATE_CREATED datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,

                                            CONSTRAINT CF_SECURITY_ROLES_PK PRIMARY KEY (ROLE_NAME)
);

CREATE TABLE EXT_USER.CF_GROUP_SECURITY_ROLE (
                                                 GROUP_ID NUMERIC(5) NOT NULL,
                                                 ROLE_NAME varchar (255) NOT NULL,
                                                 DATE_CREATED datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                 USER_CREATED NUMERIC(5) NOT NULL,
                                                 CONSTRAINT CF_GROUP_SECURITY_ROLE_PK PRIMARY KEY (GROUP_ID, ROLE_NAME),
);
ALTER TABLE EXT_USER.CF_GROUP_SECURITY_ROLE
    ADD CONSTRAINT CF_GROUP_SECURITY_ROLE_ROLE_ID_FK
        FOREIGN KEY (ROLE_NAME) REFERENCES EXT_USER.CF_SECURITY_ROLES(ROLE_NAME);


ALTER TABLE EXT_USER.CF_GROUP_SECURITY_ROLE
    ADD CONSTRAINT CF_GROUP_SECURITY_ROLE_GROUP_ID_FK
        FOREIGN KEY (GROUP_ID) REFERENCES IPASPROD.CF_THIS_GROUP(GROUP_ID);

ALTER TABLE EXT_USER.CF_GROUP_SECURITY_ROLE
    ADD CONSTRAINT CF_GROUP_SECURITY_ROLE_USER_CREATED_FK
        FOREIGN KEY (USER_CREATED) REFERENCES IPASPROD.IP_USER(USER_ID);

--EXT_OFFICE_DIVISION
CREATE TABLE [EXT_USER].[CF_OFFICE_DIVISION](
                                                           [OFFICE_DIVISION_CODE] [varchar](3) NOT NULL,
                                                           [IND_INACTIVE] varchar(1) NOT NULL,
                                                           CONSTRAINT [PK_EXT_CF_OFFICE_DIVISION] PRIMARY KEY CLUSTERED
                                                               (
                                                                [OFFICE_DIVISION_CODE] ASC
                                                               )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
;

ALTER TABLE [EXT_USER].[CF_OFFICE_DIVISION]  WITH CHECK ADD  CONSTRAINT [EXT_CF_OFFICE_DIVISION_FK] FOREIGN KEY([OFFICE_DIVISION_CODE])
    REFERENCES [IPASPROD].[CF_OFFICE_DIVISION] ([OFFICE_DIVISION_CODE])
    ON DELETE CASCADE
;

ALTER TABLE [EXT_USER].[CF_OFFICE_DIVISION] CHECK CONSTRAINT [EXT_CF_OFFICE_DIVISION_FK]
;
--END OF EXT_OFFICE_DIVISION

--EXT_OFFICE_DEPARTMENT
CREATE TABLE [EXT_USER].[CF_OFFICE_DEPARTMENT](
                                                [OFFICE_DIVISION_CODE] [varchar](3) NOT NULL,
                                                [OFFICE_DEPARTMENT_CODE] [varchar](3) NOT NULL,
                                                [IND_INACTIVE] varchar(1) NOT NULL,
                                                CONSTRAINT [PK_EXT_CF_OFFICE_DEPARTMENT] PRIMARY KEY CLUSTERED
                                                    (
                                                     [OFFICE_DIVISION_CODE] ASC,
                                                     [OFFICE_DEPARTMENT_CODE] ASC
                                                        )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
;

ALTER TABLE [EXT_USER].[CF_OFFICE_DEPARTMENT]  WITH CHECK ADD  CONSTRAINT [EXT_CF_OFFICE_DEPARTMENT_FK] FOREIGN KEY([OFFICE_DIVISION_CODE], OFFICE_DEPARTMENT_CODE)
    REFERENCES [IPASPROD].[CF_OFFICE_DEPARTMENT] ([OFFICE_DIVISION_CODE], OFFICE_DEPARTMENT_CODE)
    ON DELETE CASCADE
;

ALTER TABLE [EXT_USER].[CF_OFFICE_DEPARTMENT] CHECK CONSTRAINT [EXT_CF_OFFICE_DEPARTMENT_FK]
;

-- END OF EXT_OFFICE_DEPARTMENT

-- EXT_OFFICE_SECTION
CREATE TABLE [EXT_USER].[CF_OFFICE_SECTION](
                                                  [OFFICE_DIVISION_CODE] [varchar](3) NOT NULL,
                                                  [OFFICE_DEPARTMENT_CODE] [varchar](3) NOT NULL,
                                                  [OFFICE_SECTION_CODE] [varchar](3) NOT NULL,
                                                  [IND_INACTIVE] varchar(1) NOT NULL,
                                                  CONSTRAINT [PK_EXT_CF_OFFICE_SECTION] PRIMARY KEY CLUSTERED
                                                      (
                                                       [OFFICE_DIVISION_CODE] ASC,
                                                       [OFFICE_DEPARTMENT_CODE] ASC,
                                                       [OFFICE_SECTION_CODE] ASC
                                                          )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
;

ALTER TABLE [EXT_USER].[CF_OFFICE_SECTION]  WITH CHECK ADD  CONSTRAINT [EXT_CF_OFFICE_SECTION_FK] FOREIGN KEY([OFFICE_DIVISION_CODE], OFFICE_DEPARTMENT_CODE, OFFICE_SECTION_CODE)
    REFERENCES [IPASPROD].[CF_OFFICE_SECTION] ([OFFICE_DIVISION_CODE], OFFICE_DEPARTMENT_CODE, OFFICE_SECTION_CODE)
    ON DELETE CASCADE
;

ALTER TABLE [EXT_USER].[CF_OFFICE_SECTION] CHECK CONSTRAINT [EXT_CF_OFFICE_SECTION_FK]
;
-- END OF EXT_OFFICE_SECTION

INSERT INTO EXT_USER.CF_OFFICE_DIVISION (OFFICE_DIVISION_CODE, IND_INACTIVE) SELECT OFFICE_DIVISION_CODE, 'N' from ipasprod.CF_OFFICE_DIVISION;
INSERT INTO EXT_USER.CF_OFFICE_DEPARTMENT(OFFICE_DIVISION_CODE, OFFICE_DEPARTMENT_CODE, IND_INACTIVE)  SELECT OFFICE_DIVISION_CODE, OFFICE_DEPARTMENT_CODE, 'N' from ipasprod.CF_OFFICE_DEPARTMENT;
INSERT INTO EXT_USER.CF_OFFICE_SECTION(OFFICE_DIVISION_CODE, OFFICE_DEPARTMENT_CODE, OFFICE_SECTION_CODE, IND_INACTIVE)  SELECT OFFICE_DIVISION_CODE, OFFICE_DEPARTMENT_CODE, OFFICE_SECTION_CODE, 'N' from ipasprod.CF_OFFICE_SECTION;


INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('ipas.home.page','Начална страница');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('mark.edit','Промяна на марки');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('mark.update','Ъпдейт на марки');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('mark.object-identity-data','Марки: Преглед на панел "Идентификация"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('mark.mark-main-data','Марки: Преглед на панел "Данни за марка"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('mark.mark-vienna-data','Марки: Преглед на панел "Образни елементи"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('mark.mark-nice-data','Марки: Преглед на панел "Стоки и услуги"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('mark.mark-claims-data','Марки: Преглед на панел "Предходни права"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('mark.mark-publication-data','Марки: Преглед на панел "Публикации"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('mark.object-payments-data','Марки: Преглед на панел "Плащания"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('mark.object-person-data','Марки: Преглед на панел "Лица и контакти"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('mark.object-process-data','Марки: Преглед на панел "Работен процес"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('patent.object-person-data','Патенти: Преглед на панел "Лица и контакти"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('patent.object-identity-data','Патенти: Преглед на панел "Идентификация"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('patent.patent-main-data','Патенти: Преглед на панел "Данни за патент"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('patent.patent-rights-data','Патенти: Преглед на панел "Предходни права"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('patent.object-process-data','Патенти: Преглед на панел "Работен процес"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('patent.object-payments-data','Патенти: Преглед на панел "Плащания"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('patent.patent-publication-data','Патенти: Преглед на панел "Публикации"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('patent.patent-ipc-data','Патенти: Преглед на панел "Класификация по МПК"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('process.automatic.action.execute', 'Изпълняване на автоматични действия');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.userdoc-main-data','Вторични действия: Преглед на панел "Данни за документа"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.object-process-data','Вторични действия: Преглед на панел "Работен процес"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.view.all','Преглед на всички вторични действия');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.view.own','Преглед на собствени вторични действия');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.edit.all','Редакция на всички вторични действия');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.edit.own','Редакция на собствени вторични действия');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.userdoc-type-data','Вторични действия: Преглед на панел "Вид на документа"');
--INSERT INTO EXT_USER.CF_GROUP_SECURITY_ROLE (GROUP_ID, ROLE_NAME, DATE_CREATED, USER_CREATED) SELECT 1, ROLE_NAME, getdate(), 4 FROM EXT_USER.CF_SECURITY_ROLES

