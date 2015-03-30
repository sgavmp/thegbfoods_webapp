CREATE TABLE IF NOT EXISTS users (
    username varchar(50) not null primary key,
    password varbinary(60) not null,
    enabled boolean not null
);

CREATE TABLE IF NOT EXISTS authorities (
    username varchar(50) not null,
    authority varchar(50) not null,
    foreign key (username) references users (username),
    unique index authorities_idx_1 (username, authority)
);

CREATE TABLE IF NOT EXISTS persistent_logins (
    username character varying(64) NOT NULL,
    series character varying(64) NOT NULL,
    token character varying(64) NOT NULL,
    last_used timestamp without time zone NOT NULL
);