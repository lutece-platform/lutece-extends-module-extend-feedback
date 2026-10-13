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
package fr.paris.lutece.plugins.extend.modules.feedback.web.component;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfig;
import fr.paris.lutece.plugins.extend.business.type.ExtendableResourceType;
import fr.paris.lutece.plugins.extend.modules.feedback.business.ExtendFeedback;
import fr.paris.lutece.plugins.extend.modules.feedback.business.config.FeedbackExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.feedback.service.ExtendFeedbackService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.FeedbackPlugin;
import fr.paris.lutece.plugins.extend.modules.feedback.service.FeedbackTypeService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.IExtendFeedbackService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.IFeedbackCaptchaService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.IFeedbackTypeService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.extender.FeedbackResourceExtender;
import fr.paris.lutece.plugins.extend.modules.feedback.util.constants.FeedbackConstants;
import fr.paris.lutece.plugins.extend.modules.feedback.util.constants.StatusFeedbackEnum;
import fr.paris.lutece.plugins.extend.modules.feedback.util.constants.SortEnum;
import fr.paris.lutece.plugins.extend.service.IExtendableResourceManager;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.ResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.plugins.extend.service.type.IExtendableResourceTypeService;
import fr.paris.lutece.plugins.extend.util.ExtendErrorException;
import fr.paris.lutece.plugins.extend.web.component.AbstractResourceExtenderComponent;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.resource.IExtendableResourceService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import jakarta.enterprise.context.ApplicationScoped;


/**
 *
 * CommentResourceExtenderComponent
 *
 */
@ApplicationScoped
@Named( "extend-feedback.feedbackResourceExtenderComponent" )
public class FeedbackResourceExtenderComponent extends AbstractResourceExtenderComponent
{
    // TEMPLATES
    private static final String TEMPLATE_FEEDBACK = "skin/plugins/extend/modules/feedback/feedback.html";
    private static final String TEMPLATE_FEEDBACK_CONFIG = "admin/plugins/extend/modules/feedback/feedback_config.html";
    private static final String TEMPLATE_FEEDBACK_COMMENT= "admin/plugins/extend/modules/feedback/feedback_comment.html";

    // VARIABLES
    @Inject
    @Named( "extend-feedback.feedbackResourceExtenderConfigService" )
    private IResourceExtenderConfigService _configService;

    @Inject
    @Named( "extend-feedback.feedbackCaptchaService" )
    private IFeedbackCaptchaService _feedbackCaptchaService;

    @Inject
    @Named( "extend-feedback.extendFeedbackService" )
    private IExtendFeedbackService _extendFeedbackService;

    @Inject
    @Named( "extend.resourceExtenderService" )
    private IResourceExtenderService _resourceExtenderService;

    @Inject
    @Named( "extend-feedback.extendFeedbackTypeService" )
    private IFeedbackTypeService _feedbackTypeService;

    @Inject
    @Named( "extend-feedback.feedbackResourceExtender" )
    private IResourceExtender _resourceExtender;

    @Inject
    @Named( "extend.extendableResourceTypeService" )
    private IExtendableResourceTypeService _resourceTypeService;

    @Inject
    @Named( "extend.extendableResourceManager" )
    private IExtendableResourceManager _resourceManager;

    @Inject
    private ISecurityTokenService _securityTokenService;

    @Inject
    private WorkflowService _workflowService;
    

    /**
     * {@inheritDoc}
     */
    @Override
    public IResourceExtender getResourceExtender(  )
    {
        return _resourceExtender;
    }
    
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
        FeedbackExtenderConfig config = _configService.find( getResourceExtender(  ).getKey(  ),
                strIdExtendableResource, strExtendableResourceType );
        String strMessage = StringUtils.EMPTY;
        Map<String, Object> model = new HashMap<>(  );
        
        if ( config != null )
        {
            _feedbackCaptchaService.fillModel( model, config );
            strMessage = config.getMessage(  );
            if ( config.isShowFeedbackTypeList( ) )
            {
                model.put( FeedbackConstants.MARK_LIST_FEEDBACK_TYPE, _feedbackTypeService.getFeedbackTypesList( ) );
            }         
        }

        model.put( FeedbackConstants.MARK_MESSAGE, strMessage );
        model.put( FeedbackConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
        model.put( FeedbackConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );
        model.put( FeedbackConstants.MARK_FEEDBACK_TOKEN,
                _securityTokenService.getToken( request, FeedbackConstants.ACTION_SUBMIT_FEEDBACK ) );
        
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_FEEDBACK, request.getLocale(  ), model );

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
            I18nService.getLocalizedString( FeedbackConstants.PROPERTY_FEEDBACK_CONFIG_LABEL_NO_MAILING_LIST, locale ) );
        listIdsMailingList.addAll( AdminMailingListService.getMailingLists( AdminUserService.getAdminUser( request ) ) );

        Map<String, Object> model = new HashMap<>(  );
        model.put( FeedbackConstants.MARK_FEEDBACK_CONFIG, _configService.find( resourceExtender.getIdExtender(  ) ) );
        model.put( FeedbackConstants.MARK_LIST_IDS_MAILING_LIST, listIdsMailingList );
        model.put( FeedbackConstants.MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( FeedbackConstants.MARK_LOCALE, locale );
        model.put( FeedbackConstants.MARK_WORKFLOW_LIST, _workflowService.getWorkflowsEnabled( ( User ) AdminUserService.getAdminUser( request ), locale ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_FEEDBACK_CONFIG, request.getLocale(  ), model );

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
        Map<String, Object> model = new HashMap<>( );
        fillListModel( resourceExtender, locale, request, model );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_FEEDBACK_COMMENT, request.getLocale(  ), model );

        return template.getHtml( );
    }

    /**
     * Fills the model of the feedback list of an extender: the feedbacks matching the filters of the request, the
     * filter choices, the labels of the resource and feedback types and the address of each feedback's resource.
     *
     * @param resourceExtender
     *            the extender, its resource id being * for every resource
     * @param locale
     *            the locale of the labels
     * @param request
     *            the request holding the filters
     * @param model
     *            the model to fill
     */
    public void fillListModel( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request, Map<String, Object> model )
    {
        ResourceExtenderDTOFilter resourceExtenderDTOFilter = new ResourceExtenderDTOFilter( );
        resourceExtenderDTOFilter.setFilterExtenderType( FeedbackResourceExtender.RESOURCE_EXTENDER );

        Map<String, String> resourceTypeLabels = new TreeMap<>( );
        for ( ResourceExtenderDTO resourceExtenderDTO : _resourceExtenderService.findByFilter( resourceExtenderDTOFilter ) )
        {
            resourceTypeLabels.computeIfAbsent( resourceExtenderDTO.getExtendableResourceType( ), type -> getResourceTypeLabel( type, locale ) );
        }

        ReferenceList refResourceExtenderList = new ReferenceList( );
        refResourceExtenderList.addItem( FeedbackConstants.STAR, "#i18n{module.extend.feedback.feedback_comment.filter.label.resource_type.option}");
        resourceTypeLabels.forEach( refResourceExtenderList::addItem );

        List<ExtendFeedback> extendFeedbackList = _extendFeedbackService.findAllExtendFeedback( request, resourceExtender );
        fillWokflowActionList( extendFeedbackList, resourceExtender, request, model );

        Map<String, String> resourceUrls = new HashMap<>( );
        for ( ExtendFeedback feedback : extendFeedbackList )
        {
            resourceTypeLabels.computeIfAbsent( feedback.getResourceType( ), type -> getResourceTypeLabel( type, locale ) );
            String strUrl = getResourceUrl( String.valueOf( feedback.getIdResource( ) ), feedback.getResourceType( ) );
            if ( StringUtils.isNotEmpty( strUrl ) )
            {
                resourceUrls.put( String.valueOf( feedback.getId( ) ), strUrl );
            }
        }

        request.getSession( ).setAttribute( FeedbackPlugin.PLUGIN_NAME + FeedbackConstants.SESSION_FEEDBACK_ADMIN_POST_BACK_URL, getPostBackUrl( request ) );

        model.put( FeedbackConstants.MARK_LIST_RESOURCE_TYPE, refResourceExtenderList );
        model.put( FeedbackConstants.MARK_LIST_PROCESS_ENUM, StatusFeedbackEnum.getReferenceList(true ) );
        model.put( FeedbackConstants.MARK_LIST_SORT_ENUM, SortEnum.getReferenceList( ) );
        model.put( FeedbackConstants.MARK_LIST_EXTEND_FEEDBACK, extendFeedbackList );
        model.put( FeedbackConstants.MARK_EXTENDABLE_RESOURCE_TYPE, resourceExtender.getExtendableResourceType( ) );
        model.put( FeedbackConstants.MARK_ID_EXTENDABLE_RESOURCE, resourceExtender.getIdExtendableResource( ) );
        model.put( FeedbackConstants.MARK_FILTER_STATUS, request.getParameter( FeedbackConstants.PARAMETER_FILTER_STATUS ) );
        model.put( FeedbackConstants.MARK_FILTER_SORTING, request.getParameter( FeedbackConstants.PARAMETER_FILTER_SORTING ) );
        model.put( FeedbackConstants.MARK_FILTER_RESOURCE_TYPE, request.getParameter( FeedbackConstants.PARAMETER_FILTER_RESOURCE_TYPE ) );
        model.put( FeedbackConstants.MARK_FILTER_FEEDBACK_TYPE, request.getParameter( FeedbackConstants.PARAMETER_FEEDBACK_TYPE_FILTER ) );
        model.put( FeedbackConstants.MARK_LIST_FEEDBACK_TYPE, _feedbackTypeService.getReferenceFeedbackTypesList( ) );
        model.put( FeedbackConstants.MARK_FEEDBACK_TYPE_LABELS, _feedbackTypeService.getFeedbackTypeLabels( ) );
        model.put( FeedbackConstants.MARK_RESOURCE_TYPE_LABELS, resourceTypeLabels );
        model.put( FeedbackConstants.MARK_RESOURCE_URLS, resourceUrls );
    }

    /**
     * Returns the label of a resource type, as the service of that type describes it, or the type itself when no
     * service describes it.
     *
     * @param strResourceType
     *            the resource type
     * @param locale
     *            the locale of the label
     * @return the label
     */
    public String getResourceTypeLabel( String strResourceType, Locale locale )
    {
        ExtendableResourceType resourceType = StringUtils.isEmpty( strResourceType ) ? null : _resourceTypeService.findByPrimaryKey( strResourceType, locale );

        return resourceType != null && StringUtils.isNotBlank( resourceType.getDescription( ) ) ? resourceType.getDescription( ) : strResourceType;
    }

    /**
     * Returns the address of a resource: the form response view for a form response, the main address given by the
     * service of the resource type otherwise.
     *
     * @param strIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @return the address, or null when the resource has none
     */
    public String getResourceUrl( String strIdResource, String strResourceType )
    {
        if ( strResourceType != null && strResourceType.startsWith( FeedbackConstants.RESOURCE_PREFIX ) )
        {
            return FeedbackConstants.URL_FORM_RESPONSE + strIdResource;
        }

        IExtendableResourceService resourceService = StringUtils.isEmpty( strResourceType ) ? null : _resourceManager.getExtendableResourceService( strResourceType );

        return resourceService != null ? resourceService.getResourceUrl( strIdResource, strResourceType ) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSaveConfig( HttpServletRequest request, IExtenderConfig config )
        throws ExtendErrorException
    {
        FeedbackExtenderConfig feedbackConfig = (FeedbackExtenderConfig) config;

        if ( feedbackConfig.getIdMailingList(  ) == -1 )
        {
            throw new ExtendErrorException( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS,
                    request.getLocale(  ) ) );
        }

        _configService.update( config );
    }
    
    /**
     * fillWokflowActionList
     * @param extendFeedbackList
     * @param resourceExtender
     * @param request
     */
    private void fillWokflowActionList( List<ExtendFeedback> extendFeedbackList, ResourceExtenderDTO resourceExtender, HttpServletRequest request,
            Map<String, Object> model )
    {
        User user = AdminUserService.getAdminUser( request );
        Map<String, FeedbackExtenderConfig> configs = new HashMap<String, FeedbackExtenderConfig>( );

        String strKey = null;
        StringBuilder sb = new StringBuilder( );
        boolean bShowFeedbackTypeList = false;
        for ( ExtendFeedback feedback : extendFeedbackList)
        {
            sb.setLength( 0 );
            strKey = sb.append( resourceExtender.getExtenderType( ) ).append( '_' ).append( feedback.getIdResource( ) ).append( '_' )
                    .append( feedback.getResourceType( ) ).toString( );

            if ( !configs.containsKey( strKey ) )
            {
                configs.put( strKey, _configService.find( getResourceExtender( ).getKey( ), feedback.getIdResource( ) + "", feedback.getResourceType( ) ) );
            }
            FeedbackExtenderConfig config = configs.get( strKey );

            if ( null != config && config.getIdWorkflow( ) > 0 )
            {
                bShowFeedbackTypeList |= config.isShowFeedbackTypeList( );
                Collection<Action> workflowActionlist = _workflowService.getActions( feedback.getId( ),
                        feedback.getResourceType( ), config.getIdWorkflow( ), user );

                feedback.setListWorkflowActions( workflowActionlist );
            }
        }
        model.put( FeedbackConstants.MARK_SHOW_FEEDBACK_TYPE_LIST, bShowFeedbackTypeList );
    }
}
