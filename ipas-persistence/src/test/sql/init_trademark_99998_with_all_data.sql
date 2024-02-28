--INSERT OWNERS
delete from IPASPROD.IP_MARK_OWNERS where file_nbr = 99998 and FILE_SER = 2007 and file_typ = 'N';
INSERT INTO IPASPROD.IP_MARK_OWNERS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, NOTES, ORDER_NBR) VALUES (1, 'BG', 'N', 2007, 99998, 149120, 1, 'notes-123', 1);
INSERT INTO IPASPROD.IP_MARK_OWNERS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, NOTES, ORDER_NBR) VALUES (1, 'BG', 'N', 2007, 99998, 374331, 1, 'notes-123', 2);
INSERT INTO IPASPROD.IP_MARK_OWNERS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, NOTES, ORDER_NBR) VALUES (1, 'BG', 'N', 2007, 99998, 374333, 1, 'notes-123', 3);
INSERT INTO IPASPROD.IP_MARK_OWNERS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, NOTES, ORDER_NBR) VALUES (1, 'BG', 'N', 2007, 99998, 374335, 1, 'notes-123', 4);
INSERT INTO IPASPROD.IP_MARK_OWNERS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, NOTES, ORDER_NBR) VALUES (1, 'BG', 'N', 2007, 99998, 374337, 1, 'notes-123', 5);
INSERT INTO IPASPROD.IP_MARK_OWNERS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, NOTES, ORDER_NBR) VALUES (1, 'BG', 'N', 2007, 99998, 374339, 1, 'notes-123', 6);
INSERT INTO IPASPROD.IP_MARK_OWNERS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, NOTES, ORDER_NBR) VALUES (1, 'BG', 'N', 2007, 99998, 374341, 1, 'notes-123', 7);
INSERT INTO IPASPROD.IP_MARK_OWNERS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, NOTES, ORDER_NBR) VALUES (1, 'BG', 'N', 2007, 99998, 374343, 1, 'notes-123', 8);
INSERT INTO IPASPROD.IP_MARK_OWNERS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, NOTES, ORDER_NBR) VALUES (1, 'BG', 'N', 2007, 99998, 374329, 1, 'notes-123', 9);
INSERT INTO IPASPROD.IP_MARK_OWNERS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, NOTES, ORDER_NBR) VALUES (1, 'BG', 'N', 2007, 99998, 374347, 1, 'notes-123', 10);
INSERT INTO IPASPROD.IP_MARK_OWNERS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, NOTES, ORDER_NBR) VALUES (1, 'BG', 'N', 2007, 99998, 374349, 1, 'notes-123', 11);
INSERT INTO IPASPROD.IP_MARK_OWNERS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, NOTES, ORDER_NBR) VALUES (1, 'BG', 'N', 2007, 99998, 374351, 1, 'notes-123', 12);
INSERT INTO IPASPROD.IP_MARK_OWNERS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, NOTES, ORDER_NBR) VALUES (1, 'BG', 'N', 2007, 99998, 374353, 1, 'notes-123', 13);
INSERT INTO IPASPROD.IP_MARK_OWNERS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, NOTES, ORDER_NBR) VALUES (1, 'BG', 'N', 2007, 99998, 374355, 1, 'notes-123', 14);
INSERT INTO IPASPROD.IP_MARK_OWNERS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, NOTES, ORDER_NBR) VALUES (1, 'BG', 'N', 2007, 99998, 374357, 1, 'notes-123', 15);


--inserting representatives
delete from ip_mark_reprs where file_nbr = 99998 and FILE_SER = 2007 and file_typ = 'N';
--inserting normal representatives
INSERT INTO IPASPROD.IP_MARK_REPRS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, REPRESENTATIVE_TYP)
select top 10 1, 'BG', 'N', 2007, 99998, p.PERSON_NBR, ADDR_NBR, 'AG' from IP_PERSON p
                                                                               join IP_PERSON_ADDRESSES pa on p.PERSON_NBR = pa.PERSON_NBR
where p.AGENT_CODE is not null;
--inserting partnership representatives
INSERT INTO IPASPROD.IP_MARK_REPRS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, REPRESENTATIVE_TYP)
select top 10 1, 'BG', 'N', 2007, 99998, p.PERSON_NBR, ADDR_NBR, 'PA'
from IP_PERSON p
         join IP_PERSON_ADDRESSES pa on p.PERSON_NBR = pa.PERSON_NBR
         join EXT_AGENT.EXTENDED_PARTNERSHIP par on par.PERSON_NBR = p.PERSON_NBR;
--inserting address for correspondence
INSERT INTO IPASPROD.IP_MARK_REPRS (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, REPRESENTATIVE_TYP) VALUES (1, 'BG', 'N', 2007, 99998, 306343, 1, 'AS');

--relations from type 1
delete from IPASPROD.IP_FILE_RELATIONSHIP where file_nbr1 = 99998 and FILE_SER1 = 2007 and file_typ1 = 'N';
delete from IPASPROD.IP_FILE_RELATIONSHIP where file_nbr2 = 99998 and FILE_SER2 = 2007 and file_typ2 = 'N';
INSERT INTO IPASPROD.IP_FILE_RELATIONSHIP (ROW_VERSION, FILE_SEQ1, FILE_TYP1, FILE_SER1, FILE_NBR1, FILE_SEQ2, FILE_TYP2, FILE_SER2, FILE_NBR2, RELATIONSHIP_TYP) VALUES (1, 'BG', 'N', 2007, 99998, 'BG', 'D', 2008, 91873, 'РНМ');
INSERT INTO IPASPROD.IP_FILE_RELATIONSHIP (ROW_VERSION, FILE_SEQ1, FILE_TYP1, FILE_SER1, FILE_NBR1, FILE_SEQ2, FILE_TYP2, FILE_SER2, FILE_NBR2, RELATIONSHIP_TYP) VALUES (1, 'BG', 'N', 2007, 99998, 'BG', 'D', 2012, 102812, 'РНМ');
INSERT INTO IPASPROD.IP_FILE_RELATIONSHIP (ROW_VERSION, FILE_SEQ1, FILE_TYP1, FILE_SER1, FILE_NBR1, FILE_SEQ2, FILE_TYP2, FILE_SER2, FILE_NBR2, RELATIONSHIP_TYP) VALUES (1, 'BG', 'N', 2007, 99998, 'BG', 'D', 2012, 102813, 'РНМ');
INSERT INTO IPASPROD.IP_FILE_RELATIONSHIP (ROW_VERSION, FILE_SEQ1, FILE_TYP1, FILE_SER1, FILE_NBR1, FILE_SEQ2, FILE_TYP2, FILE_SER2, FILE_NBR2, RELATIONSHIP_TYP) VALUES (1, 'BG', 'N', 2007, 99998, 'BG', 'D', 2012, 114503, 'РНМ');
INSERT INTO IPASPROD.IP_FILE_RELATIONSHIP (ROW_VERSION, FILE_SEQ1, FILE_TYP1, FILE_SER1, FILE_NBR1, FILE_SEQ2, FILE_TYP2, FILE_SER2, FILE_NBR2, RELATIONSHIP_TYP) VALUES (1, 'BG', 'N', 2007, 99998, 'BG', 'D', 2013, 27791, 'РНМ');
INSERT INTO IPASPROD.IP_FILE_RELATIONSHIP (ROW_VERSION, FILE_SEQ1, FILE_TYP1, FILE_SER1, FILE_NBR1, FILE_SEQ2, FILE_TYP2, FILE_SER2, FILE_NBR2, RELATIONSHIP_TYP) VALUES (1, 'BG', 'N', 2007, 99998, 'BG', 'D', 2013, 91540, 'РНМ');
INSERT INTO IPASPROD.IP_FILE_RELATIONSHIP (ROW_VERSION, FILE_SEQ1, FILE_TYP1, FILE_SER1, FILE_NBR1, FILE_SEQ2, FILE_TYP2, FILE_SER2, FILE_NBR2, RELATIONSHIP_TYP) VALUES (1, 'BG', 'N', 2007, 99998, 'BG', 'D', 2013, 91541, 'РНМ');
INSERT INTO IPASPROD.IP_FILE_RELATIONSHIP (ROW_VERSION, FILE_SEQ1, FILE_TYP1, FILE_SER1, FILE_NBR1, FILE_SEQ2, FILE_TYP2, FILE_SER2, FILE_NBR2, RELATIONSHIP_TYP) VALUES (1, 'BG', 'N', 2007, 99998, 'BG', 'D', 2013, 91542, 'РНМ');
INSERT INTO IPASPROD.IP_FILE_RELATIONSHIP (ROW_VERSION, FILE_SEQ1, FILE_TYP1, FILE_SER1, FILE_NBR1, FILE_SEQ2, FILE_TYP2, FILE_SER2, FILE_NBR2, RELATIONSHIP_TYP) VALUES (1, 'BG', 'N', 2007, 99998, 'BG', 'D', 2013, 107818, 'РНМ');
INSERT INTO IPASPROD.IP_FILE_RELATIONSHIP (ROW_VERSION, FILE_SEQ1, FILE_TYP1, FILE_SER1, FILE_NBR1, FILE_SEQ2, FILE_TYP2, FILE_SER2, FILE_NBR2, RELATIONSHIP_TYP) VALUES (1, 'BG', 'N', 2007, 99998, 'BG', 'D', 2014, 91467, 'РНМ');

--relations from type 2
INSERT INTO IPASPROD.IP_FILE_RELATIONSHIP (ROW_VERSION, FILE_SEQ1, FILE_TYP1, FILE_SER1, FILE_NBR1, FILE_SEQ2, FILE_TYP2, FILE_SER2, FILE_NBR2, RELATIONSHIP_TYP) VALUES (1, 'BG', 'N', 2012, 122197, 'BG', 'N', 2007, 99998, 'РНМ');
INSERT INTO IPASPROD.IP_FILE_RELATIONSHIP (ROW_VERSION, FILE_SEQ1, FILE_TYP1, FILE_SER1, FILE_NBR1, FILE_SEQ2, FILE_TYP2, FILE_SER2, FILE_NBR2, RELATIONSHIP_TYP) VALUES (1, 'BG', 'N', 2015, 135790, 'BG', 'N', 2007, 99998, 'РНМ');


--insert priorities
delete from IP_MARK_PRIORITIES where file_nbr = 99998 and FILE_SER = 2007 and file_typ = 'N';
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'BG', '.', '2015-03-20 00:00:00.000', 'S', 'изложбен', null, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'BG', '1              ', '1996-02-16 00:00:00.000', 'S', '"Международен Панаир" ЕАД - Пловдив                                                                                                                                                                                                                           ', 1, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'BG', 'aaa1122aaa', '2018-12-20 00:00:00.000', 'N', 'notes notes', 1122, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'CA', '1308031        ', '2006-07-06 00:00:00.000', 'S', 'за клас 3                                                                                                                                                                                                                                                     ', 1308031, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'CA', '1308032        ', '2006-07-06 00:00:00.000', 'S', 'за клас 25                                                                                                                                                                                                                                                    ', 1308032, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'CA', '1308033        ', '2006-07-06 00:00:00.000', 'S', 'за клас 35                                                                                                                                                                                                                                                    ', 1308033, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'CA', '1308036        ', '2006-07-06 00:00:00.000', 'S', 'за клас 18                                                                                                                                                                                                                                                    ', 1308036, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'CA', '1308037        ', '2006-07-06 00:00:00.000', 'S', 'за клас14                                                                                                                                                                                                                                                     ', 1308037, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'CY', '87809', '2018-06-22 00:00:00.000', 'S', 'клас 3', 87809, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'CY', '87810', '2018-06-22 00:00:00.000', 'S', 'клас 5', 87810, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'CY', '87811', '2018-06-22 00:00:00.000', 'S', 'клас 29', 87811, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'CY', '87812', '2018-06-22 00:00:00.000', 'S', 'клас 30', 87812, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'CY', '87813', '2018-06-22 00:00:00.000', 'S', 'клас 32', 87813, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'CY', '87814', '2018-06-22 00:00:00.000', 'N', ' клас 35', 87814, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'CY', '87815', '2018-06-22 00:00:00.000', 'S', 'клас 41', 87815, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'CY', '87816', '2018-06-22 00:00:00.000', 'S', 'клас 42', 87816, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'CY', '87817', '2018-06-22 00:00:00.000', 'S', 'клас 44', 87817, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'DE', '302011000703.9/', '2011-01-25 00:00:00.000', 'S', '30 2011 000 703.9/09                                                                                                                                                                                                                                          ', 3020110007039, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'EM', '012914933', '2014-05-27 00:00:00.000', 'S', 'клас 16, 34, 35', 12914933, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'EM', '012915229', '2014-05-27 00:00:00.000', 'S', 'частично признат приоритет', 12915229, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'EM', '013853197', '2015-03-19 00:00:00.000', 'S', 'частично признат приоритет', 13853197, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'EM', '013853221', '2015-03-19 00:00:00.000', 'S', 'частично признат приоритет', 13853221, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'EM', '013853288', '2015-03-19 00:00:00.000', 'S', 'частично признат приоритет', 13853288, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'EM', '013853296', '2015-03-19 00:00:00.000', 'S', 'частично признат приоритет', 13853296, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'EM', '013855151', '2015-03-19 00:00:00.000', 'S', 'частично признат приоритет', 13855151, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'ES', '444604         ', '1997-01-23 00:00:00.000', 'S', 'OHIMOffice for harmonization in the international market (Trade marks and designs)                                                                                                                                                                            ', 444604, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'FR', '51122323344', '2019-01-07 14:52:24.890', 'S', 'alabala5', 51122323344, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'GB', '11223344', '2019-01-07 15:06:47.243', 'S', 'alabala5', 11223344, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'GB', '2110123        ', '1996-09-14 00:00:00.000', 'S', 'Заявената марка е с право на конвенционен приоритет.                                                                                                                                                                                                          ', 2110123, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'GB', '2115752        ', '1996-11-15 00:00:00.000', 'S', '		                                                                                                                                                                                                                                                            ', 2115752, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'RS', 'изложбен', '2015-11-05 00:00:00.000', 'S', 'Serbia Fashion Week', null, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'US', '30543', '1994-10-26 00:00:00.000', 'S', '74/590,997', 30543, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'US', '75-017916      ', '1995-11-13 00:00:00.000', 'S', 'към N: 75-017912             75-017919             75-017920                                                                                                                                                                                                  ', 75017916, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'US', '85/514817      ', '2012-01-12 00:00:00.000', 'S', 'приоритетът се отнася само за стоките "хайвер и рибено масло"                                                                                                                                                                                                 ', 85514817, null);
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'US', '87917666', '2018-05-11 00:00:00.000', 'S', 'aASD', 87917666, 'TEST');
INSERT INTO IPASPROD.IP_MARK_PRIORITIES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, COUNTRY_CODE, PRIORITY_APPL_ID, PRIORITY_DATE, IND_ACCEPTED, NOTES, PRIORITY_APPL_ID_ALT, NOTES_UNUSED) VALUES (1, 'BG', 'N', 2007, 99998, 'ZA', '99/10464       ', '1999-06-14 00:00:00.000', 'S', '99/10465,99/10466,99/10467,99/10468                                                                                                                                                                                                                           ', 9910464, null);

--nice classes
DELETE FROM IP_MARK_NICE_CLASSES where file_nbr = 99998 and FILE_SER = 2007 and file_typ = 'N';
INSERT INTO IPASPROD.IP_MARK_NICE_CLASSES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, NICE_CLASS_CODE, NICE_CLASS_STATUS_WCODE, NICE_CLASS_DESCRIPTION, NICE_CLASS_EDITION, NICE_CLASS_DESCR_LANG2, NICE_CLASS_VERSION) VALUES (1, 'BG', 'N', 2007, 99998, 6, 'R', 'неблагородни метали и техните сплави; метални строителни материали; преносими метални конструкции; неелектрически метални кабели и жици; железария, метална кинкалерия; метални тръби; метални каси; метални продукти, които не сa включени в други класове; руди', 0, null, '0');
INSERT INTO IPASPROD.IP_MARK_NICE_CLASSES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, NICE_CLASS_CODE, NICE_CLASS_STATUS_WCODE, NICE_CLASS_DESCRIPTION, NICE_CLASS_EDITION, NICE_CLASS_DESCR_LANG2, NICE_CLASS_VERSION) VALUES (1, 'BG', 'N', 2007, 99998, 19, 'R', 'неметални строителни материали; неметални твърди тръби за строителството; асфалт, катран и битум; неметални преносими конструкции; неметални паметници', 0, null, '0');
INSERT INTO IPASPROD.IP_MARK_NICE_CLASSES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, NICE_CLASS_CODE, NICE_CLASS_STATUS_WCODE, NICE_CLASS_DESCRIPTION, NICE_CLASS_EDITION, NICE_CLASS_DESCR_LANG2, NICE_CLASS_VERSION) VALUES (1, 'BG', 'N', 2007, 99998, 20, 'R', 'мебели, огледала, рамки за картини; стоки (които не са включени в други класове) от дърво, корк, камъш, тръстика, ракита, рог, кост, слонова кост, бален, раковина, кехлибар, седеф, морска пяна и от заместители на тези материали или от пластмаса', 0, null, '0');
INSERT INTO IPASPROD.IP_MARK_NICE_CLASSES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, NICE_CLASS_CODE, NICE_CLASS_STATUS_WCODE, NICE_CLASS_DESCRIPTION, NICE_CLASS_EDITION, NICE_CLASS_DESCR_LANG2, NICE_CLASS_VERSION) VALUES (1, 'BG', 'N', 2007, 99998, 27, 'R', 'килими, черги, изтривалки, рогозки, линолеум и други материали за подови покрития; нетекстилни тапети за стени', 0, null, '0');
INSERT INTO IPASPROD.IP_MARK_NICE_CLASSES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, NICE_CLASS_CODE, NICE_CLASS_STATUS_WCODE, NICE_CLASS_DESCRIPTION, NICE_CLASS_EDITION, NICE_CLASS_DESCR_LANG2, NICE_CLASS_VERSION) VALUES (1, 'BG', 'N', 2007, 99998, 37, 'R', 'строителство; ремонт; монтажни услуги', 0, null, '0');

DELETE FROM EXT_CORE.EXTENDED_IP_MARK_NICE_CLASSES where  file_nbr = 99998 and FILE_SER = 2007 and file_typ = 'N';
INSERT INTO EXT_CORE.EXTENDED_IP_MARK_NICE_CLASSES (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, NICE_CLASS_CODE, NICE_CLASS_STATUS_WCODE, ALL_TERMS_DECLARATION) VALUES ('BG', 'N', 2007, 99998, 6, 'R', 'S');
INSERT INTO EXT_CORE.EXTENDED_IP_MARK_NICE_CLASSES (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, NICE_CLASS_CODE, NICE_CLASS_STATUS_WCODE, ALL_TERMS_DECLARATION) VALUES ('BG', 'N', 2007, 99998, 19, 'R', 'S');
INSERT INTO EXT_CORE.EXTENDED_IP_MARK_NICE_CLASSES (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, NICE_CLASS_CODE, NICE_CLASS_STATUS_WCODE, ALL_TERMS_DECLARATION) VALUES ('BG', 'N', 2007, 99998, 20, 'R', 'S');
INSERT INTO EXT_CORE.EXTENDED_IP_MARK_NICE_CLASSES (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, NICE_CLASS_CODE, NICE_CLASS_STATUS_WCODE, ALL_TERMS_DECLARATION) VALUES ('BG', 'N', 2007, 99998, 27, 'R', 'S');
INSERT INTO EXT_CORE.EXTENDED_IP_MARK_NICE_CLASSES (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, NICE_CLASS_CODE, NICE_CLASS_STATUS_WCODE, ALL_TERMS_DECLARATION) VALUES ('BG', 'N', 2007, 99998, 37, 'R', 'N');

--inserting mark regulation
DELETE FROM IPASPROD.IP_MARK_REGULATION where  file_nbr = 99998 and FILE_SER = 2007 and file_typ = 'N';
INSERT INTO IPASPROD.IP_MARK_REGULATION (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, REGULATIONS_DESCRIPTION)
VALUES (1, 'BG', 'N', 2007, 99998, 'гроздова ракия с регистрирано географско указание "Сливенска перла"');


--update IP_MARK
UPDATE IPASPROD.IP_MARK SET ROW_VERSION = 10, FILING_DATE = '2007-12-04 00:00:00.000', APPL_TYP = 'НМ', APPL_SUBTYP = 'ИМ', NOTES = 'test notes', SIGN_WCODE = 'M', MARK_CODE = 125139, DISCLAIMER = 'disclaimer', TRANSLATION = 'translation', NICE_CLASS_TXT = ' 6 19 20 27 37 ',
                            MAIN_OWNER_PERSON_NBR = 149120, MAIN_OWNER_ADDR_NBR = 1, SERVICE_PERSON_NBR = 306343, SERVICE_ADDR_NBR = 1, FIRST_PRIORITY_DATE = '1994-10-26 00:00:00.000', IND_REGISTERED = 'S', REGISTRATION_NBR = 71179, REGISTRATION_DATE = '2009-09-02 00:00:00.000', ENTITLEMENT_DATE = '2007-12-04 00:00:00.000', EXPIRATION_DATE = '2027-12-04 00:00:00.000',
                            CAPTURE_USER_ID = 801, CAPTURE_DATE = '2007-12-05 00:00:00.000', PUBLICATION_DATE = '2008-02-29 00:00:00.000', JOURNAL_CODE = '201712.2', RNEW1_FILE_SEQ = null, RNEW1_FILE_TYP = null, RNEW1_FILE_SER = null, RNEW1_FILE_NBR = null, RNEW2_FILE_SEQ = null, RNEW2_FILE_TYP = null, RNEW2_FILE_SER = null, RNEW2_FILE_NBR = null,
                            LAST_RENEWAL_DATE = '2017-11-24 10:21:07.683', REGISTRATION_DUP = 'D', LAW_CODE = 1, INT_FILING_DATE = null, ASSOCIATION_NBR = null, MARK_SERIES_DESCRIPTION = 'series_descr', BY_CONSENT_DESCRIPTION = 'by_consent_description',
                            REGISTRATION_TYP = 'N', REGISTRATION_SER = 2009, IND_MANUAL_INTERPRETATION = 'S', PUBLICATION_NOTES = '2 2008', POA_DOC_ORI = null, POA_LOG_TYP = null, POA_DOC_SER = null, POA_DOC_NBR = null, EXHIBITION_DATE = '1995-01-31', EXHIBITION_NOTES = 'парижка конференция ', BASIC_FILE_REF = 'Test', INTREGN = 'testInternationalNumber',
                            NOVELTY1_DATE = '2007-12-04 00:00:00.000', NOVELTY2_DATE = '2007-12-04 00:00:00.000', DISCLAIMER_LANG2 = 'DISCLAIMER IN OTHER LANGUAGE', TRANSLATION_LANG2 = 'TRANSLATION IN OTHER LANGUAGE', MARK_TRANSLITERATION = 'TRANSLITERATION 1234', MARK_TRANSLITERATION_LANG2 = 'TRANSLITERATION IN OTHER LANGUAGE',
                            PUBLICATION_NBR = null, PUBLICATION_SER = null, PUBLICATION_TYP = null WHERE FILE_NBR = 99998 AND FILE_SEQ = 'BG' AND FILE_TYP = 'N' AND FILE_SER = 2007;
--ip_name
UPDATE IPASPROD.IP_NAME SET ROW_VERSION = 1, MARK_NAME = 'hagleitner', MAP_DENOMINACION = null, MARK_NAME_LANG2 = 'hagleitner in english' WHERE MARK_CODE = 125139;

--ext_core.IP_MARK_TRANSFORMATION
DELETE FROM ext_core.IP_MARK_TRANSFORMATION where  file_nbr = 99998 and FILE_SER = 2007 and file_typ = 'N';
INSERT INTO ext_core.IP_MARK_TRANSFORMATION (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, TRANSFORM_TYPE, REGISTRATION_DATE, FILING_DATE, PRIORITY_DATE, CANCELLATION_DATE, TRANSFORM_NUMBER)
VALUES ('BG', 'N', 2007, 99998, 'EM', '2015-01-01 00:00:00.000', '2012-01-01 00:00:00.000', '2012-03-03 00:00:00.000', '2019-01-01 00:00:00.000', '111233');

--logo vienna classes
DELETE FROM IPASPROD.IP_LOGO_VIENNA_CLASSES where  file_nbr = 99998 and FILE_SER = 2007 and file_typ = 'N';
INSERT INTO IPASPROD.IP_LOGO_VIENNA_CLASSES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, VIENNA_CLASS_CODE, VIENNA_GROUP_CODE, VIENNA_ELEM_CODE, VIENNA_EDITION_CODE, VCL_WPUBLISH_VALIDATED) VALUES (1, 'BG', 'N', 2007, 99998, 26, 1, 18, '0', null);
INSERT INTO IPASPROD.IP_LOGO_VIENNA_CLASSES (ROW_VERSION, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, VIENNA_CLASS_CODE, VIENNA_GROUP_CODE, VIENNA_ELEM_CODE, VIENNA_EDITION_CODE, VCL_WPUBLISH_VALIDATED) VALUES (1, 'BG', 'N', 2007, 99998, 27, 5, 7, '0', null);