
create table if not exists identity (
    id uuid default gen_random_uuid() unique not null ,
    authority varchar not null ,
    email varchar unique not null ,
    pass varchar not null ,
    enabled boolean not null ,
    firstname varchar not null ,
    lastname varchar not null ,
    patronymic varchar not null
)