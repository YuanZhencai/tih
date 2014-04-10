
DROP SEQUENCE SRC;
CREATE SEQUENCE SRC START WITH 500 INCREMENT BY 1 MAXVALUE 999999999 CYCLE NOCACHE;

DROP TABLE COMMONFUNCTION
;
DROP TABLE COMPANYMSTR
;
DROP TABLE DICT
;
DROP TABLE LINKMSTR
;
DROP TABLE NEWSCHANNELMSTR
;
DROP TABLE O
;
DROP TABLE P
;
DROP TABLE POSITION
;
DROP TABLE POSITIONORG
;
DROP TABLE PS
;
DROP TABLE RESOURCEMSTR
;
DROP TABLE ROLEMSTR
;
DROP TABLE ROLERESOURCE
;
DROP TABLE S
;
DROP TABLE SYNCLOG
;
DROP TABLE TAXAUTHORITY
;
DROP TABLE USERCOMPANY
;
DROP TABLE USERMSTR
;
DROP TABLE USERPOSITIONORG
;
DROP TABLE USERROLE
;
-- 
-- TABLE: COMMONFUNCTION 
--

CREATE TABLE COMMONFUNCTION(
    ID                  BIGINT         NOT NULL,
    PRIORITY            INTEGER,
    RESOURCEMSTR_ID     BIGINT,
    DEFUNCT_IND         CHAR(1)        NOT NULL WITH DEFAULT 'N',
    CREATED_BY          VARCHAR(50)    NOT NULL,
    CREATED_DATETIME    TIMESTAMP      NOT NULL,
    UPDATED_BY          VARCHAR(50)    NOT NULL,
    UPDATED_DATETIME    TIMESTAMP      NOT NULL,
    CONSTRAINT PK23 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: COMPANYMSTR 
--

CREATE TABLE COMPANYMSTR(
    ID                  BIGINT          NOT NULL,
    OID                 BIGINT,
    TAXAUTHORITY_ID     BIGINT,
    ADDRESS             CHAR(50),
    ZIPCODE             VARCHAR(10),
    TELPHONE            VARCHAR(20),
    TYPE                VARCHAR(20),
    DESC                VARCHAR(500),
    DEFUNCT_IND         CHAR(1)         NOT NULL WITH DEFAULT 'N',
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK6 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: DICT 
--

CREATE TABLE DICT(
    ID                  BIGINT          NOT NULL,
    CODE_CAT            VARCHAR(50)     NOT NULL,
    CODE_KEY            VARCHAR(50)     NOT NULL,
    CODE_VAL            VARCHAR(100)    NOT NULL,
    REMARKS             VARCHAR(200),
    SEQ_NO              BIGINT,
    SYS_IND             CHAR(1)         NOT NULL WITH DEFAULT 'N',
    LANG                CHAR(5)         NOT NULL WITH DEFAULT 'zh_CN',
    DEFUNCT_IND         CHAR(1)         NOT NULL WITH DEFAULT 'N',
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK41 PRIMARY KEY (ID)
)
;

-- 
-- TABLE: LINKMSTR 
--

CREATE TABLE LINKMSTR(
    ID                  BIGINT          NOT NULL,
    NAME                VARCHAR(20),
    LINK_URL            VARCHAR(200),
    PRIORITY            INTEGER,
    DEFUNCT_IND         CHAR(1)         NOT NULL WITH DEFAULT 'N',
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK22_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: NEWSCHANNELMSTR 
--

CREATE TABLE NEWSCHANNELMSTR(
    ID                  BIGINT          NOT NULL,
    NAME                VARCHAR(20)     NOT NULL,
    RSS                 VARCHAR(200),
    PAGE_COUNT          INTEGER         NOT NULL,
    KEYWORDS            VARCHAR(50),
    PRIORITY            INTEGER,
    DEFUNCT_IND         CHAR(1)         NOT NULL WITH DEFAULT 'N',
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK21_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: O 
--

CREATE TABLE O(
    ID             BIGINT         NOT NULL,
    BUKRS          VARCHAR(20),
    STEXT          VARCHAR(20),
    PARENT         BIGINT,
    KOSTL          VARCHAR(20),
    ZHRZZCJID      VARCHAR(20),
    ZHRZZDWID      VARCHAR(20),
    ZHRTXXLID      VARCHAR(20),
    ZHRTXXLMS      VARCHAR(50),
    DEFUNCT_IND    CHAR(1)        NOT NULL WITH DEFAULT 'N',
    CONSTRAINT PK4_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: P 
--

CREATE TABLE P(
    ID             BIGINT         NOT NULL,
    NACHN          VARCHAR(20),
    NAME2          VARCHAR(20),
    ICNUM          VARCHAR(30),
    EMAIL          VARCHAR(50),
    GESCH          VARCHAR(20),
    TELNO          VARCHAR(20),
    CELNO          VARCHAR(20),
    BUKRS          VARCHAR(20),
    KOSTL          VARCHAR(20),
    DEFUNCT_IND    CHAR(1)        NOT NULL WITH DEFAULT 'N',
    CONSTRAINT PK4_2_3 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: POSITION 
--

CREATE TABLE POSITION(
    ID                  BIGINT          NOT NULL,
    NAME                VARCHAR(20)     NOT NULL,
    DESC                VARCHAR(200),
    DEFUNCT_IND         CHAR(1),
    CREATED_BY          VARCHAR(50),
    CREATED_DATETIME    TIMESTAMP,
    UPDATED_BY          VARCHAR(50),
    UPDATED_DATETIME    TIMESTAMP,
    CONSTRAINT PK3 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: POSITIONORG 
--

CREATE TABLE POSITIONORG(
    ID                  BIGINT         NOT NULL,
    POSITION_ID         BIGINT         NOT NULL,
    OID                 BIGINT,
    DEFUNCT_IND         CHAR(1)        NOT NULL WITH DEFAULT 'N',
    CREATED_BY          VARCHAR(50)    NOT NULL,
    CREATED_DATETIME    TIMESTAMP      NOT NULL,
    UPDATED_BY          VARCHAR(50)    NOT NULL,
    UPDATED_DATETIME    TIMESTAMP      NOT NULL,
    CONSTRAINT PK48 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: PS 
--

CREATE TABLE PS(
    ID             BIGINT     NOT NULL,
    SID            BIGINT     NOT NULL,
    PID            BIGINT     NOT NULL,
    DEFUNCT_IND    CHAR(1)    NOT NULL WITH DEFAULT 'N',
    CONSTRAINT PK14_1 PRIMARY KEY (ID)
)
;

-- 
-- TABLE: RESOURCEMSTR 
--

CREATE TABLE RESOURCEMSTR(
    ID                  BIGINT          NOT NULL,
    NAME                VARCHAR(20)     NOT NULL,
    CODE                VARCHAR(50)     NOT NULL,
    SEQ_NO              VARCHAR(255),
    PARENT_ID           BIGINT,
    TYPE                VARCHAR(50)     NOT NULL,
    URI                 VARCHAR(200),
    DEFUNCT_IND         CHAR(1)         NOT NULL WITH DEFAULT 'N',
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK17 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: ROLEMSTR 
--

CREATE TABLE ROLEMSTR(
    ID                  BIGINT         NOT NULL,
    NAME                VARCHAR(20)    NOT NULL,
    DESC                VARCHAR(50),
    CODE                VARCHAR(50)    NOT NULL,
    DEFUNCT_IND         CHAR(1)        NOT NULL WITH DEFAULT 'N',
    CREATED_BY          VARCHAR(50)    NOT NULL,
    CREATED_DATETIME    TIMESTAMP      NOT NULL,
    UPDATED_BY          VARCHAR(50)    NOT NULL,
    UPDATED_DATETIME    TIMESTAMP      NOT NULL,
    CONSTRAINT PK9 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: ROLERESOURCE 
--

CREATE TABLE ROLERESOURCE(
    ID                  BIGINT         NOT NULL,
    RESOURCEMSTR_ID     BIGINT,
    ROLEMSTR_ID         BIGINT,
    DEFUNCT_IND         CHAR(1),
    CREATED_BY          VARCHAR(50),
    CREATED_DATETIME    TIMESTAMP,
    UPDATED_BY          VARCHAR(50),
    UPDATED_DATETIME    TIMESTAMP,
    CONSTRAINT PK18 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: S 
--

CREATE TABLE S(
    ID             BIGINT         NOT NULL,
    OID            BIGINT         NOT NULL,
    STEXT          VARCHAR(20),
    KOSTL          VARCHAR(20),
    ZHRTXXLID      VARCHAR(20),
    ZHRTXXLMS      VARCHAR(50),
    DEFUNCT_IND    CHAR(1)        NOT NULL WITH DEFAULT 'N',
    CONSTRAINT PK14_2 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: SYNCLOG 
--

CREATE TABLE SYNCLOG(
    ID               BIGINT         NOT NULL,
    VERSION          TIMESTAMP      NOT NULL,
    SYNC_TYPE        VARCHAR(20),
    SYNC_DATETIME    TIMESTAMP,
    REMARKS          VARCHAR(50),
    CONSTRAINT PK52 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: TAXAUTHORITY 
--

CREATE TABLE TAXAUTHORITY(
    ID                    BIGINT          NOT NULL,
    NAME                  VARCHAR(20)     NOT NULL,
    ADDRESS               VARCHAR(100),
    ZIPCODE               VARCHAR(10),
    TELPHONE              VARCHAR(20),
    LEADER_NAME           VARCHAR(20),
    LEADER_POSITION       VARCHAR(20),
    LEADER_TELPHONE       VARCHAR(20),
    CONTACTER_NAME        VARCHAR(20),
    CONTACTER_POSITION    VARCHAR(20),
    CONTACTER_TELPHONE    VARCHAR(20),
    DEFUNCT_IND           CHAR(1),
    CREATED_BY            VARCHAR(50),
    CREATED_DATETIME      TIMESTAMP,
    UPDATED_BY            VARCHAR(50),
    UPDATED_DATETIME      TIMESTAMP,
    CONSTRAINT PK5 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: USERCOMPANY 
--

CREATE TABLE USERCOMPANY(
    ID                  BIGINT         NOT NULL,
    COMPANYMSTR_ID      BIGINT,
    USERMSTR_ID         BIGINT,
    DEFUNCT_IND         CHAR(1)        NOT NULL WITH DEFAULT 'N',
    CREATED_BY          VARCHAR(50)    NOT NULL,
    CREATED_DATETIME    TIMESTAMP      NOT NULL,
    UPDATED_BY          VARCHAR(50)    NOT NULL,
    UPDATED_DATETIME    TIMESTAMP      NOT NULL,
    CONSTRAINT PK33 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: USERMSTR 
--

CREATE TABLE USERMSTR(
    ID                  BIGINT          NOT NULL,
    PID                 BIGINT          NOT NULL,
    AD_ACCOUNT          VARCHAR(50),
    PERNR               VARCHAR(20),
    ONBOARD_DATE        TIMESTAMP,
    BIRTHDAY            TIMESTAMP,
    IDENTITY_TYPE       VARCHAR(20),
    IDTENTITY_ID        VARCHAR(50),
    BACKGROUND_INFO     VARCHAR(200),
    DEFUNCT_IND         CHAR(1)         NOT NULL WITH DEFAULT 'N',
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK2 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: USERPOSITIONORG 
--

CREATE TABLE USERPOSITIONORG(
    ID                  BIGINT         NOT NULL,
    POSITIONORG_ID      BIGINT         NOT NULL,
    USERMSTR_ID         BIGINT         NOT NULL,
    DEFUNCT_IND         CHAR(10)       NOT NULL WITH DEFAULT 'N',
    CREATED_BY          VARCHAR(50)    NOT NULL,
    CREATED_DATETIME    TIMESTAMP      NOT NULL,
    UPDATED_BY          VARCHAR(50)    NOT NULL,
    UPDATED_DATETIME    TIMESTAMP      NOT NULL,
    CONSTRAINT PK32 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: USERROLE 
--

CREATE TABLE USERROLE(
    ID                  BIGINT         NOT NULL,
    ROLEMSTR_ID         BIGINT,
    USERMSTR_ID         BIGINT,
    DEFUNCT_IND         CHAR(1),
    CREATED_BY          VARCHAR(50),
    CREATED_DATETIME    TIMESTAMP,
    UPDATED_BY          VARCHAR(50),
    UPDATED_DATETIME    TIMESTAMP,
    CONSTRAINT PK14 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: COMMONFUNCTION 
--

ALTER TABLE COMMONFUNCTION ADD CONSTRAINT RefRESOURCEMSTR211 
    FOREIGN KEY (RESOURCEMSTR_ID)
    REFERENCES RESOURCEMSTR(ID)
;


-- 
-- TABLE: COMPANYMSTR 
--

ALTER TABLE COMPANYMSTR ADD CONSTRAINT RefTAXAUTHORITY51 
    FOREIGN KEY (TAXAUTHORITY_ID)
    REFERENCES TAXAUTHORITY(ID)
;


-- 
-- TABLE: POSITIONORG 
--

ALTER TABLE POSITIONORG ADD CONSTRAINT RefPOSITION471 
    FOREIGN KEY (POSITION_ID)
    REFERENCES POSITION(ID)
;


-- 
-- TABLE: ROLERESOURCE 
--

ALTER TABLE ROLERESOURCE ADD CONSTRAINT RefROLEMSTR191 
    FOREIGN KEY (ROLEMSTR_ID)
    REFERENCES ROLEMSTR(ID)
;

ALTER TABLE ROLERESOURCE ADD CONSTRAINT RefRESOURCEMSTR201 
    FOREIGN KEY (RESOURCEMSTR_ID)
    REFERENCES RESOURCEMSTR(ID)
;


-- 
-- TABLE: USERCOMPANY 
--

ALTER TABLE USERCOMPANY ADD CONSTRAINT RefUSERMSTR341 
    FOREIGN KEY (USERMSTR_ID)
    REFERENCES USERMSTR(ID)
;

ALTER TABLE USERCOMPANY ADD CONSTRAINT RefCOMPANYMSTR351 
    FOREIGN KEY (COMPANYMSTR_ID)
    REFERENCES COMPANYMSTR(ID)
;


-- 
-- TABLE: USERPOSITIONORG 
--

ALTER TABLE USERPOSITIONORG ADD CONSTRAINT RefUSERMSTR321 
    FOREIGN KEY (USERMSTR_ID)
    REFERENCES USERMSTR(ID)
;

ALTER TABLE USERPOSITIONORG ADD CONSTRAINT RefPOSITIONORG521 
    FOREIGN KEY (POSITIONORG_ID)
    REFERENCES POSITIONORG(ID)
;


-- 
-- TABLE: USERROLE 
--

ALTER TABLE USERROLE ADD CONSTRAINT RefROLEMSTR161 
    FOREIGN KEY (ROLEMSTR_ID)
    REFERENCES ROLEMSTR(ID)
;

ALTER TABLE USERROLE ADD CONSTRAINT RefUSERMSTR171 
    FOREIGN KEY (USERMSTR_ID)
    REFERENCES USERMSTR(ID)
;

INSERT INTO USERMSTR(ID,PID,AD_ACCOUNT,PERNR,ONBOARD_DATE,BIRTHDAY,IDENTITY_TYPE,IDTENTITY_ID,BACKGROUND_INFO,DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)
VALUES 				(1, 1,  'shenbo',  NULL, NULL,        NULL,    NULL,         NULL,        NULL,           'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
INSERT INTO USERMSTR(ID,PID,AD_ACCOUNT,PERNR,ONBOARD_DATE,BIRTHDAY,IDENTITY_TYPE,IDTENTITY_ID,BACKGROUND_INFO,DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)
VALUES 				(2, 1,  'wilmar_cas',  NULL, NULL,        NULL,    NULL,         NULL,        NULL,           'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');



insert into ROLEMSTR (ID, NAME, DESC, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME,CODE) 
values (1,'系统管理员', 'admin','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00','sysadmins');
insert into ROLEMSTR (ID, NAME, DESC, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME,CODE) 
values (2,'文档管理员', 'admin','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00','fnadmins');
insert into ROLEMSTR (ID, NAME, DESC, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME,CODE) 
values (5,'一般用户', 'admin','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00','commonusers');
insert into ROLEMSTR (ID, NAME, DESC, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME,CODE) 
values (6,'精灵管理员', 'admin','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00','smartadmins');
insert into ROLEMSTR (ID, NAME, DESC, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME,CODE) 
values (7,'任务管理员', 'admin','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00','taskadmins');
insert into ROLEMSTR (ID, NAME, DESC, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME,CODE) 
values (8,'项目管理员', 'admin','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00','projectadmins');


insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (1, 					'首页',	   'home',1,NULL,'MENU',NULL,											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (2, 					'文档管理','document',2,NULL,'MENU',NULL,											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (3, 					'互动交流','contact',3,NULL,'MENU',NULL,											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (4, 					'事务管理','affair',4,NULL,'MENU',NULL,											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (5, 					'报表查询','report',5,NULL,'MENU',NULL,											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (6, 					'系统管理','system',6,NULL,'MENU',NULL,											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');

insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (7, 					'组织用户管理','system:user',1,6,'MENU',NULL,											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (8, 					'基础数据管理','system:dict',2,6,'MENU',NULL,											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (9, 					'权限管理','system:security',3,6,'MENU',NULL,											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (10, 					'首页管理','system:home',4,6,'MENU',NULL,											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');

insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (11, 					'用户管理','system:user:user',1,7,'MENU','/tih/system/user/index.xhtml',											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (12, 					'税务机关管理','system:user:taxauth',2,7,'MENU','/tih/system/taxauth/index.xhtml',											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (13, 					'岗位管理','system:user:postion',3,7,'MENU','/tih/system/position/index.xhtml',											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (14, 					'公司管理','system:user:company',4,7,'MENU','/tih/system/company/index.xhtml',											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');

insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (15, 					'字典表管理','system:dict:dict',1,8,'MENU','/tih/system/dict/index.xhtml',											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');

insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (16, 					'角色管理','system:security:role',1,9,'MENU','/tih/system/role/index.xhtml',											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');

insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (17, 					'新闻频道管理','system:home:news',1,10,'MENU','/tih/system/news/index.xhtml',											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (18, 					'常用链接管理','system:home:commonlink',2,10,'MENU','/tih/system/commonlink/index.xhtml',											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (19, 					'常用功能管理','system:home:commonfunction',3,10,'MENU','/tih/system/commonfunction/index.xhtml',											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');


insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (1,1,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (2,2,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (3,3,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (4,4,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (5,5,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (6,6,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (7,7,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (8,8,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (9,9,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (10,10,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (11,11,1,									'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (12,12,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (13,13,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (14,14,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (15,15,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (16,16,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (17,17,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (18,18,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
values (19,19,1,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');

insert into USERROLE (ID, ROLEMSTR_ID, USERMSTR_ID,DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)
values (1, 1, 2,'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');

--insert into ROLERESOURCE (ID, RESOURCEMSTR_ID, ROLEMSTR_ID, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)  
--values (20,2,4,										'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');

--insert into USERROLE (ID, ROLEMSTR_ID, USERMSTR_ID,DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)
--values (2, 4, 2,'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');


update resourcemstr r set r.URI = '/faces/common/user/index.xhtml' where r.ID = 11;
update resourcemstr r set r.URI = '/faces/system/taxauth/index.xhtml' where r.ID = 12;
update resourcemstr r set r.URI = '/faces/common/position/index.xhtml' where r.ID = 13;
update resourcemstr r set r.URI = '/faces/common/company/index.xhtml' where r.ID = 14;
update resourcemstr r set r.URI = '/faces/system/dict/index.xhtml' where r.ID = 15;
update resourcemstr r set r.URI = '/faces/common/role/index.xhtml' where r.ID = 16;
update resourcemstr r set r.URI = '/faces/system/news/index.xhtml' where r.ID = 17;
update resourcemstr r set r.URI = '/faces/system/commonlink/index.xhtml' where r.ID = 18;
update resourcemstr r set r.URI = '/faces/system/commonfunction/index.xhtml' where r.ID = 19;

Insert into RESOURCEMSTR values(20, '在线解答', 'contact:onlineanswer', '1', 3, 'MENU', NULL, 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(21, '问题回复', 'contact:onlineanswer:problemreply', '1', 20, 'MENU', NULL, 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(22, '在线提问', 'contact:onlineanswer:onlineproblem', '2', 20, 'MENU', NULL, 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(23, '税务精灵', 'contact:taxgenius', '2', 3, 'MENU', NULL, 'N', 'admin', '2012-01-01-00.00.00.000000', 'admin', '2012-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(24, '精灵解答', 'contact:taxgenius:geniusanswer', '1', 23, 'MENU', '/faces/interaction/WizardAnswer/index.xhtml', 'N', 'admin', '2012-01-01-00.00.00.000000', 'admin', '2012-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(25, '精灵设置', 'contact:taxgenius:geniusset', '2', 23, 'MENU', NULL, 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(26, '任务管理', 'affair:taskmanage', '1', 4, 'MENU', NULL, 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(27, '我的草稿', 'affair:taskmanage:mydraft', '1', 26, 'MENU', NULL, 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(28, '我的申办', 'affair:taskmanage:myhandle', '2', 26, 'MENU', NULL, 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(29, '我的待办', 'affair:taskmanage:mystayhandle', '3', 26, 'MENU', NULL, 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(30, '我的已办', 'affair:taskmanage:mystophandle', '4', 26, 'MENU', NULL, 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(31, '我的授权', 'affair:taskmanage:mywarrant', '5', 26, 'MENU', NULL, 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(32, '项目管理', 'affair:projectmanage', '2', 4, 'MENU', NULL, 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(33, '我的项目', 'affair:projectmanage:myproject', '1', 32, 'MENU', '/faces/transaction/project/index.xhtml', 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(34, '专用报表处理', 'report:reportmanage', '1', 5, 'MENU', NULL, 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(35, '数据导入', 'report:reportmanage:dataimport', '1', 34, 'MENU', NULL, 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(36, '数据汇总', 'report:reportmanage:datacollect', '2', 34, 'MENU', NULL, 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');
Insert into RESOURCEMSTR values(37, '基础表报', 'report:basereport', '1', 5, 'MENU', NULL, 'N', 'admin', '2011-01-01-00.00.00.000000', 'admin', '2011-01-01-00.00.00.000000');


--调整菜单
delete from roleresource rr  where rr.id = 4;
delete from roleresource rr  where rr.id = 530;
delete from roleresource rr  where rr.id = 531;
delete from roleresource rr  where rr.id = 532;
delete from roleresource rr  where rr.id = 535;
delete from roleresource rr  where rr.id = 536;
delete from roleresource rr  where rr.id = 537;
delete from roleresource rr  where rr.id = 538;
delete from roleresource rr  where rr.id = 539;
delete from roleresource rr  where rr.id = 540;
delete from roleresource rr  where rr.id = 541;
delete from roleresource rr  where rr.id = 542;
delete from roleresource rr  where rr.id = 543;
delete from roleresource rr  where rr.id = 544;
delete from roleresource rr  where rr.id = 545;
delete from roleresource rr  where rr.id = 546;
delete from roleresource rr  where rr.id = 547;

--delete from resourcemstr rs where rs.id = 5;
delete from resourcemstr rs where rs.id = 20;
delete from resourcemstr rs where rs.id = 21;
delete from resourcemstr rs where rs.id = 22;
delete from resourcemstr rs where rs.id = 25;

delete from resourcemstr rs where rs.id = 27;
delete from resourcemstr rs where rs.id = 28;
delete from resourcemstr rs where rs.id = 29;
delete from resourcemstr rs where rs.id = 30;
delete from resourcemstr rs where rs.id = 31;
delete from resourcemstr rs where rs.id = 33;
delete from resourcemstr rs where rs.id = 34;
delete from resourcemstr rs where rs.id = 35;
delete from resourcemstr rs where rs.id = 36;
delete from resourcemstr rs where rs.id = 37;

Insert into RESOURCEMSTR values(25, '精灵导入', 'contact:taxgenius:import', '2', 23, 'MENU', NULL, 'N', 'admin', '2012-04-28-00.00.00.000000', 'admin', '2012-04-28-00.00.00.000000');
update resourcemstr rm set rm.uri='/faces/transaction/project/index.xhtml' where rm.id = 32;

update resourcemstr rm set rm.uri = '/faces/interaction/onlinetoanswer/index.xhtml' where rm.id = 25;

--RESOURCEMSTR表增加主数据同步菜单
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (33, '主数据同步','system:user:synchronous',5,7,'MENU','/faces/common/synchronous/index.xhtml', 'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');

--更新 精灵导入 的URI 地址
UPDATE RESOURCEMSTR SET URI = '/faces/interaction/smartimport/index.xhtml' WHERE id = 25;

--更新首页URI
UPDATE RESOURCEMSTR rm SET rm.URI = '/faces/document/index.xhtml' WHERE rm.id = 2;

UPDATE RESOURCEMSTR rm SET rm.defunct_ind = 'Y' WHERE rm.id='5';

UPDATE RESOURCEMSTR rm SET rm.URI = '/faces/common/dict/index.xhtml' WHERE rm.id='15';

UPDATE RESOURCEMSTR rm SET rm.URI = '/faces/transaction/task/index.xhtml' WHERE rm.id='26';


--数据库 一些表结构改变
-- O表
alter table O drop primary key;
alter table O ALTER ID SET DATA TYPE varchar(200);
alter table O ALTER BUKRS SET DATA TYPE varchar(200);
alter table O ALTER STEXT SET DATA TYPE varchar(200);
alter table O ALTER PARENT SET DATA TYPE varchar(200);
alter table O ALTER KOSTL SET DATA TYPE varchar(200);
alter table O ALTER ZHRZZCJID SET DATA TYPE varchar(200);
alter table O ALTER ZHRZZDWID SET DATA TYPE varchar(200);
alter table O ALTER ZHRTXXLID SET DATA TYPE varchar(200);
alter table O ALTER ZHRTXXLMS SET DATA TYPE varchar(200);
reorg table O;
alter table O add constraint PK4_1 Primary Key(ID);

--P表 
alter table P drop primary key;
alter table P ALTER ID SET DATA TYPE varchar(200);
alter table P ALTER NACHN SET DATA TYPE varchar(200);
alter table P ALTER NAME2 SET DATA TYPE varchar(200);
alter table P ALTER ICNUM SET DATA TYPE varchar(200);
alter table P ALTER EMAIL SET DATA TYPE varchar(200);
alter table P ALTER GESCH SET DATA TYPE varchar(200);
alter table P ALTER TELNO SET DATA TYPE varchar(200);
alter table P ALTER CELNO SET DATA TYPE varchar(200);
alter table P ALTER BUKRS SET DATA TYPE varchar(200);
alter table P ALTER KOSTL SET DATA TYPE varchar(200);
ALTER TABLE P ADD COLUMN ORGEH varchar(200);
reorg table P;
alter table P add constraint PK4_2_3 Primary Key(ID);
reorg table P;


--S表
alter table S drop primary key;
alter table S ALTER ID SET DATA TYPE varchar(200);
alter table S ALTER OID SET DATA TYPE varchar(200);
alter table S ALTER STEXT SET DATA TYPE varchar(200);
alter table S ALTER KOSTL SET DATA TYPE varchar(200);
alter table S ALTER ZHRTXXLID SET DATA TYPE varchar(200);
alter table S ALTER ZHRTXXLMS SET DATA TYPE varchar(200);
reorg table S;
alter table S add constraint PK14_2 Primary Key(ID);

--PS表
alter table PS drop primary key;
alter table PS ALTER ID SET DATA TYPE varchar(200);
alter table PS ALTER SID SET DATA TYPE varchar(200);
alter table PS ALTER PID SET DATA TYPE varchar(200);
reorg table PS;
alter table PS add constraint PK14_1 Primary Key(ID);

--Positionorg表
alter table Positionorg ALTER OID SET DATA TYPE varchar(200);
reorg table Positionorg;

--Companymstr表
alter table Companymstr ALTER OID SET DATA TYPE varchar(200);
reorg table Companymstr;

--Usermstr表
--alter table Usermstr ALTER PID SET DATA TYPE varchar(200);
reorg table Usermstr;

--同步P表增加的字段
ALTER TABLE P ADD COLUMN ZHRZJID varchar(200);
ALTER TABLE P ADD COLUMN ZHRZJMS varchar(200);
--更新用户表字段 长度
alter table Usermstr ALTER AD_ACCOUNT SET DATA TYPE varchar(200);
alter table Usermstr ALTER PERNR SET DATA TYPE varchar(200);
alter table Usermstr ALTER IDENTITY_TYPE SET DATA TYPE varchar(200);
alter table Usermstr ALTER IDTENTITY_ID SET DATA TYPE varchar(200);
alter table Usermstr ALTER BACKGROUND_INFO SET DATA TYPE varchar(200);
reorg table Usermstr;

--rss资源表
DROP TABLE RSS;
CREATE TABLE RSS
(
    ID                BIGINT       NOT NULL,
    NEWSCHANNELMSTRID BIGINT       NOT NULL,
    LINK              VARCHAR(500) NOT NULL,
    TITLE             VARCHAR(200) NOT NULL,
    PUBLISHEDDATE     TIMESTAMP    NOT NULL,
    CONSTRAINT CC1331103826813
    PRIMARY KEY (ID)
)
    IN USERSPACE1
;
ALTER TABLE RSS DATA CAPTURE NONE
;
ALTER TABLE RSS NOT VOLATILE
;
ALTER TABLE RSS APPEND OFF
;
ALTER TABLE RSS LOCKSIZE ROW
;
ALTER TABLE RSS DROP RESTRICT ON DROP
;



--删除Usermstr表pid列
alter table USERMSTR drop column PID;
reorg table USERMSTR;

--创建CAS_USR_P表
DROP TABLE CAS_USR_P; 
CREATE TABLE CAS_USR_P (
		  ID VARCHAR(200) NOT NULL , 
		  PERNR VARCHAR(200) NOT NULL , 
		  DEFUNCT_IND CHAR(1) NOT NULL WITH DEFAULT 'N' )   
		 IN USERSPACE1 ; 
-- 表上主键的 DDL 语句 
ALTER TABLE CAS_USR_P ADD CONSTRAINT CC1333073749123 PRIMARY KEY (ID);
reorg table CAS_USR_P;

--更改 address
alter table COMPANYMSTR ALTER address SET DATA TYPE varchar(500);
reorg table COMPANYMSTR;

--创建ROLEMSTR表	SYS_IND列
alter table ROLEMSTR add SYS_IND CHAR(1)        NOT NULL  With Default 'N';
reorg table ROLEMSTR;



--更改 Synclog SYNC_TYPE
alter table Synclog ALTER SYNC_TYPE SET DATA TYPE varchar(200);
--更改 Synclog remarks
alter table Synclog ALTER remarks SET DATA TYPE varchar(500);
reorg table Synclog;



--
--ER/Studio 7.0 SQL Code Generation
-- Company :      WCS
-- Project :      税务平台ER关系图.DM1
-- Author :       fuqiang
--
-- Date Created : Tuesday, February 14, 2012 11:41:32
-- Target DBMS : IBM DB2 UDB 8.x
--

DROP TABLE NOTIFICATION_RECEIVER
;
DROP TABLE NOTIFICATION_SENDER
;
DROP TABLE NOTIFICATIONMSTR
;
DROP TABLE PROJECT_ATTACHMENT
;
DROP TABLE PROJECT_MEMBERMSTR
;
DROP TABLE PROJECT_MISSIONMSTR
;
DROP TABLE PROJECT_PROBLEMMSTR
;
DROP TABLE PROJECTMSTR
;
DROP TABLE SMART_ATTACHMENTMSTR
;
DROP TABLE SMART_STATISTICSMSTR
;
DROP TABLE SMARTMSTR
;
-- 
-- TABLE: NOTIFICATION_RECEIVER 
--

CREATE TABLE NOTIFICATION_RECEIVER(
    ID                     BIGINT         NOT NULL,
    NOTIFICATION_SENDER    BIGINT,
    RECEIVED_BY            VARCHAR(50)    NOT NULL,
    READ_DATE              TIMESTAMP,
    READ_IND               CHAR(1)        NOT NULL,
    DEFUNCT_IND            CHAR(1)        NOT NULL,
    CREATED_BY             VARCHAR(50)    NOT NULL,
    CREATED_DATETIME       TIMESTAMP      NOT NULL,
    UPDATED_BY             VARCHAR(50)    NOT NULL,
    UPDATED_DATETIME       TIMESTAMP      NOT NULL,
    CONSTRAINT PK31_1_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: NOTIFICATION_SENDER 
--

CREATE TABLE NOTIFICATION_SENDER(
    ID                     BIGINT          NOT NULL,
    NOTIFICATIONMSTR_ID    BIGINT,
    SENT_BY                VARCHAR(50)     NOT NULL,
    STATUS                 VARCHAR(100)    NOT NULL WITH DEFAULT 'N',
    SEND_DATETIME          TIMESTAMP,
    SEND_OPTION            VARCHAR(100)    NOT NULL,
    DEFUNCT_IND            CHAR(1)         NOT NULL,
    CREATED_BY             VARCHAR(50)     NOT NULL,
    CREATED_DATETIME       TIMESTAMP       NOT NULL,
    UPDATED_BY             VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME       TIMESTAMP       NOT NULL,
    CONSTRAINT PK31_2 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: NOTIFICATIONMSTR 
--

CREATE TABLE NOTIFICATIONMSTR(
    ID                  BIGINT           NOT NULL,
    TITLE               VARCHAR(50)      NOT NULL,
    CONTENT             VARCHAR(1000),
    TYPE_ID             VARCHAR(100),
    TYPE                CHAR(1),
    DEFUNCT_IND         CHAR(1)          NOT NULL,
    CREATED_BY          VARCHAR(50)      NOT NULL,
    CREATED_DATETIME    TIMESTAMP        NOT NULL,
    UPDATED_BY          VARCHAR(50)      NOT NULL,
    UPDATED_DATETIME    TIMESTAMP        NOT NULL,
    CONSTRAINT PK30_1_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: PROJECT_ATTACHMENT 
--

CREATE TABLE PROJECT_ATTACHMENT(
    ID                  BIGINT          NOT NULL,
    TYPE_ID             BIGINT          NOT NULL,
    NAME                VARCHAR(50)     NOT NULL,
    TYPE                VARCHAR(100)    NOT NULL,
    FILEMSTR_ID         VARCHAR(50)     NOT NULL,
    DEFUNCT_IND         CHAR(1)         NOT NULL,
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK45_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: PROJECT_MEMBERMSTR 
--

CREATE TABLE PROJECT_MEMBERMSTR(
    ID                  CHAR(10)        NOT NULL,
    PROJECTMSTR_ID      BIGINT          NOT NULL,
    MEMBER              VARCHAR(50)     NOT NULL,
    ROLE                VARCHAR(50),
    REMARKS             VARCHAR(200),
    DEFUNCT_IND         CHAR(1)         NOT NULL,
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK52_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: PROJECT_MISSIONMSTR 
--

CREATE TABLE PROJECT_MISSIONMSTR(
    ID                  BIGINT          NOT NULL,
    PROJECTMSTR_ID      BIGINT          NOT NULL,
    NAME                VARCHAR(50)     NOT NULL,
    CHARGED_BY          VARCHAR(50)     NOT NULL,
    START_DATE          TIMESTAMP,
    CLOSE_DATE          TIMESTAMP,
    DESC                VARCHAR(200),
    STATUS              VARCHAR(100),
    DEFUNCT_IND         CHAR(1)         NOT NULL,
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK50 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: PROJECT_PROBLEMMSTR 
--

CREATE TABLE PROJECT_PROBLEMMSTR(
    ID                        BIGINT          NOT NULL,
    PROJECT_MISSIONMSTR_ID    BIGINT          NOT NULL,
    DESC                      VARCHAR(200)    NOT NULL,
    SOLVED_BY                 VARCHAR(50),
    SOLVED_DATE               TIMESTAMP,
    STATUS                    VARCHAR(100),
    PROPOSAL                  VARCHAR(200),
    DEFUNCT_IND               CHAR(1)         NOT NULL,
    CREATED_BY                VARCHAR(50)     NOT NULL,
    CREATED_DATETIME          TIMESTAMP       NOT NULL,
    UPDATED_BY                VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME          TIMESTAMP       NOT NULL,
    CONSTRAINT PK51_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: PROJECTMSTR 
--

CREATE TABLE PROJECTMSTR(
    ID                  BIGINT          NOT NULL,
    CODE                VARCHAR(50)     NOT NULL,
    PM_ID               VARCHAR(50)     NOT NULL,
    NAME                VARCHAR(50)     NOT NULL,
    STATUS              VARCHAR(100),
    DESC                VARCHAR(200)    NOT NULL,
    START_DATE          TIMESTAMP,
    CLOSE_DATE          TIMESTAMP,
    DEFUNCT_IND         CHAR(1)         NOT NULL,
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK49 PRIMARY KEY (ID)
)
;
alter table PROJECTMSTR add PROGRESS FLOAT              With Default 0;


-- 
-- TABLE: SMART_ATTACHMENTMSTR 
--

CREATE TABLE SMART_ATTACHMENTMSTR(
    ID                  BIGINT          NOT NULL,
    SMARTMSTR_ID        BIGINT          NOT NULL,
    NAME                VARCHAR(50)     NOT NULL,
    TYPE                VARCHAR(100)    NOT NULL,
    FILEMSTR_ID         VARCHAR(50)     NOT NULL,
    DEFUNCT_IND         CHAR(1)         NOT NULL,
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK54 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: SMART_STATISTICSMSTR 
--

CREATE TABLE SMART_STATISTICSMSTR(
    ID                  BIGINT         NOT NULL,
    SMARTMSTR_ID        BIGINT         NOT NULL,
    CLICK_COUNT         BIGINT,
    DEFUNCT_IND         CHAR(1)        NOT NULL,
    CREATED_BY          VARCHAR(50)    NOT NULL,
    CREATED_DATETIME    TIMESTAMP      NOT NULL,
    UPDATED_BY          VARCHAR(50)    NOT NULL,
    UPDATED_DATETIME    TIMESTAMP      NOT NULL,
    CONSTRAINT PK55 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: SMARTMSTR 
--

CREATE TABLE SMARTMSTR(
    ID                  BIGINT           NOT NULL,
    TAX_TYPE            VARCHAR(100),
    REGION              VARCHAR(100),
    QUESTION            VARCHAR(1000)    NOT NULL,
    ANSWER              VARCHAR(1000)    NOT NULL,
    DEFUNCT_IND         CHAR(1)          NOT NULL,
    CREATED_BY          VARCHAR(50)      NOT NULL,
    CREATED_DATETIME    TIMESTAMP        NOT NULL,
    UPDATED_BY          VARCHAR(50)      NOT NULL,
    UPDATED_DATETIME    TIMESTAMP        NOT NULL,
    CONSTRAINT PK29 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: NOTIFICATION_RECEIVER 
--

ALTER TABLE NOTIFICATION_RECEIVER ADD CONSTRAINT RefNOTIFICATION811 
    FOREIGN KEY (NOTIFICATION_SENDER)
    REFERENCES NOTIFICATION_SENDER(ID)
;


-- 
-- TABLE: NOTIFICATION_SENDER 
--

ALTER TABLE NOTIFICATION_SENDER ADD CONSTRAINT RefNOTIFICATION821 
    FOREIGN KEY (NOTIFICATIONMSTR_ID)
    REFERENCES NOTIFICATIONMSTR(ID)
;


-- 
-- TABLE: PROJECT_MEMBERMSTR 
--

ALTER TABLE PROJECT_MEMBERMSTR ADD CONSTRAINT RefPROJECTMSTR651 
    FOREIGN KEY (PROJECTMSTR_ID)
    REFERENCES PROJECTMSTR(ID)
;


-- 
-- TABLE: PROJECT_MISSIONMSTR 
--

ALTER TABLE PROJECT_MISSIONMSTR ADD CONSTRAINT RefPROJECTMSTR631 
    FOREIGN KEY (PROJECTMSTR_ID)
    REFERENCES PROJECTMSTR(ID)
;


-- 
-- TABLE: PROJECT_PROBLEMMSTR 
--

ALTER TABLE PROJECT_PROBLEMMSTR ADD CONSTRAINT RefPROJECT_MISS641 
    FOREIGN KEY (PROJECT_MISSIONMSTR_ID)
    REFERENCES PROJECT_MISSIONMSTR(ID)
;


-- 
-- TABLE: SMART_ATTACHMENTMSTR 
--

ALTER TABLE SMART_ATTACHMENTMSTR ADD CONSTRAINT RefSMARTMSTR671 
    FOREIGN KEY (SMARTMSTR_ID)
    REFERENCES SMARTMSTR(ID)
;


-- 
-- TABLE: SMART_STATISTICSMSTR 
--

ALTER TABLE SMART_STATISTICSMSTR ADD CONSTRAINT RefSMARTMSTR681 
    FOREIGN KEY (SMARTMSTR_ID)
    REFERENCES SMARTMSTR(ID)
;



-- 税务精灵增加 名称字段
alter table SMARTMSTR add NAME VARCHAR(50)     NOT NULL default '';
reorg table SMARTMSTR;


--更改NOTIFICATIONMSTR表TYPE字段类型
ALTER TABLE NOTIFICATIONMSTR ALTER COLUMN TYPE SET DATA TYPE VARCHAR(100);
reorg table NOTIFICATIONMSTR;















	

--添加任务管理相关表
DROP TABLE WF_INSTANCEMSTR
;
DROP TABLE WF_INSTANCEMSTR_PROPERTY
;
DROP TABLE WF_POSITION
;
DROP TABLE WF_STEPMSTR
;
DROP TABLE WF_STEPMSTR_PROPERTY
;
DROP TABLE WF_SUPERVISOR
;
-- 
-- TABLE: WF_INSTANCEMSTR 
--

CREATE TABLE WF_INSTANCEMSTR(
    ID                  BIGINT          NOT NULL,
    NO                  VARCHAR(50)     NOT NULL,
    TYPE                VARCHAR(100)    NOT NULL,
    REQUEST_BY          VARCHAR(50)     NOT NULL,
    SUBMIT_DATETIME     TIMESTAMP,
    STATUS              VARCHAR(100)    NOT NULL,
    DEFUNCT_IND         CHAR(1)         NOT NULL,
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK41_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: WF_INSTANCEMSTR_PROPERTY 
--

CREATE TABLE WF_INSTANCEMSTR_PROPERTY(
    ID                    BIGINT           NOT NULL,
    WF_INSTANCEMSTR_ID    BIGINT,
    NAME                  VARCHAR(100)     NOT NULL,
    VALUE                 VARCHAR(1000),
    CONSTRAINT PK44_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: WF_POSITION 
--

CREATE TABLE WF_POSITION(
    ID             BIGINT          NOT NULL,
    POSITION_ID    BIGINT          NOT NULL,
    TYPE           VARCHAR(100)    NOT NULL,
    VALUE          VARCHAR(10)     NOT NULL,
    CONSTRAINT PK62_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: WF_STEPMSTR 
--

CREATE TABLE WF_STEPMSTR(
    ID                    BIGINT         NOT NULL,
    WF_INSTANCEMSTR_ID    BIGINT,
    FROM_STEP_ID          BIGINT,
    NAME                  VARCHAR(50)    NOT NULL,
    CODE                  VARCHAR(50)    NOT NULL,
    CHARGED_BY            VARCHAR(50)    NOT NULL,
    COMPLETED_DATETIME    TIMESTAMP,
    DEAL_METHOD           VARCHAR(50)    NOT NULL,
    DEFUNCT_IND           CHAR(1)        NOT NULL,
    CREATED_BY            VARCHAR(50)    NOT NULL,
    CREATED_DATETIME      TIMESTAMP      NOT NULL,
    UPDATED_BY            VARCHAR(50)    NOT NULL,
    UPDATED_DATETIME      TIMESTAMP      NOT NULL,
    CONSTRAINT PK58 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: WF_STEPMSTR_PROPERTY 
--

CREATE TABLE WF_STEPMSTR_PROPERTY(
    ID                BIGINT           NOT NULL,
    WF_STEPMSTR_ID    BIGINT,
    NAME              VARCHAR(100)     NOT NULL,
    VALUE             VARCHAR(1000),
    CONSTRAINT PK64 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: WF_SUPERVISOR 
--

CREATE TABLE WF_SUPERVISOR(
    ID            BIGINT          NOT NULL,
    TYPE          VARCHAR(100)    NOT NULL,
    VALUE         CHAR(10),
    CHARGED_BY    VARCHAR(50)     NOT NULL,
    SUPERVISOR    VARCHAR(50)     NOT NULL,
    CONSTRAINT PK59 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: WF_INSTANCEMSTR_PROPERTY 
--

ALTER TABLE WF_INSTANCEMSTR_PROPERTY ADD CONSTRAINT RefWF_INSTANCEM832 
    FOREIGN KEY (WF_INSTANCEMSTR_ID)
    REFERENCES WF_INSTANCEMSTR(ID)
;


-- 
-- TABLE: WF_STEPMSTR 
--

ALTER TABLE WF_STEPMSTR ADD CONSTRAINT RefWF_INSTANCEM842 
    FOREIGN KEY (WF_INSTANCEMSTR_ID)
    REFERENCES WF_INSTANCEMSTR(ID)
;


-- 
-- TABLE: WF_STEPMSTR_PROPERTY 
--

ALTER TABLE WF_STEPMSTR_PROPERTY ADD CONSTRAINT RefWF_STEPMSTR852 
    FOREIGN KEY (WF_STEPMSTR_ID)
    REFERENCES WF_STEPMSTR(ID)
;
	
--改变组织层级表 value列数据类型和长度 以为现在存值为 TIH.TAX.TYPE.X
alter table WF_SUPERVISOR ALTER VALUE SET DATA TYPE varchar(100);


--任务授权表，任务邮件配置表
DROP TABLE WF_AUTHORIZMSTR
;
DROP TABLE WF_MAIL_CONFIG
;
-- 
-- TABLE: WF_AUTHORIZMSTR 
--

CREATE TABLE WF_AUTHORIZMSTR(
    ID                  BIGINT          NOT NULL,
    AUTHORIZED_BY       VARCHAR(50)     NOT NULL,
    AUTHORIZED_TO       VARCHAR(50)     NOT NULL,
    START_DATETIME      TIMESTAMP       NOT NULL,
    END_DATETIME        TIMESTAMP       NOT NULL,
    TYPE                VARCHAR(100)    NOT NULL,
    REMARKS             VARCHAR(500),
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK63_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: WF_MAIL_CONFIG 
--

CREATE TABLE WF_MAIL_CONFIG(
    ID                  BIGINT          NOT NULL,
    TYPE                VARCHAR(100)    NOT NULL,
    MAIL_IND            CHAR(1)         NOT NULL,
    SYS_NOTICE_IND      CHAR(1)         NOT NULL,
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK63_1_1 PRIMARY KEY (ID)
)
;

-- 税务精灵关联任务主表ID 名称字段
alter table SMARTMSTR add wf_instancemstr_id bigint;
reorg table SMARTMSTR;

-- 授权表增加，增加发送邮件和系统消息的标识
alter table WF_AUTHORIZMSTR add MAIL_IND CHAR(1);
alter table WF_AUTHORIZMSTR add SYS_NOTICE_IND CHAR(1);
reorg table WF_AUTHORIZMSTR;

--修改changed_by长度
ALTER TABLE WF_STEPMSTR ALTER COLUMN CHARGED_BY SET DATA TYPE VARCHAR ( 1000) ;
reorg table WF_STEPMSTR;

--改变岗位业务表 value列数据类型和长度 以为现在存值为 TIH.TAX.TYPE.X
alter table WF_POSITION ALTER VALUE SET DATA TYPE varchar(100);
reorg table WF_POSITION;


--添加岗位表字段code,sys_ind。
ALTER TABLE POSITION ADD COLUMN CODE VARCHAR(100);
ALTER TABLE POSITION ADD COLUMN SYS_IND CHAR(1);
reorg table POSITION;


--设置CAS_USR_P表ID不为主键并且可为空，同时设置PERNR为主键
--删除表主键
ALTER TABLE CAS_USR_P DROP PRIMARY KEY;
--设置ID列可为空
ALTER TABLE CAS_USR_P ALTER COLUMN ID DROP NOT NULL;
--设置PERNR列不为空
alter TABLE CAS_USR_P alter column PERNR set not null;
--重组CAS_USR_P表，否则设置新主键不成功
REORG TABLE CAS_USR_P;
--设置PERNR为主键
ALTER TABLE CAS_USR_P ADD PRIMARY KEY(PERNR);
REORG TABLE CAS_USR_P;


--初始化岗位数据
--TODO

--由于消息格式变化，标题字数较长，设置消息内容表设置标题长度为500
alter table Notificationmstr ALTER title SET DATA TYPE varchar(500);
REORG TABLE Notificationmstr;

--公司数据库patch开始
--公司增加成立时间和经营时间，删除税务机关ID
alter table Companymstr drop column SETUP_DATETIME;
alter table Companymstr drop column START_DATETIME;
ALTER TABLE Companymstr ADD COLUMN SETUP_DATETIME TIMESTAMP;
ALTER TABLE Companymstr ADD COLUMN START_DATETIME TIMESTAMP;
alter table Companymstr drop column TAXAUTHORITY_ID;
reorg table Companymstr;

-- 税务机关增加类型TYPE字段
alter table Taxauthority drop column TYPE;
alter table Taxauthority add TYPE  VARCHAR ( 100) ;
reorg table Taxauthority;

DROP TABLE COMPANY_ANNUAL_RETURN;
DROP TABLE COMPANY_BRANCH;
DROP TABLE COMPANY_FINANCIAL_RETURN;
DROP TABLE COMPANY_LAND_DETAILS;
DROP TABLE COMPANY_MATERIAL;
DROP TABLE COMPANY_TAX_INCENTIVES;
--年度返还
CREATE TABLE COMPANY_ANNUAL_RETURN(
    ID                             BIGINT            NOT NULL,
    COMPANY_FINANCIAL_RETURN_ID    BIGINT            NOT NULL,
    RETURN_YEAR                    TIMESTAMP         NOT NULL,
    RETURN_ACCOUNT                 DECIMAL(18, 2)    NOT NULL,
    DEFUNCT_IND                    CHAR(1)           NOT NULL,
    CREATED_BY                     VARCHAR(50)       NOT NULL,
    CREATED_DATETIME               TIMESTAMP         NOT NULL,
    UPDATED_BY                     VARCHAR(50)       NOT NULL,
    UPDATED_DATETIME               TIMESTAMP         NOT NULL,
    CONSTRAINT PK87 PRIMARY KEY (ID)
)
;

--分支机构
CREATE TABLE COMPANY_BRANCH(
    ID                    BIGINT           NOT NULL,
    COMPANYMSTR_ID        BIGINT           NOT NULL,
    BUKRS                 VARCHAR(200),
    NAME                  VARCHAR(100)     NOT NULL,
    SETUP_DATETIME        TIMESTAMP        NOT NULL,
    LOCATION              VARCHAR(1000)    NOT NULL,
    STATISTIC_DATETIME    TIMESTAMP        NOT NULL,
    BUSINESS_SCOPE        VARCHAR(1000)    NOT NULL,
    REMAKRS               VARCHAR(1000),
    DEFUNCT_IND           CHAR(1)          NOT NULL,
    CREATED_BY            VARCHAR(50)      NOT NULL,
    CREATED_DATETIME      TIMESTAMP        NOT NULL,
    UPDATED_BY            VARCHAR(50)      NOT NULL,
    UPDATED_DATETIME      TIMESTAMP        NOT NULL,
    CONSTRAINT PK69 PRIMARY KEY (ID)
)
;

--财政返还
CREATE TABLE COMPANY_FINANCIAL_RETURN(
    ID                       BIGINT          NOT NULL,
    ITME                     VARCHAR(50)     NOT NULL,
    COMPANYMSTR_ID           BIGINT          NOT NULL,
    TAX_TYPE                 VARCHAR(100)    NOT NULL,
    RETURN_START_DATETIME    TIMESTAMP       NOT NULL,
    RETURN_END_DATETIME      TIMESTAMP       NOT NULL,
    RETURN_BASE              VARCHAR(100)    NOT NULL,
    RETURN_RATIO             VARCHAR(100)    NOT NULL,
    RETURN_ACCORDING         VARCHAR(100)    NOT NULL,
    DEFUNCT_IND              CHAR(1)         NOT NULL,
    CREATED_BY               VARCHAR(50)     NOT NULL,
    CREATED_DATETIME         TIMESTAMP       NOT NULL,
    UPDATED_BY               VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME         TIMESTAMP       NOT NULL,
    CONSTRAINT PK83 PRIMARY KEY (ID)
)
;

--土地明细
CREATE TABLE COMPANY_LAND_DETAILS(
    ID                      BIGINT            NOT NULL,
    COMPANYMSTR_ID          BIGINT,
    LAND_ADDRESS            VARCHAR(200)      NOT NULL,
    LAND_NAME               VARCHAR(50),
    LAND_AREA               FLOAT             NOT NULL,
    LAND_USAGE              VARCHAR(100),
    LAND_KIND               VARCHAR(100),
    DEV_DEGREE              VARCHAR(50),
    LAND_CERTIFICATE_NO     VARCHAR(50),
    CERTIFICATE_RIGHT_BY    VARCHAR(50),
    LAND_GET_DATETIME       TIMESTAMP,
    LAND_OVER_DATETIME      TIMESTAMP,
    LAND_COST               DECIMAL(18, 2)    NOT NULL,
    TAX_ACCRODING           DECIMAL(18, 2)    NOT NULL,
    ANNUAL_PAY              DECIMAL(18, 2),
    LAND_VOLUME_RATE        FLOAT             NOT NULL,
    DEFUNCT_IND             CHAR(1),
    CREATED_BY              VARCHAR(50)       NOT NULL,
    CREATED_DATETIME        TIMESTAMP         NOT NULL,
    UPDATED_BY              VARCHAR(50)       NOT NULL,
    UPDATED_DATETIME        TIMESTAMP         NOT NULL,
    CONSTRAINT PK84_1 PRIMARY KEY (ID)
)
;

--原料及工艺
CREATE TABLE COMPANY_MATERIAL(
    ID                  BIGINT          NOT NULL,
    COMPANYMSTR_ID      BIGINT,
    MAIN_MATERIAL       VARCHAR(100)    NOT NULL,
    MAIN_PRODUCT        VARCHAR(100)    NOT NULL,
    PROCESSING          VARCHAR(100)    NOT NULL,
    ABILITY             INTEGER         NOT NULL,
    UNIT                VARCHAR(100)    NOT NULL,
    DEFUNCT_IND         VARCHAR(1)      NOT NULL,
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK82 PRIMARY KEY (ID)
)
;

--税收优惠
CREATE TABLE COMPANY_TAX_INCENTIVES(
    ID                             BIGINT           NOT NULL,
    COMPANYMSTR_ID                 BIGINT           NOT NULL,
    TAX_TYPE                       VARCHAR(100)     NOT NULL,
    PREFERENTIAL_ITEM              VARCHAR(100)     NOT NULL,
    PREFERENTIAL_START_DATETIME    TIMESTAMP        NOT NULL,
    PREFERENTIAL_END_DATETIME      TIMESTAMP        NOT NULL,
    SITUATION_REMARKS              VARCHAR(100)     NOT NULL,
    STATISTIC_DATETIME             TIMESTAMP        NOT NULL,
    APPROVAL_ORGAN                 VARCHAR(200),
    POLICY                         VARCHAR(1000)    NOT NULL,
    DEFUNCT_IND                    CHAR(1)          NOT NULL,
    CREATED_BY                     VARCHAR(50)      NOT NULL,
    CREATED_DATETIME               TIMESTAMP        NOT NULL,
    UPDATED_BY                     VARCHAR(50)      NOT NULL,
    UPDATED_DATETIME               TIMESTAMP        NOT NULL,
    CONSTRAINT PK79 PRIMARY KEY (ID)
)
;

ALTER TABLE COMPANY_ANNUAL_RETURN ADD CONSTRAINT RefCOMPANY_FINA117 
    FOREIGN KEY (COMPANY_FINANCIAL_RETURN_ID)
    REFERENCES COMPANY_FINANCIAL_RETURN(ID)
;

ALTER TABLE COMPANY_BRANCH ADD CONSTRAINT RefCOMPANYMSTR101 
    FOREIGN KEY (COMPANYMSTR_ID)
    REFERENCES COMPANYMSTR(ID)
;

ALTER TABLE COMPANY_ANNUAL_RETURN ADD CONSTRAINT RefCOMPANY_FINA117 
    FOREIGN KEY (COMPANY_FINANCIAL_RETURN_ID)
    REFERENCES COMPANY_FINANCIAL_RETURN(ID)
;


ALTER TABLE COMPANY_LAND_DETAILS ADD CONSTRAINT RefCOMPANYMSTR119 
    FOREIGN KEY (COMPANYMSTR_ID)
    REFERENCES COMPANYMSTR(ID)
;

ALTER TABLE COMPANY_MATERIAL ADD CONSTRAINT RefCOMPANYMSTR118 
    FOREIGN KEY (COMPANYMSTR_ID)
    REFERENCES COMPANYMSTR(ID)
;


ALTER TABLE COMPANY_TAX_INCENTIVES ADD CONSTRAINT RefCOMPANYMSTR102 
    FOREIGN KEY (COMPANYMSTR_ID)
    REFERENCES COMPANYMSTR(ID)
;

--税收优惠表增加审批机构表
alter table COMPANY_TAX_INCENTIVES drop column APPROVAL_ORGAN;
ALTER TABLE COMPANY_TAX_INCENTIVES ADD COLUMN APPROVAL_ORGAN VARCHAR(200);

reorg TABLE COMPANY_ANNUAL_RETURN;
reorg TABLE COMPANY_BRANCH;
reorg TABLE COMPANY_FINANCIAL_RETURN;
reorg TABLE COMPANY_LAND_DETAILS;
reorg TABLE COMPANY_MATERIAL;
reorg TABLE COMPANY_TAX_INCENTIVES;


drop table COMPANY_ANNUAL_TAX_PAY;
CREATE TABLE COMPANY_ANNUAL_TAX_PAY
(
    ID                        BIGINT        NOT NULL,
    COMPANY_TAX_TYPE_RATIO_ID BIGINT,
    TAX_PAY_YEAR              TIMESTAMP     NOT NULL,
    TAX_PAY_ACCOUNT           DECIMAL(18,2) NOT NULL,
    TAX_RATE                  DOUBLE        NOT NULL,
    REMARKS                   VARCHAR(200),
    DEFUNCT_IND               CHARACTER(1)  NOT NULL,
    CREATED_BY                VARCHAR(50)   NOT NULL,
    CREATED_DATETIME          TIMESTAMP     NOT NULL,
    UPDATED_BY                VARCHAR(50)   NOT NULL,
    UPDATED_DATETIME          TIMESTAMP     NOT NULL,
    CONSTRAINT PK88
    PRIMARY KEY (ID)
)
    IN USERSPACE1
;
ALTER TABLE COMPANY_ANNUAL_TAX_PAY DATA CAPTURE NONE
;
ALTER TABLE COMPANY_ANNUAL_TAX_PAY NOT VOLATILE
;
ALTER TABLE COMPANY_ANNUAL_TAX_PAY APPEND OFF
;
ALTER TABLE COMPANY_ANNUAL_TAX_PAY LOCKSIZE ROW
;
ALTER TABLE COMPANY_ANNUAL_TAX_PAY DROP RESTRICT ON DROP
;
drop table COMPANY_DEPRECIATION;
CREATE TABLE COMPANY_DEPRECIATION
(
    ID                     BIGINT        NOT NULL,
    COMPANY_MAIN_ASSETS_ID BIGINT,
    YEAR                   TIMESTAMP     NOT NULL,
    COST                   DECIMAL(18,2) NOT NULL,
    NET_WORTH              DECIMAL(18,2) NOT NULL,
    DEFUNCT_IND            CHARACTER(1)  NOT NULL,
    CREATED_BY             VARCHAR(50)   NOT NULL,
    CREATED_DATETIME       TIMESTAMP     NOT NULL,
    UPDATED_BY             VARCHAR(50)   NOT NULL,
    UPDATED_DATETIME       TIMESTAMP     NOT NULL,
    CONSTRAINT PK89
    PRIMARY KEY (ID)
)
    IN USERSPACE1
;
ALTER TABLE COMPANY_DEPRECIATION DATA CAPTURE NONE
;
ALTER TABLE COMPANY_DEPRECIATION NOT VOLATILE
;
ALTER TABLE COMPANY_DEPRECIATION APPEND OFF
;
ALTER TABLE COMPANY_DEPRECIATION LOCKSIZE ROW
;
ALTER TABLE COMPANY_DEPRECIATION DROP RESTRICT ON DROP
;
drop table COMPANY_ESTATE;
CREATE TABLE COMPANY_ESTATE
(
    ID                         CHARACTER(10) NOT NULL,
    COMPANYMSTR_ID             BIGINT,
    ESTATE_NO                  VARCHAR(50),
    NAME                       VARCHAR(100)  NOT NULL,
    AREA                       DOUBLE        NOT NULL,
    TYPE                       VARCHAR(100)  NOT NULL,
    LAND_NO                    VARCHAR(50),
    LAND_VOLUMN_RATE           DOUBLE,
    LAND_UNIT_COST             DOUBLE,
    LAND_COST                  DECIMAL(18,2),
    ESTATE_COMPLETION_DATETIME TIMESTAMP,
    CAPITALIZATION_DATETIME    TIMESTAMP,
    USAGE_START_DATETIME       TIMESTAMP,
    PAY_TAX_START_DATETIME     TIMESTAMP,
    CAL_TAX_TYPE               VARCHAR(100),
    ESTATE_ACCOUNT_COST        DECIMAL(18,2) NOT NULL,
    CAL_TAX_LAND_COST          DECIMAL(18,2),
    CAL_TAX_ESTATE_COST        DECIMAL(18,2),
    DEDUCTION_RATE             DOUBLE,
    TAX_RATE                   DOUBLE,
    TAX_ACCOUNT                DECIMAL(18,2),
    DEFUNCT_IND                CHARACTER(1)  NOT NULL,
    CREATED_BY                 VARCHAR(50)   NOT NULL,
    CREATED_DATETIME           TIMESTAMP     NOT NULL,
    UPDATED_BY                 VARCHAR(50)   NOT NULL,
    UPDATED_DATETIME           TIMESTAMP     NOT NULL,
    CONSTRAINT PK85_1
    PRIMARY KEY (ID)
)
    IN USERSPACE1
;
ALTER TABLE COMPANY_ESTATE DATA CAPTURE NONE
;
ALTER TABLE COMPANY_ESTATE NOT VOLATILE
;
ALTER TABLE COMPANY_ESTATE APPEND OFF
;
ALTER TABLE COMPANY_ESTATE LOCKSIZE ROW
;
ALTER TABLE COMPANY_ESTATE DROP RESTRICT ON DROP
;
drop table COMPANY_MAIN_ASSETS;
CREATE TABLE COMPANY_MAIN_ASSETS
(
    ID                 BIGINT       NOT NULL,
    COMPANYMSTR_ID     BIGINT,
    ITEM               VARCHAR(100) NOT NULL,
    ACCOUNT            DOUBLE,
    UNIT               VARCHAR(100),
    DEPRECIATION_TIMES DOUBLE       NOT NULL,
    DEFUNCT_IND        VARCHAR(50)  NOT NULL,
    CREATED_BY         VARCHAR(50)  NOT NULL,
    CREATED_DATETIME   TIMESTAMP    NOT NULL,
    UPDATED_BY         VARCHAR(50)  NOT NULL,
    UPDATED_DATETIME   TIMESTAMP    NOT NULL,
    CONSTRAINT PK92
    PRIMARY KEY (ID)
)
    IN USERSPACE1
;
ALTER TABLE COMPANY_MAIN_ASSETS DATA CAPTURE NONE
;
ALTER TABLE COMPANY_MAIN_ASSETS NOT VOLATILE
;
ALTER TABLE COMPANY_MAIN_ASSETS APPEND OFF
;
ALTER TABLE COMPANY_MAIN_ASSETS LOCKSIZE ROW
;
ALTER TABLE COMPANY_MAIN_ASSETS DROP RESTRICT ON DROP
;
drop table COMPANY_TAX_TYPE_RATIO;
CREATE TABLE COMPANY_TAX_TYPE_RATIO
(
    ID               BIGINT       NOT NULL,
    COMPANYMSTR_ID   BIGINT       NOT NULL,
    TAX_TYPE         VARCHAR(100) NOT NULL,
    TAX_BASIS        VARCHAR(50),
    REPORT_FREQUENCY VARCHAR(100) NOT NULL,
    REMARKS          VARCHAR(100),
    DEFUNCT_IND      CHARACTER(1) NOT NULL,
    CREATED_BY       VARCHAR(50)  NOT NULL,
    CREATED_DATETIME TIMESTAMP    NOT NULL,
    UPDATED_BY       VARCHAR(50)  NOT NULL,
    UPDATED_DATETIME TIMESTAMP    NOT NULL,
    CONSTRAINT PK68
    PRIMARY KEY (ID)
)
    IN USERSPACE1
;
ALTER TABLE COMPANY_TAX_TYPE_RATIO DATA CAPTURE NONE
;
ALTER TABLE COMPANY_TAX_TYPE_RATIO NOT VOLATILE
;
ALTER TABLE COMPANY_TAX_TYPE_RATIO APPEND OFF
;
ALTER TABLE COMPANY_TAX_TYPE_RATIO LOCKSIZE ROW
;
ALTER TABLE COMPANY_TAX_TYPE_RATIO DROP RESTRICT ON DROP
;

--投资情况信息
drop table COMPANY_INVESTMENT;
CREATE TABLE COMPANY_INVESTMENT(
    ID                BIGINT            NOT NULL,
    COMPANYMSTR_ID    BIGINT,
    PHASE             INTEGER           NOT NULL,
    START_DATETIME    TIMESTAMP         NOT NULL,
    END_DATETIME      TIMESTAMP,
    INVEST_ACCOUNT    DECIMAL(18, 2)    NOT NULL,
    CURRENCY          VARCHAR(100)      NOT NULL,
    DEFUNCT_IND       CHAR(1)           NOT NULL,
    CREATED_BY              VARCHAR(50)       NOT NULL,
    CREATED_DATETIME        TIMESTAMP         NOT NULL,
    UPDATED_BY              VARCHAR(50)       NOT NULL,
    UPDATED_DATETIME        TIMESTAMP         NOT NULL,
    CONSTRAINT PK86 PRIMARY KEY (ID)
)
;
--股权机构信息
drop table COMPANY_STOCK_STRUCTURE;
CREATE TABLE COMPANY_STOCK_STRUCTURE(
    ID                     BIGINT            NOT NULL,
    COMPANYMSTR_ID         BIGINT            NOT NULL,
    SHAREHOLDER            VARCHAR(50)       NOT NULL,
    TYPE                   VARCHAR(100)      NOT NULL,
    REGISTERED_CAPITAL     DECIMAL(20, 2)    NOT NULL,
    CURRENCY               VARCHAR(100)      NOT NULL,
    RATIO                  FLOAT             NOT NULL,
    STATISTICS_DATETIME    TIMESTAMP         NOT NULL,
    DEFUNCT_IND            CHAR(1)           NOT NULL,
    CREATED_BY             VARCHAR(50)       NOT NULL,
    CREATED_DATETIME       TIMESTAMP         NOT NULL,
    UPDATED_BY             VARCHAR(50)       NOT NULL,
    UPDATED_DATETIME       TIMESTAMP         NOT NULL,
    CONSTRAINT PK66 PRIMARY KEY (ID)
)
;
--税务机关
drop table TAXAUTHORITY_COMPANYMSTR;
CREATE TABLE TAXAUTHORITY_COMPANYMSTR(
    ID                     BIGINT         NOT NULL,
    TAXAUTHORITY_ID        BIGINT         NOT NULL,
    COMPANYMSTR_ID         BIGINT         NOT NULL,
    TAXPAYER_IDENTIFIER    VARCHAR(50)    NOT NULL,
    DEFUNCT_IND           CHAR(1),
    CREATED_BY            VARCHAR(50),
    CREATED_DATETIME      TIMESTAMP,
    UPDATED_BY            VARCHAR(50),
    UPDATED_DATETIME      TIMESTAMP,
    CONSTRAINT PK80 PRIMARY KEY (ID, TAXAUTHORITY_ID, COMPANYMSTR_ID)
)
;

ALTER TABLE COMPANY_INVESTMENT ADD CONSTRAINT RefCOMPANYMSTR127 
    FOREIGN KEY (COMPANYMSTR_ID)
    REFERENCES COMPANYMSTR(ID)
;

ALTER TABLE COMPANY_STOCK_STRUCTURE ADD CONSTRAINT RefCOMPANYMSTR94 
    FOREIGN KEY (COMPANYMSTR_ID)
    REFERENCES COMPANYMSTR(ID)
;

ALTER TABLE TAXAUTHORITY_COMPANYMSTR ADD CONSTRAINT RefTAXAUTHORITY109 
    FOREIGN KEY (TAXAUTHORITY_ID)
    REFERENCES TAXAUTHORITY(ID)
;

ALTER TABLE TAXAUTHORITY_COMPANYMSTR ADD CONSTRAINT RefCOMPANYMSTR110 
    FOREIGN KEY (COMPANYMSTR_ID)
    REFERENCES COMPANYMSTR(ID)
;
--公司数据库patch结束

--增加我的公司管理菜单
delete RESOURCEMSTR r where r.CODE = 'system:mycompany';
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (34, '我的公司管理','system:mycompany',5,6,'MENU','/faces/common/company/my_company.xhtml','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');

--资源表修改name的长度
alter table RESOURCEMSTR ALTER NAME SET DATA TYPE varchar(100);
reorg table RESOURCEMSTR;
--增加汇总报表菜单
delete RESOURCEMSTR r where r.CODE = 'report:special';
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (35, '专用报表处理','report:special',1,5,'MENU','/faces/report/aboutTax/index.xhtml','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
delete RESOURCEMSTR r where r.CODE = 'report:companytax';
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (36, '公司涉税信息汇总','report:companytax',1,35,'MENU','/faces/report/aboutTax/index.xhtml','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
delete RESOURCEMSTR r where r.CODE = 'report:vattnputtax';
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (37, '增值税进项税额抵扣汇总','report:vattnputtax',2,35,'MENU','/faces/report/vat_summary/index.xhtml','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
delete RESOURCEMSTR r where r.CODE = 'report:paytax';
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME) 
values (38, '应交税费综合信息汇总','report:paytax',3,35,'MENU','/faces/report/payabletax/index.xhtml','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');

--报表汇总历史表
DROP TABLE REPORT_SUMMARY_HISTORY;
CREATE TABLE REPORT_SUMMARY_HISTORY(
    ID                  BIGINT          NOT NULL,
    REPORT_TYPE         VARCHAR(100)    NOT NULL,
    SUMMARY_DATETIME    TIMESTAMP       NOT NULL,
    NAME                VARCHAR(200)    NOT NULL,
    FILE_ID             VARCHAR(100)    NOT NULL,
    DEFUNCT_IND         CHAR(1)         NOT NULL,
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    CONSTRAINT PK118 PRIMARY KEY (ID)
);

--增值税进项税抵扣表
DROP TABLE REPORT_VAT_IPT_DEDUCTION;
DROP TABLE REPORT_VAT_IPT_DEDUCTION_DETAIL;
CREATE TABLE REPORT_VAT_IPT_DEDUCTION(
    ID                    BIGINT          NOT NULL,
    COMPANYMSTR_ID        BIGINT          NOT NULL,
    COMPANY_NAME          VARCHAR(200)    NOT NULL,
    BUKRS                 VARCHAR(200)    NOT NULL,
    TAX_RATE              FLOAT           NOT NULL,
    STATISTIC_DATETIME    TIMESTAMP       NOT NULL,
    STATUS                CHAR(100)       NOT NULL,
    DEFUNCT_IND           CHAR(1)         NOT NULL,
    CREATED_BY            VARCHAR(50)     NOT NULL,
    CREATED_DATETIME      TIMESTAMP       NOT NULL,
    UPDATED_BY            VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME      TIMESTAMP       NOT NULL,
    CONSTRAINT PK77_1 PRIMARY KEY (ID)
);
CREATE TABLE REPORT_VAT_IPT_DEDUCTION_DETAIL(
    ID                             BIGINT            NOT NULL,
    REPORT_VAT_IPT_DEDUCTION_ID    BIGINT            NOT NULL,
    INVOICE_TYPE_NAME              VARCHAR(100)      NOT NULL,
    INVOICE_TYPE_CODE              VARCHAR(100)      NOT NULL,
    VARIETY_NAME                   VARCHAR(100)      NOT NULL,
    VARIETY_CODE                   VARCHAR(100)      NOT NULL,
    TAX_RATE                       FLOAT             NOT NULL,
    AMMOUNT                        FLOAT             NOT NULL,
    MONEY_SUM                      DECIMAL(20, 2)    NOT NULL,
    TAX_AMMOUNT                    DECIMAL(30, 2),
    CREATED_BY                     VARCHAR(50)       NOT NULL,
    CREATED_DATETIME               TIMESTAMP         NOT NULL,
    UPDATED_BY                     VARCHAR(50)       NOT NULL,
    UPDATED_DATETIME               TIMESTAMP         NOT NULL,
    CONSTRAINT PK77 PRIMARY KEY (ID)
);
ALTER TABLE REPORT_VAT_IPT_DEDUCTION_DETAIL ADD CONSTRAINT RefREPORT_VAT_1281 
    FOREIGN KEY (REPORT_VAT_IPT_DEDUCTION_ID)
    REFERENCES REPORT_VAT_IPT_DEDUCTION(ID)
;

--应缴税费表
DROP TABLE REPORT_PAYABLE_TAX;
DROP TABLE REPORT_PAYABLE_TAX_ADDED_MATERIAL;
DROP TABLE REPORT_PAYABLE_TAX_ESTATE;
DROP TABLE REPORT_PAYABLE_TAX_INCOME;
DROP TABLE REPORT_PAYABLE_TAX_LAND;
DROP TABLE REPORT_PAYABLE_TAX_OTHERS;
DROP TABLE REPORT_PAYABLE_TAX_STAMP;
DROP TABLE REPORT_PAYABLE_TAX_STAYED;
DROP TABLE REPORT_PAYABLE_TAX_VAT;
CREATE TABLE REPORT_PAYABLE_TAX(
    ID                    BIGINT          NOT NULL,
    COMPANYMSTR_ID        BIGINT          NOT NULL,
    COMPANY_NAME          VARCHAR(200)    NOT NULL,
    BUKRS                 VARCHAR(200)    NOT NULL,
    STATISTIC_DATETIME    TIMESTAMP       NOT NULL,
    STATUS                CHAR(100)       NOT NULL,
    DEFUNCT_IND           CHAR(1)         NOT NULL,
    CREATED_BY            VARCHAR(50)     NOT NULL,
    CREATED_DATETIME      TIMESTAMP       NOT NULL,
    UPDATED_BY            VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME      TIMESTAMP       NOT NULL,
    CONSTRAINT PK77_1_1 PRIMARY KEY (ID)
);
CREATE TABLE REPORT_PAYABLE_TAX_ADDED_MATERIAL(
    ID                       BIGINT            NOT NULL,
    REPORT_PAYABLE_TAX_ID    BIGINT,
    ITEM_NAME                VARCHAR(100)      NOT NULL,
    ITEM_CODE                VARCHAR(100),
    LAST_YEAR_MONTH_PAY      DECIMAL(18, 2),
    THIS_MONTH_PAY           DECIMAL(18, 2),
    THIS_YEAR_ACCUM_PAY      DECIMAL(18, 2),
    REMARKS                  VARCHAR(200),
    DEFUNCT_IND           CHAR(1)         NOT NULL,
    CREATED_BY            VARCHAR(50)     NOT NULL,
    CREATED_DATETIME      TIMESTAMP       NOT NULL,
    UPDATED_BY            VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME      TIMESTAMP       NOT NULL,
    CONSTRAINT PK76_1 PRIMARY KEY (ID)
);
CREATE TABLE REPORT_PAYABLE_TAX_ESTATE(
    ID                       BIGINT            NOT NULL,
    REPORT_PAYABLE_TAX_ID    BIGINT,
    ITEM_NAME                VARCHAR(50),
    ITEM_CODE                CHAR(10),
    YEAR_SHOULD_DECLARE      DECIMAL(18, 2),
    CURR_DECLARE             DECIMAL(18, 2),
    YEAR_ACCUM_DECLARE       DECIMAL(18, 2),
    REMARKS                  VARCHAR(200),
    DEFUNCT_IND           CHAR(1)         NOT NULL,
    CREATED_BY            VARCHAR(50)     NOT NULL,
    CREATED_DATETIME      TIMESTAMP       NOT NULL,
    UPDATED_BY            VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME      TIMESTAMP       NOT NULL,
    CONSTRAINT PK75 PRIMARY KEY (ID)
);
CREATE TABLE REPORT_PAYABLE_TAX_INCOME(
    ID                       BIGINT            NOT NULL,
    REPORT_PAYABLE_TAX_ID    BIGINT,
    FIRST_SEASON             DECIMAL(10, 0),
    SECOND_SEASON            DECIMAL(10, 0),
    THIRD_SEASON             DECIMAL(10, 0),
    FOURTH_SEASON            DECIMAL(10, 0),
    SUM                      DECIMAL(10, 0),
    FINAL_SET_DECLARE        DECIMAL(18, 2),
    SHOULD_ADD_NOR_RETRUN    DECIMAL(18, 2),
    EVALUATE_AMMOUNT         DECIMAL(18, 2),
    RETURN_AMMOUNT           DECIMAL(18, 2),
    DEFUNCT_IND           CHAR(1)         NOT NULL,
    CREATED_BY            VARCHAR(50)     NOT NULL,
    CREATED_DATETIME      TIMESTAMP       NOT NULL,
    UPDATED_BY            VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME      TIMESTAMP       NOT NULL,
    CONSTRAINT PK72 PRIMARY KEY (ID)
);
CREATE TABLE REPORT_PAYABLE_TAX_LAND(
    ID                       BIGINT            NOT NULL,
    REPORT_PAYABLE_TAX_ID    BIGINT,
    ITEM_NAME                VARCHAR(100)      NOT NULL,
    ITEM_CODE                VARCHAR(100),
    YEAR_SHOULD_DECLARE      DECIMAL(18, 2),
    CURR_DECLARE             FLOAT,
    YEAR_ACCUM_DECLARE       FLOAT,
    REMARKS                  VARCHAR(100),
    DEFUNCT_IND           CHAR(1)         NOT NULL,
    CREATED_BY            VARCHAR(50)     NOT NULL,
    CREATED_DATETIME      TIMESTAMP       NOT NULL,
    UPDATED_BY            VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME      TIMESTAMP       NOT NULL,
    CONSTRAINT PK76 PRIMARY KEY (ID)
);
CREATE TABLE REPORT_PAYABLE_TAX_OTHERS(
    ID                       BIGINT            NOT NULL,
    REPORT_PAYABLE_TAX_ID    BIGINT,
    TAX_NAME                 VARCHAR(100)      NOT NULL,
    TAX_CODE                 VARCHAR(100),
    BEGIN_YEAR_OVERAGE       DECIMAL(18, 2),
    END_YEAR_OVERAGE         DECIMAL(18, 2),
    CURR_MONTH_DECLARE       DECIMAL(18, 2),
    BEGIN_YEAR_NOT_PAY       DECIMAL(18, 2),
    YEAR_ACCUM_SHOULD_PAY    DECIMAL(18, 2),
    YEAR_ACCUM_PAIED         DECIMAL(18, 2),
    YEAR_ACCUM_NOT_PAY       DECIMAL(10, 0),
    DIFFERENCE               DECIMAL(18, 2),
    REMARKS                  VARCHAR(200),
    DEFUNCT_IND           CHAR(1)         NOT NULL,
    CREATED_BY            VARCHAR(50)     NOT NULL,
    CREATED_DATETIME      TIMESTAMP       NOT NULL,
    UPDATED_BY            VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME      TIMESTAMP       NOT NULL,
    CONSTRAINT PK71 PRIMARY KEY (ID)
);
CREATE TABLE REPORT_PAYABLE_TAX_STAMP(
    ID                       BIGINT            NOT NULL,
    REPORT_PAYABLE_TAX_ID    BIGINT,
    TAX_RATING               VARCHAR(100)      NOT NULL,
    TAX_RATING_CODE          CHAR(100),
    CONTRACT_AMOUNT          DECIMAL(18, 2),
    TAX_RATE                 FLOAT,
    YEAR_ACCUM_DECLARE       DECIMAL(18, 2),
    DEFUNCT_IND           CHAR(1)         NOT NULL,
    CREATED_BY            VARCHAR(50)     NOT NULL,
    CREATED_DATETIME      TIMESTAMP       NOT NULL,
    UPDATED_BY            VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME      TIMESTAMP       NOT NULL,
    CONSTRAINT PK74 PRIMARY KEY (ID)
);
CREATE TABLE REPORT_PAYABLE_TAX_STAYED(
    ID                       BIGINT            NOT NULL,
    REPORT_PAYABLE_TAX_ID    BIGINT,
    MONTHLY_AMMOUNT          DECIMAL(18, 2),
    MONTH                    INTEGER           NOT NULL,
    DEFUNCT_IND           CHAR(1)         NOT NULL,
    CREATED_BY            VARCHAR(50)     NOT NULL,
    CREATED_DATETIME      TIMESTAMP       NOT NULL,
    UPDATED_BY            VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME      TIMESTAMP       NOT NULL,
    CONSTRAINT PK73 PRIMARY KEY (ID)
);
CREATE TABLE REPORT_PAYABLE_TAX_VAT(
    ID                       BIGINT            NOT NULL,
    REPORT_PAYABLE_TAX_ID    BIGINT            NOT NULL,
    ITEM_NAME                VARCHAR(100)      NOT NULL,
    ITEM_CODE                VARCHAR(100),
    BEGIN_YEAR_OVERAGE       DECIMAL(18, 2),
    END_YEAR_OVERAGE         DECIMAL(18, 2),
    CUR_DECLARE_NUM          FLOAT,
    YEAR_ACCUM_SHOULD_PAY    DECIMAL(18, 2),
    YEAR_ACCUM_HAVE_PAIED    DECIMAL(18, 2),
    YEAR_ACCUM_NOT_PAY       DECIMAL(18, 2),
    DIFFERENCE               DECIMAL(18, 2),
    REMARKS                  VARCHAR(200),
    DEFUNCT_IND           CHAR(1)         NOT NULL,
    CREATED_BY            VARCHAR(50)     NOT NULL,
    CREATED_DATETIME      TIMESTAMP       NOT NULL,
    UPDATED_BY            VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME      TIMESTAMP       NOT NULL,
    CONSTRAINT PK70 PRIMARY KEY (ID)
);
ALTER TABLE REPORT_PAYABLE_TAX_ADDED_MATERIAL ADD CONSTRAINT RefREPORT_PAYA1382 
    FOREIGN KEY (REPORT_PAYABLE_TAX_ID)
    REFERENCES REPORT_PAYABLE_TAX(ID)
;
ALTER TABLE REPORT_PAYABLE_TAX_ESTATE ADD CONSTRAINT RefREPORT_PAYA1362 
    FOREIGN KEY (REPORT_PAYABLE_TAX_ID)
    REFERENCES REPORT_PAYABLE_TAX(ID)
;
ALTER TABLE REPORT_PAYABLE_TAX_INCOME ADD CONSTRAINT RefREPORT_PAYA1392 
    FOREIGN KEY (REPORT_PAYABLE_TAX_ID)
    REFERENCES REPORT_PAYABLE_TAX(ID)
;
ALTER TABLE REPORT_PAYABLE_TAX_LAND ADD CONSTRAINT RefREPORT_PAYA1372 
    FOREIGN KEY (REPORT_PAYABLE_TAX_ID)
    REFERENCES REPORT_PAYABLE_TAX(ID)
;
ALTER TABLE REPORT_PAYABLE_TAX_OTHERS ADD CONSTRAINT RefREPORT_PAYA1322 
    FOREIGN KEY (REPORT_PAYABLE_TAX_ID)
    REFERENCES REPORT_PAYABLE_TAX(ID)
;
ALTER TABLE REPORT_PAYABLE_TAX_STAMP ADD CONSTRAINT RefREPORT_PAYA1342 
    FOREIGN KEY (REPORT_PAYABLE_TAX_ID)
    REFERENCES REPORT_PAYABLE_TAX(ID)
;
ALTER TABLE REPORT_PAYABLE_TAX_STAYED ADD CONSTRAINT RefREPORT_PAYA1332 
    FOREIGN KEY (REPORT_PAYABLE_TAX_ID)
    REFERENCES REPORT_PAYABLE_TAX(ID)
;
ALTER TABLE REPORT_PAYABLE_TAX_VAT ADD CONSTRAINT RefREPORT_PAYA1302 
    FOREIGN KEY (REPORT_PAYABLE_TAX_ID)
    REFERENCES REPORT_PAYABLE_TAX(ID)
;


-- 应缴税费Resource
UPDATE RESOURCEMSTR SET URI = '/faces/report/vat_summary/index.xhtml' WHERE ID = 37;
UPDATE RESOURCEMSTR SET URI = '/faces/report/payabletax/index.xhtml' WHERE ID = 38;

--精灵表修改name的长度为150
alter table SMARTMSTR ALTER NAME SET DATA TYPE varchar(150);
reorg table SMARTMSTR;


--给测试人员设置邮箱地址
update P p set p.email = 'cailingling@wcs-global.com' where p.id in (select cup.pernr from CAS_USR_P cup where cup.id in('linlin','myloke','jiangdong','renfenlan','wangmin'));
update P p set p.email = 'daijinghua@wcs-global.com' where p.id in (select cup.pernr from CAS_USR_P cup where cup.id in('chenhong','chenjing','jingying','daimiao','jianghong'));
update P p set p.email = 'wangyun@wcs-global.com' where p.id in (select cup.pernr from CAS_USR_P cup where cup.id in('majinfengtj','shenbo','yanhao','zhangyunfeng','zhouyingying'));

--税务机关管理表name长度修改为90,允许输入30个汉字
alter table TAXAUTHORITY ALTER NAME SET DATA TYPE varchar(90);
reorg table TAXAUTHORITY;

--常用链接表name长度修改为45,允许输入15个汉字
alter table LINKMSTR ALTER NAME SET DATA TYPE varchar(45);
reorg table LINKMSTR;

--项目管理.任务表
ALTER TABLE PROJECT_MISSIONMSTR ALTER COLUMN NAME SET DATA TYPE VARCHAR(150);
ALTER TABLE PROJECT_MISSIONMSTR ALTER COLUMN DESC SET DATA TYPE VARCHAR(600);
REORG TABLE PROJECT_MISSIONMSTR;
--项目管理.问题表
ALTER TABLE PROJECT_PROBLEMMSTR ALTER COLUMN DESC SET DATA TYPE VARCHAR(600);
ALTER TABLE PROJECT_PROBLEMMSTR ALTER COLUMN PROPOSAL SET DATA TYPE VARCHAR(600);
REORG TABLE PROJECT_PROBLEMMSTR;
--项目管理.项目表
ALTER TABLE PROJECTMSTR ALTER COLUMN NAME SET DATA TYPE VARCHAR(150);
ALTER TABLE PROJECTMSTR ALTER COLUMN DESC SET DATA TYPE VARCHAR(600);
REORG TABLE PROJECTMSTR;
--项目管理.附件表
ALTER TABLE PROJECT_ATTACHMENT ALTER COLUMN NAME SET DATA TYPE VARCHAR(150);
REORG TABLE PROJECT_ATTACHMENT;
--项目管理.干系人表
ALTER TABLE PROJECT_MEMBERMSTR ALTER COLUMN ROLE SET DATA TYPE VARCHAR(150);
ALTER TABLE PROJECT_MEMBERMSTR ALTER COLUMN REMARKS SET DATA TYPE VARCHAR(600);
REORG TABLE PROJECT_MEMBERMSTR;

--更改NOTIFICATIONMSTR表TITLE字段类型
ALTER TABLE NOTIFICATIONMSTR ALTER COLUMN TITLE SET DATA TYPE VARCHAR(500);
REORG TABLE NOTIFICATIONMSTR;

--更新岗位表字段
ALTER TABLE POSITION ALTER COLUMN NAME SET DATA TYPE VARCHAR(60);
ALTER TABLE POSITION ALTER COLUMN DESC SET DATA TYPE VARCHAR(600);
ALTER TABLE POSITION ALTER COLUMN CODE SET DATA TYPE VARCHAR(300);
REORG TABLE POSITION;
--更新角色表字段
ALTER TABLE ROLEMSTR ALTER COLUMN NAME SET DATA TYPE VARCHAR(60);
ALTER TABLE ROLEMSTR ALTER COLUMN DESC SET DATA TYPE VARCHAR(150);
ALTER TABLE ROLEMSTR ALTER COLUMN CODE SET DATA TYPE VARCHAR(150);
REORG TABLE ROLEMSTR;
--房产明细
ALTER TABLE COMPANY_ESTATE ALTER COLUMN ESTATE_NO SET DATA TYPE VARCHAR(60);
ALTER TABLE COMPANY_ESTATE ALTER COLUMN AREA DROP NOT NULL;
ALTER TABLE COMPANY_ESTATE ALTER COLUMN LAND_NO SET DATA TYPE VARCHAR(60);
REORG TABLE COMPANY_ESTATE;
--税种税率信息
ALTER TABLE COMPANY_TAX_TYPE_RATIO ALTER COLUMN TAX_BASIS SET DATA TYPE VARCHAR(150);
ALTER TABLE COMPANY_TAX_TYPE_RATIO ALTER COLUMN REMARKS SET DATA TYPE VARCHAR(150);
REORG TABLE COMPANY_TAX_TYPE_RATIO;
--年度纳税金额信息
ALTER TABLE COMPANY_ANNUAL_TAX_PAY ALTER COLUMN REMARKS SET DATA TYPE VARCHAR(300);
REORG TABLE COMPANY_ANNUAL_TAX_PAY;

--分支机构
ALTER TABLE COMPANY_BRANCH ALTER COLUMN NAME SET DATA TYPE VARCHAR(310);
ALTER TABLE COMPANY_BRANCH ALTER COLUMN location SET DATA TYPE VARCHAR(1510);
ALTER TABLE COMPANY_BRANCH ALTER COLUMN BUSINESS_SCOPE SET DATA TYPE VARCHAR(1510);
ALTER TABLE COMPANY_BRANCH ALTER COLUMN remakrs SET DATA TYPE VARCHAR(1510);
REORG TABLE COMPANY_BRANCH;
--税收优惠
ALTER TABLE COMPANY_TAX_INCENTIVES ALTER COLUMN PREFERENTIAL_ITEM SET DATA TYPE VARCHAR(310);
ALTER TABLE COMPANY_TAX_INCENTIVES ALTER COLUMN policy SET DATA TYPE VARCHAR(3010);
ALTER TABLE COMPANY_TAX_INCENTIVES ALTER COLUMN APPROVAL_ORGAN SET DATA TYPE VARCHAR(610);
REORG TABLE COMPANY_TAX_INCENTIVES;
--财政返还
ALTER TABLE COMPANY_FINANCIAL_RETURN ALTER COLUMN itme SET DATA TYPE VARCHAR(160);
ALTER TABLE COMPANY_FINANCIAL_RETURN ALTER COLUMN RETURN_BASE SET DATA TYPE VARCHAR(310);
ALTER TABLE COMPANY_FINANCIAL_RETURN ALTER COLUMN RETURN_RATIO SET DATA TYPE VARCHAR(310);
ALTER TABLE COMPANY_FINANCIAL_RETURN ALTER COLUMN RETURN_ACCORDING SET DATA TYPE VARCHAR(310);
REORG TABLE COMPANY_FINANCIAL_RETURN;
--原料及工艺
ALTER TABLE COMPANY_MATERIAL ALTER COLUMN MAIN_MATERIAL SET DATA TYPE VARCHAR(310);
ALTER TABLE COMPANY_MATERIAL ALTER COLUMN MAIN_PRODUCT SET DATA TYPE VARCHAR(310);
ALTER TABLE COMPANY_MATERIAL ALTER COLUMN processing SET DATA TYPE VARCHAR(310);
REORG TABLE COMPANY_MATERIAL;
--土地明细
ALTER TABLE COMPANY_LAND_DETAILS ALTER COLUMN LAND_CERTIFICATE_NO SET DATA TYPE VARCHAR(160);
ALTER TABLE COMPANY_LAND_DETAILS ALTER COLUMN LAND_NAME SET DATA TYPE VARCHAR(160);
ALTER TABLE COMPANY_LAND_DETAILS ALTER COLUMN LAND_ADDRESS SET DATA TYPE VARCHAR(610);
REORG TABLE COMPANY_LAND_DETAILS;

--授权
ALTER TABLE WF_AUTHORIZMSTR ALTER COLUMN remarks SET DATA TYPE VARCHAR(1510);
REORG TABLE WF_AUTHORIZMSTR;

--精灵附件表name长度修改为200,允许输入60个汉字
alter table SMART_ATTACHMENTMSTR ALTER NAME SET DATA TYPE varchar(200);
reorg table SMART_ATTACHMENTMSTR;

reorg table USERMSTR;

UPDATE RESOURCEMSTR SET DEFUNCT_IND='N' WHERE ID = 5;

reorg table WF_SUPERVISOR;
reorg table POSITIONORG;




--消息内容表类型字段增加长度
ALTER TABLE NOTIFICATIONMSTR ALTER COLUMN TYPE SET DATA TYPE VARCHAR(100);
reorg table NOTIFICATIONMSTR;
--消息内容表内容字段增加长度
ALTER TABLE NOTIFICATIONMSTR ALTER COLUMN CONTENT SET DATA TYPE VARCHAR(2000);
reorg table NOTIFICATIONMSTR;

--精灵主表问题描述,回答描述修改为1600字节,允许输入500个汉字.
alter table SMARTMSTR ALTER QUESTION SET DATA TYPE varchar(1600);
alter table SMARTMSTR ALTER ANSWER SET DATA TYPE varchar(1600);
reorg table SMARTMSTR;

--流程实例属性表value字段修改为2000字节。
alter table WF_INSTANCEMSTR_PROPERTY ALTER VALUE SET DATA TYPE varchar(2000);
reorg table WF_INSTANCEMSTR_PROPERTY;

--流程步骤属性表value字段修改为2000字节。
alter table WF_STEPMSTR_PROPERTY ALTER VALUE SET DATA TYPE varchar(2000);
reorg table WF_STEPMSTR_PROPERTY;


--二级菜单URL地址的添加
update RESOURCEMSTR set URI = '/faces/common/user/index.xhtml' where NAME = '组织用户管理';
update RESOURCEMSTR set URI = '/faces/common/dict/index.xhtml' where NAME = '基础数据管理';
update RESOURCEMSTR set URI = '/faces/common/role/index.xhtml' where NAME = '权限管理';
update RESOURCEMSTR set URI = '/faces/system/news/index.xhtml' where NAME = '首页管理';
update RESOURCEMSTR set URI = '/faces/interaction/WizardAnswer/index.xhtml' where NAME = '税务精灵';


--增值税
--报送报表流程实例增值税属性表remarks字段修改为1600字节。
alter table REPORT_PAYABLE_TAX_VAT ALTER REMARKS SET DATA TYPE varchar(1600);
reorg table REPORT_PAYABLE_TAX_VAT;

--其他税种
--报送报表流程实例其他税种属性表remarks字段修改为1600字节。
alter table REPORT_PAYABLE_TAX_OTHERS ALTER REMARKS SET DATA TYPE varchar(1600);
reorg table REPORT_PAYABLE_TAX_OTHERS;

--房产税
--报送报表流程实例房产税属性表remarks字段修改为1600字节。
alter table REPORT_PAYABLE_TAX_ESTATE ALTER REMARKS SET DATA TYPE varchar(1600);
reorg table REPORT_PAYABLE_TAX_ESTATE;

--土地使用税
--报送报表流程实例土地使用税属性表remarks字段修改为1600字节。
alter table REPORT_PAYABLE_TAX_LAND ALTER REMARKS SET DATA TYPE varchar(1600);
reorg table REPORT_PAYABLE_TAX_LAND;

--补充资料
--报送报表流程实例补充资料属性表remarks字段修改为1600字节。
alter table REPORT_PAYABLE_TAX_ADDED_MATERIAL ALTER REMARKS SET DATA TYPE varchar(1600);
reorg table REPORT_PAYABLE_TAX_ADDED_MATERIAL;

--给WF_INSTANCEMSTR表中增加描述字段REMARKS
Alter table WF_INSTANCEMSTR add COLUMN REMARKS VARCHAR(2000);
reorg table WF_INSTANCEMSTR;

--给WF_INSTANCEMSTR表中增加“重要程度”
Alter table WF_INSTANCEMSTR add COLUMN IMPORTANCE VARCHAR(200);
reorg table WF_INSTANCEMSTR;

--给WF_INSTANCEMSTR表中增加“紧急程度”
Alter table WF_INSTANCEMSTR add COLUMN URGENCY VARCHAR(200);
reorg table WF_INSTANCEMSTR;

--RSS表LINK，TITLE字段修改为1000字节。
alter table RSS ALTER LINK SET DATA TYPE varchar(1000);
alter table RSS ALTER TITLE SET DATA TYPE varchar(1000);
reorg table RSS;

--定时邮件的需要
--给定时发送邮件 创建一张表JOB_INFO
CREATE TABLE JOB_INFO(
    ID                  BIGINT          NOT NULL,
    JOB_ID              VARCHAR(100)    NOT NULL,
    JOB_NAME            VARCHAR(200)    NOT NULL,
    JOB_CLASS_NAME      VARCHAR(200)    NOT NULL,
    DESCRIPTION         VARCHAR(500)    NOT NULL,
    START_DATE          TIMESTAMP,
    END_DATE            TIMESTAMP,
    SECOND              VARCHAR(10),
    MINUTE              VARCHAR(10),
    HOUR                VARCHAR(10),
    DAY_OF_WEEK         VARCHAR(10),
    DAY_OF_MONTH        VARCHAR(10),
    MONTH               VARCHAR(10),
    YEAR                VARCHAR(10),
    NEXT_TIMEOUT        TIMESTAMP,
    CREATED_BY          VARCHAR(50)     NOT NULL,
    CREATED_DATETIME    TIMESTAMP       NOT NULL,
    UPDATED_BY          VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME    TIMESTAMP       NOT NULL,
    DEFUNCT_IND         VARCHAR(1)      NOT NULL,
    CONSTRAINT PK128 PRIMARY KEY (ID)
)
;

--定时邮件的需要
--在WF_MAIL_CONFIG表中添加JOB_IND和JOB_ID
alter table WF_MAIL_CONFIG add JOB_IND CHAR(1);
alter table WF_MAIL_CONFIG add JOB_ID VARCHAR(100);
reorg table WF_MAIL_CONFIG;

--2013-1-8 11.48
--COMPANY_TAX_TYPE_RATIO表备注字段增加到300个字符
alter table COMPANY_TAX_TYPE_RATIO ALTER REMARKS SET DATA TYPE varchar(300);
reorg table COMPANY_TAX_TYPE_RATIO;

--2013-1-24 9:45
--COMPANYMSTR表增加REGION，PROVINCE字段
alter table COMPANYMSTR add REGION varchar(50);
alter table COMPANYMSTR add PROVINCE varchar(50);
reorg table COMPANYMSTR;








--王旋：2013/3/24  稽查，转让定价，反避税
--ER/Studio 7.0 SQL Code Generation
-- Company :      WCS
-- Project :      税务平台ER关系图.DM1
-- 

DROP TABLE INVS_ANTI_AVOIDANCE
;
DROP TABLE INVS_ANTI_AVOIDANCE_HISTORY
;
DROP TABLE INVS_ANTI_RESULT
;
DROP TABLE INVS_ANTI_RESULT_HISTORY
;
DROP TABLE INVS_INSPECTATION
;
DROP TABLE INVS_INSPECTATION_HISTORY
;
DROP TABLE INVS_INSPECTATION_RESULT
;
DROP TABLE INVS_INSPECTATION_RESULT_HISTORY
;
DROP TABLE INVS_TRANSFER_PRICE
;
DROP TABLE INVS_TRANSFER_PRICE_HISTORY
;
DROP TABLE INVS_VERIFY_TRANS_TYPE
;
DROP TABLE INVS_VERIFY_TRANS_TYPE_HISTORY
;
-- 
-- TABLE: INVS_ANTI_AVOIDANCE 
--

CREATE TABLE INVS_ANTI_AVOIDANCE(
    ID                        BIGINT            NOT NULL,
    WF_NO                     VARCHAR(50),
    COMPANYMSTR_ID            BIGINT            NOT NULL,
    COMPANY_NAME              VARCHAR(200)      NOT NULL,
    SPONSOR_ORG               VARCHAR(200),
    IMPLEMENT_ORG             VARCHAR(200),
    CAUSE                     VARCHAR(2000),
    INVEST_TYPE               VARCHAR(100),
    TAX_TYPES                 VARCHAR(1000)     NOT NULL,
    MISSION_START_DATETIME    TIMESTAMP         NOT NULL,
    MISSION_END_DATETIME      TIMESTAMP         NOT NULL,
    INVEST_START_DATETIME     TIMESTAMP,
    INVEST_END_DATETIME       TIMESTAMP,
    METHOD                    VARCHAR(2000),
    DOUBT                     VARCHAR(2000),
    RISK_ACCOUNT              DECIMAL(18, 2),
    DEAL_WITH                 VARCHAR(2000),
    PHASE_REMARKS             VARCHAR(2000),
    CONCLUSION                VARCHAR(2000),
    TRACE_START_DATETIME      TIMESTAMP,
    TRACE_END_DATETIME        TIMESTAMP,
    CONTACT                   VARCHAR(50),
    CONTACT_NUM               VARCHAR(50),
    CREATED_BY                VARCHAR(50)       NOT NULL,
    CREATED_DATETIME          TIMESTAMP         NOT NULL,
    UPDATED_BY                VARCHAR(50)       NOT NULL,
    UPDATED_DATETIME          TIMESTAMP         NOT NULL,
    DEFUNCT_IND               CHAR(1)           NOT NULL,
    CONSTRAINT PK96 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: INVS_ANTI_AVOIDANCE_HISTORY 
--

CREATE TABLE INVS_ANTI_AVOIDANCE_HISTORY(
    ID                        BIGINT            NOT NULL,
    WF_NO                     VARCHAR(50),
    COMPANYMSTR_ID            BIGINT            NOT NULL,
    INVS_ANTI_AVOIDANCE_ID    BIGINT            NOT NULL,
    COMPANY_NAME              VARCHAR(200)      NOT NULL,
    SPONSOR_ORG               VARCHAR(200),
    IMPLEMENT_ORG             VARCHAR(200),
    CAUSE                     VARCHAR(2000),
    INVEST_TYPE               VARCHAR(100),
    TAX_TYPES                 VARCHAR(1000)     NOT NULL,
    MISSION_START_DATETIME    TIMESTAMP         NOT NULL,
    MISSION_END_DATETIME      TIMESTAMP         NOT NULL,
    INVEST_START_DATETIME     TIMESTAMP,
    INVEST_END_DATETIME       TIMESTAMP,
    METHOD                    VARCHAR(2000),
    DOUBT                     VARCHAR(2000),
    RISK_ACCOUNT              DECIMAL(18, 2),
    DEAL_WITH                 VARCHAR(2000),
    PHASE_REMARKS             VARCHAR(2000),
    CONCLUSION                VARCHAR(2000),
    TRACE_START_DATETIME      TIMESTAMP,
    TRACE_END_DATETIME        TIMESTAMP,
    CONTACT                   VARCHAR(50),
    CONTACT_NUM               VARCHAR(50),
    OPERATE_IND               CHAR(1)           NOT NULL,
    CREATED_BY                VARCHAR(50)       NOT NULL,
    CREATED_DATETIME          TIMESTAMP         NOT NULL,
    UPDATED_BY                VARCHAR(50)       NOT NULL,
    UPDATED_DATETIME          TIMESTAMP         NOT NULL,
    DEFUNCT_IND               CHAR(1)           NOT NULL,
    CONSTRAINT PK96_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: INVS_ANTI_RESULT 
--

CREATE TABLE INVS_ANTI_RESULT(
    ID                        BIGINT            NOT NULL,
    INVS_ANTI_AVOIDANCE_ID    BIGINT,
    VAT                       DECIMAL(18, 2),
    CIT                       DECIMAL(18, 2),
    ADD_INTEREST              DECIMAL(18, 2),
    ADD_FINE                  DECIMAL(18, 2),
    REDUCED_LOSS              DECIMAL(18, 2),
    CREATED_BY                VARCHAR(50)       NOT NULL,
    CREATED_DATETIME          TIMESTAMP         NOT NULL,
    UPDATED_BY                VARCHAR(50)       NOT NULL,
    UPDATED_DATETIME          TIMESTAMP         NOT NULL,
    DEFUNCT_IND               CHAR(1)           NOT NULL,
    CONSTRAINT PK97_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: INVS_ANTI_RESULT_HISTORY 
--

CREATE TABLE INVS_ANTI_RESULT_HISTORY(
    ID                                BIGINT            NOT NULL,
    INVS_ANTI_AVOIDANCE_HISTORY_ID    BIGINT,
    VAT                               DECIMAL(18, 2),
    CIT                               DECIMAL(18, 2),
    ADD_INTEREST                      DECIMAL(18, 2),
    ADD_FINE                          DECIMAL(18, 2),
    REDUCED_LOSS                      DECIMAL(18, 2),
    OPERATE_IND                       CHAR(1)           NOT NULL,
    CREATED_BY                        VARCHAR(50)       NOT NULL,
    CREATED_DATETIME                  TIMESTAMP         NOT NULL,
    UPDATED_BY                        VARCHAR(50)       NOT NULL,
    UPDATED_DATETIME                  TIMESTAMP         NOT NULL,
    DEFUNCT_IND                       CHAR(1)           NOT NULL,
    CONSTRAINT PK97_1_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: INVS_INSPECTATION 
--

CREATE TABLE INVS_INSPECTATION(
    ID                        BIGINT            NOT NULL,
    WF_NO                     VARCHAR(50),
    COMPANYMSTR_ID            BIGINT            NOT NULL,
    COMPANY_NAME              VARCHAR(200),
    INSPECT_ORG               VARCHAR(50),
    MISSION_START_DATETIME    TIMESTAMP,
    MISSION_END_DATETIME      TIMESTAMP,
    INSPECT_START_DATETIME    TIMESTAMP,
    INSPECT_END_DATETIME      TIMESTAMP,
    INSPECT_TYPE              VARCHAR(100),
    TAX_TYPES                 VARCHAR(1000),
    CONTACT                   VARCHAR(50),
    CONTACT_NUM               VARCHAR(50),
    MAIN_PROBLEM_DESC         VARCHAR(2000),
    RECTIFICATION_PLAN        VARCHAR(2000),
    RECTIFICATION_RESULT      VARCHAR(2000),
    TOTAL_PENALTY             DECIMAL(18, 2)    NOT NULL,
    TOTAL_FINE                DECIMAL(18, 2)    NOT NULL,
    CREATED_BY                VARCHAR(50)       NOT NULL,
    CREATED_DATETIME          TIMESTAMP         NOT NULL,
    UPDATED_DATETIME          TIMESTAMP         NOT NULL,
    UPDATED_BY                VARCHAR(50)       NOT NULL,
    DEFUNCT_IND               CHAR(1)           NOT NULL,
    CONSTRAINT PK92_2 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: INVS_INSPECTATION_HISTORY 
--

CREATE TABLE INVS_INSPECTATION_HISTORY(
    ID                        BIGINT            NOT NULL,
    WF_NO                     VARCHAR(50),
    INVS_INSPECTATION_ID      BIGINT            NOT NULL,
    COMPANYMSTR_ID            BIGINT            NOT NULL,
    COMPANY_NAME              VARCHAR(200)      NOT NULL,
    INSPECT_ORG               VARCHAR(50)       NOT NULL,
    MISSION_START_DATETIME    TIMESTAMP         NOT NULL,
    MISSION_END_DATETIME      TIMESTAMP         NOT NULL,
    INSPECT_START_DATETIME    TIMESTAMP         NOT NULL,
    INSPECT_END_DATETIME      TIMESTAMP         NOT NULL,
    INSPECT_TYPE              VARCHAR(1000)     NOT NULL,
    TAX_TYPES                 VARCHAR(1000)     NOT NULL,
    CONTACT                   VARCHAR(50),
    CONTACT_NUM               VARCHAR(50),
    MAIN_PROBLEM_DESC         VARCHAR(2000)     NOT NULL,
    RECTIFICATION_PLAN        VARCHAR(2000)     NOT NULL,
    RECTIFICATION_RESULT      VARCHAR(2000)     NOT NULL,
    TOTAL_PENALTY             DECIMAL(18, 2)    NOT NULL,
    TOTAL_FINE                DECIMAL(18, 2)    NOT NULL,
    OPERATE_IND               CHAR(1),
    CREATED_BY                VARCHAR(50)       NOT NULL,
    CREATED_DATETIME          TIMESTAMP         NOT NULL,
    UPDATED_BY                VARCHAR(50)       NOT NULL,
    UPDATED_DATETIME          TIMESTAMP         NOT NULL,
    DEFUNCT_IND               CHAR(1)           NOT NULL,
    CONSTRAINT PK92_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: INVS_INSPECTATION_RESULT 
--

CREATE TABLE INVS_INSPECTATION_RESULT(
    ID                      BIGINT            NOT NULL,
    INVS_INSPECTATION_ID    BIGINT,
    TAX_TYPE                VARCHAR(100),
    OVERDUE_TAX             DECIMAL(18, 2),
    PENALTY                 DECIMAL(18, 2),
    INPUT_TAX_TURNS_OUT     DECIMAL(18, 2),
    REDUCTION_PREV_LOSS     DECIMAL(18, 2),
    FINE                    DECIMAL(18, 2),
    SITUATION_REMARKS       VARCHAR(2000),
    CREATED_BY              VARCHAR(50)       NOT NULL,
    CREATED_DATETIME        TIMESTAMP         NOT NULL,
    UPDATED_BY              VARCHAR(50)       NOT NULL,
    UPDATED_DATETIME        TIMESTAMP         NOT NULL,
    DEFUNCT_IND             CHAR(1)           NOT NULL,
    CONSTRAINT PK93 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: INVS_INSPECTATION_RESULT_HISTORY 
--

CREATE TABLE INVS_INSPECTATION_RESULT_HISTORY(
    ID                              BIGINT            NOT NULL,
    INVS_INSPECTATION_HISTORY_ID    BIGINT,
    TAX_TYPE                        VARCHAR(100)      NOT NULL,
    OVERDUE_TAX                     DECIMAL(18, 2)    NOT NULL,
    PENALTY                         DECIMAL(18, 2)    NOT NULL,
    INPUT_TAX_TURNS_OUT             DECIMAL(18, 2)    NOT NULL,
    REDUCTION_PREV_LOSS             DECIMAL(18, 2)    NOT NULL,
    FINE                            DECIMAL(18, 2)    NOT NULL,
    SITUATION_REMARKS               VARCHAR(2000)     NOT NULL,
    OPERATE_IND                     CHAR(1)           NOT NULL,
    UPDATED_BY                      VARCHAR(50)       NOT NULL,
    UPDATED_DATETIME                TIMESTAMP         NOT NULL,
    CREATED_BY                      VARCHAR(50)       NOT NULL,
    CREATED_DATETIME                TIMESTAMP         NOT NULL,
    DEFUNCT_IND                     CHAR(1)           NOT NULL,
    CONSTRAINT PK93_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: INVS_TRANSFER_PRICE 
--

CREATE TABLE INVS_TRANSFER_PRICE(
    ID                        BIGINT           NOT NULL,
    COMPANYMSTR_ID            BIGINT           NOT NULL,
    COMPANY_NAME              VARCHAR(200)     NOT NULL,
    DECADE                    TIMESTAMP,
    ASSO_DEBT_EQUITY_RATIO    FLOAT,
    PREPARE_DOC_IND           CHAR(1),
    SUBMIT_DOC_IND            CHAR(1),
    DOC_SUBMIT_DATETIME       TIMESTAMP,
    REMARKS                   VARCHAR(2000),
    CREATED_BY                VARCHAR(50)      NOT NULL,
    CREATED_DATETIME          TIMESTAMP        NOT NULL,
    UPDATED_BY                VARCHAR(50)      NOT NULL,
    UPDATED_DATETIME          TIMESTAMP        NOT NULL,
    DEFUNCT_IND               CHAR(1)          NOT NULL,
    CONSTRAINT PK94 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: INVS_TRANSFER_PRICE_HISTORY 
--

CREATE TABLE INVS_TRANSFER_PRICE_HISTORY(
    ID                        BIGINT           NOT NULL,
    INVS_TRANSFER_PRICE_ID    BIGINT           NOT NULL,
    COMPANYMSTR_ID            BIGINT           NOT NULL,
    COMPANY_NAME              VARCHAR(200)     NOT NULL,
    DECADE                    TIMESTAMP,
    ASSO_DEBT_EQUITY_RATIO    FLOAT,
    PREPARE_DOC_IND           CHAR(1),
    SUBMIT_DOC_IND            CHAR(1),
    DOC_SUBMIT_DATETIME       TIMESTAMP,
    REMARKS                   VARCHAR(2000),
    OPERATE_IND               CHAR(1)          NOT NULL,
    CREATED_BY                VARCHAR(50)      NOT NULL,
    CREATED_DATETIME          TIMESTAMP        NOT NULL,
    UPDATED_BY                VARCHAR(50)      NOT NULL,
    UPDATED_DATETIME          TIMESTAMP        NOT NULL,
    DEFUNCT_IND               CHAR(1)          NOT NULL,
    CONSTRAINT PK94_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: INVS_VERIFY_TRANS_TYPE 
--

CREATE TABLE INVS_VERIFY_TRANS_TYPE(
    ID                        BIGINT          NOT NULL,
    INVS_TRANSFER_PRICE_ID    BIGINT,
    TRANS_TYPE                VARCHAR(100),
    VALIDATION_METHOD         VARCHAR(100),
    COMPARE_COMPANY_MEDIAN    FLOAT,
    BEFORE_ADJUST_RATIO       FLOAT,
    AFTER_ADJUST_RATIO        FLOAT,
    ADJUST_SPECIAL_REASON     VARCHAR(500),
    CREATED_BY                VARCHAR(50)     NOT NULL,
    CREATED_DATETIME          TIMESTAMP       NOT NULL,
    UPDATED_BY                VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME          TIMESTAMP       NOT NULL,
    DEFUNCT_IND               CHAR(1)         NOT NULL,
    CONSTRAINT PK95 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: INVS_VERIFY_TRANS_TYPE_HISTORY 
--

CREATE TABLE INVS_VERIFY_TRANS_TYPE_HISTORY(
    ID                                BIGINT          NOT NULL,
    INVS_TRANSFER_PRICE_HISTORY_ID    BIGINT,
    TRANS_TYPE                        VARCHAR(100),
    VALIDATION_METHOD                 VARCHAR(100),
    COMPARE_COMPANY_MEDIAN            FLOAT,
    BEFORE_ADJUST_RATIO               FLOAT,
    AFTER_ADJUST_RATIO                FLOAT,
    ADJUST_SPECIAL_REASON             VARCHAR(500),
    OPERATE_IND                       CHAR(1)         NOT NULL,
    CREATED_BY                        VARCHAR(50)     NOT NULL,
    CREATED_DATETIME                  TIMESTAMP       NOT NULL,
    UPDATED_BY                        VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME                  TIMESTAMP       NOT NULL,
    DEFUNCT_IND                       CHAR(1)         NOT NULL,
    CONSTRAINT PK95_1 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: INVS_ANTI_AVOIDANCE_HISTORY 
--

ALTER TABLE INVS_ANTI_AVOIDANCE_HISTORY ADD CONSTRAINT RefINVS_ANTI_AV153 
    FOREIGN KEY (INVS_ANTI_AVOIDANCE_ID)
    REFERENCES INVS_ANTI_AVOIDANCE(ID)
;


-- 
-- TABLE: INVS_ANTI_RESULT 
--

ALTER TABLE INVS_ANTI_RESULT ADD CONSTRAINT RefINVS_ANTI_AV151 
    FOREIGN KEY (INVS_ANTI_AVOIDANCE_ID)
    REFERENCES INVS_ANTI_AVOIDANCE(ID)
;


-- 
-- TABLE: INVS_ANTI_RESULT_HISTORY 
--

ALTER TABLE INVS_ANTI_RESULT_HISTORY ADD CONSTRAINT RefINVS_ANTI_AV152 
    FOREIGN KEY (INVS_ANTI_AVOIDANCE_HISTORY_ID)
    REFERENCES INVS_ANTI_AVOIDANCE_HISTORY(ID)
;


-- 
-- TABLE: INVS_INSPECTATION_HISTORY 
--

ALTER TABLE INVS_INSPECTATION_HISTORY ADD CONSTRAINT RefINVS_INSPECT145 
    FOREIGN KEY (INVS_INSPECTATION_ID)
    REFERENCES INVS_INSPECTATION(ID)
;


-- 
-- TABLE: INVS_INSPECTATION_RESULT 
--

ALTER TABLE INVS_INSPECTATION_RESULT ADD CONSTRAINT RefINVS_INSPECT142 
    FOREIGN KEY (INVS_INSPECTATION_ID)
    REFERENCES INVS_INSPECTATION(ID)
;


-- 
-- TABLE: INVS_INSPECTATION_RESULT_HISTORY 
--

ALTER TABLE INVS_INSPECTATION_RESULT_HISTORY ADD CONSTRAINT RefINVS_INSPECT143 
    FOREIGN KEY (INVS_INSPECTATION_HISTORY_ID)
    REFERENCES INVS_INSPECTATION_HISTORY(ID)
;


-- 
-- TABLE: INVS_TRANSFER_PRICE_HISTORY 
--

ALTER TABLE INVS_TRANSFER_PRICE_HISTORY ADD CONSTRAINT RefINVS_TRANSFE149 
    FOREIGN KEY (INVS_TRANSFER_PRICE_ID)
    REFERENCES INVS_TRANSFER_PRICE(ID)
;


-- 
-- TABLE: INVS_VERIFY_TRANS_TYPE 
--

ALTER TABLE INVS_VERIFY_TRANS_TYPE ADD CONSTRAINT RefINVS_TRANSFE147 
    FOREIGN KEY (INVS_TRANSFER_PRICE_ID)
    REFERENCES INVS_TRANSFER_PRICE(ID)
;


-- 
-- TABLE: INVS_VERIFY_TRANS_TYPE_HISTORY 
--

ALTER TABLE INVS_VERIFY_TRANS_TYPE_HISTORY ADD CONSTRAINT RefINVS_TRANSFE148 
    FOREIGN KEY (INVS_TRANSFER_PRICE_HISTORY_ID)
    REFERENCES INVS_TRANSFER_PRICE_HISTORY(ID)
;


--王旋：2013/3/25
--稽查，转让定价，反避税
--删除稽查主表WF_NO
alter table INVS_INSPECTATION drop column WF_NO;
reorg table INVS_INSPECTATION;

--稽查，转让定价，反避税
--删除稽查历史WF_NO
alter table INVS_INSPECTATION_HISTORY drop column WF_NO;
reorg table INVS_INSPECTATION_HISTORY;

--稽查，转让定价，反避税
--删除反避税主表WF_NO
alter table INVS_ANTI_AVOIDANCE drop column WF_NO;
reorg table INVS_ANTI_AVOIDANCE;

--稽查，转让定价，反避税
--删除反避税历史WF_NO
alter table INVS_ANTI_AVOIDANCE_HISTORY drop column WF_NO;
reorg table INVS_ANTI_AVOIDANCE_HISTORY;

--稽查，转让定价，反避税
--添加稽查主表WF_ID
alter table INVS_INSPECTATION add WF_ID BIGINT;
reorg table INVS_INSPECTATION;

--稽查，转让定价，反避税
--添加稽查历史WF_ID
alter table INVS_INSPECTATION_HISTORY add WF_ID BIGINT;
reorg table INVS_INSPECTATION_HISTORY;

--稽查，转让定价，反避税
--添加反避税主表WF_ID
alter table INVS_ANTI_AVOIDANCE add WF_ID BIGINT;
reorg table INVS_ANTI_AVOIDANCE;

--稽查，转让定价，反避税
--添加反避税历史WF_ID
alter table INVS_ANTI_AVOIDANCE_HISTORY add WF_ID BIGINT;
reorg table INVS_ANTI_AVOIDANCE_HISTORY;

--王旋：2013/3/27
--稽查，转让定价，反避税
--修改稽查历史表操作标识的长度
alter table INVS_INSPECTATION_HISTORY ALTER OPERATE_IND SET DATA TYPE varchar(100);
reorg table INVS_INSPECTATION_HISTORY;

--稽查，转让定价，反避税
--修改稽查历史结果表操作标识的长度
alter table INVS_INSPECTATION_RESULT_HISTORY ALTER OPERATE_IND SET DATA TYPE varchar(100);
reorg table INVS_INSPECTATION_RESULT_HISTORY;

--稽查，转让定价，反避税
--修改反避税历史表操作标识的长度
alter table INVS_ANTI_AVOIDANCE_HISTORY ALTER OPERATE_IND SET DATA TYPE varchar(100);
reorg table INVS_ANTI_AVOIDANCE_HISTORY;

--稽查，转让定价，反避税
--修改反避税历史结果表操作标识的长度
alter table INVS_ANTI_RESULT_HISTORY ALTER OPERATE_IND SET DATA TYPE varchar(100);
reorg table INVS_ANTI_RESULT_HISTORY;

--稽查，转让定价，反避税
--修改转让定价历史表操作标识的长度
alter table INVS_TRANSFER_PRICE_HISTORY ALTER OPERATE_IND SET DATA TYPE varchar(100);
reorg table INVS_TRANSFER_PRICE_HISTORY;

--稽查，转让定价，反避税
--修改转让定价历史结果表操作标识的长度
alter table INVS_VERIFY_TRANS_TYPE_HISTORY ALTER OPERATE_IND SET DATA TYPE varchar(100);
reorg table INVS_VERIFY_TRANS_TYPE_HISTORY;

--Yuan 2013/4/22
--转让定价，交易类型明细，调整的特殊因素字段长度为1000
alter table INVS_VERIFY_TRANS_TYPE ALTER ADJUST_SPECIAL_REASON SET DATA TYPE varchar(1000);
reorg table INVS_VERIFY_TRANS_TYPE;
--转让定价，交易类型明细历史表，调整的特殊因素字段长度为1000
alter table INVS_VERIFY_TRANS_TYPE_HISTORY ALTER ADJUST_SPECIAL_REASON SET DATA TYPE varchar(1000);
reorg table INVS_VERIFY_TRANS_TYPE_HISTORY;

--2013/4/22
--将任务结束时间NOT NULL改为NULL
--删除稽查历史表的任务结束时间
alter table INVS_INSPECTATION_HISTORY drop column MISSION_END_DATETIME;
reorg table INVS_INSPECTATION_HISTORY;

--2013/4/22
--将任务结束时间NOT NULL改为NULL
----添加稽查历史表的任务结束时间
ALTER TABLE INVS_INSPECTATION_HISTORY ADD COLUMN MISSION_END_DATETIME TIMESTAMP;
reorg table INVS_INSPECTATION_HISTORY;

--2013/4/22
--将任务结束时间NOT NULL改为NULL
--删除反避税历史表的任务结束时间
alter table INVS_ANTI_AVOIDANCE_HISTORY drop column MISSION_END_DATETIME;
reorg table INVS_ANTI_AVOIDANCE_HISTORY;

--2013/4/22
--将任务结束时间NOT NULL改为NULL
--添加反避税历史表的任务结束时间
ALTER TABLE INVS_ANTI_AVOIDANCE_HISTORY ADD COLUMN MISSION_END_DATETIME TIMESTAMP;
reorg table INVS_ANTI_AVOIDANCE_HISTORY;

--公司表中曾加公司代码code字段
ALTER TABLE COMPANYMSTR ADD COLUMN CODE VARCHAR(50);
REORG TABLE COMPANYMSTR;


update WF_INSTANCEMSTR set IMPORTANCE = 'TIH.TAX.WORKFLOWIMPORTANCE.1'  where IMPORTANCE is null;
update WF_INSTANCEMSTR set URGENCY = 'TIH.TAX.WORKFLOWURGENCY.1'  where URGENCY is null;
reorg table WF_INSTANCEMSTR;

--2013/4/24
--将稽查结束条件NOT NULL改为NULL
--删除稽查字段
alter table INVS_INSPECTATION_HISTORY drop column MISSION_START_DATETIME;
alter table INVS_INSPECTATION_HISTORY drop column INSPECT_START_DATETIME;
alter table INVS_INSPECTATION_HISTORY drop column INSPECT_END_DATETIME;
alter table INVS_INSPECTATION_HISTORY drop column INSPECT_TYPE;
alter table INVS_INSPECTATION_HISTORY drop column TAX_TYPES;
alter table INVS_INSPECTATION_HISTORY drop column MAIN_PROBLEM_DESC;
alter table INVS_INSPECTATION_HISTORY drop column RECTIFICATION_PLAN;
alter table INVS_INSPECTATION_HISTORY drop column RECTIFICATION_RESULT;
reorg table INVS_INSPECTATION_HISTORY;

--将稽查结束条件NOT NULL改为NULL
--添加除稽查字段
alter TABLE INVS_INSPECTATION_HISTORY add COLUMN MISSION_START_DATETIME TIMESTAMP;
alter TABLE INVS_INSPECTATION_HISTORY add COLUMN INSPECT_START_DATETIME TIMESTAMP;
alter TABLE INVS_INSPECTATION_HISTORY add COLUMN INSPECT_END_DATETIME TIMESTAMP;
alter table INVS_INSPECTATION_HISTORY add COLUMN INSPECT_TYPE VARCHAR(1000);
alter table INVS_INSPECTATION_HISTORY add COLUMN TAX_TYPES VARCHAR(1000);
alter table INVS_INSPECTATION_HISTORY add COLUMN MAIN_PROBLEM_DESC VARCHAR(2000);
alter table INVS_INSPECTATION_HISTORY add COLUMN RECTIFICATION_PLAN VARCHAR(2000);
alter table INVS_INSPECTATION_HISTORY add COLUMN RECTIFICATION_RESULT VARCHAR(2000);
reorg table INVS_INSPECTATION_HISTORY;

--将反避税结束条件NOT NULL改为NULL
--删除反避税字段
alter table INVS_ANTI_AVOIDANCE drop column TAX_TYPES;
alter table INVS_ANTI_AVOIDANCE drop column MISSION_START_DATETIME;
alter table INVS_ANTI_AVOIDANCE drop column MISSION_END_DATETIME;
reorg table INVS_ANTI_AVOIDANCE;

alter table INVS_ANTI_AVOIDANCE_HISTORY drop column MISSION_START_DATETIME;
alter table INVS_ANTI_AVOIDANCE_HISTORY drop column TAX_TYPES;
reorg table INVS_ANTI_AVOIDANCE_HISTORY;

--将反避税结束条件NOT NULL改为NULL
--添加反避税字段
alter table INVS_ANTI_AVOIDANCE add COLUMN MISSION_START_DATETIME TIMESTAMP;
alter table INVS_ANTI_AVOIDANCE add COLUMN MISSION_END_DATETIME TIMESTAMP;
alter table INVS_ANTI_AVOIDANCE add COLUMN TAX_TYPES VARCHAR(1000);
reorg table INVS_ANTI_AVOIDANCE;

alter table INVS_ANTI_AVOIDANCE_HISTORY add COLUMN MISSION_START_DATETIME TIMESTAMP;
alter table INVS_ANTI_AVOIDANCE_HISTORY add COLUMN TAX_TYPES VARCHAR(1000);
reorg table INVS_ANTI_AVOIDANCE_HISTORY;

--给WF_SUPERVISOR添加公司ID
alter table WF_SUPERVISOR add COMPANYMSTR_ID BIGINT;
reorg table WF_SUPERVISOR;

--删除公司管理中原料及工艺表中的所有数据
delete from COMPANY_MATERIAL;
reorg table COMPANY_MATERIAL;


DROP TABLE NOTIFICATION_EXT
;
-- 
-- TABLE: NOTIFICATION_EXT 
--

CREATE TABLE NOTIFICATION_EXT(
    ID                     BIGINT         NOT NULL,
    NOTIFICATIONMSTR_ID    BIGINT         NOT NULL,
    TABLE_NAME             VARCHAR(50)    NOT NULL,
    TABLE_COLUMN           VARCHAR(50)    NOT NULL,
    TABLE_ID               BIGINT         NOT NULL,
    CREATED_BY             VARCHAR(50)    NOT NULL,
    UPDATED_BY             VARCHAR(50)    NOT NULL,
    UPDATED_DATETIME       TIMESTAMP      NOT NULL,
    CREATED_DATETIME       TIMESTAMP      NOT NULL,
    DEFUNCT_IND            CHAR(1)        NOT NULL,
    CONSTRAINT PK145 PRIMARY KEY (ID)
)
;

--修改最新税率（TAX_RATE）字段的数据类型
--先添加一个新字段NEW_TAX_RATE
alter table COMPANY_ANNUAL_TAX_PAY add column NEW_TAX_RATE VARCHAR(300);
--再把TAX_RATE字段的数据拷贝到NEW_TAX_RATE字段中
update COMPANY_ANNUAL_TAX_PAY set NEW_TAX_RATE = TAX_RATE;
--最后是删除TAX_RATE字段
alter table COMPANY_ANNUAL_TAX_PAY drop column TAX_RATE;
reorg table COMPANY_ANNUAL_TAX_PAY;

-- Bug #11837
update Dict d set d.CODE_CAT = 'TIH.TAX',d.CODE_KEY = 'REPORT' where d.id = 298;
update Dict d set d.CODE_CAT = 'TIH.TAX.REPORT',d.CODE_KEY = '1' where d.id = 299;
update Dict d set d.CODE_CAT = 'TIH.TAX.REPORT',d.CODE_KEY = '2' where d.id = 300;
update Dict d set d.CODE_CAT = 'TIH.TAX.REPORT',d.CODE_KEY = '3' where d.id = 301;

-- Bug #14544
UPDATE RESOURCEMSTR r SET r.NAME = '精灵设置' WHERE r.ID = 25;

INSERT INTO DICT (ID,CODE_CAT,CODE_KEY,CODE_VAL,REMARKS,SEQ_NO,SYS_IND,LANG,DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)VALUES (358,'TIH.TAX.TIMEOUTEMAIL','TYPE','超时邮件类型','',0,'Y','zh_CN','N','system','2012-03-27-13.36.47.046133','system','2012-03-27-13.36.47.046133');
INSERT INTO DICT (ID,CODE_CAT,CODE_KEY,CODE_VAL,REMARKS,SEQ_NO,SYS_IND,LANG,DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)VALUES (359,'TIH.TAX.TIMEOUTEMAIL.TYPE','1','流程超时','',1,'Y','zh_CN','N','system','2012-03-27-13.36.47.046134','system','2012-03-27-13.36.47.046134');
INSERT INTO DICT (ID,CODE_CAT,CODE_KEY,CODE_VAL,REMARKS,SEQ_NO,SYS_IND,LANG,DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)VALUES (360,'TIH.TAX.TIMEOUTEMAIL.TYPE','2','岗位超时','',2,'Y','zh_CN','N','system','2012-03-27-13.36.47.046135','system','2012-03-27-13.36.47.046135');


--
-- ER/Studio 8.0 SQL Code Generation
-- Company :      WCS
-- Project :      税务平台ER关系图.DM1
-- Author :       yuanzhencai
--
-- Date Created : Tuesday, February 11, 2014 15:48:25
-- Target DBMS : IBM DB2 UDB 8.x
--

-- 
-- TABLE: POSITION_TIMEOUT_REMIND 
--


DROP TABLE POSITION_TIMEOUT_REMIND;
DROP TABLE WF_TIMEOUT_CONFIG;
DROP TABLE WF_TIMEOUT_REMIND;

CREATE TABLE POSITION_TIMEOUT_REMIND(
    ID                      BIGINT          NOT NULL,
    WF_TIMEOUT_CONFIG_ID    BIGINT          NOT NULL,
    POSITIONE_NAME          VARCHAR(50),
    WP_TIMEOUT_DAYS         BIGINT          NOT NULL,
    WP_INTERVAL_DAYS        BIGINT          NOT NULL,
    WP_URGE_DAYS            BIGINT          NOT NULL,
    REMARKS                 VARCHAR(500),
    ENABLE_IND              CHAR(1),
    CREATED_BY              VARCHAR(50)     NOT NULL,
    CREATED_DATETIME        TIMESTAMP       NOT NULL,
    UPDATED_BY              VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME        TIMESTAMP       NOT NULL,
    DEFUNCT_IND             CHAR(1)         NOT NULL,
    CONSTRAINT PK152 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: WF_TIMEOUT_CONFIG 
--

CREATE TABLE WF_TIMEOUT_CONFIG(
    ID                      BIGINT          NOT NULL,
    JOB_ID                  VARCHAR(50),
    WF_TYPE                 VARCHAR(50)     NOT NULL,
    WF_REQUESTFORM_TYPE     VARCHAR(500)     NOT NULL,
    WF_TIMEOUT_IND          CHAR(1)         NOT NULL,
    POSITION_TIMEOUT_IND    CHAR(1)         NOT NULL,
    EFFECTIVE_DAYS          BIGINT          NOT NULL,
    MAIL_IND                CHAR(1),
    SYS_NOTICE_IND          CHAR(1),
    REMARKS                 VARCHAR(500),
    ENABLE_IND              CHAR(1)         NOT NULL,
    CREATED_BY              VARCHAR(50)     NOT NULL,
    CREATED_DATETIME        TIMESTAMP       NOT NULL,
    UPDATED_BY              VARCHAR(50)     NOT NULL,
    UPDATED_DATETIME        TIMESTAMP       NOT NULL,
    DEFUNCT_IND             CHAR(1)         NOT NULL,
    CONSTRAINT PK151 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: WF_TIMEOUT_REMIND 
--

CREATE TABLE WF_TIMEOUT_REMIND(
    ID                  BIGINT         NOT NULL,
    WF_ID               BIGINT,
    WF_COMPLETE_DATE    TIMESTAMP,
    WF_INTERVAL_DAYS    BIGINT,
    WF_URGE_DATE        TIMESTAMP,
    CREATED_BY          VARCHAR(50)    NOT NULL,
    CREATED_DATETIME    TIMESTAMP      NOT NULL,
    UPDATED_BY          VARCHAR(50)    NOT NULL,
    UPDATED_DATETIME    TIMESTAMP      NOT NULL,
    DEFUNCT_IND         CHAR(1)        NOT NULL,
    CONSTRAINT PK150 PRIMARY KEY (ID)
)
;



-- 
-- TABLE: POSITION_TIMEOUT_REMIND 
--

ALTER TABLE POSITION_TIMEOUT_REMIND ADD CONSTRAINT RefWF_TIMEOUT_C172 
    FOREIGN KEY (WF_TIMEOUT_CONFIG_ID)
    REFERENCES WF_TIMEOUT_CONFIG(ID)
;

-- Story #14852
ALTER TABLE COMPANY_ANNUAL_TAX_PAY ALTER TAX_PAY_ACCOUNT DROP NOT NULL;
CALL sysproc.admin_cmd('reorg table COMPANY_ANNUAL_TAX_PAY');

-- Story #14906
alter table COMPANYMSTR ALTER DESC SET DATA TYPE varchar(1500);
CALL sysproc.admin_cmd('reorg table COMPANYMSTR');

alter table COMPANY_FINANCIAL_RETURN ALTER ITME DROP NOT NULL;
alter table COMPANY_FINANCIAL_RETURN ALTER RETURN_ACCORDING SET DATA TYPE VARCHAR(1500);
alter table COMPANY_FINANCIAL_RETURN ALTER RETURN_START_DATETIME DROP NOT NULL;
alter table COMPANY_FINANCIAL_RETURN ALTER RETURN_END_DATETIME DROP NOT NULL;
alter table COMPANY_FINANCIAL_RETURN add REGISTRATION VARCHAR(500);
CALL sysproc.admin_cmd('reorg table COMPANY_FINANCIAL_RETURN');

alter table COMPANY_ANNUAL_RETURN ALTER RETURN_YEAR DROP NOT NULL;
alter table COMPANY_ANNUAL_RETURN add ACTUAL_RETURN_ACCOUNT DECIMAL(18, 2);
alter table COMPANY_ANNUAL_RETURN add BASE_RETURN_ACCOUNT DECIMAL(18, 2);
alter table COMPANY_ANNUAL_RETURN add SHOULD_RETURN_ACCOUNT DECIMAL(18, 2);
alter table COMPANY_ANNUAL_RETURN add PAYMENT_DATETIME TIMESTAMP;
alter table COMPANY_ANNUAL_RETURN add RETURN_PURPOSE VARCHAR(1500);
alter table COMPANY_ANNUAL_RETURN add REMARK VARCHAR(1500);
CALL sysproc.admin_cmd('reorg table COMPANY_ANNUAL_RETURN');

-- copy item to ACCORDING
UPDATE COMPANY_FINANCIAL_RETURN c SET c.RETURN_ACCORDING =CONCAT(CONCAT(CONCAT('项目：',VALUE(c.ITME,'')),'；'),c.RETURN_ACCORDING);
-- 单位为元
UPDATE COMPANY_ANNUAL_RETURN c SET c.RETURN_ACCOUNT = c.RETURN_ACCOUNT * 10000;

alter table COMPANY_INVESTMENT alter column PHASE SET DATA TYPE VARCHAR(200);
alter table COMPANY_INVESTMENT add column INVESTEE VARCHAR(500);
alter table COMPANY_INVESTMENT add column INVEST_ADDRESS VARCHAR(500);
alter table COMPANY_INVESTMENT add column INVESTMENT_RATIO VARCHAR(500);
CALL sysproc.admin_cmd('reorg table COMPANY_INVESTMENT');

