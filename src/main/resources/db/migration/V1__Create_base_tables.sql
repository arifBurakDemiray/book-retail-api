CREATE TABLE users
(
    id              bigint       not null AUTO_INCREMENT primary key,
    name            varchar(255),
    surname         varchar(255),
    email           varchar(255) not null unique,
    phone_number    varchar(15),
    password        varchar(60)  not null,
    role            int          not null,
    created_on      date         not null,
    confirmed       tinyint      not null,
    banned          tinyint      not null,
    profile_picture varchar(255)
);

CREATE TABLE verification_codes
(
    id         bigint not null AUTO_INCREMENT primary key,
    code       varchar(255),
    user_id    bigint,
    expiration date   not null,
    type       int    not null,
    constraint fk_verification_codes_users foreign key (user_id) references users (id)
        on delete cascade
        on update restrict
);

CREATE TABLE onesignal_notifications
(
    id            bigint       not null AUTO_INCREMENT primary key,
    heading       varchar(255),
    content       varchar(255),
    one_signal_id varchar(255) not null,
    user_id       bigint,
    created_on    date         not null,
    constraint fk_onesignal_notifications_users foreign key (user_id) references users (id)
        on delete cascade
        on update restrict
);

CREATE TABLE user_notifications
(
    id                        bigint  not null AUTO_INCREMENT primary key,
    user_id                   bigint  not null,
    onesignal_notification_id bigint  not null,
    read_at                   date,
    is_read                   tinyint not null,
    constraint fk_user_notifications_users foreign key (user_id) references users (id)
        on delete cascade
        on update restrict,
    constraint fk_user_notifications_onesignal_notifications foreign key (onesignal_notification_id)
        references onesignal_notifications (id)
        on delete cascade
        on update restrict
);

CREATE TABLE mobile_devices
(
    id            bigint      not null AUTO_INCREMENT primary key,
    user_id       bigint      not null,
    device_id     varchar(36) not null unique,
    created_at    date        not null,
    updated_at    date        not null,
    refresh_token varchar(36) not null unique,
    constraint fk_mobile_devices_users foreign key (user_id) references users (id)
        on delete cascade
        on update restrict
);