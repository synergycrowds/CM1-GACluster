create table execution(
	execution_id serial primary key,
	data_timestamp_begin timestamp without time zone,
	data_timestamp_end timestamp without time zone,
	execution_timestamp_begin timestamp without time zone,
	execution_timestamp_end timestamp without time zone
);

create table execution_currency_pair(
	execution_currency_pair_id serial primary key,
	execution_id integer,
	currency_pair_id integer
);