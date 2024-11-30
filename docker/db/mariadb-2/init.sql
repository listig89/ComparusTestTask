use dockermaria2;
create table ldap_users
(
    ldap_user_id INT NOT NULL PRIMARY KEY
);

insert into ldap_users(ldap_user_id) values (60006);