create table downloading_accounts
(
    id         varchar(32) primary key,
    limitation integer,
    version    integer -- optimistic locking
);

create table downloaded_assets
(
    id            integer auto_increment primary key,
    asset_id      varchar(32),
    country_code  varchar(32),
    version       integer, -- optimistic locking
    account       varchar(32) references downloading_accounts (id),
    account_order integer, -- to ensure proper ordering of assigned assets
    constraint asset_in_country unique (asset_id, country_code)
);

