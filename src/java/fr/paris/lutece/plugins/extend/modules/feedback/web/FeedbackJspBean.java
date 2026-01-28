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
import fr.paris.lutece.plugins.extend.modules.feedback.business.ExtendFeedback;
import fr.paris.lutece.plugins.extend.modules.feedback.service.ExtendFeedbackService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.IExtendFeedbackService;
import fr.paris.lutece.plugins.extend.modules.feedback.util.constants.FeedbackConstants;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.html.HtmlTemplate;

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
@Controller( controllerJsp = "ManageFeedback.jsp", controllerPath = "jsp/admin/plugins/extend/modules/feedback/", right = "FEEDBACK_MANAGEMENT" )
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

    // VIEW
    private static final String    VIEW_LIST_FEEDBACK                 = "list_feedback";
    private static final String    VIEW_DETAIL_FEEDBACK               = "detail_feedback_view";

    // ACTION
    private static final String    ACTION_PROCESS_WORKFLOW_ACTION     = "do_process_workflow_action";
    private static final String    ACTION_SAVE_TASK_FORM              = "save_task_form";

    // SERVICE
    @Inject
    @Named( "extend-feedback.extendFeedbackService" )
    private IExtendFeedbackService _extendFeedbackService;

    // JSP
    private static final String    JSP_FEEDBACK_LIST                  = "../../ViewExtenderInfo.jsp?feedbackTypeFilter=*&sorting=*&extenderType=feedback&extendableResourceType=*&extendableResourceTypeFilter=*&idExtendableResource=*&status=*";

    // SESSION VARIABLE
    private int                    _nIdFeedback;
    private int                    _nIdAction;
    private int                    _nIdWorkflow;

    /**
     * Redirect to feedback list view
     * 
     * @param request
     * @return redirect to feedback list view
     */
    @View( value = VIEW_LIST_FEEDBACK, defaultView = true )
    public String doRedirectFeedbackListView( HttpServletRequest request )
    {       
        // Init
        _nIdFeedback = 0;
        _nIdAction = 0;
        _nIdWorkflow = 0;
        return redirect( request, JSP_FEEDBACK_LIST );
    }

    /**
     * View detail feedback
     * @param request
     * @return return view feedback detail
     */
    @View( VIEW_DETAIL_FEEDBACK )
    public String getDetailFeedbackView( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );

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
                model.put( FeedbackConstants.MARK_HISTORY_WORKFLOW, WorkflowService.getInstance( ).getDisplayDocumentHistory( _nIdFeedback, feedback.get( ).getResourceType( ), _nIdWorkflow, request,
                        getLocale( ), model, TEMPLATE_FEEDBACK_WORKFLOW_HISTORY, user ) );

                feedback.get( ).setListWorkflowActions( WorkflowService.getInstance( ).getActions( feedback.get( ).getId( ), feedback.get( ).getResourceType( ), _nIdWorkflow, user ) );
                model.put( FeedbackConstants.MARK_FEEDBACK, feedback.get( ) );

                HtmlTemplate html = AppTemplateService.getTemplate( TEMPLATE_FEEDBACK_DETAIL_VIEW, getLocale( ), model );

                return html.getHtml( );
            }
        }
        catch ( AppException e )
        {
            AppLogService.error( "Error detail feedback for id feedback {} - cause : ", _nIdFeedback, e.getMessage( ), e );
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
                    if ( WorkflowService.getInstance( ).isDisplayTasksForm( _nIdAction, getLocale( ) ) )
                    {
                        String strHtmlTasksForm = WorkflowService.getInstance( ).getDisplayTasksForm( feedback.get( ).getId( ), feedback.get( ).getResourceType( ), _nIdAction, request, getLocale( ),
                                getUser( ) );
                        Map<String, Object> model = new HashMap<>( );
                        model.put( FeedbackConstants.MARK_TASK_FORM, strHtmlTasksForm );
                        return getPage( FeedbackConstants.PROPERTY_PAGE_TITLE_TASKS_FORM_WORKFLOW, TEMPLATE_TASKS_FORM_WORKFLOW, model );
                    }
                    else
                    {
                        WorkflowService.getInstance( ).doProcessAction( feedback.get( ).getId( ), feedback.get( ).getResourceType( ), _nIdAction, feedback.get( ).getIdResource( ), request,
                                getLocale( ), false, getUser( ) );
                    }
                }
                catch ( AppException e )
                {
                    AppLogService.error( "Error processing action for id feedback {} - cause : ", _nIdFeedback, e.getMessage( ), e );
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
                    String strError = WorkflowService.getInstance( ).doSaveTasksForm( feedback.get( ).getId( ), feedback.get( ).getResourceType( ), _nIdAction, feedback.get( ).getIdResource( ),
                            request, getLocale( ), getUser( ) );

                    if ( strError != null )
                    {
                        return redirect( request, strError );
                    }

                    WorkflowService.getInstance( ).doProcessAction( feedback.get( ).getId( ), feedback.get( ).getResourceType( ), _nIdAction, feedback.get( ).getIdResource( ), request, getLocale( ),
                            false, getUser( ) );
                }
                catch ( AppException e )
                {
                    AppLogService.error( "Error processing action for record {}", _nIdFeedback, e );
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
