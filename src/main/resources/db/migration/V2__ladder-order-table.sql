create table "LADDER_STEP"
(
    "ID" RAW(16) NOT NULL,
    "LADDER_ID" RAW(16) NOT NULL,
    "ORDER_ID" RAW(16) NOT NULL,
    "CREATED_ON" TIMESTAMP NOT NULL
);

alter table "LADDER_STEP" add constraint "LADDER_STEP_PK" primary key ("ID");