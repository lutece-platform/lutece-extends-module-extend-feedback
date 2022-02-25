/*
 * Copyright (c) 2002-2022, City of Paris
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

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.extend.modules.feedback.business.FeedbackType;
import fr.paris.lutece.plugins.extend.modules.feedback.service.FeedbackTypeService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.IFeedbackTypeService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

/**
 * 
 * FeedbackTypeJspBean
 *
 */
@Controller( controllerJsp = "ManageFeedbackType.jsp", controllerPath = "jsp/admin/plugins/extend/modules/feedback/", right = "FEEDBACK_TYPE_MANAGEMENT" )
public class FeedbackTypeJspBean extends MVCAdminJspBean
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 390366409518564861L;
	
	// TEMPLATE
	private static final String TEMPLATE_MANAGE_FEEDBACK_TYPE = "/admin/plugins/extend/modules/feedback/manage_feedback_type.html";
	private static final String TEMPLATE_DETAIL_FEEDBACK_TYPE = "/admin/plugins/extend/modules/feedback/detail_feedback_type.html";
	
	// VIEW
	private static final String VIEW_MANAGE_FEEDBACK_TYPE = "manageView";
	private static final String VIEW_DETAIL_FEEDBACK_TYPE = "detailView";
	private static final String VIEW_REMOVE_FEEDBACK_TYPE = "removeView";
	
	// ACTION
	private static final String ACTION_SAVE_FEEDBACK = "saveFeedbackType";
	private static final String ACTION_REMOVE_FEEDBACK = "removeFeedbackType";
	private static final String ACTION_MOVE_UP_ORDER = "moveUpOrder";
	private static final String ACTION_MOVE_DOWN_ORDER = "moveDownOrder";
	
	// MARKS
	private static final String MARK_FEEDBACK_TYPES = "feedbackTypes";
	private static final String MARK_FEEDBACK_TYPE = "feedbackType";
	
	// PARAMETER
	private static final String PARAMETER_ID = "id";
	
	//PROPERTY
	private static final String PROPERTY_MESSAGE_CONFIRM_REMOVE = "module.extend.feedback.manage_feedback_type.remove.confirm";
	
	// SERVICE
	IFeedbackTypeService _feedbackTypeService = SpringContextService.getBean( FeedbackTypeService.BEAN_SERVICE );
	
	@View ( value = VIEW_MANAGE_FEEDBACK_TYPE, defaultView = true )
	public String getManageFeedbackTypes ( HttpServletRequest request )
	{
		Locale locale = getLocale( );
		Map<String, Object> model = getModel( );
		
		
		model.put( MARK_FEEDBACK_TYPES, _feedbackTypeService.getFeedbackTypesList( ) );
		HtmlTemplate html = AppTemplateService.getTemplate( TEMPLATE_MANAGE_FEEDBACK_TYPE, locale, model );
		
		return html.getHtml( );
	}
	
	@View ( value = VIEW_DETAIL_FEEDBACK_TYPE )
	public String getDetailFeedbackType ( HttpServletRequest request )
	{
		Locale locale = getLocale( );
		
		String strId = request.getParameter( PARAMETER_ID );
		
		Map<String, Object> model = getModel( );
		
		if ( StringUtils.isNotEmpty( strId ) )
		{
			Optional<FeedbackType> feedbackType = _feedbackTypeService.findByPrimaryKey( Integer.parseInt( strId ) );
			if ( feedbackType.isPresent( ) )
			{
				model.put( MARK_FEEDBACK_TYPE, feedbackType.get( )  ) ;
			}
		}
		
		HtmlTemplate html = AppTemplateService.getTemplate( TEMPLATE_DETAIL_FEEDBACK_TYPE , locale, model );
		
		return html.getHtml( );
	}
	
	@Action ( value = ACTION_SAVE_FEEDBACK )
	public String doSaveFeedback ( HttpServletRequest request )
	{		
		FeedbackType feedbackType = new FeedbackType( );
		populate( feedbackType, request );
		
		if( feedbackType.getId( ) != 0 )
		{
			_feedbackTypeService.update( feedbackType );
		}
		else
		{
			_feedbackTypeService.create( feedbackType );
		}
		
		return redirectView(request, VIEW_MANAGE_FEEDBACK_TYPE );
	}

	@Action ( value = ACTION_MOVE_UP_ORDER )
	public String doMoveUpOrder ( HttpServletRequest request )
	{
		String strId = request.getParameter( PARAMETER_ID );
		
		if ( StringUtils.isNotEmpty( strId ) )
		{
			Optional<FeedbackType> feedbackTypeUp = _feedbackTypeService.findByPrimaryKey( Integer.parseInt( strId ) );
		
			if ( feedbackTypeUp.isPresent( )  )
			{
				int nNewOrder = feedbackTypeUp.get( ).getOrder( ) - 1;
				
				//Down order
				Optional<FeedbackType> feedbackTypeDown = _feedbackTypeService.findByOrder( nNewOrder );
				if ( feedbackTypeDown.isPresent( ) )
				{
					feedbackTypeDown.get( ).setOrder( feedbackTypeUp.get( ).getOrder( ) );
					//Update
					_feedbackTypeService.update( feedbackTypeDown.get( ) );
				}
				
				//Up order
				feedbackTypeUp.get( ).setOrder( nNewOrder );
				
				//Update
				_feedbackTypeService.update( feedbackTypeUp.get( ) );
			}
		}
		return redirectView(request, VIEW_MANAGE_FEEDBACK_TYPE );
	}
	
	@Action ( value = ACTION_MOVE_DOWN_ORDER )
	public String doMoveDownOrder ( HttpServletRequest request )
	{
		String strId = request.getParameter( PARAMETER_ID );
		
		if ( StringUtils.isNotEmpty( strId ) )
		{
			Optional<FeedbackType> feedbackTypeDown = _feedbackTypeService.findByPrimaryKey( Integer.parseInt( strId ) );
		
			if ( feedbackTypeDown.isPresent( ) )
			{
				int nNewOrder = feedbackTypeDown.get( ).getOrder( ) + 1;
				
				//Up order
				Optional<FeedbackType> feedbackTypeUp = _feedbackTypeService.findByOrder( nNewOrder );
				if( feedbackTypeUp.isPresent( ) )
				{
					feedbackTypeUp.get( ).setOrder( feedbackTypeDown.get( ).getOrder( ) );
					_feedbackTypeService.update( feedbackTypeUp.get( ) );
				}
				
				//Down order
				feedbackTypeDown.get( ).setOrder( nNewOrder );
				
				//Update
				_feedbackTypeService.update( feedbackTypeDown.get( ) );
			}
		}
		return redirectView(request, VIEW_MANAGE_FEEDBACK_TYPE );
	}
	
    /**
     * Gets the confirmation page of delete feedback type
     * 
     * @param request
     *            The HTTP request
     * @return the html code to confirm
     */
    @View( value = VIEW_REMOVE_FEEDBACK_TYPE )
    public String getConfirmRemoveField( HttpServletRequest request )
    {
        String strId = request.getParameter( PARAMETER_ID );

        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_FEEDBACK ) );
        url.addParameter( PARAMETER_ID, strId );
        
        String strMessageUrl = AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_CONFIRM_REMOVE, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Perform suppression feedback type
     * 
     * @param request
     *            The HTTP request
     * @return The URL to go after performing the action
     */
    @Action( ACTION_REMOVE_FEEDBACK )
    public String doRemoveField( HttpServletRequest request )
    {
    	String strId = request.getParameter( PARAMETER_ID );
    	
    	_feedbackTypeService.remove( Integer.parseInt( strId ) );

        return redirectView( request, VIEW_MANAGE_FEEDBACK_TYPE);
    }

}
