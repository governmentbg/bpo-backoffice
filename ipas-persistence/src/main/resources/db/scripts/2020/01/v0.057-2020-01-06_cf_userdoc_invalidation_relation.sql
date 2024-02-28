create table EXT_CORE.CF_USERDOC_INVALIDATION_RELATION
(
    USERDOC_TYPE varchar(20) not null,
    INVALIDATED_USERDOC_TYPE  varchar(20) not null,
    constraint CF_USERDOC_INVALIDATION_RELATION_PK
        primary key (USERDOC_TYPE, INVALIDATED_USERDOC_TYPE)
);

INSERT INTO EXT_CORE.CF_USERDOC_INVALIDATION_RELATION (USERDOC_TYPE, INVALIDATED_USERDOC_TYPE) VALUES ('ПЛ', 'Лиц');
INSERT INTO EXT_CORE.CF_USERDOC_INVALIDATION_RELATION (USERDOC_TYPE, INVALIDATED_USERDOC_TYPE) VALUES ('ПЛ', 'НИЛ');


