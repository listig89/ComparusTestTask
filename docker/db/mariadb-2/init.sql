use dockermaria2;
create table saml_users
(
    saml_userid varchar(128) NOT NULL PRIMARY KEY,
    saml_username varchar(128),
    saml_name varchar(128),
    saml_surname varchar(128)
);

insert into saml_users(saml_userid,saml_username,saml_name,saml_surname) values ('60006','maria-saml-username-6','maria-saml-name-6','maria-saml-surname-6');
insert into saml_users(saml_userid,saml_username,saml_name,saml_surname) values ('60007','maria-saml-username-7','maria-saml-name-7','maria-saml-surname-7');
insert into saml_users(saml_userid,saml_username,saml_name,saml_surname) values ('60008','maria-saml-username-8','maria-saml-name-8','maria-saml-surname-8');