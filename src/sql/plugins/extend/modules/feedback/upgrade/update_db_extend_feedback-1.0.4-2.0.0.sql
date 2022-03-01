--
-- Table structure for table extend_feedback
--
CREATE TABLE extend_feedback (
  id INT NOT NULL AUTO_INCREMENT,
  id_history INT DEFAULT 0 NOT NULL,
  id_resource INT DEFAULT 0 NOT NULL,
  resource_type VARCHAR(255) DEFAULT '' NOT NULL,
  comment LONG VARCHAR NOT NULL,
  update_status_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  feedback_type VARCHAR(255) DEFAULT '',
  status SMALLINT default 0 NOT NULL,
  lutece_user_name LONG VARCHAR,
  email LONG VARCHAR,
  PRIMARY KEY ( id )
);

--
-- Table structure for table extend_feedback_type
--
CREATE TABLE extend_feedback_type (
  id INT NOT NULL AUTO_INCREMENT,
  label LONG VARCHAR NOT NULL,
  value_type VARCHAR(255) NOT NULL,
  default_option INT DEFAULT 0 NOT NULL,
  order_type INT DEFAULT 0 NOT NULL,
  PRIMARY KEY ( id )
);

--
-- Alter table table extend_feedback_config
--
ALTER TABLE extend_feedback_config
ADD COLUMN captcha INT DEFAULT 0 NOT NULL;

ALTER TABLE extend_feedback_config
ADD COLUMN show_feedback_type_list INT DEFAULT 0 NOT NULL;

ALTER TABLE extend_feedback_config
ADD COLUMN id_workflow INT DEFAULT 0 NOT NULL;

ALTER TABLE extend_feedback_config
ADD COLUMN authenticated_mode INT DEFAULT 0 NOT NULL;

--
-- Data for table core_admin_right
--
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('FEEDBACK_TYPE_MANAGEMENT','module.extend.feedback.module.manageFeedbackType.name',1,'jsp/admin/plugins/extend/modules/feedback/ManageFeedbackType.jsp','module.extend.feedback.module.manageFeedbackType.description',0,'extend-feedback','MANAGERS',NULL,NULL,4)

--
-- Alter table table extend_feedback
--
ALTER TABLE extend_feedback
ADD COLUMN lutece_user_name LONG VARCHAR;

ALTER TABLE extend_feedback
ADD COLUMN email LONG VARCHAR;