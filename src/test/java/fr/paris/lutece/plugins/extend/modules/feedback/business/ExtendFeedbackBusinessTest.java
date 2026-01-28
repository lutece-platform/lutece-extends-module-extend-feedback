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

package fr.paris.lutece.plugins.extend.modules.feedback.business;

import fr.paris.lutece.test.LuteceTestCase;

import java.util.Optional;

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.modules.feedback.service.ExtendFeedbackService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.IExtendFeedbackService;

import jakarta.enterprise.inject.spi.CDI;

public class ExtendFeedbackBusinessTest extends LuteceTestCase
{
    private static final int IDRESOURCE1 = 1;
    private static final int IDRESOURCE2 = 2;
    private static final int IDHISTORY1 = 1;
    private static final int IDHISTORY2 = 2;
    private static final String RESOURCETYPE1 = "ResourceType1";
    private static final String RESOURCETYPE2 = "ResourceType2";
    private static final String COMMENT1 = "Comment1";
    private static final String COMMENT2 = "Comment2";

    public void testBusiness(  )
    {
    	IExtendFeedbackService extendFeedbackService = CDI.current().select(ExtendFeedbackService.class).get();
    	
        // Initialize an object
        ExtendFeedback extendFeedback = new ExtendFeedback();
        extendFeedback.setIdResource( IDRESOURCE1 );
        extendFeedback.setResourceType( RESOURCETYPE1 );
        extendFeedback.setComment( COMMENT1 );
        extendFeedback.setIdResource( IDHISTORY1 );
        
        ResourceExtenderHistory resourceExtenderHistory = new ResourceExtenderHistory( );
        resourceExtenderHistory.setIdHistory( IDHISTORY1 );
        extendFeedback.setResourceExtenderHistory( resourceExtenderHistory );
        
        // Create test
        extendFeedbackService.create( extendFeedback );
        Optional<ExtendFeedback> extendFeedbackStored = extendFeedbackService.findById( extendFeedback.getId( ) );
        if ( extendFeedbackStored.isPresent( ) )
        {
	        assertEquals( extendFeedbackStored.get( ).getId() , extendFeedback.getId() );
	        assertEquals( extendFeedbackStored.get( ).getIdResource() , extendFeedback.getIdResource() );
	        assertEquals( extendFeedbackStored.get( ).getResourceType() , extendFeedback.getResourceType() );
	        assertEquals( extendFeedbackStored.get( ).getComment() , extendFeedback.getComment() );
        }

        // Update test
        extendFeedback.setIdResource( IDRESOURCE2 );
        extendFeedback.setResourceType( RESOURCETYPE2 );
        extendFeedback.setComment( COMMENT2 );
        extendFeedback.setIdResource( IDHISTORY2 );
        
        resourceExtenderHistory.setIdHistory( IDHISTORY2 );
        extendFeedback.setResourceExtenderHistory( resourceExtenderHistory );
        
        extendFeedbackService.update( extendFeedback );
        extendFeedbackStored = extendFeedbackService.findById( extendFeedback.getId( ) );
        if( extendFeedbackStored.isPresent( ) )
        {
	        assertEquals( extendFeedbackStored.get( ).getId() , extendFeedback.getId() );
	        assertEquals( extendFeedbackStored.get( ).getIdResource() , extendFeedback.getIdResource() );
	        assertEquals( extendFeedbackStored.get( ).getResourceType() , extendFeedback.getResourceType() );
	        assertEquals( extendFeedbackStored.get( ).getComment() , extendFeedback.getComment() );
        }

        // List test
        extendFeedbackService.findAll( );

        // Delete test
        extendFeedbackService.delete( extendFeedback.getId( ) );
        extendFeedbackStored = extendFeedbackService.findById( extendFeedback.getId() );
        if(extendFeedbackStored.isPresent( ) )
        {
        	assertNull( extendFeedbackStored.get( ) );
        }
        
    }

}
