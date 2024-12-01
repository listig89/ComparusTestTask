create database ldap_db2;
use ldap_db2;

create table ldap_users
(
    ldap_userid varchar(128) NOT NULL PRIMARY KEY,
    ldap_username varchar(128),
    ldap_name varchar(128),
    ldap_surname varchar(128)
);

insert into ldap_users(ldap_userid,ldap_username,ldap_name,ldap_surname) values ('40004','mysql-ldap-username-4','mysql-ldap-name-4','mysql-ldap-surname-4');
insert into ldap_users(ldap_userid,ldap_username,ldap_name,ldap_surname) values ('40005','mysql-ldap-username-5','mysql-ldap-name-5','mysql-ldap-surname-5');
insert into ldap_users(ldap_userid,ldap_username,ldap_name,ldap_surname) values ('40006','mysql-ldap-username-6','mysql-ldap-name-6','mysql-ldap-surname-6');