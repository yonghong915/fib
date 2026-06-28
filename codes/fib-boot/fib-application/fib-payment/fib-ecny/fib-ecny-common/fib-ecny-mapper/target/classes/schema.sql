create table if not exists t_wallet_info(
    ecny_cust_no varchar(50) not null,
    wallet_id varchar(34) not null,
    wallet_name varchar(100) not null,
    wallet_level char(4) not null,
    wallet_status char(4) not null,
    remark varchar(200) not null default '',
    create_dt timestamp not null default current_timestamp,
    update_dt timestamp not null default current_timestamp,
    primary key (ecny_cust_no,wallet_id)
);