create schema if not exists snooper;

use snooper;

create table if not exists users (
                                     id varchar(255) primary key not null,
                                     name varchar(70) not null,
                                     photo_url varchar(255)
);

create table if not exists messages (
                                        id int primary key auto_increment,
                                        latitude double not null,
                                        longitude double not null,
                                        address varchar(128),
                                        description varchar(128),
                                        owner_id varchar(255) not null,
                                        date timestamp default now(),
                                        foreign key (owner_id)
                                            references users(id)
                                            on delete cascade
);

create table if not exists subscriptions (
                                             id int primary key auto_increment,
                                             follower_id varchar(255) not null,
                                             followee_id varchar(255) not null,
                                             foreign key (follower_id)
                                                 references users(id)
                                                 on delete cascade,
                                             foreign key (followee_id)
                                                 references users(id)
                                                 on delete cascade
);
