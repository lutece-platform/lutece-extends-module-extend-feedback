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


--
-- Table structure for table extend_feedback
--
DROP TABLE IF EXISTS extend_feedback;
CREATE TABLE extend_feedback (
  id INT NOT NULL AUTO_INCREMENT,
  id_history INT DEFAULT 0 NOT NULL,
  id_resource INT DEFAULT 0 NOT NULL,
  resource_type VARCHAR(255) DEFAULT '' NOT NULL,
  comment LONG VARCHAR NOT NULL,
  update_status_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  status SMALLINT default 0 NOT NULL,
  PRIMARY KEY ( id )
);