
CREATE SEQUENCE SRC START WITH 500 INCREMENT BY 1 MAXVALUE 999999999 CYCLE NOCACHE;



INSERT INTO USERMSTR(ID,PID,AD_ACCOUNT,PERNR,ONBOARD_DATE,BIRTHDAY,IDENTITY_TYPE,IDTENTITY_ID,BACKGROUND_INFO,DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)
VALUES 				(1, 1,  'shenbo',  NULL, NULL,        NULL,    NULL,         NULL,        NULL,           'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');
INSERT INTO USERMSTR(ID,PID,AD_ACCOUNT,PERNR,ONBOARD_DATE,BIRTHDAY,IDENTITY_TYPE,IDTENTITY_ID,BACKGROUND_INFO,DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME)
VALUES 				(2, 1,  'wilmar_cas',  NULL, NULL,        NULL,    NULL,         NULL,        NULL,           'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');



insert into ROLEMSTR (ID, NAME, DESC, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME,CODE) 
values (1,'系统管理员', 'admin','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00','sysadmin');
insert into ROLEMSTR (ID, NAME, DESC, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME,CODE) 
values (2,'文档管理员', 'admin','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00','fileadmin');
insert into ROLEMSTR (ID, NAME, DESC, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME,CODE) 
values (3,'文档审核员', 'admin','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00','fileviewer');
insert into ROLEMSTR (ID, NAME, DESC, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME,CODE) 
values (4,'高级用户', 'admin','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00','superuser');
insert into ROLEMSTR (ID, NAME, DESC, DEFUNCT_IND,CREATED_BY,CREATED_DATETIME,UPDATED_BY,UPDATED_DATETIME,CODE) 
values (5,'一般用户', 'admin','N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00','commonuser');



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
values (14, 					'服务公司管理','system:user:company',4,7,'MENU','/tih/system/company/index.xhtml',											'N',        'admin',   '2012-01-01 00:00:00','admin','2011-01-01 00:00:00');

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


--2013-3-15 14:00
--增减报表查询的稽查信息汇总、反避税信息汇总、转让定价信息汇总页面
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND, CREATED_BY, CREATED_DATETIME, UPDATED_BY, UPDATED_DATETIME) values (39, '稽查信息汇总', 'report:check', '4', 12, 'MENU', '/faces/report/check/index.xhtml', 'N', 'admin', '2012-01-01 00:00:00', 'admin', '2011-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND, CREATED_BY, CREATED_DATETIME, UPDATED_BY, UPDATED_DATETIME) values (40, '反避税信息汇总', 'report:taxAvoidance', '5', 12, 'MENU', '/faces/report/taxAvoidance/index.xhtml', 'N', 'admin', '2012-01-01 00:00:00', 'admin', '2011-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND, CREATED_BY, CREATED_DATETIME, UPDATED_BY, UPDATED_DATETIME) values (41, '转让定价信息汇总', 'report:transferPrice', '6', 12, 'MENU', '/faces/report/transferPrice/index.xhtml', 'N', 'admin', '2012-01-01 00:00:00', 'admin', '2011-01-01 00:00:00');
--增加专项管理中的转让定价信息页面
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND, CREATED_BY, CREATED_DATETIME, UPDATED_BY, UPDATED_DATETIME) values (52, '转让定价信息', 'affair:special:transferprice', '3', 50, 'MENU', '/faces/transaction/special/transferPrice/index.xhtml', 'N', 'admin', '2012-01-01 00:00:00', 'admin', '2012-01-01 00:00:00');

insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND, CREATED_BY, CREATED_DATETIME, UPDATED_BY, UPDATED_DATETIME) values (50, '专项管理', 'affair:special', '3', 4, 'MENU', '/faces/transaction/special/inspection/index.xhtml', 'N', 'admin', '2012-01-01 00:00:00', 'admin', '2012-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND, CREATED_BY, CREATED_DATETIME, UPDATED_BY, UPDATED_DATETIME) values (51, '稽查信息', 'affair:special:inspection', '1', 50, 'MENU', '/faces/transaction/special/inspection/index.xhtml', 'N', 'admin', '2012-01-01 00:00:00', 'admin', '2012-01-01 00:00:00');
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND, CREATED_BY, CREATED_DATETIME, UPDATED_BY, UPDATED_DATETIME) values (53, '反避税信息', 'affair:special:antiavoidance', '2', 50, 'MENU', '/faces/transaction/special/antiAvoidance/index.xhtml', 'N', 'admin', '2012-01-01 00:00:00', 'admin', '2012-01-01 00:00:00');

--报表中的数据分析
insert into RESOURCEMSTR (ID, NAME, CODE, SEQ_NO, PARENT_ID, TYPE, URI, DEFUNCT_IND, CREATED_BY, CREATED_DATETIME, UPDATED_BY, UPDATED_DATETIME) values (54, '数据分析', 'report:summaryChart', '2', 5, 'MENU', '/faces/report/summaryChart/index.xhtml', 'N', 'admin', '2013-04-02 00:00:00', 'admin', '2013-04-02 00:00:00');

--2013-4-15 14:00
--移动我的公司管理
update ROLERESOURCE  set DEFUNCT_IND = 'Y'  where RESOURCEMSTR_ID = 34;
update RESOURCEMSTR  set PARENT_ID = 50,SEQ_NO = '4',CODE = 'affair:special:mycompany'  where id = 34;
