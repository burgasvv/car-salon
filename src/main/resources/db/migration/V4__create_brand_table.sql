
create table if not exists brand (
    id uuid default gen_random_uuid() unique not null ,
    name varchar unique not null ,
    description text unique not null
);

begin ;
insert into brand(name, description) values ('BMW', 'Описание и год создания бренда BMW');
insert into brand(name, description) values ('Porsche', 'Описание и год создания бренда Porsche');
insert into brand(name, description) values ('Aston Matrin', 'Описание и год создания бренда Aston Matrin');
commit ;