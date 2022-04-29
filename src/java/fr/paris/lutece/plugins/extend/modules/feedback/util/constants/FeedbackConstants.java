/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.extend.modules.feedback.util.constants;


/**
 *
 * FeedbackConstants
 *
 */
public final class FeedbackConstants
{
    // BEAN
    public static final String BEAN_CONFIG_SERVICE = "extend-feedback.feedbackResourceExtenderconfigService";

    // MESSAGE
    public static final String MESSAGE_DEFAULT_MESSAGE = "module.extend.feedback.message.defaultMessage";
    public static final String MESSAGE_NOTIFY_SUBJECT = "module.extend.feedback.message.notifySubject";
    public static final String MESSAGE_MESSAGE_SENT = "module.extend.feedback.message.messageSent";
    public static final String MESSAGE_ERROR_BAD_JCAPTCHA = "module.extend.feedback.message.error.badJcaptcha";
    public static final String MESSAGE_ERROR_GENERIC_MESSAGE = "module.extend.comment.message.error.genericMessage";

    // PROPERTIES
    public static final String PROPERTY_USE_CAPTCHA = "module.extend.feedback.useCaptcha";
    public static final String PROPERTY_FEEDBACK_CONFIG_LABEL_NO_MAILING_LIST = "module.extend.feedback.feedback_config.labelNoMailingList";
    public static final String PROPERTY_WEBMASTER_EMAIL = "email.webmaster";

    // PARAMETERES
    public static final String PARAMETER_MESSAGE = "message";
    public static final String PARAMETER_ID_EXTENDABLE_RESOURCE = "idExtendableResource";
    public static final String PARAMETER_EXTENDABLE_RESOURCE_TYPE = "extendableResourceType";
    public static final String PARAMETER_EXTENDER_TYPE = "extenderType";
    public static final String PARAMETER_FILTER_STATUS = "status";
    public static final String PARAMETER_FILTER_RESOURCE_TYPE = "extendableResourceTypeFilter";
    public static final String PARAMETER_FILTER_SORTING = "sorting";
    public static final String PARAMETER_CAPTCHA_ENABLED = "captchaEnabled";
    public static final String PARAMETER_FEEDBACK_TYPE_FILTER = "feedbackTypeFilter";
    public static final String PARAMETER_FEEDBACK_TYPE = "feedbackType";
    public static final String PARAMETER_REFERER = "referer";
    public static final String PARAMETER_SESSION_BACK_URL = "backUrl";
    public static final String PARAMETER_ID_ACTION = "id_action";
    public static final String PARAMETER_ID_FEEDBACK = "id_feedback";
    
    // MARKS
    public static final String MARK_MESSAGE = "message";
    public static final String MARK_ID_EXTENDABLE_RESOURCE = "idExtendableResource";
    public static final String MARK_EXTENDABLE_RESOURCE_TYPE = "extendableResourceType";
    public static final String MARK_FEEDBACK_CONFIG = "feedbackConfig";
    public static final String MARK_LIST_IDS_MAILING_LIST = "listIdsMailingList";
    public static final String MARK_WEBAPP_URL = "webapp_url";
    public static final String MARK_LOCALE = "locale";
    public static final String MARK_RESOURCE_EXTENDER_NAME = "resourceExtenderName";
    public static final String MARK_CAPTCHA = "captcha";
    public static final String MARK_IS_ACTIVE_CAPTCHA = "is_active_captcha";
    public static final String MARK_LIST_EXTEND_FEEDBACK = "listExtendFeedback";
    public static final String MARK_LIST_PROCESS_ENUM = "listProcessEnum";
    public static final String MARK_LIST_SORT_ENUM = "listSortEnum";
    public static final String MARK_LIST_RESOURCE_TYPE = "listResourceType";
    public static final String MARK_FILTER_STATUS = "filterStatusValue";
    public static final String MARK_FILTER_RESOURCE_TYPE = "filterResourceTypeValue";
    public static final String MARK_FILTER_SORTING = "filterSortingValue";
    public static final String MARK_RESOURCE_PREFIX = "resourcePrefix";
    public static final String MARK_FILTER_FEEDBACK_TYPE = "filterFeedbackTypeValue";
    public static final String MARK_LIST_FEEDBACK_TYPE = "listFeedbackType";
    public static final String MARK_WORKFLOW_LIST = "listWorkflow";
    public static final String MARK_WORKFLOW_ACTION_LIST = "workflowActionList";
    
    // CONSTANTS
    public static final String JCAPTCHA_PLUGIN = "jcaptcha";
    public static final String STAR = "*";
    public static final String RESOURCE_PREFIX = "FORMS_FORM_RESPONSE_";
    public static final String SESSION_FEEDBACK_ADMIN_POST_BACK_URL = "feedbackAdminPostBackUrlSession";
    
    /**
     * Instantiates a new your opinion constants.
     */
    private FeedbackConstants(  )
    {
    }
}
