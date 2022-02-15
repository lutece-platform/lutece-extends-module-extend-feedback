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
package fr.paris.lutece.plugins.extend.modules.feedback.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.modules.feedback.business.ExtendFeedback;
import fr.paris.lutece.plugins.extend.modules.feedback.business.IExtendFeedbackDAO;
import fr.paris.lutece.plugins.extend.modules.feedback.business.config.FeedbackExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.feedback.service.extender.FeedbackResourceExtender;
import fr.paris.lutece.plugins.extend.modules.feedback.util.constants.FeedbackConstants;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.ResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.extender.history.ResourceExtenderHistoryService;
import fr.paris.lutece.portal.business.mailinglist.Recipient;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * 
 * ExtendFeedbackService
 *
 */
public class ExtendFeedbackService implements IExtendFeedbackService
{
    /** The Constant BEAN_SERVICE. */
    public static final String BEAN_SERVICE = FeedbackPlugin.PLUGIN_NAME + ".extendFeedbackService";
    
    // TEMPLATE
    private static final String TEMPLATE_FEEDBACK_NOTIFY_MESSAGE = "skin/plugins/extend/modules/feedback/feedback_notify_message.html";
  
    @Inject
    IExtendFeedbackDAO _extendFeedbackDAO;
    @Inject
    @Named( ResourceExtenderHistoryService.BEAN_SERVICE  )
    IResourceExtenderHistoryService _resourceHistoryService;
    @Inject
    @Named( FeedbackConstants.BEAN_CONFIG_SERVICE )
    IResourceExtenderConfigService _configService;
    @Inject
    @Named( ResourceExtenderService.BEAN_SERVICE  )
    IResourceExtenderService _resourceExtenderService;

    
    /**
     * {@inheritDoc}
     */
	@Override
	public void create( ExtendFeedback extendFeedback )
	{
		_extendFeedbackDAO.insert( extendFeedback, FeedbackPlugin.getPlugin( ) );		
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void update( ExtendFeedback extendFeedback )
	{
		_extendFeedbackDAO.store( extendFeedback, FeedbackPlugin.getPlugin( ) );		
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void delete( int nId )
	{
		_extendFeedbackDAO.delete( nId, FeedbackPlugin.getPlugin( ) );		
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public List<ExtendFeedback> findAll( )
	{
		return _extendFeedbackDAO.selectExtendFeedbacksList( FeedbackPlugin.getPlugin( ) );
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public ExtendFeedback findById( int nId )
	{
		return _extendFeedbackDAO.load( nId, FeedbackPlugin.getPlugin( ) );
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public List<ExtendFeedback> findAllByIdAndTypeResource( int nIdResource, String strResourceType )
	{
		return _extendFeedbackDAO.findAllByIdAndTypeResource(nIdResource, strResourceType, FeedbackPlugin.getPlugin( ) );
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public boolean doSubmitFeedback( HttpServletRequest request )
	{
        String strMessage = request.getParameter( FeedbackConstants.PARAMETER_MESSAGE );
        String strIdExtendableResource = request.getParameter( FeedbackConstants.PARAMETER_ID_EXTENDABLE_RESOURCE );
        String strExtendableResourceType = request.getParameter( FeedbackConstants.PARAMETER_EXTENDABLE_RESOURCE_TYPE );
        String strFeedbackType = request.getParameter( FeedbackConstants.PARAMETER_FEEDBACK_TYPE );
       
        if ( StringUtils.isNotBlank( strMessage ) && StringUtils.isNotBlank( strIdExtendableResource ) && StringUtils.isNotBlank( strExtendableResourceType ) )
        {
            FeedbackExtenderConfig config = _configService.find( FeedbackResourceExtender.RESOURCE_EXTENDER, strIdExtendableResource, strExtendableResourceType );
            
            for ( Recipient recipient : AdminMailingListService.getRecipients( config.getIdMailingList(  ) ) )
            {
                doSendMail( request, strMessage, strIdExtendableResource, strExtendableResourceType, recipient );
            }

            // Add to resource extender history
            ResourceExtenderHistory resourceExtenderHistory = _resourceHistoryService.create( FeedbackResourceExtender.RESOURCE_EXTENDER, strIdExtendableResource, strExtendableResourceType, request );
            
            // Add extendFeedback
            create( new ExtendFeedback( Integer.parseInt( strIdExtendableResource ), strExtendableResourceType, strMessage, resourceExtenderHistory, strFeedbackType ) );
            
            return true;
        }
        return false;
	}

	/**
	 * Send mail
	 * @param request
	 * @param strMessage
	 * @param strIdExtendableResource
	 * @param strExtendableResourceType
	 * @param recipient
	 */
	private void doSendMail(HttpServletRequest request, String strMessage, String strIdExtendableResource,
			String strExtendableResourceType, Recipient recipient)
	{
		Map<String, Object> model = new HashMap< >(  );

		String strSenderEmail = AppPropertiesService.getProperty( FeedbackConstants.PROPERTY_WEBMASTER_EMAIL );
		String strResourceName = _resourceExtenderService.getExtendableResourceName( strIdExtendableResource, strExtendableResourceType );

		Object[] params = { strResourceName };
		String strSubject = I18nService.getLocalizedString( FeedbackConstants.MESSAGE_NOTIFY_SUBJECT, params, request.getLocale(  ) );

		model.put( FeedbackConstants.MARK_RESOURCE_EXTENDER_NAME, strResourceName );
		model.put( FeedbackConstants.MARK_MESSAGE, strMessage );

		HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_FEEDBACK_NOTIFY_MESSAGE, request.getLocale(  ), model );

		MailService.sendMailHtml( recipient.getEmail(  ), recipient.getEmail(  ), strSenderEmail, strSubject, template.getHtml(  ) );
	}

	@Override
	public List<ExtendFeedback> findAllExtendFeedback(HttpServletRequest request , ResourceExtenderDTO resourceExtenderDTO)
	{
        String strStatus = request.getParameter( FeedbackConstants.PARAMETER_FILTER_STATUS );
        String strSorting = request.getParameter( FeedbackConstants.PARAMETER_FILTER_SORTING );
        String strExtendableResourceTypeFilter = request.getParameter( FeedbackConstants.PARAMETER_FILTER_RESOURCE_TYPE );
        String strFeedbackTypeFilter = request.getParameter( FeedbackConstants.PARAMETER_FEEDBACK_TYPE_FILTER );
        
		return _extendFeedbackDAO.selectExtendFeedbacksList( strStatus, strSorting, strFeedbackTypeFilter, strExtendableResourceTypeFilter, resourceExtenderDTO, FeedbackPlugin.getPlugin( ) );
	}
}
