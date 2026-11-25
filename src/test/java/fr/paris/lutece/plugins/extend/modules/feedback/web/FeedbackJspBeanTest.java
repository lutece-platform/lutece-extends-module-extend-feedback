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
package fr.paris.lutece.plugins.extend.modules.feedback.web;

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.modules.feedback.business.ExtendFeedback;
import fr.paris.lutece.plugins.extend.modules.feedback.service.IExtendFeedbackService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

/**
 * Detail of a feedback and workflow action of the back office.
 */
public class FeedbackJspBeanTest extends LuteceTestCase
{
    private static final String RIGHT = "FEEDBACK_MANAGEMENT";
    private static final String TYPE = "E2E_FEEDBACK_BO";

    /** The mock response of the test library keeps no header: the redirection is recorded here. */
    private static final class RecordingResponse extends MockHttpServletResponse
    {
        private String _strLocation;

        @Override
        public void sendRedirect( String strLocation )
        {
            _strLocation = strLocation;
        }
    }

    @Inject
    private FeedbackJspBean _bean;
    @Inject
    private IExtendFeedbackService _feedbackService;
    @Inject
    private ISecurityTokenService _securityTokenService;

    private MockHttpServletRequest request( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), RIGHT );

        return request;
    }

    private String detail( ExtendFeedback feedback, String strIdWorkflow, RecordingResponse response ) throws Exception
    {
        MockHttpServletRequest request = request( );
        request.addParameter( "view", "detail_feedback_view" );
        request.addParameter( "id_feedback", String.valueOf( feedback.getId( ) ) );
        request.addParameter( "id_workflow", strIdWorkflow );

        return _bean.processController( request, response );
    }

    /**
     * EX-02: the link of the list carries an empty id_workflow when the feedback has no workflow action: the detail is displayed anyway.
     *
     * @throws Exception
     *             if the controller fails
     */
    @Test
    public void testDetailWithoutWorkflow( ) throws Exception
    {
        ExtendFeedback feedback = feedback( "e2e detail without workflow" );

        try
        {
            RecordingResponse response = new RecordingResponse( );
            String strHtml = detail( feedback, "", response );
            assertNull( response._strLocation, "redirected to " + response._strLocation );
            assertNotNull( strHtml );
            assertTrue( strHtml.contains( "e2e detail without workflow" ) );
        }
        finally
        {
            _feedbackService.delete( feedback.getId( ) );
        }
    }

    /**
     * EX-05: the workflow action applies to the feedback of the form that posted it, not to the last detail opened in the session.
     *
     * @throws Exception
     *             if the controller fails
     */
    @Test
    public void testActionAppliesToThePostedFeedback( ) throws Exception
    {
        ExtendFeedback first = feedback( "e2e first" );
        ExtendFeedback second = feedback( "e2e second" );

        try
        {
            detail( first, "0", new RecordingResponse( ) );
            detail( second, "0", new RecordingResponse( ) );

            MockHttpServletRequest request = request( );
            request.setMethod( "POST" );
            request.addParameter( "action", "do_process_workflow_action" );
            request.addParameter( "id_action", "999999" );
            request.addParameter( "id_feedback", String.valueOf( first.getId( ) ) );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, "do_process_workflow_action" ) );
            RecordingResponse response = new RecordingResponse( );
            String strResult = _bean.processController( request, response );

            String strLocation = response._strLocation;
            assertNotNull( (Object) strLocation, "status " + response.getStatus( ) + " result " + strResult + " body " + response.getContentAsString( ) );
            assertTrue( strLocation.contains( "id_feedback=" + first.getId( ) ), strLocation );
        }
        finally
        {
            _feedbackService.delete( first.getId( ) );
            _feedbackService.delete( second.getId( ) );
        }
    }

    private ExtendFeedback feedback( String strComment )
    {
        ExtendFeedback feedback = new ExtendFeedback( );
        feedback.setIdResource( 1 );
        feedback.setResourceType( TYPE );
        feedback.setComment( strComment );
        ResourceExtenderHistory history = new ResourceExtenderHistory( );
        history.setIdHistory( 0 );
        feedback.setResourceExtenderHistory( history );
        _feedbackService.create( feedback );

        return feedback;
    }
}
