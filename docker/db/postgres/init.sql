create table users_table
(
    user_id varchar constraint users_table_pk primary key,
    username varchar,
    name varchar,
    surname varchar
);
insert into users_table(user_id,username,name,surname) values('10001', 'pg-username-1', 'pg-name-1', 'pg-surname-1');
insert into users_table(user_id,username,name,surname) values('10002', 'pg-username-2', 'pg-name-2', 'pg-surname-2');
insert into users_table(user_id,username,name,surname) values('10003', 'pg-username-3', 'pg-name-3', 'pg-surname-3');