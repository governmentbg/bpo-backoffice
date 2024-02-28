UPDATE EXT_CORE.CF_APPLICANT_AUTHORITY SET name = 'Оправомощено съгласно съответното законодателство лице' where id = 6

UPDATE ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND SET earlier_right_type_id = 1 where legal_ground_type_id = 29

delete from ext_core.CF_EARLIER_RIGHT_TO_PANEL where earlier_right_type_id = 2

delete from  ext_core.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY where earlier_right_type_id = 2

update ext_core.CF_EARLIER_RIGHT_TYPES set earlier_right_type_code = 'earlierTradeMark, wellKnow' where id = 1

DELETE FROM ext_core.CF_EARLIER_RIGHT_TYPES WHERE ID = 2

UPDATE ext_core.CF_EARLIER_RIGHT_TYPES SET name = 'По-ранна марка' where id = 1

UPDATE ext_core.CF_EARLIER_RIGHT_TYPES SET name = 'Ползваща се с известност по-ранна марка' where id = 3

UPDATE ext_core.CF_EARLIER_RIGHT_TYPES SET name = 'Нерегистрирана марка, която се ползва в търговската дейност' where id = 4

UPDATE ext_core.CF_EARLIER_RIGHT_TYPES SET name = 'Недобросъвестно подадена заявка' where id = 5

UPDATE ext_core.CF_EARLIER_RIGHT_TYPES SET name = 'Марка, заявена от агент представител' where id = 6

UPDATE ext_core.CF_EARLIER_RIGHT_TYPES SET name = 'По-ранна фирма' where id = 7

UPDATE ext_core.CF_EARLIER_RIGHT_TYPES SET name = 'По-ранно регистрирано географско означение' where id = 8



