
create table if not exists rent (
    id uuid default gen_random_uuid() unique not null ,
    identity_id uuid references identity(id) on delete cascade on update cascade ,
    car_id uuid references car(id) on delete set null on update cascade ,
    price decimal not null default 0 check ( price >= 0 ) ,
    start_time timestamp not null ,
    end_time timestamp not null ,
    closed boolean not null
)