--
-- Alter table table extend_feedback_config
--
ALTER TABLE extend_feedback_config
ADD COLUMN captcha INT DEFAULT 0 NOT NULL;
