create table account (
    id identity,
    display_name varchar,
    provider varchar not null,
    provider_user_id varchar not null,
    primary key (id)
);