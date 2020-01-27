create table device
(
    id             uuid not null
        constraint device_pk
            primary key,
    version        integer,
    device_id      varchar,
    device_version varchar,
    type           varchar,
    battery        varchar,
    last_sync_time date
);

alter table device
    owner to postgres;

create unique index device_id_uindex
    on device (id);

create table patient_device
(
    id          uuid not null
        constraint patient_device_pk
            primary key,
    version     integer,
    init_date   date,
    return_date date,
    expert_id   uuid,
    device_id   uuid
        constraint patient_device_device_id_fk
            references device,
    patient_id  uuid
);

alter table patient_device
    owner to postgres;

create unique index patient_device_id_uindex
    on patient_device (id);

create table activity
(
    id                     uuid not null
        constraint activity_pkey
            primary key,
    version                integer,
    calories               integer,
    steps                  integer,
    distance               double precision,
    minutes_sedentary      integer,
    minutes_lightly_active integer,
    minutes_fairly_active  integer,
    minutes_very_active    integer,
    activity_calories      integer,
    datetime               date
);

alter table activity
    owner to postgres;

create table auth
(
    id                 serial not null
        constraint auth_pkey
            primary key,
    version            integer,
    authorization_code varchar,
    access_token       varchar,
    refresh_token      varchar,
    expired_token      boolean,
    expires_in         integer,
    scope              varchar,
    token_type         varchar
);

alter table auth
    owner to postgres;

create table activities_steps
(
    id               serial  not null
        constraint activities_steps_pk
            primary key,
    date             date    not null,
    steps            integer not null,
    dataset          jsonb   not null,
    dataset_interval integer not null,
    version          integer not null
);

alter table activities_steps
    owner to postgres;

create unique index activities_steps_id_uindex
    on activities_steps (id);

create function inc(val integer) returns integer
    language plpgsql
as
$$
BEGIN
    RETURN val + 1;
END;
$$;

alter function inc(integer) owner to postgres;


