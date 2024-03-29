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

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.portal.service.plugin.Plugin;
import java.util.List;
import java.util.Optional;



 /**
 * IExtendFeedbackDAO Interface
 */

public interface IExtendFeedbackDAO
{


    /**
     * Insert a new record in the table.
     * @param extendFeedback instance of the ExtendFeedback object to inssert
     * @param plugin the Plugin
     */

    void insert( ExtendFeedback extendFeedback, Plugin plugin );



     /**
     * Update the record in the table
     * @param extendFeedback the reference of the ExtendFeedback
     * @param plugin the Plugin
     */

    void store( ExtendFeedback extendFeedback, Plugin plugin );


    /**
     * Delete a record from the table
     * @param nIdExtendFeedback int identifier of the ExtendFeedback to delete
     * @param plugin the Plugin
     */

    void delete( int nIdExtendFeedback, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * @param strId The identifier of the extendFeedback
     * @param plugin the Plugin
     * @return The instance of the extendFeedback
     */

    Optional<ExtendFeedback> load( int nKey, Plugin plugin );



    /**
     * Load the data from the table
     * @param nIdResource The identifier of resource
     * @param strResourceType The type of resource
     * @param plugin the Plugin
     * @return The instance of the extendFeedback
     */

    List<ExtendFeedback> findAllByIdAndTypeResource( int nIdResource, String strResourceType, Plugin plugin );
    
    /**
     * Load the data from the table
     * @param nIdHistory 
     * @param plugin the Plugin
     * @return The instance of the extendFeedback
     */

    Optional<ExtendFeedback> findByIdHistory( int nIdHistory, Plugin plugin );
    
     /**
     * Load the data of all the extendFeedback objects and returns them as a List
     * @param plugin the Plugin
     * @return The List which contains the data of all the extendFeedback objects
     */

    List<ExtendFeedback> selectExtendFeedbacksList( Plugin plugin );
    
    /**
    * Load the data of all the extendFeedback objects and returns them as a List
    * @param plugin the Plugin
    * @return The List which contains the data of all the extendFeedback objects
    */

   List<ExtendFeedback> selectExtendFeedbacksList( String strStatus, String strSorting, String strFeedbackType, String extendableResourceTypeFilter, ResourceExtenderDTO resourceExtenderDTO, Plugin plugin );
    
}
