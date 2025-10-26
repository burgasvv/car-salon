
create table if not exists identity_media (
    identity_id uuid references identity(id) on delete cascade on update cascade ,
    media_id uuid references media(id) on delete cascade on update cascade ,
    primary key (identity_id, media_id)
)