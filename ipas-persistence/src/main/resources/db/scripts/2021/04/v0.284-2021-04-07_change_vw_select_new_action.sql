--liquibase formatted sql

--changeset dveizov:284.1
DROP VIEW VW_SELECT_NEXT_PROCESS_ACTIONS;

--changeset dveizov:284.2 splitStatements:false
CREATE VIEW VW_SELECT_NEXT_PROCESS_ACTIONS as
(
select a.ACTION_TYP,
       a.ACTION_TYPE_NAME,
       a.AUTOMATIC_ACTION_WCODE,
       a.DUE_DATE_CALCULATION_WCODE,
       a.ACTION_TYPE_GROUP,
       a.LIST_CODE,
       a.RESTRICT_LAW_CODE,
       a.RESTRICT_FILE_TYP,
       a.RESTRICT_APPL_TYP,
       a.RESTRICT_APPL_SUBTYP,
       a.USERDOC_LIST_CODE_INCLUDE,
       a.USERDOC_LIST_CODE_EXCLUDE,
       s.STATUS_CODE,
       s.STATUS_NAME,
       'NORMAL'                                                                                                     as PROCESS_ACTION_TYPE,
       (CASE
            WHEN a.NOTES1_PROMPT IS NOT NULL OR
                 a.NOTES2_PROMPT IS NOT NULL OR
                 a.NOTES3_PROMPT IS NOT NULL OR
                 a.NOTES4_PROMPT IS NOT NULL OR
                 a.NOTES5_PROMPT IS NOT NULL
                THEN 1
            ELSE 0
           END
           )                                                                                                        as CONTAIN_NOTES,
       (CASE WHEN a.DUE_DATE_CALCULATION_WCODE = 6 THEN 1 ELSE 0 END)                                               as CONTAIN_MANUAL_DUE_DATE,
       (CASE WHEN a.DUE_DATE_CALCULATION_WCODE IS NULL AND s.IND_TRIGGER_END_FREEZE='S'  THEN 1 ELSE 0 END)         as CALC_TERM_FROM_ACTION_DATE,
       (CASE WHEN m.OFFIDOC_TYP IS NOT NULL THEN m.OFFIDOC_TYP ELSE null END)                                       as GENERATED_OFFIDOC,
       m.PROC_TYP                                                                                                   as PROC_TYP,
       m.INITIAL_STATUS_CODE                                                                                        as INITIAL_STATUS_CODE
from ipasprod.CF_MIGRATION m
         join ipasprod.CF_ACTION_TYPE a on m.ACTION_TYP = a.ACTION_TYP
         join ipasprod.cf_status s on m.FINAL_STATUS_CODE = s.STATUS_CODE and (s.ind_dummy <> 'S' or s.ind_dummy is null)
where m.PROC_TYP = s.PROC_TYP
UNION ALL
select a.ACTION_TYP,
       a.ACTION_TYPE_NAME,
       a.AUTOMATIC_ACTION_WCODE,
       a.DUE_DATE_CALCULATION_WCODE,
       a.ACTION_TYPE_GROUP,
       a.LIST_CODE,
       a.RESTRICT_LAW_CODE,
       a.RESTRICT_FILE_TYP,
       a.RESTRICT_APPL_TYP,
       a.RESTRICT_APPL_SUBTYP,
       a.USERDOC_LIST_CODE_INCLUDE,
       a.USERDOC_LIST_CODE_EXCLUDE,
       '',
       '',
       'SPECIAL_SPECIFIC_TO_RANDOM'                                                                                 as PROCESS_ACTION_TYPE,
       (CASE
            WHEN a.NOTES1_PROMPT IS NOT NULL OR
                 a.NOTES2_PROMPT IS NOT NULL OR
                 a.NOTES3_PROMPT IS NOT NULL OR
                 a.NOTES4_PROMPT IS NOT NULL OR
                 a.NOTES5_PROMPT IS NOT NULL
                THEN 1
            ELSE 0
           END
           )                                                                                                        as CONTAIN_NOTES,
       (CASE WHEN a.DUE_DATE_CALCULATION_WCODE = 6 THEN 1 ELSE 0 END)                                               as CONTAIN_MANUAL_DUE_DATE,
       (CASE WHEN a.DUE_DATE_CALCULATION_WCODE IS NULL AND s.IND_TRIGGER_END_FREEZE='S'  THEN 1 ELSE 0 END)         as CALC_TERM_FROM_ACTION_DATE,
       (CASE WHEN m.OFFIDOC_TYP IS NOT NULL THEN m.OFFIDOC_TYP ELSE null END)                                       as GENERATED_OFFIDOC,
       m.PROC_TYP                                                                                                   as PROC_TYP,
       m.INITIAL_STATUS_CODE                                                                                        as INITIAL_STATUS_CODE
from ipasprod.CF_MIGRATION m
         join ipasprod.CF_ACTION_TYPE a on m.ACTION_TYP = a.ACTION_TYP
         join ipasprod.cf_status s on m.FINAL_STATUS_CODE = s.STATUS_CODE and s.ind_dummy = 'S'
where m.PROC_TYP = s.PROC_TYP
UNION ALL
select a.ACTION_TYP,
       a.ACTION_TYPE_NAME,
       a.AUTOMATIC_ACTION_WCODE,
       a.DUE_DATE_CALCULATION_WCODE,
       a.ACTION_TYPE_GROUP,
       a.LIST_CODE,
       a.RESTRICT_LAW_CODE,
       a.RESTRICT_FILE_TYP,
       a.RESTRICT_APPL_TYP,
       a.RESTRICT_APPL_SUBTYP,
       a.USERDOC_LIST_CODE_INCLUDE,
       a.USERDOC_LIST_CODE_EXCLUDE,
       s2.STATUS_CODE,
       s2.STATUS_NAME,
       'SPECIAL_RANDOM_TO_SPECIFIC'                                                                                 as PROCESS_ACTION_TYPE,
       (CASE
            WHEN a.NOTES1_PROMPT IS NOT NULL OR
                 a.NOTES2_PROMPT IS NOT NULL OR
                 a.NOTES3_PROMPT IS NOT NULL OR
                 a.NOTES4_PROMPT IS NOT NULL OR
                 a.NOTES5_PROMPT IS NOT NULL
                THEN 1
            ELSE 0
           END
           )                                                                                                         as CONTAIN_NOTES,
       (CASE WHEN a.DUE_DATE_CALCULATION_WCODE = 6 THEN 1 ELSE 0 END)                                                as CONTAIN_MANUAL_DUE_DATE,
       (CASE WHEN a.DUE_DATE_CALCULATION_WCODE IS NULL AND s2.IND_TRIGGER_END_FREEZE='S'  THEN 1 ELSE 0 END)         as CALC_TERM_FROM_ACTION_DATE,
       (CASE WHEN m.OFFIDOC_TYP IS NOT NULL THEN m.OFFIDOC_TYP ELSE null END)                                        as GENERATED_OFFIDOC,
       m.PROC_TYP                                                                                                    as PROC_TYP,
       ''                                                                                                            as INITIAL_STATUS_CODE
from ipasprod.CF_MIGRATION m
         join ipasprod.CF_ACTION_TYPE a on m.ACTION_TYP = a.ACTION_TYP
         join ipasprod.cf_status s1
              on m.INITIAL_STATUS_CODE = s1.STATUS_CODE and m.PROC_TYP = s1.PROC_TYP and s1.ind_dummy = 'S'
         join ipasprod.cf_status s2 on m.FINAL_STATUS_CODE = s2.STATUS_CODE and (s2.ind_dummy <> 'S' or s2.ind_dummy is null)

UNION ALL
select a.ACTION_TYP,
       a.ACTION_TYPE_NAME,
       a.AUTOMATIC_ACTION_WCODE,
       a.DUE_DATE_CALCULATION_WCODE,
       a.ACTION_TYPE_GROUP,
       a.LIST_CODE,
       a.RESTRICT_LAW_CODE,
       a.RESTRICT_FILE_TYP,
       a.RESTRICT_APPL_TYP,
       a.RESTRICT_APPL_SUBTYP,
       a.USERDOC_LIST_CODE_INCLUDE,
       a.USERDOC_LIST_CODE_EXCLUDE,
       '',
       '',
       'SPECIAL_RANDOM_TO_RANDOM'                                                                                   as PROCESS_ACTION_TYPE,
       (CASE
            WHEN a.NOTES1_PROMPT IS NOT NULL OR
                 a.NOTES2_PROMPT IS NOT NULL OR
                 a.NOTES3_PROMPT IS NOT NULL OR
                 a.NOTES4_PROMPT IS NOT NULL OR
                 a.NOTES5_PROMPT IS NOT NULL
                THEN 1
            ELSE 0
           END
           )                                                                                                        as CONTAIN_NOTES,
       (CASE WHEN a.DUE_DATE_CALCULATION_WCODE = 6 THEN 1 ELSE 0 END)                                               as CONTAIN_MANUAL_DUE_DATE,
       (CASE WHEN a.DUE_DATE_CALCULATION_WCODE IS NULL AND s2.IND_TRIGGER_END_FREEZE='S'  THEN 1 ELSE 0 END)        as CALC_TERM_FROM_ACTION_DATE,
       (CASE WHEN m.OFFIDOC_TYP IS NOT NULL THEN m.OFFIDOC_TYP ELSE null END)                                       as GENERATED_OFFIDOC,
       m.PROC_TYP                                                                                                   as PROC_TYP,
       ''                                                                                                           as INITIAL_STATUS_CODE
from ipasprod.CF_MIGRATION m
         join ipasprod.CF_ACTION_TYPE a on m.ACTION_TYP = a.ACTION_TYP
         join ipasprod.cf_status s1
              on m.INITIAL_STATUS_CODE = s1.STATUS_CODE and m.PROC_TYP = s1.PROC_TYP and s1.ind_dummy = 'S'
         join ipasprod.cf_status s2 on m.FINAL_STATUS_CODE = s2.STATUS_CODE and s2.ind_dummy = 'S'
UNION ALL
select a.ACTION_TYP,
       a.ACTION_TYPE_NAME,
       a.AUTOMATIC_ACTION_WCODE,
       a.DUE_DATE_CALCULATION_WCODE,
       a.ACTION_TYPE_GROUP,
       a.LIST_CODE,
       a.RESTRICT_LAW_CODE,
       a.RESTRICT_FILE_TYP,
       a.RESTRICT_APPL_TYP,
       a.RESTRICT_APPL_SUBTYP,
       a.USERDOC_LIST_CODE_INCLUDE,
       a.USERDOC_LIST_CODE_EXCLUDE,
       '',
       '',
       'NOTE'                                                         as PROCESS_ACTION_TYPE,
       (CASE
            WHEN a.NOTES1_PROMPT IS NOT NULL OR
                 a.NOTES2_PROMPT IS NOT NULL OR
                 a.NOTES3_PROMPT IS NOT NULL OR
                 a.NOTES4_PROMPT IS NOT NULL OR
                 a.NOTES5_PROMPT IS NOT NULL
                THEN 1
            ELSE 0
           END
           )                                                          as CONTAIN_NOTES,
       (CASE WHEN a.DUE_DATE_CALCULATION_WCODE = 6 THEN 1 ELSE 0 END) as CONTAIN_MANUAL_DUE_DATE,
       0                                                              as CALC_TERM_FROM_ACTION_DATE,
       null                                                           as GENERATED_OFFIDOC,
       SUBSTRING(a.ACTION_TYPE_GROUP, 13, LEN(a.ACTION_TYPE_GROUP))   as PROC_TYP,
       ''                                                             as INITIAL_STATUS_CODE
from CF_ACTION_TYPE a
where a.ACTION_TYPE_GROUP is not null
    );

