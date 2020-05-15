create table user_subscriptions (
    channel_id int8 not null,
    subscriber_id int8 not null
);

alter table if exists user_subscriptions
    add constraint subscriber_user_fk foreign key (channel_id) references usr(id);

alter table if exists user_subscriptions
    add constraint subscription_user_fk foreign key (subscriber_id) references usr(id);