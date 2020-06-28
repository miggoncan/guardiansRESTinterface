create database db_guardians;
create user 'springuser'@'%' identified by 'MYSQL_PASSWORD';
grant select, insert, delete, update on db_guardians.* to 'springuser'@'%';
