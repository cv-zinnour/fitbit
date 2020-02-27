create table device
(
    id               uuid not null
        constraint device_pk
            primary key,
    version          integer,
    device_version   varchar,
    type             varchar,
    last_sync_date   date,
    authorized       boolean,
    admin_id         uuid,
    available        boolean,
    institution_code uuid
);

create unique index device_id_uindex
    on device (id);

create table patient_device
(
    version         integer,
    init_date       date,
    return_date     date,
    expert_id       uuid,
    device_id       uuid
        constraint patient_device_device_id_fk
            references device,
    medical_file_id uuid,
    id              serial not null
        constraint patient_device_pk
            primary key
);

create unique index patient_device_medical_file_id_uindex
    on patient_device (medical_file_id);

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

create table auth
(
    version            integer,
    authorization_code varchar,
    access_token       varchar,
    refresh_token      varchar,
    expired_token      boolean,
    expires_in         integer,
    scope              varchar,
    token_type         varchar,
    id                 uuid not null
        constraint auth_pk
            primary key
        constraint auth_device_id_fk
            references device
);

create unique index auth_id_uindex
    on auth (id);

create table activities_steps
(
    id                serial  not null
        constraint activities_steps_pk
            primary key,
    date              date    not null,
    steps             integer not null,
    dataset           jsonb   not null,
    dataset_interval  integer not null,
    version           integer not null,
    patient_device_id integer
        constraint activities_steps_patient_device_id_fk
            references patient_device
);

create unique index activities_steps_id_uindex
    on activities_steps (id);

create table activities_calories
(
    id                serial           not null
        constraint activities_calories_pk
            primary key,
    version           integer          not null,
    date              date             not null,
    calories          double precision not null,
    dataset           jsonb            not null,
    dataset_interval  integer          not null,
    patient_device_id integer          not null
        constraint activities_calories_patient_device_id_fk
            references patient_device
);

create table fitbit_subscription
(
    id              serial not null
        constraint fitbit_subscription_pk
            primary key,
    device_id       uuid
        constraint fitbit_subscription_device_id_fk
            references device,
    collection_type varchar,
    owner_id        varchar,
    owner_type      varchar,
    subscriber_id   varchar,
    subscription_id varchar,
    version         integer,
    date            date
);

create unique index fitbit_subscription_id_uindex
    on fitbit_subscription (id);


