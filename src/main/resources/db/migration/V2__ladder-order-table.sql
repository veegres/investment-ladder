create table "LADDER_STEP"
(
    "ID" VARCHAR(20) NOT NULL,
    "LADDER_ID" VARCHAR(20) NOT NULL,
    "ORDER_ID" VARCHAR(20) NOT NULL,
    "CREATED_ON" TIMESTAMP NOT NULL
);

alter table "LADDER_STEP" add constraint "LADDER_STEP_PK" primary key ("ID");