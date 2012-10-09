/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.socialhub.modules.youropinion.web;

import fr.paris.lutece.plugins.socialhub.modules.youropinion.business.config.YourOpinionExtenderConfig;
import fr.paris.lutece.plugins.socialhub.modules.youropinion.service.IYourOpinionCaptchaService;
import fr.paris.lutece.plugins.socialhub.modules.youropinion.service.YourOpinionCaptchaService;
import fr.paris.lutece.plugins.socialhub.modules.youropinion.service.extender.YourOpinionResourceExtender;
import fr.paris.lutece.plugins.socialhub.modules.youropinion.util.constants.YourOpinionConstants;
import fr.paris.lutece.plugins.socialhub.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.socialhub.service.extender.ResourceExtenderService;
import fr.paris.lutece.plugins.socialhub.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.plugins.socialhub.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.socialhub.service.extender.history.ResourceExtenderHistoryService;
import fr.paris.lutece.portal.business.mailinglist.Recipient;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * YourOpinionApp
 *
 */
public class YourOpinionApp implements XPageApplication
{
    // TEMPLATES
    private static final String TEMPLATE_YOUR_OPINION_NOTIFY_MESSAGE = "skin/plugins/socialhub/modules/youropinion/your_opinion_notify_message.html";
    private IYourOpinionCaptchaService _yourOpinionCaptchaService = SpringContextService.getBean( YourOpinionCaptchaService.BEAN_SERVICE );
    private IResourceExtenderHistoryService _resourceHistoryService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );

    /**
     * {@inheritDoc}
     */
    @Override
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
        throws UserNotSignedException, SiteMessageException
    {
        String strMessage = request.getParameter( YourOpinionConstants.PARAMETER_MESSAGE );
        String strIdExtendableResource = request.getParameter( YourOpinionConstants.PARAMETER_ID_EXTENDABLE_RESOURCE );
        String strExtendableResourceType = request.getParameter( YourOpinionConstants.PARAMETER_EXTENDABLE_RESOURCE_TYPE );

        // Test the captcha
        _yourOpinionCaptchaService.validateCaptcha( request );

        if ( StringUtils.isNotBlank( strMessage ) && StringUtils.isNotBlank( strIdExtendableResource ) &&
                StringUtils.isNotBlank( strExtendableResourceType ) )
        {
            IResourceExtenderConfigService configService = SpringContextService.getBean( YourOpinionConstants.BEAN_CONFIG_SERVICE );
            IResourceExtenderService resourceExtenderService = SpringContextService.getBean( ResourceExtenderService.BEAN_SERVICE );
            YourOpinionExtenderConfig config = configService.find( YourOpinionResourceExtender.RESOURCE_EXTENDER,
                    strIdExtendableResource, strExtendableResourceType );
            int nMailingListId = config.getIdMailingList(  );
            Collection<Recipient> listRecipients = AdminMailingListService.getRecipients( nMailingListId );

            for ( Recipient recipient : listRecipients )
            {
                Map<String, Object> model = new HashMap<String, Object>(  );

                String strSenderEmail = AppPropertiesService.getProperty( YourOpinionConstants.PROPERTY_WEBMASTER_EMAIL );
                String strResourceName = resourceExtenderService.getExtendableResourceName( strIdExtendableResource,
                        strExtendableResourceType );

                Object[] params = { strResourceName };
                String strSubject = I18nService.getLocalizedString( YourOpinionConstants.MESSAGE_NOTIFY_SUBJECT,
                        params, request.getLocale(  ) );

                model.put( YourOpinionConstants.MARK_RESOURCE_EXTENDER_NAME, strResourceName );
                model.put( YourOpinionConstants.MARK_MESSAGE, strMessage );

                HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_YOUR_OPINION_NOTIFY_MESSAGE,
                        request.getLocale(  ), model );
                String strBody = template.getHtml(  );

                MailService.sendMailHtml( recipient.getEmail(  ), recipient.getEmail(  ), strSenderEmail, strSubject,
                    strBody );
            }

            // Add to resource extender history
            _resourceHistoryService.create( YourOpinionResourceExtender.RESOURCE_EXTENDER, strIdExtendableResource,
                strExtendableResourceType, request );

            SiteMessageService.setMessage( request, YourOpinionConstants.MESSAGE_MESSAGE_SENT,
                SiteMessage.TYPE_CONFIRMATION );
        }

        SiteMessageService.setMessage( request, Messages.MANDATORY_FIELDS, SiteMessage.TYPE_STOP );

        return null;
    }
}
