TRUNCATE table DIFF; 

insert into DIFF(id, left_direction, right_direction) values (100, 'abcde', 'abcde');
insert into DIFF(id, left_direction, right_direction) values (101, 'abcde', 'abcdefg');
insert into DIFF(id, left_direction, right_direction) values (102, 'abcde', 'aXcXe');
insert into DIFF(id, left_direction, right_direction) values (103, null, 'abc');