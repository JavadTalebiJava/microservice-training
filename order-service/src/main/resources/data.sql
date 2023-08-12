create table if not exists public.order_items
(
    id           bigint not null
        primary key,
    barcode      varchar(255),
    created_date timestamp(6),
    price        numeric(38, 2),
    qty          real   not null,
    sku          varchar(255)
);

alter table public.order_items
    owner to postgres;

create table if not exists public.orders
(
    id           bigint not null
        primary key,
    created_date timestamp(6),
    tax          numeric(38, 2),
    total        numeric(38, 2)
);

alter table public.orders
    owner to postgres;

create table if not exists public.orders_order_items
(
    order_id       bigint not null
        constraint fk3l8rktw0f4w5t6tift31e2d7c
            references public.orders,
    order_items_id bigint not null
        constraint uk_9d47gapmi35omtannusv6btu3
            unique
        constraint fk1ldyot20dlv6os5xhp0adi687
            references public.order_items
);

alter table public.orders_order_items
    owner to postgres;

create sequence public.order_items_seq
    increment by 1;

alter sequence public.order_items_seq owner to postgres;

create sequence public.orders_seq
    increment by 1;

alter sequence public.orders_seq owner to postgres;

