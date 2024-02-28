--liquibase formatted sql

--changeset mmurlev:118.1
alter table ext_core.CF_LEGAL_GROUND_TYPES
add legal_ground_code varchar(100)

alter table ext_core.CF_EARLIER_RIGHT_TYPES
add earlier_right_type_code varchar(100)

alter table ext_core.CF_EARLIER_RIGHT_TYPES
add version varchar(4)

alter table ext_core.CF_APPLICANT_AUTHORITY
add version varchar(4)

alter table ext_core.CF_APPLICANT_AUTHORITY
add applicant_code varchar(100)

--changeset mmurlev:118.2
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'invalidityGround.2' where id = 1 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'invalidityGround.11.1.1' where id = 2 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'absoluteGround.11.2, invalidityGround.11.1.2' where id = 3 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'absoluteGround.11.3, invalidityGround.11.1.3' where id = 4 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'absoluteGround.11.4, invalidityGround.11.1.4' where id = 5 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.1.1, invalidityGround.12.1.1' where id = 6 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'absoluteGround.11.5, invalidityGround.11.1.5b' where id = 7 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'absoluteGround.11.5, invalidityGround.11.1.5v' where id = 8 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'absoluteGround.11.6, invalidityGround.11.1.6' where id = 9 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'absoluteGround.11.7, invalidityGround.11.1.7' where id = 10 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'absoluteGround.11.8, invalidityGround.11.1.8' where id = 11 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'absoluteGround.11.9, invalidityGround.11.1.9' where id = 12 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'invalidityGround.11.1.10' where id = 13 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'absoluteGround.11.11, invalidityGround.11.1.11' where id = 14 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'absoluteGround.11.12, invalidityGround.11.1.12' where id = 15 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'absoluteGround.11.13, invalidityGround.11.1.13' where id = 16 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'invalidityGround.11.1.14' where id = 17 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'invalidityGround.11.1.15' where id = 18 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'absoluteGround.11.5, invalidityGround.11.1.5a' where id = 19 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.1.2, invalidityGround.12.1.2' where id = 20 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.2.1, invalidityGround.12.2.1' where id = 21 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.2.2, invalidityGround.12.2.2' where id = 22 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.2.3, invalidityGround.12.2.3' where id = 23 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.2.4, invalidityGround.12.2.4' where id = 24 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.2.5, invalidityGround.12.2.5' where id = 25 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.2.6, invalidityGround.12.2.6' where id = 26 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.2.7, invalidityGround.12.2.7' where id = 27 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.2.8, invalidityGround.12.2.8' where id = 28 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.2.9, invalidityGround.12.2.9' where id = 29 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.3, invalidityGround.12.3' where id = 30 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.4, invalidityGround.12.4' where id = 31 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.5, invalidityGround.12.5' where id = 32 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.6, invalidityGround.12.6' where id = 33 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.7, invalidityGround.12.7' where id = 34 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'relativeGround.12.8, invalidityGround.12.8' where id = 35 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'invalidityGround.36.3.2b' where id = 36 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'invalidityGround.36.3.2a' where id = 37 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'invalidityGround.36.3.2v' where id = 38 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'invalidityGround.36.3.2g' where id = 39 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'revocationGround.35.1.1' where id = 40 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'revocationGround.35.1.2' where id = 41 and version = '2019'
UPDATE ext_core.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'revocationGround.35.1.3' where id = 42 and version = '2019'



INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (43,'REL','2011','Чл. 25(1) т. 1','т. 1. марката не е била използвана съгласно чл. 19;','revocationGround.25.1.1')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (44,'REL','2011','Чл. 25(1) т. 2','т. 2. в резултат на действието или бездействието на притежателя марката е станала обичайно означение за стоката или услугата, за която е регистрирана;','revocationGround.25.1.2')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (45,'REL','2011','Чл. 25(1) т. 3','т. 3. използването на марката от притежателя или от друго лице с негово съгласие за стоките или услугите, за които е регистрирана, е по начин, който въвежда в заблуждение потребителите относно естеството, качеството или географския произход на стоките или услугите.
','revocationGround.25.1.3')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (46,'ABS','2011','Чл. 2','Марката е регистрирана в нарушение на изискванията по чл. 2 на ЗМГО','invalidityGround.2')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (47,'ABS','2011','Чл. 11(1) т. 1','т. 1. знак, който не е марка по смисъла на чл. 9, ал. 1;','invalidityGround.11.1.1')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (48,'ABS','2011','Чл 11 (1) т.2.','Чл. 11 (1) т. 2. (изм. - ДВ, бр. 43 от 2005 г.) марка, която няма отличителен характер;','invalidityGround.11.1.2, absoluteGround.11.2')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (49,'ABS','2011','Чл. 11 (1) т. 3.','Чл. 11 (1) т. 3. (доп. - ДВ, бр. 43 от 2005 г.) марка, която се състои изключително от знаци или означения, станали обичайни в говоримия език или в установената търговска практика в Република България по отношение на заявените стоки или услуги;
','invalidityGround.11.1.3, absoluteGround.11.3')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (50,'ABS','2011','Чл. 11 (1) т. 4.','Чл. 11 (1) т. 4. (изм. и доп. - ДВ, бр. 43 от 2005 г.) марка, която се състои изключително от знаци или означения, които указват вида, качеството, количеството, предназначението, стойността, географския произход, времето или метода на производство на стоките, начина на предоставяне на услугите или други характеристики на стоките или услугите;
','invalidityGround.11.1.4, absoluteGround.11.4')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (51,'ABS','2011','Чл. 11(1) т. 5 а) ','а) формата, която произтича от естеството на самата стока;','invalidityGround.11.1.5a, absoluteGround.11.5')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (52,'ABS','2011','Чл. 11(1) т. 5 б)','б) формата на стоката, която е необходима за постигане на технически резултат;','invalidityGround.11.1.5b, absoluteGround.11.5')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (53,'ABS','2011','Чл. 11(1) т. 5 в)','в) формата, която придава значителна стойност на стоката;','invalidityGround.11.1.5v, absoluteGround.11.5')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (54,'ABS','2011','Чл. 11 (1) т. 6.','Чл. 11 (1) т. 6. марка, която противоречи на обществения ред или на добрите нрави;','invalidityGround.11.1.6, absoluteGround.11.6')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (55,'ABS','2011','Чл. 11 (1) т. 7.','Чл. 11 (1) т. 7. марка, която може да въведе в заблуждение потребителите относно естеството, качеството или географския произход на стоките или услугите;','invalidityGround.11.1.7, absoluteGround.11.7')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (56,'ABS','2011','Чл. 11 (1) т. 8.','Чл. 11 (1) т. 8. (изм. - ДВ, бр. 43 от 2005 г., доп., бр. 19 от 2010 г., в сила от 10.06.2010 г.) марка, която се състои от или включва гербове, знамена или други символи, както и техни имитации на държави - членки на Парижката конвенция, както и гербове, знамена или други символи, съкращения или наименования на международни междуправителствени организации, обявени по чл. 6 ter от Парижката конвенция;
','invalidityGround.11.1.8, absoluteGround.11.8')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (57,'ABS','2011','Чл. 11 (1) т. 9.','Чл. 11 (1) т. 9. (нова - ДВ, бр. 19 от 2010 г., в сила от 10.06.2010 г.) марка, която съдържа знаци, емблеми или гербове, различни от тези, обявени по чл. 6 ter от Парижката конвенция, и представляващи особен обществен интерес;10. (доп. - ДВ, бр. 43 от 2005 г., предишна т. 9, бр. 19 от 2010 г., в сила от 10.06.2010 г.) марка, която се състои от или включва официални знаци и клейма за контрол и гаранция, когато те са предназначени за означаване на идентични или сходни стоки;
','invalidityGround.11.1.9, absoluteGround.11.9')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (58,'ABS','2011','Чл. 11(1) т. 10 ','т. 10. марка, която се състои от или включва официални знаци и клейма за контрол и гаранция, когато те са предназначени за означаване на идентични или сходни стоки;','invalidityGround.11.1.10')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (59,'ABS','2011','Чл. 11 (1) т. 11.','Чл. 11 (1) т. 11. (изм. - ДВ, бр. 28 от 2005 г., бр. 94 от 2005 г., бр. 54 от 2011 г. ) марка, която се състои от или включва наименование или изображение на културна ценност или части от културни ценности, определени по реда на Закона за културното наследство;
','invalidityGround.11.1.11, absoluteGround.11.11')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (60,'ABS','2011','Чл. 11 (1) т. 12.','Чл. 11 (1) т. 12. (отм. - ДВ, бр. 43 от 2005 г., нова, бр. 19 от 2010 г., в сила от 10.03.2011 г.) марка, която се състои изключително от заявено или регистрирано географско означение, действащо на територията на Република България, или от негови производни;
','invalidityGround.11.1.12, absoluteGround.11.12')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (61,'ABS','2011','Чл. 11 (1) т. 13.','Чл. 11 (1) т. 13. (нова - ДВ, бр. 19 от 2010 г., в сила от 10.03.2011 г.) марка, която съдържа заявено или регистрирано географско означение, действащо на територията на Република България, или негови производни, когато заявителят не е вписан ползвател на географското означение.
','invalidityGround.11.1.13, absoluteGround.11.13')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (62,'REL','2011','чл.12, ал.1, т.1','т. 1. която е идентична на по-ранна марка, когато стоките или услугите на заявената и на по-ранната марка са идентични;
','relativeGround.12.1.1, invalidityGround.12.1.1')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (63,'REL','2011','чл.12, ал.1, т.2','т. 2. когато поради нейната идентичност или сходство с по-ранна марка и идентичността или сходството на стоките или услугите на двете марки съществува вероятност за объркване на потребителите, която включва възможност за свързване с по-ранната марка;
','relativeGround.12.1.2, invalidityGround.12.1.2')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (64,'REL','2011','чл.12, ал.2, т.1','марка с по-ранна дата на подаване на заявката или с по-ранен приоритет, регистрирана по реда на ЗМГО;
','relativeGround.12.2.1, invalidityGround.12.2.1')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (65,'REL','2011','чл.12, ал.2, т.2','заявена марка с по-ранна дата на подаване или с по-ранен приоритет, ако бъде регистрирана по реда на ЗМГО;
','relativeGround.12.2.2, invalidityGround.12.2.2')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (66,'REL','2011','чл.12, ал.2, т.3','марка, регистрирана по реда на Мадридската спогодба или Протокола, с по-ранна дата на регистрацията или с по-ранен приоритет и с признато действие на територията на Република България;
','relativeGround.12.2.3, invalidityGround.12.2.3')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (67,'REL','2011','чл.12, ал.2, т.4','марка, регистрирана по реда на Мадридската спогодба или Протокола, с по-ранна дата на регистрацията или с по-ранен приоритет, ако действието й бъде признато на територията на Република България;
','relativeGround.12.2.4, invalidityGround.12.2.4')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (68,'REL','2011','чл.12, ал.2, т.5','марка на Общността с по-ранна дата на подаване на заявката или с по-ранен приоритет, или с по-ранно старшинство за територията на Република България, признато по реда на Регламент (ЕО) № 207/2009 на Съвета от 26 февруари 2009 г. относно марката на Общността (ОВ, L 78/1 от 24 март 2009 г.), наричан по-нататък "Регламент (ЕО) № 207/2009";
','relativeGround.12.2.5, invalidityGround.12.2.5')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (69,'REL','2011','чл.12, ал.2, т.6','заявена марка на Общността с по-ранна дата на подаване на заявката или с по-ранен приоритет, или с по-ранно старшинство за територията на Република България, признато по реда на Регламент (ЕО) № 207/2009, ако бъде регистрирана по реда на този регламент;
','relativeGround.12.2.6, invalidityGround.12.2.6')


INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (70,'REL','2011','чл.12, ал.6','Чл. 12. (6) При опозиция, подадена от действителния притежател на нерегистрирана марка, която се използва в търговската дейност на територията на Република България, не се регистрира марка, чиято дата на подаване е по-късна от датата на действителното търговско използване на нерегистрираната марка.
','relativeGround.12.6, invalidityGround.12.6')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (71,'REL','2011','чл.12, ал.5','Чл. 12. (5) При опозиция, подадена от действителния притежател на марка, не се регистрира марка, когато е заявена от името на агента или представителя на действителния притежател без негово съгласие.
','relativeGround.12.5, invalidityGround.12.5')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (72,'REL','2011','чл.12, ал.3','Чл. 12. (3) Не се регистрира марка, която е идентична или сходна на по-ранна марка и е предназначена за стоки и услуги, които не са идентични или сходни на тези, за които по-ранната марка е регистрирана, когато по-ранната марка се ползва с известност на територията на Република България и използването без основание на заявената марка би довело до несправедливо облагодетелстване от отличителния характер или известността на по-ранната марка или би ги увредило.','relativeGround.12.3, invalidityGround.12.3')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (73,'REL','2011','чл.12, ал.2, т.7','марка, която е общоизвестна на територията на Република България към датата на подаване на заявка за марка, съответно към датата на приоритета.
','invalidityGround.12.2.7')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (74,'REL','2011','','т. 3. марката е регистрирана на името на агент или представител на притежателя без неговото съгласие;
','invalidityGround.26.3.3')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (75,'REL','2011','','т. 4. заявителят е действал недобросъвестно при подаване на заявката, което е установено с влязло в сила съдебно решение;
','invalidityGround.26.3.4')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (76,'REL','2011','','а) право на име и портрет;','invalidityGround.26.3.5a')


INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (77,'REL','2011','','б) авторско право;','invalidityGround.26.3.5b')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (78,'REL','2011','','в) право на селекционер върху наименование на сорт или порода;','invalidityGround.26.3.5v')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (79,'REL','2011','','г) право на индустриална собственост;','invalidityGround.26.3.5g')

INSERT INTO ext_core.CF_LEGAL_GROUND_TYPES(id, legal_ground_category, version, title, descr, legal_ground_code)
VALUES (80,'REL','2011','','т. 6. марката се състои от или съдържа фирмата на друго лице, която фирма е регистрирана и използвана в Република България преди датата на подаване на заявката за регистрация във връзка с идентични или сходни стоки или услуги.
','invalidityGround.26.3.6')

UPDATE ext_Core.CF_EARLIER_RIGHT_TYPES SET earlier_right_type_code='earlierTradeMark',version='2019'
where id = 1

UPDATE ext_Core.CF_EARLIER_RIGHT_TYPES SET earlier_right_type_code='wellKnow',version='2019'
where id = 2

UPDATE ext_Core.CF_EARLIER_RIGHT_TYPES SET earlier_right_type_code='reputation',version='2019'
where id = 3

UPDATE ext_Core.CF_EARLIER_RIGHT_TYPES SET earlier_right_type_code='nonRegistered',version='2019'
where id = 4

UPDATE ext_Core.CF_EARLIER_RIGHT_TYPES SET earlier_right_type_code='badFaith',version='2019'
where id = 5

UPDATE ext_Core.CF_EARLIER_RIGHT_TYPES SET earlier_right_type_code='agentApplied',version='2019'
where id = 6

UPDATE ext_Core.CF_EARLIER_RIGHT_TYPES SET earlier_right_type_code='businessName',version='2019'
where id = 7

UPDATE ext_Core.CF_EARLIER_RIGHT_TYPES SET earlier_right_type_code='earlierGI',version='2019'
where id = 8

UPDATE ext_Core.CF_EARLIER_RIGHT_TYPES SET earlier_right_type_code='rightsAuthor',version='2019'
where id = 9

UPDATE ext_Core.CF_EARLIER_RIGHT_TYPES SET earlier_right_type_code='rightsPortraitName',version='2019'
where id = 10

UPDATE ext_Core.CF_EARLIER_RIGHT_TYPES SET earlier_right_type_code='rightsPlantVariation',version='2019'
where id = 11

UPDATE ext_Core.CF_EARLIER_RIGHT_TYPES SET earlier_right_type_code='industrialPropertyRight',version='2019'
where id = 12

INSERT INTO ext_Core.CF_EARLIER_RIGHT_TYPES (id, name, earlier_right_type_code, version)
VALUES (13,'По-ранна/и марка/и по смисъла на чл.12, ал.2, т.1-6 от ЗМГО','earlierTradeMark','2011')

INSERT INTO ext_Core.CF_EARLIER_RIGHT_TYPES (id, name, earlier_right_type_code, version)
VALUES (14,'Общоизвестна марка- чл.12, ал.2, т.7 от ЗМГО','wellKnow','2011')

INSERT INTO ext_Core.CF_EARLIER_RIGHT_TYPES (id, name, earlier_right_type_code, version)
VALUES (15,'Ползваща се с известност марка- чл.12, ал.3 от ЗМГО','reputation','2011')

INSERT INTO ext_Core.CF_EARLIER_RIGHT_TYPES (id, name, earlier_right_type_code, version)
VALUES (16,'Марка, заявена от агент/ представител - чл.12, ал.5 от ЗМГО','agentApplied','2011')

INSERT INTO ext_Core.CF_EARLIER_RIGHT_TYPES (id, name, earlier_right_type_code, version)
VALUES (17,'Нерегистрирана марка, която се използва в търговската дейност- чл.12, ал.6 от ЗМГО','nonRegistered','2011')

INSERT INTO ext_Core.CF_EARLIER_RIGHT_TYPES (id, name, earlier_right_type_code, version)
VALUES (18,'Недобросъвестност','badFaith','2011')

INSERT INTO ext_Core.CF_EARLIER_RIGHT_TYPES (id, name, earlier_right_type_code, version)
VALUES (19,'Други права','otherRights','2011')




update  ext_core.CF_APPLICANT_AUTHORITY set version = '2019',applicant_code='Owner'
where id = 1

update  ext_core.CF_APPLICANT_AUTHORITY set version = '2019',applicant_code='Actual-Owner'
where id = 2

update  ext_core.CF_APPLICANT_AUTHORITY set version = '2019',applicant_code='Co-Owner'
where id = 3

update  ext_core.CF_APPLICANT_AUTHORITY set version = '2019',applicant_code='Licensee'
where id = 4

update  ext_core.CF_APPLICANT_AUTHORITY set version = '2019',applicant_code='Merchant'
where id = 5

update  ext_core.CF_APPLICANT_AUTHORITY set version = '2019',applicant_code='Person-Authorized'
where id = 6

INSERT INTO ext_core.CF_APPLICANT_AUTHORITY (id, name, version, applicant_code)
VALUES(7,'Притежател','2011','Owner')

INSERT INTO ext_core.CF_APPLICANT_AUTHORITY (id, name, version, applicant_code)
VALUES(8,'Притежател','2011','Licensee')


