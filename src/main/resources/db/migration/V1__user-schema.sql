create table "USER"
(
    "ID"         VARCHAR(255) NOT NULL,
    "NAME"       VARCHAR(255) NOT NULL,
    "AGE"     NUMBER(10)  NOT NULL
);

alter table "USER"
    add constraint "USER_PK" primary key ("ID");