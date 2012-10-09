--
-- Structure for table socialhub_youropinion_config
--
DROP TABLE IF EXISTS socialhub_youropinion_config;
CREATE TABLE socialhub_youropinion_config (
	id_extender INT DEFAULT 0 NOT NULL,
	message LONG VARCHAR NOT NULL,
	id_mailing_list INT DEFAULT 0 NOT NULL,
	PRIMARY KEY (id_extender)
);
