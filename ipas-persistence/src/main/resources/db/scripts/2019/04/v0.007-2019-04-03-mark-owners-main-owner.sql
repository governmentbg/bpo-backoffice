update mo
    set mo.order_nbr = 1
    FROM IP_MARK_OWNERS mo
    join ip_mark m on mo.FILE_NBR = m.FILE_NBR and mo.FILE_SEQ = m.FILE_SEQ and mo.FILE_TYP = m.FILE_TYP and mo.FILE_SER = m.FILE_SER
     where mo.person_nbr = m.MAIN_OWNER_PERSON_NBR AND mo.ADDR_NBR = m.MAIN_OWNER_ADDR_NBR;

--updating order_nbr of the trademarks with more than one owner
update mo
set mo.ORDER_NBR = mo1.ROWNUM
from IP_MARK_OWNERS mo
    join

     (select 1 + ROW_NUMBER() OVER(PARTITION BY FILE_SER, FILE_TYP, FILE_SER, FILE_NBR order by FILE_SER, FILE_TYP, FILE_SER, FILE_NBR)
        AS ROWNUM,
    FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR
FROM IP_MARK_OWNERS
WHERE order_nbr is null) as mo1 on mo.FILE_SEQ = mo1.FILE_SEQ and mo.FILE_TYP = mo1.FILE_TYP and mo.FILE_SER = mo1.FILE_SER and mo.FILE_NBR = mo1.FILE_NBR and mo.PERSON_NBR = mo1.PERSON_NBR and mo.ADDR_NBR = mo1.ADDR_NBR;


