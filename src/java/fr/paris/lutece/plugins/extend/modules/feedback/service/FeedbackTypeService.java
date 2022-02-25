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

import javax.inject.Inject;

import fr.paris.lutece.plugins.extend.modules.feedback.business.FeedbackType;
import fr.paris.lutece.plugins.extend.modules.feedback.business.IFeedbackTypeDAO;
import fr.paris.lutece.plugins.extend.modules.feedback.util.constants.FeedbackConstants;
import fr.paris.lutece.util.ReferenceList;

/**
 * 
 * FeedbackTypeService
 *
 */
public class FeedbackTypeService implements IFeedbackTypeService
{
    /** The Constant BEAN_SERVICE. */
    public static final String BEAN_SERVICE = FeedbackPlugin.PLUGIN_NAME + ".extendFeedbackTypeService";
    
    @Inject
    private IFeedbackTypeDAO _feedbackTypeDAO;

	@Override
	public FeedbackType create( FeedbackType feedbackType )
	{
		return _feedbackTypeDAO.insert( feedbackType, FeedbackPlugin.getPlugin( ) );
	}

	@Override
	public void update( FeedbackType feedbackType )
	{
		_feedbackTypeDAO.store( feedbackType, FeedbackPlugin.getPlugin( ) );
	}

	@Override
	public void remove( int nFeedbackTypeId )
	{
		_feedbackTypeDAO.delete( nFeedbackTypeId, FeedbackPlugin.getPlugin( ) );
	}

	@Override
	public Optional<FeedbackType> findByPrimaryKey( int nKey )
	{
		return _feedbackTypeDAO.load( nKey, FeedbackPlugin.getPlugin( ) );
	}

	@Override
	public List<FeedbackType> getFeedbackTypesList( )
	{
		return _feedbackTypeDAO.selectFeedbackTypesList( FeedbackPlugin.getPlugin( ) );
	}

	@Override
	public Optional<FeedbackType> findByOrder( int nOrder )
	{
		return _feedbackTypeDAO.findByOrder( nOrder, FeedbackPlugin.getPlugin( ) );
	}

	@Override
	public ReferenceList getReferenceFeedbackTypesList( )
	{
		ReferenceList referenceList = new ReferenceList( );
		referenceList.addItem( FeedbackConstants.STAR , "#i18n{module.extend.feedback.feedback_comment.filter.label.feedbackType}");
		for ( FeedbackType feedbackType : getFeedbackTypesList( ) )
		{
			referenceList.addItem( feedbackType.getValue( ), feedbackType.getLabel( ) );
		}
		return referenceList;
	}   
    
}
