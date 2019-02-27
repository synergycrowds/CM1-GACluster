create table exec_result_cluster_value (
	exec_result_cluster_value_id serial primary key,
	execution_id integer,
	cluster_id integer,
	value double precision
);

create table exec_result_cluster_content (
	exec_result_cluster_content_id serial primary key,
	execution_id integer,
	cluster_id integer,
	currency_pair_id integer
);