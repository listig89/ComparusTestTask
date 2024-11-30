create database ldap_db2;
use ldap_db2;

create table ldap_users
(
    ldap_user_id INT NOT NULL PRIMARY KEY
);

insert into ldap_users(ldap_user_id) values (40004);