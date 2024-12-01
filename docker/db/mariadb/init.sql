use dockermaria;
create table saml_users
(
    saml_user_id varchar(128) NOT NULL PRIMARY KEY,
    saml_user_name varchar(128),
    saml_name varchar(128),
    saml_sur_name varchar(128)
);

insert into saml_users(saml_user_id,saml_user_name,saml_name,saml_sur_name) values ('50005','maria-saml-user-name-5','maria-saml-name-5','maria-saml-sur-name-5');
insert into saml_users(saml_user_id,saml_user_name,saml_name,saml_sur_name) values ('50006','maria-saml-user-name-6','maria-saml-name-6','maria-saml-sur-name-6');
insert into saml_users(saml_user_id,saml_user_name,saml_name,saml_sur_name) values ('50007','maria-saml-user-name-7','maria-saml-name-7','maria-saml-sur-name-7');