--
-- Alter table table extend_feedback_config
--
ALTER TABLE extend_feedback_config
ADD COLUMN captcha INT DEFAULT 0 NOT NULL;

ALTER TABLE extend_feedback_config
ADD COLUMN show_feedback_type_list INT DEFAULT 0 NOT NULL;
