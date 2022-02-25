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

import java.util.List;
import java.util.Optional;

import fr.paris.lutece.plugins.extend.modules.feedback.business.FeedbackType;
import fr.paris.lutece.util.ReferenceList;

/**
 * 
 * IFeedbackTypeService
 *
 */
public interface IFeedbackTypeService
{
	/**
     * Create an instance of the feedbackType class
     * @param feedbackType The instance of the FeedbackType which contains the informations to store
     * @return The  instance of feedbackType which has been created with its primary key.
     */
     FeedbackType create( FeedbackType feedbackType );


    /**
     * Update of the feedbackType which is specified in parameter
     * @param feedbackType The instance of the FeedbackType which contains the data to store
     * @return The instance of the  feedbackType which has been updated
     */

    void update( FeedbackType feedbackType );


    /**
     * Remove the feedbackType whose identifier is specified in parameter
     * @param nFeedbackTypeId The feedbackType Id
     */
    void remove( int nFeedbackTypeId );


    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a feedbackType whose identifier is specified in parameter
     * @param nKey The feedbackType primary key
     * @return an instance of FeedbackType
     */
    Optional<FeedbackType> findByPrimaryKey( int nKey );

    /**
     * Returns an instance of a feedbackType by order
     * @param nOrder The feedbackType nOrder
     * @return an instance of FeedbackType
     */
    Optional<FeedbackType> findByOrder( int nOrder );

    /**
     * Load the data of all the feedbackType objects and returns them in form of a list
     * @return the list which contains the data of all the feedbackType objects
     */
    List<FeedbackType> getFeedbackTypesList( );
    
    /**
     * Return reference list
     * @return the reference list
     */
    ReferenceList getReferenceFeedbackTypesList( );
}
