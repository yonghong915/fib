create table t_communication(
	id bigint(20) not null,
	name varchar(100),
	create_dt datetime not null,
	create_by bigint(20),
	update_dt datetime,
	update_by bigint(20)
);