
create table if not exists car_media (
    car_id uuid references car(id) on delete cascade on update cascade ,
    media_id uuid references media(id) on delete cascade on update cascade ,
    primary key (car_id, media_id)
)