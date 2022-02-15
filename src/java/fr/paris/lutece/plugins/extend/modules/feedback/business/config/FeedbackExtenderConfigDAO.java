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
package fr.paris.lutece.plugins.extend.modules.feedback.business.config;

import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfigDAO;
import fr.paris.lutece.plugins.extend.modules.feedback.service.FeedbackPlugin;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 *
 * FeedbackConfigDAO
 *
 */
public class FeedbackExtenderConfigDAO implements IExtenderConfigDAO<FeedbackExtenderConfig>
{
    private static final String SQL_QUERY_INSERT = " INSERT INTO extend_feedback_config ( id_extender, message, id_mailing_list, captcha, show_feedback_type_list ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_UPDATE = " UPDATE extend_feedback_config SET message = ?, id_mailing_list = ?, captcha = ?, show_feedback_type_list = ? WHERE id_extender = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM extend_feedback_config WHERE id_extender = ? ";
    private static final String SQL_QUERY_SELECT = " SELECT id_extender, message, id_mailing_list, captcha, show_feedback_type_list FROM extend_feedback_config WHERE id_extender = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( FeedbackExtenderConfig config )
    {
        int nIndex = 1;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, FeedbackPlugin.getPlugin(  ) ) )
        {
	        daoUtil.setInt( nIndex++, config.getIdExtender(  ) );
	        daoUtil.setString( nIndex++, config.getMessage(  ) );
	        daoUtil.setInt( nIndex++, config.getIdMailingList(  ) );
	        daoUtil.setBoolean( nIndex++, config.isCaptcha( ) );
	        daoUtil.setBoolean( nIndex, config.isShowFeedbackTypeList( ) );
	        
	        daoUtil.executeUpdate(  );
	        daoUtil.free(  );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( FeedbackExtenderConfig config )
    {
        int nIndex = 1;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, FeedbackPlugin.getPlugin(  ) ) )
        {
	        daoUtil.setString( nIndex++, config.getMessage(  ) );
	        daoUtil.setInt( nIndex++, config.getIdMailingList(  ) );
	        daoUtil.setBoolean( nIndex++, config.isCaptcha( ) );
	        daoUtil.setBoolean( nIndex++, config.isShowFeedbackTypeList( ) );
	        
	        daoUtil.setInt( nIndex, config.getIdExtender(  ) );
	
	        daoUtil.executeUpdate(  );
	        daoUtil.free(  );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdExtender )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, FeedbackPlugin.getPlugin(  ) ) )
        {
	        daoUtil.setInt( 1, nIdExtender );
	
	        daoUtil.executeUpdate(  );
	        daoUtil.free(  );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeedbackExtenderConfig load( int nIdExtender )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, FeedbackPlugin.getPlugin(  ) ) )
        {
	        daoUtil.setInt( 1, nIdExtender );
	        daoUtil.executeQuery(  );
	
	        FeedbackExtenderConfig config = null;
	
	        if ( daoUtil.next(  ) )
	        {
	            int nIndex = 1;
	            config = new FeedbackExtenderConfig(  );
	            config.setIdExtender( daoUtil.getInt( nIndex++ ) );
	            config.setMessage( daoUtil.getString( nIndex++ ) );
	            config.setIdMailingList( daoUtil.getInt( nIndex++ ) );
	            config.setCaptcha( daoUtil.getBoolean( nIndex++ ) );
	            config.setShowFeedbackTypeList( daoUtil.getBoolean( nIndex++ ) );
	        }
	
	        daoUtil.free(  );
	
	        return config;
        }
    }
}
