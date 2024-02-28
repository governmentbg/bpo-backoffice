create table EXT_CORE.IP_ERROR_LOG
(
    ID                int identity
        constraint IP_ERROR_LOG_pk primary key,
    ABOUT             varchar(255)  NOT NULL,
    ACTION            varchar(1000),
    ERROR_MESSAGE     varchar(2000) NOT NULL,
    USERNAME          varchar(255),
    DATE_CREATED      datetime      NOT NULL,
    CUSTOM_MESSAGE    varchar(2000),
    NEED_MANUAL_FIX   bit default 0,
    INSTRUCTION       varchar(2000),
    RESOLVED_DATE     datetime,
    RESOLVED_USERNAME varchar(255),
    RESOLVED_COMMENT  varchar(255),
    PRIORITY          varchar(255)
);


