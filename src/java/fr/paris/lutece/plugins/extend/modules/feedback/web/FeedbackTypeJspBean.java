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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.extend.modules.feedback.business.FeedbackType;
import fr.paris.lutece.plugins.extend.modules.feedback.service.FeedbackTypeService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.IFeedbackTypeService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.util.url.UrlItem;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;

/**
 * 
 * FeedbackTypeJspBean
 *
 */
@SessionScoped
@Named
@Controller( controllerJsp = "ManageFeedbackType.jsp", controllerPath = "jsp/admin/plugins/extend/modules/feedback/", right = "FEEDBACK_TYPE_MANAGEMENT", securityTokenEnabled = true )
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
	private static final String ACTION_MOVE_ORDER = "moveOrder";
	
	// MARKS
	private static final String MARK_FEEDBACK_TYPES = "feedbackTypes";
	private static final String MARK_FEEDBACK_TYPE = "feedbackType";
	
	// PARAMETER
	private static final String PARAMETER_ID = "id";
	private static final String PARAMETER_LABEL = "label";
	private static final String PARAMETER_VALUE = "value";
	private static final String PARAMETER_MOVE_UP = "move_up";
	private static final String PARAMETER_MOVE_DOWN = "move_down";
	
	//PROPERTY
	private static final String PROPERTY_PAGE_TITLE_MANAGE_FEEDBACK_TYPE = "module.extend.feedback.manage_feedback_type.label";
	private static final String PROPERTY_PAGE_TITLE_DETAIL_FEEDBACK_TYPE = "module.extend.feedback.create_feedback_type.label";
	private static final String MESSAGE_ERROR_DUPLICATE_VALUE = "module.extend.feedback.message.error.duplicateValue";
	private static final String PROPERTY_MESSAGE_CONFIRM_REMOVE = "module.extend.feedback.manage_feedback_type.remove.confirm";
	
	// SERVICE
	@Inject
	@Named( "extend-feedback.extendFeedbackTypeService" )
	private IFeedbackTypeService _feedbackTypeService;
	
	/**
	 * Displays the list of feedback types; a request that names no view is redirected to this view by name, so that it issues its token on a core that gives none to a bare default view.
	 * @param request the HTTP request
	 * @param model the model
	 * @return the page
	 */
	@View ( value = VIEW_MANAGE_FEEDBACK_TYPE, defaultView = true, securityTokenAction = ACTION_MOVE_ORDER )
	public String getManageFeedbackTypes ( HttpServletRequest request, Models model )
	{
		if ( request.getParameter( MVCUtils.PARAMETER_VIEW ) == null )
		{
			return redirectView( request, VIEW_MANAGE_FEEDBACK_TYPE );
		}

		model.put( MARK_FEEDBACK_TYPES, _feedbackTypeService.getFeedbackTypesList( ) );

		return getPage( PROPERTY_PAGE_TITLE_MANAGE_FEEDBACK_TYPE, TEMPLATE_MANAGE_FEEDBACK_TYPE );
	}
	
	/**
	 * Displays the creation or modification form of a feedback type.
	 * @param request the HTTP request
	 * @param model the model
	 * @return the page
	 */
	@View ( value = VIEW_DETAIL_FEEDBACK_TYPE, securityTokenAction = ACTION_SAVE_FEEDBACK )
	public String getDetailFeedbackType ( HttpServletRequest request, Models model )
	{
		String strId = request.getParameter( PARAMETER_ID );
		Optional<FeedbackType> feedbackType = StringUtils.isNumeric( strId ) ? _feedbackTypeService.findByPrimaryKey( NumberUtils.toInt( strId ) ) : Optional.empty( );

		if ( request.getParameter( PARAMETER_LABEL ) != null )
		{
			FeedbackType typedFeedbackType = feedbackType.orElseGet( FeedbackType::new );
			typedFeedbackType.setLabel( request.getParameter( PARAMETER_LABEL ) );
			typedFeedbackType.setValue( request.getParameter( PARAMETER_VALUE ) );
			feedbackType = Optional.of( typedFeedbackType );
		}

		feedbackType.ifPresent( type -> model.put( MARK_FEEDBACK_TYPE, type ) );
		
		return getPage( PROPERTY_PAGE_TITLE_DETAIL_FEEDBACK_TYPE, TEMPLATE_DETAIL_FEEDBACK_TYPE );
	}
	
	/**
	 * Creates or updates a feedback type.
	 * @param request the HTTP request
	 * @return the URL of the list
	 */
	@Action ( value = ACTION_SAVE_FEEDBACK )
	public String doSaveFeedback ( HttpServletRequest request )
	{		
		FeedbackType feedbackType = new FeedbackType( );
		populate( feedbackType, request );

		if ( StringUtils.isBlank( feedbackType.getLabel( ) ) || StringUtils.isBlank( feedbackType.getValue( ) ) )
		{
			addError( Messages.MANDATORY_FIELDS, getLocale( ) );
			return redirectToDetail( request, feedbackType );
		}

		if ( isValueTaken( feedbackType ) )
		{
			addError( MESSAGE_ERROR_DUPLICATE_VALUE, getLocale( ) );
			return redirectToDetail( request, feedbackType );
		}

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

	/**
	 * Redirects to the form of a refused feedback type, with what the user typed.
	 * @param request the HTTP request
	 * @param feedbackType the refused feedback type
	 * @return the URL of the form
	 */
	private String redirectToDetail( HttpServletRequest request, FeedbackType feedbackType )
	{
		Map<String, String> mapParameters = new HashMap<>( );
		mapParameters.put( PARAMETER_ID, String.valueOf( feedbackType.getId( ) ) );
		mapParameters.put( PARAMETER_LABEL, StringUtils.defaultString( feedbackType.getLabel( ) ) );
		mapParameters.put( PARAMETER_VALUE, StringUtils.defaultString( feedbackType.getValue( ) ) );

		return redirect( request, VIEW_DETAIL_FEEDBACK_TYPE, mapParameters );
	}

	/**
	 * Tells whether another feedback type already holds the value of this one.
	 * @param feedbackType the feedback type to save
	 * @return true when the value is taken
	 */
	private boolean isValueTaken( FeedbackType feedbackType )
	{
		return _feedbackTypeService.getFeedbackTypesList( ).stream( )
				.anyMatch( other -> other.getId( ) != feedbackType.getId( ) && feedbackType.getValue( ).equals( other.getValue( ) ) );
	}

	/**
	 * Moves a feedback type up or down, according to the button posted.
	 * @param request the HTTP request
	 * @return the URL of the list
	 */
	@Action ( value = ACTION_MOVE_ORDER )
	public String doMoveOrder ( HttpServletRequest request )
	{
		if ( request.getParameter( PARAMETER_MOVE_UP ) != null )
		{
			moveUpOrder( request.getParameter( PARAMETER_MOVE_UP ) );
		}
		else
		{
			moveDownOrder( request.getParameter( PARAMETER_MOVE_DOWN ) );
		}
		return redirectView(request, VIEW_MANAGE_FEEDBACK_TYPE );
	}

	/**
	 * Swaps a feedback type with the one above it.
	 * @param strId the id of the feedback type
	 */
	private void moveUpOrder ( String strId )
	{
		if ( StringUtils.isNumeric( strId ) )
		{
			Optional<FeedbackType> feedbackTypeUp = _feedbackTypeService.findByPrimaryKey( NumberUtils.toInt( strId ) );
		
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
	}
	
	/**
	 * Swaps a feedback type with the one below it.
	 * @param strId the id of the feedback type
	 */
	private void moveDownOrder ( String strId )
	{
		if ( StringUtils.isNumeric( strId ) )
		{
			Optional<FeedbackType> feedbackTypeDown = _feedbackTypeService.findByPrimaryKey( NumberUtils.toInt( strId ) );
		
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
	}
	
    /**
     * Gets the confirmation page of delete feedback type
     * 
     * @param request
     *            The HTTP request
     * @return the html code to confirm
     */
    @View( value = VIEW_REMOVE_FEEDBACK_TYPE, securityTokenAction = ACTION_REMOVE_FEEDBACK )
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
    	
    	if ( StringUtils.isNumeric( strId ) )
    	{
    		_feedbackTypeService.remove( NumberUtils.toInt( strId ) );
    	}

        return redirectView( request, VIEW_MANAGE_FEEDBACK_TYPE);
    }
}