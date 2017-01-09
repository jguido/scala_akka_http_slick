create table TOKENS
(
  ID CHAR(36) NOT NULL PRIMARY KEY,
  USER_ID CHAR(36) NOT NULL
);

alter table TOKENS add constraint FK_SESSION_USER foreign key(USER_ID) references USERS(ID) on update NO ACTION on delete NO ACTION;