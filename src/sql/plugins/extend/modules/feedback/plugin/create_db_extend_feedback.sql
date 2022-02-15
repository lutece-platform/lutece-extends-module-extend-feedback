--
-- Structure for table extend_feedback_config
--
DROP TABLE IF EXISTS extend_feedback_config;
CREATE TABLE extend_feedback_config (
	id_extender INT DEFAULT 0 NOT NULL,
	message LONG VARCHAR NOT NULL,
	id_mailing_list INT DEFAULT 0 NOT NULL,
	captcha INT DEFAULT 0 NOT NULL,
	show_feedback_type_list INT DEFAULT 0 NOT NULL
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
  feedback_type VARCHAR(255) DEFAULT '',
  status SMALLINT default 0 NOT NULL,
  PRIMARY KEY ( id )
);

--
-- Table structure for table extend_feedback_type
--
DROP TABLE IF EXISTS extend_feedback_type;
CREATE TABLE extend_feedback_type (
  id INT NOT NULL AUTO_INCREMENT,
  label LONG VARCHAR NOT NULL,
  value_type VARCHAR(255) NOT NULL,
  default_option INT DEFAULT 0 NOT NULL,
  order_type INT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY ( id )
);