create table EXT_CORE.CF_USERDOC_UI_PANEL
(
    PANEL varchar(50) not null
        constraint CF_USERDOC_UI_PANEL_pk
            primary key nonclustered,
);

INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL) VALUES ('UserdocTypeData');
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL) VALUES ('UserdocMainData');
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL) VALUES ('Process');


