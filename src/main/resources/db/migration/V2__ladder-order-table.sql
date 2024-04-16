create table "LADDER_ORDER"
(
    "ID" VARCHAR(20) NOT NULL,
    "LADDER_ID" VARCHAR(20) NOT NULL,
    "ORDER_ID" VARCHAR(20) NOT NULL,
    "CREATED_ON" TIMESTAMP NOT NULL
);

alter table "LADDER_ORDER" add constraint "LADDER_ORDER_PK" primary key ("ID");