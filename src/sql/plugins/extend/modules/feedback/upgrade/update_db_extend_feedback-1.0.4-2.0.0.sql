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

--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'FEEDBACK_TYPE_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('FEEDBACK_TYPE_MANAGEMENT','module.extend.feedback.module.manageFeedbackType.name',1,'jsp/admin/plugins/extend/modules/feedback/ManageFeedbackType.jsp','module.extend.feedback.module.manageFeedbackType.description',0,'extend-feedback','MANAGERS',NULL,NULL,4)
