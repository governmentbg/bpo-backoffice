create table EXT_CORE.CF_DISTRICT
(
    id                    int         NOT NULL
        constraint CF_DISTRICT_pk
            primary key,
    code                  varchar(10) not null,
    code2                 varchar(10),
    secondlevelregioncode varchar(10),
    name                  varchar(255),
    mainsettlementcode    varchar(10),
    alias                 varchar(200),
    description           varchar(500),
    isactive              bit,
    version               int         NOT NULL DEFAULT 0,
    nameen                varchar(200),
);


insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(1,'BLG','01','BG41','Благоевград','04279','','',1,0,'Blaevgrad');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(2,'BGS','02','BG34','Бургас','07079','','',1,0,'Burgas');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(3,'VAR','03','BG33','Варна','10135','','',1,0,'Varna');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(4,'VTR','04','BG32','Велико Търново','10447','','',1,0,'Veliko Tarnovo');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(5,'VID','05','BG31','Видин','10971','','',1,0,'Vidin');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(6,'VRC','06','BG31','Враца','12259','','',1,0,'Vratsa');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(7,'GAB','07','BG32','Габрово','14218','','',1,0,'Gabrovo');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(8,'DOB','08','BG33','Добрич','72624','','',1,0,'Dobrich');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(9,'KRZ','09','BG42','Кърджали','40909','','',1,0,'Kardzhali');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(10,'KNL','10','BG41','Кюстендил','41112','','',1,0,'Kyustendil');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(11,'LOV','11','BG31','Ловеч','43952','','',1,0,'Lovech');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(12,'MON','12','BG31','Монтана','48489','','',1,0,'Montana');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(13,'PAZ','13','BG42','Пазарджик','55155','','',1,0,'Pazardzhik');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(14,'PER','14','BG41','Перник','55871','','',1,0,'Pernik');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(15,'PVN','15','BG31','Плевен','56722','','',1,0,'Pleven');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(16,'PDV','16','BG42','Пловдив','56784','','',1,0,'Plovdiv');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(17,'RAZ','17','BG32','Разград','61710','','',1,0,'Razgrad');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(18,'RSE','18','BG32','Русе','63427','','',1,0,'Ruse');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(19,'SLS','19','BG32','Силистра','66425','','',1,0,'Silistra');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(20,'SLV','20','BG34','Сливен','67338','','',1,0,'Sliven');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(21,'SML','21','BG42','Смолян','67653','','',1,0,'Smolyan');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(22,'SOF','22','BG41','София (столица)','68134','','',1,0,'Sofia (capital)');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(23,'SFO','23','BG41','София','68134','','',1,0,'Sofia');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(24,'SZR','24','BG34','Стара Загора','68850','','',1,0,'Stara Zara');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(25,'TGV','25','BG33','Търговище','73626','','',1,0,'Tarvishte');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(26,'HKV','26','BG42','Хасково','77195','','',1,0,'Haskovo');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(27,'SHU','27','BG33','Шумен','83510','','',1,0,'Shumen');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(28,'JAM','28','BG34','Ямбол','87374','','',1,0,'Yambol');
insert into EXT_CORE.CF_DISTRICT(id,code, code2, secondlevelregioncode, name, mainsettlementcode, alias, description, isactive, version, nameen) values(29,'SYS','99','SYS','*','99992','','',1,0,'');


