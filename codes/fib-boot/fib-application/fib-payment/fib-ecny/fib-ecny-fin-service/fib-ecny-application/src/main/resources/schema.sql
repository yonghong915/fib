create table if not exists t_wallet_info(
    ecny_cust_no varchar(50) not null,
    wallet_id varchar(34) not null,
    wallet_name varchar(100) not null,
    wallet_level char(4) not null,
    wallet_status char(4) not null,
    remark varchar(200) not null default '',
    create_dt datetime(3) not null default current_timestamp,
    update_dt datetime(3) not null default current_timestamp,
    primary key (ecny_cust_no,wallet_id)
);

create table if not exists tqa_main_serial(
    id bigint  not null ,
    gl_serial_no varchar(64) not null,
    chnl_no varchar(10) not null,
    chnl_dt varchar(8) not null,
    chnl_tm varchar(6) not null,
    tran_code varchar(20) not null,
    tran_status tinyint not null,
    tran_amt decimal(17,2) not null,
    tran_ccy char(3) not null default 'CNY',
    biz_type varchar(3) not null default '',
    biz_category  varchar(8) not null default '',
    remark varchar(200) not null default '',
    create_dt timestamp not null default current_timestamp,
    update_dt timestamp not null default current_timestamp,
    primary key (id)
    );
create unique index uk_tqa_main_serial_gl_serial_no on tqa_main_serial(gl_serial_no);

create table if not exists tqa_capital_serial(
    id bigint  not null ,
    gl_serial_no varchar(64) not null,
    chnl_serial_no varchar(34) not null,
    tran_code varchar(20) not null,
    tran_status tinyint not null,
    tran_amt decimal(17,2) not null,
    tran_ccy char(3) not null default 'CNY',
    remark varchar(200) not null default '',
    create_dt timestamp not null default current_timestamp,
    update_dt timestamp not null default current_timestamp,
    primary key (id)
    );
create unique index uk_tqa_capital_serial_gl_chnl_serial_no on tqa_capital_serial(gl_serial_no,chnl_serial_no);

create table if not exists tqw_wallet_tran_detail(
    id bigint  not null ,
    gl_serial_no varchar(64) not null,
    chnl_serial_no varchar(34) not null,
    tran_code varchar(20) not null,
    wallet_id varchar(34) not null,
    vourcher_no varchar(64) not null,
    tran_dt varchar(32) not null,
    tran_wallet_id varchar(34) not null,
    tran_amt decimal(17,2) not null,
    tran_ccy char(3) not null default 'CNY',
    tran_status tinyint not null,
    resersal_flag char(1) not null default 'N',
    ref_vourcher_no varchar(64) ,
    remark varchar(1000),
    create_dt timestamp not null default current_timestamp,
    update_dt timestamp not null default current_timestamp,
    primary key (id)
    );

create table if not exists batch_pay_roll(
    batch_id bigint  not null ,
    batch_name varchar(64) not null,
    status varchar(10) not null,
    total_cnt int not null,
    total_amt decimal(17,2) not null,
    success_cnt int not null default 0,
    success_amt decimal(17,2) not null default 0.00,
    fail_cnt int not null default 0,
    fail_amt decimal(17,2) not null default 0.00,
    remark varchar(1000),
    create_dt timestamp not null default current_timestamp,
    update_dt timestamp not null default current_timestamp,
    primary key (batch_id)
    );
create table if not exists batch_pay_roll_detail(
    batch_id bigint  not null ,
    seq_id bigint not null,
    payee_wallet_id varchar(34) not null,
    payee_wallet_level varchar(10) not null,
    payee_wallet_name varchar(100) not null,
    payee_name varchar(100) not null default '',
    status varchar(10) not null default '1',
    amt decimal(17,2) not null,
    remark varchar(1000),
    create_dt timestamp not null default current_timestamp,
    update_dt timestamp not null default current_timestamp,
    primary key (batch_id,seq_id)
    );