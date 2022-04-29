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
package fr.paris.lutece.plugins.extend.modules.feedback.web;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.extend.modules.feedback.business.ExtendFeedback;
import fr.paris.lutece.plugins.extend.modules.feedback.service.ExtendFeedbackService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.FeedbackPlugin;
import fr.paris.lutece.plugins.extend.modules.feedback.service.IExtendFeedbackService;
import fr.paris.lutece.plugins.extend.modules.feedback.util.constants.FeedbackConstants;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.url.UrlItem;

/**
 * 
 * FeedbackJspBean
 *
 */
public class FeedbackJspBean extends PluginAdminPageJspBean
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6287952387536871603L;
	
	// Service
    private IExtendFeedbackService _extendFeedbackService = SpringContextService.getBean( ExtendFeedbackService.BEAN_SERVICE );
    
    // JSP
    private static final String JSP_FEEDBACK_LIST = "../../ViewExtenderInfo.jsp?feedbackTypeFilter=*&sorting=*&extenderType=feedback&extendableResourceType=FORMS_FORM_RESPONSE_1&extendableResourceTypeFilter=*&idExtendableResource=*&status=*";
    
    /**
     * Do process a workflow action over an feedback
     * 
     * @param request
     *            The request
     * @return The next URL to redirect to
     */
    public String doProcessWorkflowAction( HttpServletRequest request )
    {
        String strIdAction = request.getParameter( FeedbackConstants.PARAMETER_ID_ACTION );
        String strIdFeedback = request.getParameter( FeedbackConstants.PARAMETER_ID_FEEDBACK );

        if ( StringUtils.isNotEmpty( strIdAction ) && StringUtils.isNumeric( strIdAction ) && StringUtils.isNotEmpty( strIdFeedback )
                && StringUtils.isNumeric( strIdFeedback ) )
        {
            int nIdAction = Integer.parseInt( strIdAction );
            int nIdFeedback = Integer.parseInt( strIdFeedback );
            
            Optional<ExtendFeedback> feedback = _extendFeedbackService.findById( nIdFeedback );
        	User user = AdminUserService.getAdminUser( request );
        	
            if( feedback.isPresent( ) )
            {	
                if ( WorkflowService.getInstance( ).isDisplayTasksForm( nIdAction, getLocale( ) ) )
                {
                    String strError = WorkflowService.getInstance( ).doSaveTasksForm( feedback.get( ).getId( ), feedback.get( ).getResourceType( ), nIdAction, feedback.get( ).getIdResource( ), request,
                            getLocale( ), user );
                    
                    if ( strError != null )
                    {
                        AppLogService.error( strError );
                        return AdminMessageService.getMessageUrl( request, FeedbackConstants.MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR );
                    }
                }
                else
                {
                	WorkflowService.getInstance( ).doProcessAction( feedback.get( ).getId( ), feedback.get( ).getResourceType( ), nIdAction, feedback.get( ).getIdResource( ), request, getLocale( ), false, user );
                }
            }

            String strPostBackUrl = (String) request.getSession( )
                    .getAttribute( FeedbackPlugin.PLUGIN_NAME + FeedbackConstants.SESSION_FEEDBACK_ADMIN_POST_BACK_URL );

            request.getSession( ).setAttribute( FeedbackPlugin.PLUGIN_NAME + FeedbackConstants.SESSION_FEEDBACK_ADMIN_POST_BACK_URL, null );

            UrlItem url = new UrlItem( strPostBackUrl );

            return url.getUrl( );

        }
        return AdminMessageService.getMessageUrl( request, FeedbackConstants.MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR );
    }
    
    /**
     * Redirect to feedback list view
     * @param request
     * @return
     */
    public String doRedirectFeedbackListView( )
    {
        UrlItem url = new UrlItem( JSP_FEEDBACK_LIST );
        return url.getUrl( );
    }
}
