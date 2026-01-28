package fr.paris.lutece.plugins.extend.modules.feedback.service.extender;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.modules.feedback.business.config.FeedbackExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.feedback.service.ExtendFeedbackService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.IExtendFeedbackService;
import fr.paris.lutece.plugins.extend.modules.feedback.util.constants.FeedbackConstants;
import fr.paris.lutece.plugins.extend.service.extender.AbstractResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfigDAO;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.config.ResourceExtenderConfigService;
import fr.paris.lutece.plugins.extend.modules.feedback.web.component.FeedbackResourceExtenderComponent;
import fr.paris.lutece.plugins.extend.modules.feedback.business.config.FeedbackExtenderConfigDAO;

import fr.paris.lutece.portal.service.cache.Lutece107Cache;
import fr.paris.lutece.portal.service.cache.LuteceCache;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class FeedbackResourceExtenderConfigServiceProducer {

    @Inject
    @LuteceCache(cacheName = "extendConfigCache", keyType = String.class, valueType = Object.class, enable = true)
    Lutece107Cache<String, Object> _extendConfigCache;

    @Inject 
    IExtenderConfigDAO<FeedbackExtenderConfig> feedbackExtenderConfigDAO;

    @Inject
    IResourceExtenderService resourceExtenderService;
        
    @Produces
    @ApplicationScoped
    @Named( "extend-feedback.feedbackResourceExtenderConfigService" )
    public IResourceExtenderConfigService produceFeedbackResourceExtenderConfigService( )
    {
        ResourceExtenderConfigService feedbackExtenderConfigService = new ResourceExtenderConfigService( );

        feedbackExtenderConfigService.setExtenderConfigDAO( ( IExtenderConfigDAO ) feedbackExtenderConfigDAO );
        feedbackExtenderConfigService.setExtenderService( resourceExtenderService );
        feedbackExtenderConfigService.setExtenderCache( _extendConfigCache );

        return feedbackExtenderConfigService;
    }
}