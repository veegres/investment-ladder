create table "LADDER_STEP"
(
    "ID" UUID NOT NULL,
    "LADDER_ID" UUID NOT NULL,
    "ORDER_ID" VARCHAR(64) NOT NULL,
    "CREATED_ON" TIMESTAMP NOT NULL
);

alter table "LADDER_STEP" add constraint "LADDER_STEP_PK" primary key ("ID");