create database ldap_db;
use ldap_db;

create table ldap_users
(
    ldap_user_id varchar(128) NOT NULL PRIMARY KEY,
    ldap_user_name varchar(128),
    ldap_name varchar(128),
    ldap_sur_name varchar(128)
);

insert into ldap_users(ldap_user_id,ldap_user_name,ldap_name,ldap_sur_name) values ('30003','mysql-ldap-user-name-3','mysql-ldap-name-3','mysql-ldap-sur-name-3');
insert into ldap_users(ldap_user_id,ldap_user_name,ldap_name,ldap_sur_name) values ('30004','mysql-ldap-user-name-4','mysql-ldap-name-4','mysql-ldap-sur-name-4');
insert into ldap_users(ldap_user_id,ldap_user_name,ldap_name,ldap_sur_name) values ('30005','mysql-ldap-user-name-5','mysql-ldap-name-5','mysql-ldap-sur-name-5');