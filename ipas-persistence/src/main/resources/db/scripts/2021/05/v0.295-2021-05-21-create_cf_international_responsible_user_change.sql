--liquibase formatted sql

--changeset mmihova:295.1
CREATE TABLE EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (
  ID integer identity(1,1) primary key,
  USER_ID numeric (5) not null,
  USER_NAME varchar (350) not null unique
);

--changeset mmihova:295.2
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56, 'М.Петрова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365, 'Колчагова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (211, 'Тренкова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1, 'Шамандура');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418, 'Минтова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (4131, 'Д. Мартиновски');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (4135, 'А.Богомилова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1363, 'А.Иванова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (116, 'Вангелова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365, 'M. Колчагова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418, 'Mинтова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1351, 'Якмаджиева');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (312, 'Желева');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365, 'М. Колчаговва');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (205, 'Кюркчийска');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418, 'Н.Минтова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1, 'Р.Георгиева');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (211, 'Р.Тренкова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56, 'Петрова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (109, 'Миланова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (302, 'Сн.Михайлова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (106, 'Абрашева');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1348, 'Николова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1364, 'Чавдарова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (309, 'Антонов');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (312, 'М.Желева');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (113, 'Божилова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1333, 'Йончева');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1, 'Иванова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1349, 'Драганов');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (112, 'Й.Михайлова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1371, 'Тотева');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (309, 'Ст.Антонов');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1372, 'Чунчукова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365, 'Колчагова/ Р.Тр.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (116, 'Св.Вангелова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365, 'М. Колчагова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1, 'Симеонова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56, 'М.Петрова/ Р.Тр');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56, 'М.Петрова/А.Ив.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (107, 'Д.Георгиева');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (114, 'Матеина');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (212, 'Ерменкова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56, 'М.Петрова/ Л.Н.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1, 'Дойнова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365, 'M.Колчагова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56, 'М.Петрова/Л.Н.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56, 'М.Петрова/Р.Тр');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365, 'Колчагова/ Б.Ч.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1332, 'В.Николова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365, 'Колчагова/ А.Й.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56, 'М.Петрова/ Б.Ч.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (209, 'Радулова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418, 'Mинтова/Б.Ч.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1331, 'Е.Димитров');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365, 'Колчагова/ Н.М.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1348, 'Л.Николова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56, 'М.Петрова/Р.Ч.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (206, 'Боянова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56, 'М.Петрова/А.Й.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1371, 'Т.Тотева');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56, 'М.Петрова/ Н.М.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56, 'М.Петрова/Й.М.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418, 'Mинтова/Л.Н.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56, 'М.Петрова/Б.Ч.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56, 'М.Петрова/ Й.М.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (105, 'Йоцова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (312, 'Желева/ А.Ив.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365,'Колчагова/ Й.М.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365,'Колчагова/ А.Ив.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365,'Колчагова / Р.Ч.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56,'М.Петрова/Р.Тр.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (312,'Желева/ Р.Тр.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1,'Йорданова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (12,'Р.Гетова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365,'Колчагова/ Л.Н.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (3127,'Б.Георгиев');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418,'Н.Минтова/ Л.Н.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (103,'П.Добрева');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418,'Mинтова/Р.Тр.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365,'Колчагова / Н.М.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56,'М.Петрова / Б.Ч.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365,'Колчагова/ Б.Л.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365,'Колчагова / Б.Л.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418,'Н.Минтова/ Б.Ч.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1363,'А.Иванова/ А.Ив.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56,'Петрова/АВ');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365,'Колчагова / А.Ив.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (109,'Й.Миланова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418,'Минтова/Л.Н.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56,'М.Петрова/ Б.Л.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (312,'Желева / Л.Н.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56,'М.Петрова/?');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365,'Колчагова/ Р.Г.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56,'М.Петрова / Р.Тр.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418,'Mинтова / Б.Н.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56,'М.Петрова/ А.Ив.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365,'Колчагова / А.Й.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (312,'Желева/Л.Н.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (312,'М.Желева/Й.М.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418,'Mинтова/Б.Ч');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418,'Минтова/А.Й.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418,'Mинтова/Р.Ч.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (312,'Желева/Б.Н.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418,'Mинтова/ Й.М.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1365,'Колчагова/ А.Л.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (312,'Желева / Р.Тр.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56,'М.Петрова/ Й.М./Е.Д.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418,'Минтова/ Й.М.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1333,'А.Йончева');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418,'Mинтова/А.Й.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (56,'М.Петрова / Л.Н.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1,'М.Дойнова');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (312,'Желева/А.Й.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (1418,'Mинтова / Й.М.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_RESPONSIBLE_USER_CHANGE (USER_ID, USER_NAME) values (312,'Желева / Й.М.');