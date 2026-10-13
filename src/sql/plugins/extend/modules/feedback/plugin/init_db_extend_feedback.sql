-- liquibase formatted sql
-- changeset extend-feedback:init_db_extend_feedback.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM extend_feedback_config WHERE id_extender = -1
INSERT INTO extend_feedback_config(id_extender,message,id_mailing_list) VALUES (-1,'Vous avez une remarque à faire, une expérience à raconter sur le contenu de la page ou sur le service dont il est question ?',1);
