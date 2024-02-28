--liquibase formatted sql

--changeset ggeorgiev:246.1
drop procedure [IPASPROD].[deleteMarkDetails];

--changeset ggeorgiev:246.2 splitStatements:false
CREATE PROCEDURE [IPASPROD].[deleteMarkDetails]
    @fileSeq [varchar](2),
    @fileTyp [varchar](1),
    @fileSer [numeric](4, 0),
    @fileNbr [numeric](10, 0)

AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION


            print '--1.  records related to the mark';

            delete
            FROM [IP_MARK_NICE_CLASSES]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete
            FROM [IP_MARK_REGULATION]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete
            FROM [IP_MARK_LOG_CHANGES]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete
            FROM [IP_LOGO_VIENNA_CLASSES]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete
            FROM [IP_LOGO]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete
            FROM [IP_MARK_WORDS]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            DELETE
            FROM [IP_MARK_REPRS]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            DELETE
            FROM [IP_MARK_OWNERS]
            WHERE FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete
            from IP_FILE_RELATIONSHIP
            where FILE_SEQ1=@fileSeq and FILE_TYP1=@fileTyp and FILE_NBR1=@fileNbr and FILE_SER1=@fileSer;

            delete
            from IP_FILE_RELATIONSHIP
            where FILE_SEQ2=@fileSeq and FILE_TYP2=@fileTyp and FILE_NBR2=@fileNbr and FILE_SER2=@fileSer;

            delete
            from IP_MARK_PRIORITIES
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            -- mark actions
            delete a
            FROM [IP_ACTION] a
                     join [IP_PROC] p on a.PROC_NBR=p.PROC_NBR and a.PROC_TYP=p.PROC_TYP
            where p.FILE_SEQ=@fileSeq and p.FILE_TYP=@fileTyp and p.FILE_NBR=@fileNbr and p.FILE_SER=@fileSer;

            print '--2. office docs'
            delete a
            from IP_OFFIDOC od
                     join IP_PROC odp on od.OFFIDOC_PROC_NBR=odp.PROC_NBR and od.OFFIDOC_PROC_TYP=odp.PROC_TYP
                     join IP_ACTION a on a.PROC_NBR=odp.PROC_NBR and a.PROC_TYP=odp.PROC_TYP
                     join IP_PROC p on od.PROC_NBR=p.PROC_NBR and od.PROC_TYP=p.PROC_TYP
                     join IP_DOC pt on pt.FILE_NBR=p.FILE_NBR and pt.FILE_SEQ=p.FILE_SEQ and pt.FILE_SER=p.FILE_SER and pt.FILE_TYP=p.FILE_TYP
            where pt.FILE_SEQ=@fileSeq and pt.FILE_TYP=@fileTyp and pt.FILE_NBR=@fileNbr and pt.FILE_SER=@fileSer;


            delete od
            from IP_OFFIDOC od
                     join IP_PROC p on od.PROC_NBR=p.PROC_NBR and od.PROC_TYP=p.PROC_TYP
                     join IP_DOC pt on pt.FILE_NBR=p.FILE_NBR and pt.FILE_SEQ=p.FILE_SEQ and pt.FILE_SER=p.FILE_SER and pt.FILE_TYP=p.FILE_TYP
            where pt.FILE_SEQ=@fileSeq and pt.FILE_TYP=@fileTyp and pt.FILE_NBR=@fileNbr and pt.FILE_SER=@fileSer;

            delete odp
            from IP_PROC odp
                     join IP_PROC p on odp.UPPER_PROC_NBR=p.PROC_NBR and odp.UPPER_PROC_TYP=p.PROC_TYP
                     join IP_DOC pt on pt.FILE_NBR=p.FILE_NBR and pt.FILE_SEQ=p.FILE_SEQ and pt.FILE_SER=p.FILE_SER and pt.FILE_TYP=p.FILE_TYP
            where pt.FILE_SEQ=@fileSeq and pt.FILE_TYP=@fileTyp and pt.FILE_NBR=@fileNbr and pt.FILE_SER=@fileSer and odp.PROC_TYP=3;

            print '--3. userdocs'

            --3.0.0 reception tables
            delete c
            from ext_reception.USERDOC_CORRESPONDENT c
                     join EXT_RECEPTION.RECEPTION_USERDOC_REQUEST rr on rr.ID = c.RECEPTION_USERDOC_REQUEST_ID
                     JOIN IP_DOC_FILES df on rr.DOC_LOG = df.DOC_LOG AND rr.DOC_ORI = df.DOC_ORI and rr.DOC_SER = df.DOC_SER and rr.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete rr
            FROM EXT_RECEPTION.RECEPTION_USERDOC_REQUEST rr
                     JOIN IP_DOC_FILES df on rr.DOC_LOG = df.DOC_LOG AND rr.DOC_ORI = df.DOC_ORI and rr.DOC_SER = df.DOC_SER and rr.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            --3.0.1 ext tables
            delete e
            FROM EXT_CORE.IP_USERDOC_APPROVED_DATA e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_APPROVED_NICE_CLASSES e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_COURT_APPEAL e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_EFILING_DATA e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_GROUND_NICE_CLASSES e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_NICE_CLASSES e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_PERSON e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_REGISTRATION_NUMBER_CHANGE_LOG e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_REVIEWERS e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_ROOT_GROUNDS e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_SINGLE_DESIGN e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_SINGLE_DESIGN_LOCARNO_CLASSES e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_SUB_GROUNDS e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            -- 3.1. userdoc parties
            delete d
            from IP_DOC_FILES df
                     join IP_USERDOC_OLD_OWNERS d on d.DOC_LOG=df.DOC_LOG and d.DOC_SER=df.DOC_SER and d.DOC_NBR=df.DOC_NBR and d.DOC_ORI=df.DOC_ORI
            where df.FILE_SEQ=@fileSeq and df.FILE_TYP=@fileTyp and df.file_ser=@fileSer and df.FILE_NBR=@fileNbr;

            delete d
            from IP_DOC_FILES df
                     join IP_USERDOC_NEW_OWNERS d on d.DOC_LOG=df.DOC_LOG and d.DOC_SER=df.DOC_SER and d.DOC_NBR=df.DOC_NBR and d.DOC_ORI=df.DOC_ORI
            where df.FILE_SEQ=@fileSeq and df.FILE_TYP=@fileTyp and df.file_ser=@fileSer and df.FILE_NBR=@fileNbr;

            delete d
            from IP_DOC_FILES df
                     join IP_USERDOC_REPRS d on d.DOC_LOG=df.DOC_LOG and d.DOC_SER=df.DOC_SER and d.DOC_NBR=df.DOC_NBR and d.DOC_ORI=df.DOC_ORI
            where df.FILE_SEQ=@fileSeq and df.FILE_TYP=@fileTyp and df.file_ser=@fileSer and df.FILE_NBR=@fileNbr;


            -- 3.2. actions for all the userdocs associated with a mark (only if a workflow has been executed for any of the user documents)
            delete ud_a
            from ip_proc main_proc
                     join ip_proc ud_proc on ud_proc.UPPER_PROC_NBR=main_proc.PROC_NBR and ud_proc.UPPER_PROC_TYP=main_proc.PROC_TYP
                     join IP_ACTION ud_a on ud_a.PROC_NBR=ud_proc.PROC_NBR and ud_a.PROC_TYP=ud_proc.PROC_TYP
            where main_proc.file_ser=@fileSer and main_proc.file_nbr=@fileNbr and main_proc.FILE_SEQ=@fileSeq and main_proc.FILE_TYP=@fileTyp;

            -- 3.3. redundant tables

            --process responsible users log changes for all the userdocs associated with a patent
            delete ruc
            from IP_DOC_FILES df
                     join IP_DOC d on d.DOC_LOG=df.DOC_LOG and d.DOC_SER=df.DOC_SER and d.DOC_NBR=df.DOC_NBR and d.DOC_ORI=df.DOC_ORI and d.FILE_NBR is null
                     join IP_PROC p on d.DOC_LOG=p.DOC_LOG and d.DOC_ORI=p.DOC_ORI and d.DOC_SER=p.DOC_SER and d.DOC_NBR=p.DOC_NBR
                     JOIN EXT_CORE.IP_PROC_RESPONSIBLE_USER_CHANGES ruc on ruc.PROC_NBR = p.PROC_NBR and ruc.PROC_TYP = p.PROC_TYP
            where df.FILE_SEQ=@fileSeq and df.FILE_TYP=@fileTyp and df.file_ser=@fileSer and df.FILE_NBR=@fileNbr;

            -- IP_DOC_FILES (all info is in user document IP_PROC record)
            delete
            from IP_DOC_FILES
            where file_ser=@fileSer and file_nbr=@fileNbr and FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp;

            -- IP_USERDOC_PROCS (all info is in user document IP_PROC record)
            delete
            from IP_USERDOC_PROCS
            where userdoc_file_ser=@fileSer and userdoc_file_nbr=@fileNbr and userdoc_FILE_SEQ=@fileSeq and userdoc_FILE_TYP=@fileTyp;


            print '-- 3.4. sub-processes for userdocs'
            -- 3.4.1. actions for sub-processes for userdocs
            delete a
            from ip_proc main_proc
                     join ip_proc ud_proc on ud_proc.UPPER_PROC_NBR=main_proc.PROC_NBR and ud_proc.UPPER_PROC_TYP=main_proc.PROC_TYP
                     join ip_proc ud_subproc on ud_subproc.UPPER_PROC_NBR=ud_proc.PROC_NBR and ud_subproc.UPPER_PROC_TYP=ud_proc.PROC_TYP
                     join IP_ACTION a on a.PROC_NBR=ud_subproc.PROC_NBR and a.PROC_TYP=ud_subproc.PROC_TYP
            where main_proc.file_ser=@fileSer and main_proc.file_nbr=@fileNbr and main_proc.FILE_SEQ=@fileSeq and main_proc.FILE_TYP=@fileTyp;

            -- 3.4.2. officedocs for sub-processes for userdocs
            -- actions off the officedocs of the subprocess es of the userdoc
            delete a
            from ip_proc main_proc
                     join ip_proc ud_proc on ud_proc.UPPER_PROC_NBR=main_proc.PROC_NBR and ud_proc.UPPER_PROC_TYP=main_proc.PROC_TYP
                     join ip_proc ud_subproc on ud_subproc.UPPER_PROC_NBR=ud_proc.PROC_NBR and ud_subproc.UPPER_PROC_TYP=ud_proc.PROC_TYP
                     join ip_proc od_proc on od_proc.UPPER_PROC_NBR=ud_subproc.PROC_NBR and od_proc.UPPER_PROC_TYP=ud_subproc.PROC_TYP
                     join IP_ACTION a on od_proc.PROC_NBR=a.PROC_NBR and od_proc.PROC_TYP=a.PROC_TYP
            where main_proc.file_ser=@fileSer and main_proc.file_nbr=@fileNbr and main_proc.FILE_SEQ=@fileSeq and main_proc.FILE_TYP=@fileTyp;


            -- office docs
            delete od
            from ip_proc main_proc
                     join ip_proc ud_proc on ud_proc.UPPER_PROC_NBR=main_proc.PROC_NBR and ud_proc.UPPER_PROC_TYP=main_proc.PROC_TYP
                     join ip_proc ud_subproc on ud_subproc.UPPER_PROC_NBR=ud_proc.PROC_NBR and ud_subproc.UPPER_PROC_TYP=ud_proc.PROC_TYP
                     join IP_OFFIDOC od on od.PROC_NBR=ud_subproc.PROC_NBR and od.PROC_TYP=ud_subproc.PROC_TYP
            where main_proc.file_ser=@fileSer and main_proc.file_nbr=@fileNbr and main_proc.FILE_SEQ=@fileSeq and main_proc.FILE_TYP=@fileTyp;

            -- officedoc processes
            delete od_proc
            from ip_proc main_proc
                     join ip_proc ud_proc on ud_proc.UPPER_PROC_NBR=main_proc.PROC_NBR and ud_proc.UPPER_PROC_TYP=main_proc.PROC_TYP
                     join ip_proc ud_subproc on ud_subproc.UPPER_PROC_NBR=ud_proc.PROC_NBR and ud_subproc.UPPER_PROC_TYP=ud_proc.PROC_TYP
                     join ip_proc od_proc on od_proc.UPPER_PROC_NBR=ud_subproc.PROC_NBR and od_proc.UPPER_PROC_TYP=ud_subproc.PROC_TYP
            where main_proc.file_ser=@fileSer and main_proc.file_nbr=@fileNbr and main_proc.FILE_SEQ=@fileSeq and main_proc.FILE_TYP=@fileTyp;



            --3.4.3. officedocs for userdocs (each officedoc is a usrdoc subproc; actions are already deleted ин 3.4.1)
            delete od
            from ip_proc main_proc
                     join ip_proc ud_proc on ud_proc.UPPER_PROC_NBR=main_proc.PROC_NBR and ud_proc.UPPER_PROC_TYP=main_proc.PROC_TYP
                     join IP_OFFIDOC od on od.PROC_NBR=ud_proc.PROC_NBR and od.PROC_TYP=ud_proc.PROC_TYP
            where main_proc.file_ser=@fileSer and main_proc.file_nbr=@fileNbr and main_proc.FILE_SEQ=@fileSeq and main_proc.FILE_TYP=@fileTyp;

            --3.4.4. sub-processes IP_PROC
            delete ud_subproc
            from ip_proc main_proc
                     join ip_proc ud_proc on ud_proc.UPPER_PROC_NBR=main_proc.PROC_NBR and ud_proc.UPPER_PROC_TYP=main_proc.PROC_TYP
                     join ip_proc ud_subproc on ud_subproc.UPPER_PROC_NBR=ud_proc.PROC_NBR and ud_subproc.UPPER_PROC_TYP=ud_proc.PROC_TYP
            where main_proc.file_ser=@fileSer and main_proc.file_nbr=@fileNbr and main_proc.FILE_SEQ=@fileSeq and main_proc.FILE_TYP=@fileTyp;

            print '-- 3.5. User docs	'

            -- IP_USERDOC_TYPES
            delete udt
            from ip_proc main_proc
                     join ip_proc ud_proc on ud_proc.UPPER_PROC_NBR=main_proc.PROC_NBR and ud_proc.UPPER_PROC_TYP=main_proc.PROC_TYP
                     join IP_USERDOC_TYPES udt on udt.DOC_LOG=ud_proc.DOC_LOG and udt.DOC_SER=ud_proc.DOC_SER and udt.DOC_NBR=ud_proc.DOC_NBR and udt.DOC_ORI=ud_proc.DOC_ORI
            where main_proc.file_ser=@fileSer and main_proc.file_nbr=@fileNbr and main_proc.FILE_SEQ=@fileSeq and main_proc.FILE_TYP=@fileTyp;

            -- IP_USERDOC
            delete udt
            from ip_proc main_proc
                     join ip_proc ud_proc on ud_proc.UPPER_PROC_NBR=main_proc.PROC_NBR and ud_proc.UPPER_PROC_TYP=main_proc.PROC_TYP
                     join IP_USERDOC udt on udt.DOC_LOG=ud_proc.DOC_LOG and udt.DOC_SER=ud_proc.DOC_SER and udt.DOC_NBR=ud_proc.DOC_NBR and udt.DOC_ORI=ud_proc.DOC_ORI
            where main_proc.file_ser=@fileSer and main_proc.file_nbr=@fileNbr and main_proc.FILE_SEQ=@fileSeq and main_proc.FILE_TYP=@fileTyp;

            -- IP_DOC
            delete udt
            from ip_proc main_proc
                     join ip_proc ud_proc on ud_proc.UPPER_PROC_NBR=main_proc.PROC_NBR and ud_proc.UPPER_PROC_TYP=main_proc.PROC_TYP
                     join IP_DOC udt on udt.DOC_LOG=ud_proc.DOC_LOG and udt.DOC_SER=ud_proc.DOC_SER and udt.DOC_NBR=ud_proc.DOC_NBR and udt.DOC_ORI=ud_proc.DOC_ORI
            where main_proc.file_ser=@fileSer and main_proc.file_nbr=@fileNbr and main_proc.FILE_SEQ=@fileSeq and main_proc.FILE_TYP=@fileTyp;

            -- processes for all the userdocs associated with a mark
            delete ud_proc
            from ip_proc main_proc
                     join ip_proc ud_proc on ud_proc.UPPER_PROC_NBR=main_proc.PROC_NBR and ud_proc.UPPER_PROC_TYP=main_proc.PROC_TYP
            where main_proc.file_ser=@fileSer and main_proc.file_nbr=@fileNbr and main_proc.FILE_SEQ=@fileSeq and main_proc.FILE_TYP=@fileTyp;

            print '--4.  mark itsef '

            delete c
            from EXT_RECEPTION.CORRESPONDENT c
                     join EXT_RECEPTION.RECEPTION_REQUEST rr on rr.ID = c.RECEPTION_REQUEST_ID
            where rr.FILE_SEQ = @fileSeq and rr.FILE_TYP = @fileTyp and rr.FILE_SER = @fileSer and rr.FILE_NBR = @fileNbr;

            delete from EXT_RECEPTION.RECEPTION_REQUEST where FILE_SEQ = @fileSeq and FILE_TYP = @fileTyp and FILE_SER = @fileSer and FILE_NBR = @fileNbr;

            delete FROM EXT_CORE.IP_MARK_ATTACHMENT_VIENNA_CLASSES
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete FROM EXT_CORE.IP_MARK_ATTACHMENT
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete from EXT_CORE.IP_FILE_RECORDAL
            where FILE_SEQ = @fileSeq and FILE_TYP = @fileTyp and FILE_SER = @fileSer and FILE_NBR = @fileNbr;

            delete from EXT_CORE.IP_FILE_RELATIONSHIP_EXTENDED
            where FILE_SEQ = @fileSeq and FILE_TYP = @fileTyp and FILE_SER = @fileSer and FILE_NBR = @fileNbr;

            delete from EXT_CORE.IP_OBJECT_EFILING_DATA
            where FILE_SEQ = @fileSeq and FILE_TYP = @fileTyp and FILE_SER = @fileSer and FILE_NBR = @fileNbr;


            delete
            FROM [IP_MARK]
            WHERE FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete
            FROM [IP_FILE]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            -- main mark IP_DOC
            delete
            FROM [IP_DOC]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            -- main mark IP_PROC
            delete p
            FROM [IP_PROC] p
            where p.FILE_SEQ=@fileSeq and p.FILE_TYP=@fileTyp and p.FILE_NBR=@fileNbr and p.FILE_SER=@fileSer;

            delete
            FROM [IP_DOC_FILES]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;
        COMMIT TRANSACTION
    END TRY
    BEGIN CATCH
        DECLARE @ErrorSeverity INT,
            @ErrorNumber  INT,
            @ErrorMessage NVARCHAR(4000),
            @ErrorState INT,
            @ErrorLine INT,
            @ErrorProc NVARCHAR(200)
        SET @ErrorSeverity = ERROR_SEVERITY()
        SET @ErrorNumber  = ERROR_NUMBER()
        SET @ErrorMessage = ERROR_MESSAGE()
        SET @ErrorState  = ERROR_STATE()

        -- Not all errors generate an error state, to set to 1 if it's zero
        IF @ErrorState = 0
            SET @ErrorState = 1
        -- If the error renders the transaction as uncommittable or we have open transactions, we may want to rollback
        IF @@TRANCOUNT > 0
            BEGIN
                ROLLBACK TRANSACTION
            END
        RAISERROR (@ErrorMessage , @ErrorSeverity, @ErrorState, @ErrorNumber)
    END CATCH
    RETURN @@ERROR
END



