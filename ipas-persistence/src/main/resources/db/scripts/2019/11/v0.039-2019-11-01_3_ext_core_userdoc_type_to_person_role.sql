create table EXT_CORE.CF_USERDOC_TYPE_TO_PERSON_ROLE
(
    USERDOC_TYP varchar(7) not null
        constraint CF_USERDOC_TYPE_TO_PERSON_ROLE_USERDOC_TYPE_USERDOC_TYP_fk
            references IPASPROD.CF_USERDOC_TYPE,
    ROLE        varchar(50)        not null
        constraint CF_USERDOC_TYPE_TO_PERSON_ROLE_CF_USERDOC_PERSON_ROLE_ROLE_fk
            references EXT_CORE.CF_USERDOC_PERSON_ROLE,
    constraint CF_USERDOC_TYPE_TO_PERSON_ROLE_pk
        primary key nonclustered (USERDOC_TYP, ROLE)
)
;

INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_PERSON_ROLE (USERDOC_TYP, ROLE)
SELECT  u.USERDOC_TYP,
        'APPLICANT'
FROM IPASPROD.CF_USERDOC_TYPE u;


