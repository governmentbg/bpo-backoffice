create table EXT_CORE.CF_SETTLEMENT
(
    id int not null
        constraint CF_SETTLEMENT_pk
            primary key,
    municipalityid int NOT NULL,
    districtid int NOT NULL,
    code varchar(10) NOT NULL,
    municipalitycode varchar(10),
    districtcode varchar(10),
    municipalitycode2 varchar(10),
    districtcode2 varchar(10),
    name varchar(255),
    typename varchar(20),
    settlementname varchar(255),
    typecode varchar(50),
    mayoraltycode varchar(50),
    category varchar(50),
    altitude varchar(50),
    alias varchar(200),
    description varchar(500),
    isdistrict bit NOT NULL,
    isactive bit NOT NULL,
    version int NOT NULL DEFAULT 0,
    settlementnameen varchar(255)
)
;

create index CF_SETTLEMENT_name_index
    on EXT_CORE.CF_SETTLEMENT (name)
;

create index CF_SETTLEMENT_settlementname_index
    on EXT_CORE.CF_SETTLEMENT (settlementname)
;



