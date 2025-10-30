
create table if not exists car (
    id uuid default gen_random_uuid() unique not null ,
    brand_id uuid references brand(id) on delete set null on update cascade ,
    model varchar unique not null ,
    characteristics text not null ,
    rent_price decimal not null default 0 check ( rent_price >= 0 )
)