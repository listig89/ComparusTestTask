create table users_table
(
    user_id integer
        constraint users_table_pk
            primary key
);
insert into users_table(user_id) values(10001);