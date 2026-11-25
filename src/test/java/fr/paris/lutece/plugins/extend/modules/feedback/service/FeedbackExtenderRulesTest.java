/*
 * Copyright (c) 2002-2026, City of Paris
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

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.modules.feedback.business.ExtendFeedback;
import fr.paris.lutece.plugins.extend.modules.feedback.business.config.FeedbackExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.feedback.service.extender.FeedbackResourceExtender;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;

import fr.paris.lutece.plugins.extend.service.IExtendableResourceManager;
import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.service.page.PageExtendableResourceService;
import fr.paris.lutece.portal.service.resource.IExtendableResource;
import fr.paris.lutece.portal.service.resource.IExtendableResourceService;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.inject.Named;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

/**
 * Feedback list of an extender, submission gate and configuration of a new extender.
 */
public class FeedbackExtenderRulesTest extends LuteceTestCase
{
    private static final String TYPE = "E2E_FEEDBACK_TYPE";
    private static final String OTHER_TYPE = "E2E_FEEDBACK_OTHER";
    private static final String PAGE = "PAGE";

    @Inject
    private IExtendFeedbackService _feedbackService;
    @Inject
    private IResourceExtenderService _extenderService;
    @Inject
    private FeedbackResourceExtender _feedbackExtender;
    @Inject
    @Named( "extend-feedback.feedbackResourceExtenderConfigService" )
    private IResourceExtenderConfigService _configService;

    /**
     * EX-03: the information screen of an extender lists the feedback of its own resource type only.
     */
    @Test
    public void testListOfAnExtenderIgnoresOtherResourceTypes( )
    {
        ExtendFeedback a = feedback( 77, TYPE, "e2e A" );
        ExtendFeedback b = feedback( 77, OTHER_TYPE, "e2e B" );

        try
        {
            ResourceExtenderDTO extender = new ResourceExtenderDTO( );
            extender.setExtenderType( FeedbackResourceExtender.RESOURCE_EXTENDER );
            extender.setIdExtendableResource( "77" );
            extender.setExtendableResourceType( TYPE );

            List<String> listComments = _feedbackService.findAllExtendFeedback( new MockHttpServletRequest( ), extender ).stream( )
                    .map( ExtendFeedback::getComment ).collect( Collectors.toList( ) );
            assertTrue( listComments.contains( "e2e A" ), listComments.toString( ) );
            assertFalse( listComments.contains( "e2e B" ), listComments.toString( ) );

            extender.setIdExtendableResource( "*" );
            listComments = _feedbackService.findAllExtendFeedback( new MockHttpServletRequest( ), extender ).stream( ).map( ExtendFeedback::getComment )
                    .collect( Collectors.toList( ) );
            assertTrue( listComments.contains( "e2e A" ), listComments.toString( ) );
            assertFalse( listComments.contains( "e2e B" ), listComments.toString( ) );
        }
        finally
        {
            _feedbackService.delete( a.getId( ) );
            _feedbackService.delete( b.getId( ) );
        }
    }

    /**
     * EX-04: feedback is accepted only on an enabled extender of an existing resource.
     */
    @Test
    public void testFeedbackOpenOnlyOnAnEnabledExtender( )
    {
        // page 1 exists in the test database, page 987654 does not
        ResourceExtenderDTO extender = extender( "1", PAGE );

        try
        {
            assertTrue( _feedbackService.isFeedbackOpen( "1", PAGE, new MockHttpServletRequest( ) ) );
            assertFalse( _feedbackService.isFeedbackOpen( "987654", PAGE, new MockHttpServletRequest( ) ), "no extender" );
            assertFalse( _feedbackService.isFeedbackOpen( "1", "PA%", new MockHttpServletRequest( ) ), "SQL pattern as type" );
            assertFalse( _feedbackService.isFeedbackOpen( "*", PAGE, new MockHttpServletRequest( ) ), "wildcard id" );

            extender.setIsActive( false );
            _extenderService.update( extender );
            assertFalse( _feedbackService.isFeedbackOpen( "1", PAGE, new MockHttpServletRequest( ) ), "disabled extender" );
        }
        finally
        {
            _extenderService.remove( extender.getIdExtender( ) );
        }

        ResourceExtenderDTO wildcard = extender( "*", PAGE );

        try
        {
            assertTrue( _feedbackService.isFeedbackOpen( "1", PAGE, new MockHttpServletRequest( ) ) );
            assertFalse( _feedbackService.isFeedbackOpen( "987654", PAGE, new MockHttpServletRequest( ) ), "wildcard extender, page that does not exist" );
        }
        finally
        {
            _extenderService.remove( wildcard.getIdExtender( ) );
        }
    }

    /**
     * EX-14: no feedback on a page restricted to a role the visitor does not hold (same rule as the portal: Page.isVisible).
     */
    @Test
    public void testNoFeedbackOnAPageTheVisitorCannotSee( )
    {
        Page restricted = new Page( )
        {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible( HttpServletRequest request )
            {
                return false;
            }
        };
        restricted.setId( 42 );
        Page open = new Page( );
        open.setId( 43 );

        assertFalse( ExtendFeedbackService.isExistingResource( pageManager( restricted ), "42", PAGE, new MockHttpServletRequest( ) ) );
        assertTrue( ExtendFeedbackService.isExistingResource( pageManager( open ), "43", PAGE, new MockHttpServletRequest( ) ) );
    }

    private static IExtendableResourceManager pageManager( Page page )
    {
        return new IExtendableResourceManager( )
        {
            @Override
            public List<IExtendableResourceService> getExtendableResourceServices( )
            {
                return List.of( );
            }

            @Override
            public IExtendableResourceService getExtendableResourceService( String strResourceType )
            {
                return new PageExtendableResourceService( );
            }

            @Override
            public IExtendableResource getResource( String strIdResource, String strResourceType )
            {
                return page;
            }

            @Override
            public boolean useDefaultExtendableResourceService( String strIdResource, String strResourceType )
            {
                return false;
            }
        };
    }

    /**
     * EX-07: a new extender takes every field of the default configuration.
     */
    @Test
    public void testNewExtenderInheritsTheDefaultConfiguration( )
    {
        FeedbackExtenderConfig defaultConfig = _configService.find( -1 );
        assertNotNull( defaultConfig );

        boolean bCaptcha = defaultConfig.isCaptcha( );
        boolean bShowList = defaultConfig.isShowFeedbackTypeList( );
        boolean bAuthenticated = defaultConfig.isAuthenticatedMode( );
        int nIdWorkflow = defaultConfig.getIdWorkflow( );
        ResourceExtenderDTO extender = new ResourceExtenderDTO( );
        extender.setIdExtender( 987654 );

        try
        {
            defaultConfig.setCaptcha( true );
            defaultConfig.setShowFeedbackTypeList( true );
            defaultConfig.setAuthenticatedMode( true );
            defaultConfig.setIdWorkflow( 42 );
            _configService.update( defaultConfig );

            _feedbackExtender.doCreateResourceAddOn( extender );

            FeedbackExtenderConfig config = _configService.find( 987654 );
            assertTrue( config.isCaptcha( ) );
            assertTrue( config.isShowFeedbackTypeList( ) );
            assertTrue( config.isAuthenticatedMode( ) );
            assertEquals( 42, config.getIdWorkflow( ) );
            assertEquals( defaultConfig.getIdMailingList( ), config.getIdMailingList( ) );
            assertEquals( defaultConfig.getMessage( ), config.getMessage( ) );
        }
        finally
        {
            _configService.remove( 987654 );
            defaultConfig.setCaptcha( bCaptcha );
            defaultConfig.setShowFeedbackTypeList( bShowList );
            defaultConfig.setAuthenticatedMode( bAuthenticated );
            defaultConfig.setIdWorkflow( nIdWorkflow );
            _configService.update( defaultConfig );
        }
    }

    private ResourceExtenderDTO extender( String strId, String strType )
    {
        ResourceExtenderDTO extender = new ResourceExtenderDTO( );
        extender.setExtenderType( FeedbackResourceExtender.RESOURCE_EXTENDER );
        extender.setIdExtendableResource( strId );
        extender.setExtendableResourceType( strType );
        extender.setIsActive( true );
        _extenderService.create( extender );

        return extender;
    }

    private ExtendFeedback feedback( int nIdResource, String strType, String strComment )
    {
        ExtendFeedback feedback = new ExtendFeedback( );
        feedback.setIdResource( nIdResource );
        feedback.setResourceType( strType );
        feedback.setComment( strComment );
        ResourceExtenderHistory history = new ResourceExtenderHistory( );
        history.setIdHistory( 0 );
        feedback.setResourceExtenderHistory( history );
        _feedbackService.create( feedback );

        return feedback;
    }
}
