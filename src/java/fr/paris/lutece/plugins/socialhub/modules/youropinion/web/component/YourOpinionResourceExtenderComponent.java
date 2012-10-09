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
package fr.paris.lutece.plugins.socialhub.modules.youropinion.web.component;

import fr.paris.lutece.plugins.socialhub.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.socialhub.business.extender.config.IExtenderConfig;
import fr.paris.lutece.plugins.socialhub.modules.youropinion.business.config.YourOpinionExtenderConfig;
import fr.paris.lutece.plugins.socialhub.modules.youropinion.service.IYourOpinionCaptchaService;
import fr.paris.lutece.plugins.socialhub.modules.youropinion.util.constants.YourOpinionConstants;
import fr.paris.lutece.plugins.socialhub.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.plugins.socialhub.util.SocialHubErrorException;
import fr.paris.lutece.plugins.socialhub.web.component.AbstractResourceExtenderComponent;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * CommentResourceExtenderComponent
 *
 */
public class YourOpinionResourceExtenderComponent extends AbstractResourceExtenderComponent
{
    // TEMPLATES
    private static final String TEMPLATE_YOUR_OPINION = "skin/plugins/socialhub/modules/youropinion/your_opinion.html";
    private static final String TEMPLATE_YOUR_OPINION_CONFIG = "admin/plugins/socialhub/modules/youropinion/your_opinion_config.html";

    // VARIABLES
    @Inject
    @Named( YourOpinionConstants.BEAN_CONFIG_SERVICE )
    private IResourceExtenderConfigService _configService;
    @Inject
    private IYourOpinionCaptchaService _yourOpinionCaptchaService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void buildXmlAddOn( String strIdExtendableResource, String strExtendableResourceType, String strParameters,
        StringBuffer strXml )
    {
        // Nothing yet
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPageAddOn( String strIdExtendableResource, String strExtendableResourceType, String strParameters,
        HttpServletRequest request )
    {
        YourOpinionExtenderConfig config = _configService.find( getResourceExtender(  ).getKey(  ),
                strIdExtendableResource, strExtendableResourceType );
        String strMessage = StringUtils.EMPTY;

        if ( config != null )
        {
            strMessage = config.getMessage(  );
        }

        Map<String, Object> model = new HashMap<String, Object>(  );

        _yourOpinionCaptchaService.fillModel( model );

        model.put( YourOpinionConstants.MARK_MESSAGE, strMessage );
        model.put( YourOpinionConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
        model.put( YourOpinionConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_YOUR_OPINION, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request )
    {
        ReferenceList listIdsMailingList = new ReferenceList(  );
        listIdsMailingList.addItem( -1,
            I18nService.getLocalizedString( YourOpinionConstants.PROPERTY_YOUR_OPINION_CONFIG_LABEL_NO_MAILING_LIST,
                locale ) );
        listIdsMailingList.addAll( AdminMailingListService.getMailingLists( AdminUserService.getAdminUser( request ) ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( YourOpinionConstants.MARK_YOUR_OPINION_CONFIG,
            _configService.find( resourceExtender.getIdExtender(  ) ) );
        model.put( YourOpinionConstants.MARK_LIST_IDS_MAILING_LIST, listIdsMailingList );
        model.put( YourOpinionConstants.MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( YourOpinionConstants.MARK_LOCALE, locale );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_YOUR_OPINION_CONFIG, request.getLocale(  ),
                model );

        return template.getHtml(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IExtenderConfig getConfig( int nIdExtender )
    {
        return _configService.find( nIdExtender );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInfoHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request )
    {
        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSaveConfig( HttpServletRequest request, IExtenderConfig config )
        throws SocialHubErrorException
    {
        YourOpinionExtenderConfig yourOpinionConfig = (YourOpinionExtenderConfig) config;

        if ( yourOpinionConfig.getIdMailingList(  ) == -1 )
        {
            throw new SocialHubErrorException( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS,
                    request.getLocale(  ) ) );
        }

        _configService.update( config );
    }
}
