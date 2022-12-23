CREATE TABLE books
(
    id          bigint not null AUTO_INCREMENT primary key,
    title       varchar(255),
    author      varchar(255),
    isbn        varchar(255),
    publisher   varchar(255),
    year        varchar(255),
    description varchar(255)
);

CREATE TABLE book_details
(
    id      bigint not null AUTO_INCREMENT primary key,
    price   double not null,
    stock   bigint not null,
    book_id bigint not null unique,
    constraint fk_book_details_books foreign key (book_id) references books (id)
        on delete cascade
        on update restrict
);

CREATE TABLE orders
(
    id         bigint   not null AUTO_INCREMENT primary key,
    user_id    bigint   not null,
    book_id    bigint   not null,
    quantity   bigint   not null,
    cost       double   not null,
    status     int      not null,
    created_at datetime not null,
    updated_at datetime not null,
    constraint fk_orders_users foreign key (user_id) references users (id)
        on delete cascade
        on update restrict,
    constraint fk_orders_books foreign key (book_id) references books (id)
        on delete cascade
        on update restrict
);
