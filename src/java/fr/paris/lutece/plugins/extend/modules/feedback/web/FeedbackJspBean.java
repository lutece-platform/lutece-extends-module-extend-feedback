/*
 * Copyright (c) 2002-2022, Mairie de Paris
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
package fr.paris.lutece.plugins.extend.modules.feedback.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.modules.feedback.business.ExtendFeedback;
import fr.paris.lutece.plugins.extend.modules.feedback.service.IExtendFeedbackService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.IFeedbackTypeService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.extender.FeedbackResourceExtender;
import fr.paris.lutece.plugins.extend.modules.feedback.util.constants.FeedbackConstants;
import fr.paris.lutece.plugins.extend.modules.feedback.web.component.FeedbackResourceExtenderComponent;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.workflowcore.exception.ResourceWorkflowLockedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.template.HtmlMarkup;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.cdi.mvc.Models;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;

/**
 * 
 * FeedbackJspBean
 *
 */

@SessionScoped
@Named
@Controller( controllerJsp = "ManageFeedback.jsp", controllerPath = "jsp/admin/plugins/extend/modules/feedback/", right = "FEEDBACK_MANAGEMENT", securityTokenEnabled = true )
public class FeedbackJspBean extends MVCAdminJspBean
{
    /**
     * 
     */
    private static final long      serialVersionUID                   = -6287952387536871603L;

    // TEMPLATE
    private static final String    TEMPLATE_FEEDBACK_WORKFLOW_HISTORY = "admin/plugins/extend/modules/feedback/feedback_workflow_history.html";
    private static final String    TEMPLATE_FEEDBACK_DETAIL_VIEW      = "admin/plugins/extend/modules/feedback/feedback_detail_view.html";
    private static final String    TEMPLATE_TASKS_FORM_WORKFLOW       = "admin/plugins/extend/modules/feedback/tasks_form_workflow.html";
    private static final String    TEMPLATE_FEEDBACK_LIST             = "admin/plugins/extend/modules/feedback/feedback_list.html";

    // MARK
    private static final String    MARK_FEEDBACK_PAGE                 = "feedback_page";
    private static final String    MARK_RESOURCE_NAME                 = "resourceName";
    private static final String    MARK_RESOURCE_TYPE_LABEL           = "feedbackResourceTypeLabel";
    private static final String    MARK_RESOURCE_URL                  = "resourceUrl";

    // PROPERTY
    private static final String    PROPERTY_PAGE_TITLE_DETAIL_FEEDBACK = "module.extend.feedback.feedback_detail_view.title";
    private static final String    PROPERTY_PAGE_TITLE_LIST_FEEDBACK  = "module.extend.feedback.module.manageFeedback.name";

    // VIEW
    private static final String    VIEW_LIST_FEEDBACK                 = "list_feedback";
    private static final String    VIEW_DETAIL_FEEDBACK               = "detail_feedback_view";
    private static final String    VIEW_TASKS_FORM                    = "tasks_form_view";

    // ACTION
    private static final String    ACTION_PROCESS_WORKFLOW_ACTION     = "do_process_workflow_action";
    private static final String    ACTION_SAVE_TASK_FORM              = "save_task_form";

    // SERVICE
    @Inject
    @Named( "extend-feedback.extendFeedbackService" )
    private IExtendFeedbackService _extendFeedbackService;

    @Inject
    private WorkflowService _workflowService;

    @Inject
    @Named( "extend-feedback.extendFeedbackTypeService" )
    private IFeedbackTypeService _feedbackTypeService;

    @Inject
    @Named( "extend-feedback.feedbackResourceExtenderComponent" )
    private FeedbackResourceExtenderComponent _feedbackComponent;

    @Inject
    @Named( "extend.resourceExtenderService" )
    private IResourceExtenderService _resourceExtenderService;

    // SESSION VARIABLE
    private int                    _nIdFeedback;
    private int                    _nIdAction;
    private int                    _nIdWorkflow;

    /**
     * Displays the feedbacks of every resource, filtered by the parameters of the request.
     *
     * @param request
     *            the request
     * @param model
     *            the model
     * @return the feedback list page
     */
    @View( value = VIEW_LIST_FEEDBACK, defaultView = true )
    public String getFeedbackList( HttpServletRequest request, Models model )
    {
        _nIdFeedback = 0;
        _nIdAction = 0;
        _nIdWorkflow = 0;

        ResourceExtenderDTO resourceExtender = new ResourceExtenderDTO( );
        resourceExtender.setExtenderType( FeedbackResourceExtender.RESOURCE_EXTENDER );
        resourceExtender.setIdExtendableResource( FeedbackConstants.STAR );
        resourceExtender.setExtendableResourceType( FeedbackConstants.STAR );

        Map<String, Object> listModel = new HashMap<>( );
        _feedbackComponent.fillListModel( resourceExtender, getLocale( ), request, listModel );
        listModel.forEach( model::put );
        model.put( MARK_FEEDBACK_PAGE, true );

        return getPage( PROPERTY_PAGE_TITLE_LIST_FEEDBACK, TEMPLATE_FEEDBACK_LIST );
    }

    /**
     * View detail feedback
     * @param request
     * @param model the model
     * @return return view feedback detail
     */
    @View( value = VIEW_DETAIL_FEEDBACK, securityTokenAction = ACTION_PROCESS_WORKFLOW_ACTION )
    public String getDetailFeedbackView( HttpServletRequest request, Models model )
    {
        String strIdFeedback = request.getParameter( FeedbackConstants.PARAMETER_ID_FEEDBACK );
        String strIdWorkflow = request.getParameter( FeedbackConstants.PARAMETER_ID_WORKFLOW );

        if ( StringUtils.isNumeric( strIdFeedback ) && StringUtils.isNumeric( strIdWorkflow ) )
        {
            _nIdFeedback = Integer.parseInt( strIdFeedback );
            _nIdWorkflow = Integer.parseInt( strIdWorkflow );
        }

        try
        {
            Optional<ExtendFeedback> feedback = _extendFeedbackService.findById( _nIdFeedback );

            if ( feedback.isPresent( ) )
            {
                User user = AdminUserService.getAdminUser( request );
                // HTML rendered by the workflow with the history template of this module
                model.put( FeedbackConstants.MARK_HISTORY_WORKFLOW, HtmlMarkup.of( _workflowService.getDisplayDocumentHistory( _nIdFeedback,
                        feedback.get( ).getResourceType( ), _nIdWorkflow, request, getLocale( ), new HashMap<>( ), TEMPLATE_FEEDBACK_WORKFLOW_HISTORY, user ) ) );

                feedback.get( ).setListWorkflowActions( _workflowService.getActions( feedback.get( ).getId( ), feedback.get( ).getResourceType( ), _nIdWorkflow, user ) );
                String strIdResource = String.valueOf( feedback.get( ).getIdResource( ) );
                String strResourceType = feedback.get( ).getResourceType( );
                model.put( FeedbackConstants.MARK_FEEDBACK, feedback.get( ) );
                model.put( FeedbackConstants.MARK_FEEDBACK_TYPE_LABELS, _feedbackTypeService.getFeedbackTypeLabels( ) );
                model.put( MARK_RESOURCE_TYPE_LABEL, _feedbackComponent.getResourceTypeLabel( strResourceType, getLocale( ) ) );
                model.put( MARK_RESOURCE_URL, _feedbackComponent.getResourceUrl( strIdResource, strResourceType ) );
                model.put( MARK_RESOURCE_NAME, _resourceExtenderService.getExtendableResourceName( strIdResource, strResourceType ) );

                return getPage( PROPERTY_PAGE_TITLE_DETAIL_FEEDBACK, TEMPLATE_FEEDBACK_DETAIL_VIEW );
            }
        }
        catch ( AppException e )
        {
            AppLogService.error( "Error detail feedback for id feedback {} - cause : {}", _nIdFeedback, e.getMessage( ), e );
        }
        return redirectView( request, VIEW_LIST_FEEDBACK );
    }

    /**
     * Do process a workflow action over an feedback
     * 
     * @param request
     *            The request
     * @return The next URL to redirect to
     */
    @Action( ACTION_PROCESS_WORKFLOW_ACTION )
    public String doProcessWorkflowAction( HttpServletRequest request )
    {
        String strIdAction = request.getParameter( FeedbackConstants.PARAMETER_ID_ACTION );

        if ( StringUtils.isNumeric( strIdAction ) )
        {
            _nIdAction = Integer.parseInt( strIdAction );

            Optional<ExtendFeedback> feedback = _extendFeedbackService.findById( _nIdFeedback );

            if ( feedback.isPresent( ) )
            {
                try
                {
                    if ( _workflowService.isDisplayTasksForm( _nIdAction, getLocale( ) ) )
                    {
                        return redirectView( request, VIEW_TASKS_FORM );
                    }
                    User user = getUser( );
                    _workflowService.doProcessAction( feedback.get( ).getId( ), feedback.get( ).getResourceType( ), _nIdAction, feedback.get( ).getIdResource( ), request,
                            getLocale( ), false, user );
                }
                catch ( AppException e )
                {
                    if ( e.getCause( ) instanceof ResourceWorkflowLockedException )
                    {
                        // Another action is in progress on this feedback (other user or instance) or its state has changed
                        addError( FeedbackConstants.MESSAGE_ERROR_ACTION_LOCKED, getLocale( ) );
                    }
                    else
                    {
                        AppLogService.error( "Error processing action for id feedback {} - cause : {}", _nIdFeedback, e.getMessage( ), e );
                    }
                }
            }
            else
            {
                AppLogService.error( "Error processing action for feedback {} - cause : the feedback doesn't exist", _nIdFeedback );
            }
            return redirectView( request, VIEW_DETAIL_FEEDBACK );

        }
        return redirectView( request, VIEW_LIST_FEEDBACK );
    }

    /**
     * Displays the tasks form of the selected workflow action
     * 
     * @param request
     *            The request
     * @param model
     *            The model
     * @return the tasks form page
     */
    @View( value = VIEW_TASKS_FORM, securityTokenAction = ACTION_SAVE_TASK_FORM )
    public String getTasksForm( HttpServletRequest request, Models model )
    {
        Optional<ExtendFeedback> feedback = _extendFeedbackService.findById( _nIdFeedback );

        if ( _nIdAction == 0 || feedback.isEmpty( ) )
        {
            return redirectView( request, VIEW_LIST_FEEDBACK );
        }

        User user = getUser( );
        String strHtmlTasksForm = _workflowService.getDisplayTasksForm( feedback.get( ).getId( ), feedback.get( ).getResourceType( ), _nIdAction, request, getLocale( ),
                user );
        // HTML of the task components of the workflow
        model.put( FeedbackConstants.MARK_TASK_FORM, HtmlMarkup.of( strHtmlTasksForm ) );

        return getPage( FeedbackConstants.PROPERTY_PAGE_TITLE_TASKS_FORM_WORKFLOW, TEMPLATE_TASKS_FORM_WORKFLOW );
    }

    /**
     * Process workflow task action
     * 
     * @param request
     * @return VIEW_DETAIL_FEEDBACK
     */
    @Action( ACTION_SAVE_TASK_FORM )
    public String doSaveTaskForm( HttpServletRequest request )
    {
        if ( _nIdAction != 0 && _nIdFeedback != 0 )
        {
            Optional<ExtendFeedback> feedback = _extendFeedbackService.findById( _nIdFeedback );
            if ( feedback.isPresent( ) )
            {
                try
                {
                    User user = getUser( );
                    String strError = _workflowService.doSaveTasksForm( feedback.get( ).getId( ), feedback.get( ).getResourceType( ), _nIdAction, feedback.get( ).getIdResource( ),
                            request, getLocale( ), user );

                    if ( strError != null )
                    {
                        return redirect( request, strError );
                    }
                }
                catch ( AppException e )
                {
                    if ( e.getCause( ) instanceof ResourceWorkflowLockedException )
                    {
                        addError( FeedbackConstants.MESSAGE_ERROR_ACTION_LOCKED, getLocale( ) );
                    }
                    else
                    {
                        AppLogService.error( "Error processing action for record {}", _nIdFeedback, e );
                    }
                }
            }
            else
            {
                AppLogService.error( "Error processing action for feedback {} - cause : the feedback doesn't exist", _nIdFeedback );
            }
            return redirectView( request, VIEW_DETAIL_FEEDBACK );
        }
        return redirectView( request, VIEW_LIST_FEEDBACK );
    }
}
