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
    last_used timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS feed (
  code_name varchar(255) NOT NULL,
  create_date datetime DEFAULT NULL,
  update_date datetime DEFAULT NULL,
  version int(11) NOT NULL,
  date_format varchar(255) DEFAULT NULL,
  languaje varchar(255) DEFAULT NULL,
  last_news_link varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  news_link varchar(255) DEFAULT NULL,
  url varchar(255) DEFAULT NULL,
  url_news varchar(255) DEFAULT NULL,
  urlrss varchar(255) DEFAULT NULL,
  use_selector bit(1) NOT NULL,
  withrss bit(1) NOT NULL,
  PRIMARY KEY (code_name)
);

CREATE TABLE IF NOT EXISTS feed_selector_html (
  feed_codeName varchar(255) NOT NULL,
  attribute varchar(255) DEFAULT NULL,
  value_css varchar(255) DEFAULT NULL,
  KEY FK_oqxh34ygnq014lbf56xyrwqa (feed_codeName),
  CONSTRAINT FK_oqxh34ygnq014lbf56xyrwqa FOREIGN KEY (feed_codeName) REFERENCES feed (code_name)
);

CREATE TABLE IF NOT EXISTS feed_selector_meta (
  feed_codeName varchar(255) NOT NULL,
  attribute varchar(255) DEFAULT NULL,
  value_css varchar(255) DEFAULT NULL,
  KEY FK_ed45a40fgqmfb4ifyfolvn2p8 (feed_codeName),
  CONSTRAINT FK_ed45a40fgqmfb4ifyfolvn2p8 FOREIGN KEY (feed_codeName) REFERENCES feed (code_name)
);

CREATE TABLE IF NOT EXISTS alert (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  create_date datetime DEFAULT NULL,
  update_date datetime DEFAULT NULL,
  version int(11) NOT NULL,
  is_check bit(1) DEFAULT NULL,
  date_pub datetime DEFAULT NULL,
  date_tratamiento datetime DEFAULT NULL,
  info_alert longtext,
  level int(11) DEFAULT NULL,
  link varchar(255) DEFAULT NULL,
  title varchar(255) DEFAULT NULL,
  type_alert varchar(255) DEFAULT NULL,
  site_code_name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_kmv0jgfiiw792i7rmkfwxw9n2 (link,type_alert),
  KEY FK_kbutfie66igk2db2w1qecpn61 (site_code_name),
  CONSTRAINT FK_kbutfie66igk2db2w1qecpn61 FOREIGN KEY (site_code_name) REFERENCES feed (code_name)
);