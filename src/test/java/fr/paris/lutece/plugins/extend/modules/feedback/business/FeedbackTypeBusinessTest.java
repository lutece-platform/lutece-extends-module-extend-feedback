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
import fr.paris.lutece.plugins.extend.modules.feedback.service.FeedbackTypeService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.IFeedbackTypeService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class FeedbackTypeBusinessTest extends LuteceTestCase
{
    private static final String LABEL1 = "Label1";
    private static final String LABEL2 = "Label2";
    private static final String VALUE1 = "Value1";
    private static final String VALUE2 = "Value2";
    private static final boolean DEFAULT1 = true;
    private static final boolean DEFAULT2 = true;
    private static final int ORDER1 = 1;
    private static final int ORDER2 = 2;

    public void testBusiness(  )
    {
    	IFeedbackTypeService extendFeedbackService = SpringContextService.getBean( FeedbackTypeService.BEAN_SERVICE );
        
        // Initialize an object
        FeedbackType feedbackType = new FeedbackType();
        feedbackType.setLabel( LABEL1 );
        feedbackType.setDefault( DEFAULT1 );
        feedbackType.setOrder( ORDER1 );
        feedbackType.setValue( VALUE1 );

        // Create test
        feedbackType = extendFeedbackService.create( feedbackType );
        FeedbackType feedbackTypeStored = extendFeedbackService.findByPrimaryKey( feedbackType.getId( ) );
        assertEquals( feedbackTypeStored.getId() , feedbackType.getId() );
        assertEquals( feedbackTypeStored.getLabel() , feedbackType.getLabel() );
        assertEquals( feedbackTypeStored.getDefault() , feedbackType.getDefault() );
        assertEquals( feedbackTypeStored.getOrder() , feedbackType.getOrder() );

        // Update test
        feedbackType.setLabel( LABEL2 );
        feedbackType.setDefault( DEFAULT2 );
        feedbackType.setOrder( ORDER2 );
        feedbackType.setValue( VALUE1 );
        
        extendFeedbackService.update( feedbackType );
        feedbackTypeStored = extendFeedbackService.findByPrimaryKey( feedbackType.getId( ) );
        assertEquals( feedbackTypeStored.getId() , feedbackType.getId() );
        assertEquals( feedbackTypeStored.getLabel() , feedbackType.getLabel() );
        assertEquals( feedbackTypeStored.getDefault() , feedbackType.getDefault() );
        assertEquals( feedbackTypeStored.getOrder() , feedbackType.getOrder() );

        // List test
        extendFeedbackService.getFeedbackTypesList( );

        // Delete test
        extendFeedbackService.remove( feedbackType.getId( ) );
        feedbackTypeStored = extendFeedbackService.findByPrimaryKey( feedbackType.getId( ) );
        assertNull( feedbackTypeStored );
        
    }

}
