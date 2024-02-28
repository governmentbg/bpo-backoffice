update ipasprod.CF_DOC_SEQUENCE set IND_CONT_NBR_FOR_FILE = 'S';
update ipasprod.CF_DOC_SEQUENCE set IND_CONT_NBR_FOR_FILE = null where doc_seq_typ = 'EV';
update ipasprod.CF_DOC_SEQUENCE set IND_CONT_NBR_FOR_FILE = null where doc_seq_typ in (select doc_seq_typ from ipasprod.cf_file_type);
