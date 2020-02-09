TRUNCATE table DIFF_OFFSET; 

insert into DIFF_OFFSET(id, left_direction, right_direction) values (100, 'abcde', 'abcde');
insert into DIFF_OFFSET(id, left_direction, right_direction) values (101, 'abcde', 'abcdefg');
insert into DIFF_OFFSET(id, left_direction, right_direction) values (102, 'YWJjZA==', 'YWXXZAXX');
insert into DIFF_OFFSET(id, left_direction, right_direction) values (103, null, 'abc');