create table users_table
(
    userid varchar constraint users_table_pk primary key,
    user_name varchar,
    name varchar,
    sur_name varchar
);
insert into users_table(userid,user_name,name,sur_name) values('20001', 'pg2-user_name-1', 'pg2-name-1', 'pg2-sur_name-1');
insert into users_table(userid,user_name,name,sur_name) values('20002', 'pg2-user_name-2', 'pg2-name-2', 'pg2-sur_name-2');
insert into users_table(userid,user_name,name,sur_name) values('20003', 'pg2-user_name-3', 'pg2-name-3', 'pg2-sur_name-3');