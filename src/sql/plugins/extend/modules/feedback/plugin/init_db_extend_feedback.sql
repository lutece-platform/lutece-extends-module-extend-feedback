INSERT INTO extend_feedback_config(id_extender,message,id_mailing_list) VALUES (-1,'Vous avez une remarque à faire, une expérience à raconter sur le contenu de la page ou sur le service dont il est question ?',1);

--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'FEEDBACK_TYPE_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('FEEDBACK_TYPE_MANAGEMENT','module.extend.feedback.module.manageFeedbackType.name',1,'jsp/admin/plugins/extend/modules/feedback/ManageFeedbackType.jsp','module.extend.feedback.module.manageFeedbackType.description',0,'extend-feedback','MANAGERS',NULL,NULL,4)
