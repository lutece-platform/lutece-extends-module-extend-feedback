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

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.modules.feedback.business.ExtendFeedback;
import fr.paris.lutece.plugins.extend.modules.feedback.business.config.FeedbackExtenderConfig;

/**
 * 
 * IExtendFeedbackService
 *
 */
public interface IExtendFeedbackService
{
    /**
     * Create a new extendFeedback
     * 
     * @param extendFeedback
     *            the extendFeedback
     */
    void create( ExtendFeedback extendFeedback );

    /**
     * Update a ExtendFeedback
     * 
     * @param extendFeedback
     *            the extendFeedback
     */
    void update( ExtendFeedback extendFeedback );

    /**
     * Delete extendFeedback by id
     * 
     * @param nId
     *            the id extendFeedback
     */
    void delete( int nId );

    /**
     * Find extendFeedback by id
     * 
     * @param nId
     *            the id extendFeedback
     */
    Optional<ExtendFeedback> findById( int nId );
    
    
    /**
     * Find all extendFeedback by id resource and resource type
     * 
     * @param nIdResource
     *            the id resource
     * @param strResourceType
     *            the resource type
     */
    List<ExtendFeedback> findAllByIdAndTypeResource( int nIdResource, String strResourceType );

    /**
     * Find extendFeedback by id history
     * 
     * @param nIdHistory
     *            the id history
     */
    Optional<ExtendFeedback> findByIdHistory( int nIdHistory );
    
    /**
     * Find all the extendFeedback
     * 
     * @return a list of {@link ExtendFeedback}
     */
    List<ExtendFeedback> findAll( );
    
    /**
     * Process feedback
     * @param request 
     * 			the request
     */
    boolean doSubmitFeedback( HttpServletRequest request );
    
    /**
     * Gets list of extendFeedback filtred
     * @param strStatus
     * @param strSorting
     * @param extendableResourceTypeFilter
     * @param resourceExtenderDTO
     * @return list of extendFeedback filtred
     */
    List<ExtendFeedback> findAllExtendFeedback( HttpServletRequest request, ResourceExtenderDTO resourceExtenderDTO );
    
    /**
     * Process Workflow
     * @param extendFeedback
     * @param config
     * @return true if workflow is processed
     */
    boolean doProcessWorkflow( ExtendFeedback extendFeedback, FeedbackExtenderConfig config );
    
    /**
     * Returns true if user is authorized for send feedback
     * @param request
     * @param config
     * @return
     */
    boolean isAuthorized( HttpServletRequest request, FeedbackExtenderConfig config );
}
