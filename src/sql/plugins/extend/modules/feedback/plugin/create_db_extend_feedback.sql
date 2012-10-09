--
-- Structure for table extend_feedback_config
--
DROP TABLE IF EXISTS extend_feedback_config;
CREATE TABLE extend_feedback_config (
	id_extender INT DEFAULT 0 NOT NULL,
	message LONG VARCHAR NOT NULL,
	id_mailing_list INT DEFAULT 0 NOT NULL,
	PRIMARY KEY (id_extender)
);
