--liquibase formatted sql

--changeset murlev:357.1
alter table ext_Core.CF_ATTACHMENT_TYPE_BOOKMARKS add bookmark_order integer;

--changeset murlev:357.2
update ext_Core.CF_ATTACHMENT_TYPE_BOOKMARKS set bookmark_order = 1 where BOOKMARK_NAME = 'BIBLIOGRAPHIC_DATA'
update ext_Core.CF_ATTACHMENT_TYPE_BOOKMARKS set bookmark_order = 2 where BOOKMARK_NAME = 'ABSTRACT'
update ext_Core.CF_ATTACHMENT_TYPE_BOOKMARKS set bookmark_order = 3 where BOOKMARK_NAME = 'DESCRIPTION'
update ext_Core.CF_ATTACHMENT_TYPE_BOOKMARKS set bookmark_order = 4 where BOOKMARK_NAME = 'CLAIMS'
update ext_Core.CF_ATTACHMENT_TYPE_BOOKMARKS set bookmark_order = 5 where BOOKMARK_NAME = 'DRAWINGS'
