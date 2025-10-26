
create table if not exists media (
    id uuid default gen_random_uuid() unique not null ,
    name varchar not null ,
    content_type varchar not null ,
    format varchar not null ,
    size bigint not null ,
    data bytea not null
)