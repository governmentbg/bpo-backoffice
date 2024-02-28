--liquibase formatted sql

--changeset akehayov:355.1
alter table IP_PATENT_IPC_CLASSES drop constraint SOLICITUD_CIP_FK_CIP;

--changeset akehayov:355.2
alter table IP_PATENT_IPC_CLASSES
    add constraint SOLICITUD_CIP_FK_CIP
        foreign key (IPC_EDITION_CODE, IPC_SECTION_CODE, IPC_CLASS_CODE, IPC_SUBCLASS_CODE, IPC_GROUP_CODE,
                     IPC_SUBGROUP_CODE) references CF_CLASS_IPC
            on update cascade;