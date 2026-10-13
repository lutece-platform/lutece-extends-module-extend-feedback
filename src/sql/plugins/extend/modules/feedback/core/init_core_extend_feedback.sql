-- liquibase formatted sql
-- changeset extend-feedback:init_core_extend_feedback.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_admin_right WHERE id_right IN ('FEEDBACK_MANAGEMENT','FEEDBACK_TYPE_MANAGEMENT')
--
-- Init  table core_admin_right
--
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name)
VALUES ('FEEDBACK_TYPE_MANAGEMENT','module.extend.feedback.module.manageFeedbackType.name',1,'jsp/admin/plugins/extend/modules/feedback/ManageFeedbackType.jsp','module.extend.feedback.module.manageFeedbackType.description',0,'extend-feedback');
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name)
VALUES ('FEEDBACK_MANAGEMENT','module.extend.feedback.module.manageFeedback.name',1,'jsp/admin/plugins/extend/modules/feedback/ManageFeedback.jsp','module.extend.feedback.module.manageFeedback.description',0,'extend-feedback');

--
-- Init  table core_user_right
--
INSERT INTO core_user_right (id_right,id_user) VALUES ('FEEDBACK_TYPE_MANAGEMENT',1);
INSERT INTO core_user_right (id_right,id_user) VALUES ('FEEDBACK_MANAGEMENT',1);
