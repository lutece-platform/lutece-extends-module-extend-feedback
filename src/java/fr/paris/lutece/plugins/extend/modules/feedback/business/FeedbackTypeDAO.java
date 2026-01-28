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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.Statement;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * This class provides Data Access methods for FeedbackType objects
 */
@ApplicationScoped
public final class FeedbackTypeDAO implements IFeedbackTypeDAO
{
	// Constants
	private static final String SQL_QUERY_SELECT = "SELECT id, label, value_type, default_option, order_type FROM extend_feedback_type WHERE id = ?";
	private static final String SQL_QUERY_INSERT = "INSERT INTO extend_feedback_type ( id, label, value_type, default_option, order_type ) VALUES ( ?, ?, ?, ?, ? ) ";
	private static final String SQL_QUERY_DELETE = "DELETE FROM extend_feedback_type WHERE id = ? ";
	private static final String SQL_QUERY_UPDATE = "UPDATE extend_feedback_type SET id = ?, label = ?, value_type = ?, default_option = ?, order_type = ? WHERE id = ?";
	private static final String SQL_QUERY_SELECTALL = "SELECT id, label, value_type, default_option, order_type FROM extend_feedback_type ORDER BY order_type";
	private static final String SQL_QUERY_SELECT_BY_ORDER = "SELECT id, label, value_type, default_option, order_type FROM extend_feedback_type WHERE order_type = ?";
	private static final String SQL_QUERY_MAX_ORDER = "SELECT max(order_type) FROM extend_feedback_type";
        
    /**
     * {@inheritDoc }
     */
    @Override
	public FeedbackType insert( FeedbackType feedbackType, Plugin plugin )
	{
		try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT , Statement.RETURN_GENERATED_KEYS, plugin ) )
		{             
            int nIndex = 1;
            daoUtil.setInt ( nIndex++, feedbackType.getId ( ) );
            daoUtil.setString ( nIndex++, feedbackType.getLabel ( ) );
            daoUtil.setString ( nIndex++, feedbackType.getValue ( ) );
            daoUtil.setBoolean ( nIndex++, feedbackType.getDefault ( ) );
            daoUtil.setInt ( nIndex++, getNextOrder( plugin ) );

            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) ) 
            {
                feedbackType.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
            return feedbackType;
		}
    }

        
    /**
     * {@inheritDoc }
     */
    @Override
    public Optional<FeedbackType> load( int nId, Plugin plugin )
	{
		try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT , plugin ) )
		{
			daoUtil.setInt( 1 , nId );
			daoUtil.executeQuery();
	
			FeedbackType feedbackType = null;
	
			if ( daoUtil.next() )
			{
				int nIndex = 1;
				feedbackType = new FeedbackType();
	
                feedbackType.setId( daoUtil.getInt( nIndex++ ) );
                feedbackType.setLabel( daoUtil.getString( nIndex++ ) );
                feedbackType.setValue( daoUtil.getString( nIndex++ ) );
                feedbackType.setDefault( daoUtil.getBoolean( nIndex++ ) );
                feedbackType.setOrder( daoUtil.getInt( nIndex++ ) );
			}
	
			return Optional.ofNullable( feedbackType );
		}
	}

        
    /**
     * {@inheritDoc }
     */
    @Override
	public void delete( int nFeedbackTypeId, Plugin plugin )
	{
		try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE , plugin ) )
		{
			daoUtil.setInt( 1 , nFeedbackTypeId );
			daoUtil.executeUpdate();
		}
	}

        
    /**
     * {@inheritDoc }
     */
    @Override
	public void store( FeedbackType feedbackType, Plugin plugin )
	{
		try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE , plugin ) )
		{             
		    int nIndex = 1;
		    daoUtil.setInt( nIndex++, feedbackType.getId( ) );
		    daoUtil.setString( nIndex++, feedbackType.getLabel( ) );
		    daoUtil.setString( nIndex++, feedbackType.getValue( ) );
		    daoUtil.setBoolean( nIndex++, feedbackType.getDefault( ) );
		    daoUtil.setInt( nIndex++, feedbackType.getOrder( ) );
		    daoUtil.setInt( nIndex++, feedbackType.getId( ) );
		            
			daoUtil.executeUpdate( );
		}
	}

        
    /**
     * {@inheritDoc }
     */
    @Override
    public List<FeedbackType> selectFeedbackTypesList( Plugin plugin )
	{
		List<FeedbackType> listFeedbackTypes = new ArrayList< >(  );
		try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL , plugin ) )
		{
			daoUtil.executeQuery(  );
	
			while ( daoUtil.next(  ) )
			{
				int nIndex = 1;
	            FeedbackType feedbackType = new FeedbackType(  );
	            feedbackType.setId( daoUtil.getInt( nIndex++ ) );
	            feedbackType.setLabel( daoUtil.getString( nIndex++ ) );
	            feedbackType.setValue( daoUtil.getString( nIndex++ ) );
	            feedbackType.setDefault( daoUtil.getBoolean( nIndex++ ) );
	            feedbackType.setOrder( daoUtil.getInt( nIndex++ ) );
	            listFeedbackTypes.add( feedbackType );
			}
	
			return listFeedbackTypes;
		}
	}

    /**
     * {@inheritDoc }
     */
	@Override
	public Optional<FeedbackType> findByOrder( int nOrder, Plugin plugin )
	{
		try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ORDER , plugin ) )
		{
			daoUtil.setInt( 1 , nOrder );
			daoUtil.executeQuery();
	
			FeedbackType feedbackType = null;
	
			if ( daoUtil.next() )
			{
				int nIndex = 1;
				feedbackType = new FeedbackType();
	
                feedbackType.setId( daoUtil.getInt( nIndex++ ) );
                feedbackType.setLabel( daoUtil.getString( nIndex++ ) );
                feedbackType.setValue( daoUtil.getString( nIndex++ ) );
                feedbackType.setDefault( daoUtil.getBoolean( nIndex++ ) );
                feedbackType.setOrder( daoUtil.getInt( nIndex++ ) );
			}
	
			return Optional.ofNullable( feedbackType );
		}
	}

	/**
	 * Get next order
	 * @param plugin
	 * @return next order
	 */
	private int getNextOrder ( Plugin plugin )
	{
		try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MAX_ORDER , plugin ) )
		{
			daoUtil.executeQuery();	
			
			int nNextOrder = 1;
			if ( daoUtil.next() )
			{
				nNextOrder = daoUtil.getInt( 1 ) + 1;
			}
			
			return nNextOrder;
		}
	}
}
