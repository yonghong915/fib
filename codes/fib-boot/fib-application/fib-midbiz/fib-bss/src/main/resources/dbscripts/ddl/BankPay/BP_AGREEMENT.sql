create table bp_agreement(
	id bigint(20) not null,
	org_code varchar(60) not null,
	protocol_num varchar(60) not null,
	name varchar(60) not null,
	document_type varchar(60) not null,
	document_num varchar(60) not null,
	protocol_type varchar(60) not null,
	sign_date date not null,
	cancel_date date,
	to_validity date not null,
	debit_acct_no varchar(60) ,
	debit_acct_name varchar(60) ,
	credit_acct_no varchar(60) ,
	credit_acct_name varchar(60) ,
	mobile varchar(60) ,
	pay_type varchar(60) not null,
	single_limit decimal(19,2) ,
	day_limit decimal(19,2) ,
	month_limit decimal(19,2),
	remark varchar(60) ,
	sign_status varchar(60) not null default '0',
	sc_ctrct_entity_type varchar(60) ,
	sc_ctrct_entity_num varchar(60) ,
	cust_ctrct_code_fw varchar(60) ,
	cust_ctrct_code_gw varchar(60) ,
	create_dt timestamp not null,
	create_by bigint(20),
	update_dt timestamp,
	update_by bigint(20),
	primary key(id)
);

comment on table bp_agreement is '校园卡协议信息表';
comment on column bp_agreement.id is '主键';

create index idx_bp_agreement_org_code on bp_agreement(org_code);






