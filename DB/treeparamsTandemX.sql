CREATE TABLE tree (
	tree_id serial primary key
);

CREATE TABLE rdmparams
(
    tree_id integer primary key,
    wait_btw_sessions bigint
);

CREATE TABLE normvwapparams
(
    tree_id integer primary key,
    wait_btw_sessions bigint,
    min_volume_threshold double precision
);

CREATE TABLE emparams
(
    tree_id integer primary key,
    number_of_observations integer,
    step_size integer,
    wait_btw_sessions bigint,
    min_number_of_symbols integer,
    volume_threshold double precision,
    norm_price_threshold double precision
);

CREATE TABLE ccparams
(
    tree_id integer primary key,
    clusters_number integer,
    evolutions_nr integer,
    min_fitness integer,
    wait_btw_sessions bigint
);

INSERT INTO tree VALUES (1);
INSERT INTO rdmparams VALUES (1, 86400000);
INSERT INTO normvwapparams VALUES (1, 3600000, 1e-008);
INSERT INTO emparams VALUES (1, 50, 1, 14400000, 8, 0, 4);
INSERT INTO ccparams VALUES (1, 3, 10, 2, 14400000);