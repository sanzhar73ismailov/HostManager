INSERT INTO   `instrument_model` ( `id`,  `name`,  `brand`,  `type`,  `com_protocol`,  `sid_size`,  `test_code_int`) 
VALUE (  8,  'SYSMEX_XS_500_i',  'siemens',  'hematology',  'ASTM E1394-91',  15,  0);

INSERT INTO   `instrument` (  `id`,  `name`,  `model_id`,  `ip`,  `port`,   `mode`,  `active`,   `test_mode` ) 
VALUE (   8,   'sysmexXS500i',   8,   '192.168.1.205',   950,   'batch',   1,   1 );

insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','WBC','WBC','10e3/µL','1000','1001');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','RBC','RBC','10e6/µL','1000','1002');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','HGB','HGB','g/dL','1000','1003');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','HCT','HCT','%','1000','1004');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','MCV','MCV','fL','1000','1005');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','MCH','MCH','pg','1000','1006');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','MCHC','MCHC','g/dL','1000','1007');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','RDW-CV','RDW-CV','%','1000','1008');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','PLT','PLT','10e3/µL','1000','1010');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','MPV','MPV','fL','1000','1011');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','PDW','PDW','%','1000','1012');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','PCT','PCT','%','1000','1013');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','NEUT#','NEUT#','10e3/µL','1000','1014');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','LYMPH#','LYMPH#','10e3/µL','1000','1015');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','MONO#','MONO#','10e3/µL','1000','1016');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','EO#','EO#','10e3/µL','1000','1017');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','BASO#','BASO#','10e3/µL','1000','1018');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','NEUT%','NEUT%','%','1000','1020');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','LYMPH%','LYMPH%','%','1000','1021');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','MONO%','MONO%','%','1000','1022');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','EO%','EO%','%','1000','1023');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','BASO%','BASO%','%','1000','1024');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','NRBC','NRBC','10e9/L','1000','1029');
insert into test (instrument_id, code, name, units, test_order, parameter_id) values('8','RDW-SD','RDW-SD','fL','1000',null);

insert into parameter (id, name, test_id) values (5011, 'RDW-SD относительная ширина распределения эритроцитов по объёму, стандартное отклонение', null);

update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='BASO#') where id=1018;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='BASO#') where id=1018;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='EO#') where id=1017;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='LYMPH#') where id=1015;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='MONO#') where id=1016;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='NEUT#') where id=1014;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='NRBC') where id=1029;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='BASO%') where id=1024;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='EO%') where id=1023;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='LYMPH%') where id=1021;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='MONO%') where id=1022;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='NEUT%') where id=1020;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='HCT') where id=1004;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='HGB') where id=1003;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='MCH') where id=1006;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='MCHC') where id=1007;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='MCV') where id=1005;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='MPV') where id=1011;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='PCT') where id=1013;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='PDW') where id=1012;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='PLT') where id=1010;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='RBC') where id=1002;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='RDW-CV') where id=1008;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='WBC') where id=1001;
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='RDW-SD') where id=5011;

update test set parameter_id=5011 where instrument_id=8 and code='RDW-SD';

ALTER TABLE `result` MODIFY COLUMN test_code VARCHAR(100); -- old size=20
ALTER TABLE `result` MODIFY COLUMN value VARCHAR(250); -- old size=30


update test set parameter_id=1028, units="%" where instrument_id=8 and code='NRBC';
update parameter set test_id=(select t.id from test t where t.instrument_id=8 and t.code='NRBC') where id=1028;
update parameter set test_id=(select t.id from test t where t.instrument_id=1 and t.code='29') where id=1029;






