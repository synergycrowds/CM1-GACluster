create table exec_result_cluster_value (
	exec_result_cluster_value_id serial primary key,
	execution_id integer,
	cluster_id integer,
	value double precision
);