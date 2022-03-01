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

import java.sql.Timestamp;

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;

/**
 * This is the business class for the object ExtendFeedback
 */ 
public class ExtendFeedback
{
    // Variables declarations 
    private int _nId;
    private int _nIdResource;
    private String _strResourceType;
    private String _strComment;
    private Timestamp _dateUpdateStatusDate;
    private boolean _bStatus;
    private ResourceExtenderHistory _resourceExtenderHistory;
    private String _strFeedbackType;
    private String _strLuteceUserName;
    private String _strEmail;
    
   /**
    * Returns the Id
    * @return The Id
    */ 
    public int getId()
    {
        return _nId;
    }

   /**
    * Sets the Id
    * @param nId The Id
    */ 
    public void setId( int nId )
    {
        _nId = nId;
    }

   /**
    * Returns the IdResource
    * @return The IdResource
    */ 
    public int getIdResource()
    {
        return _nIdResource;
    }

   /**
    * Sets the IdResource
    * @param nIdResource The IdResource
    */ 
    public void setIdResource( int nIdResource )
    {
        _nIdResource = nIdResource;
    }

   /**
    * Returns the ResourceType
    * @return The ResourceType
    */ 
    public String getResourceType()
    {
        return _strResourceType;
    }

   /**
    * Sets the ResourceType
    * @param strResourceType The ResourceType
    */ 
    public void setResourceType( String strResourceType )
    {
        _strResourceType = strResourceType;
    }

   /**
    * Returns the Comment
    * @return The Comment
    */ 
    public String getComment()
    {
        return _strComment;
    }

   /**
    * Sets the Comment
    * @param strComent The Comment
    */ 
    public void setComment( String strComment )
    {
        _strComment = strComment;
    }

	/**
	 * @return the _dateUpdateStatusDate
	 */
	public Timestamp getUpdateStatusDate()
	{
		return (Timestamp) _dateUpdateStatusDate.clone();
	}

	/**
	 * @param dateUpdateStatusDate the _dateUpdateStatusDate to set
	 */
	public void setUpdateStatusDate(Timestamp dateUpdateStatusDate)
	{
		this._dateUpdateStatusDate = (Timestamp) dateUpdateStatusDate.clone();
	}

	/**
	 * @return the _bStatus
	 */
	public boolean isStatus()
	{
		return _bStatus;
	}

	/**
	 * @param bStatus the _bStatus to set
	 */
	public void setStatus(boolean bStatus)
	{
		this._bStatus = bStatus;
	}

	/**
	 * @return the _resourceExtenderHistory
	 */
	public ResourceExtenderHistory getResourceExtenderHistory( )
	{
		return _resourceExtenderHistory;
	}

	/**
	 * @param resourceExtenderHistory the _resourceExtenderHistory to set
	 */
	public void setResourceExtenderHistory( ResourceExtenderHistory resourceExtenderHistory )
	{
		this._resourceExtenderHistory = resourceExtenderHistory;
	}

	/**
	 * @return the _strFeedbackType
	 */
	public String getFeedbackType( )
	{
		return _strFeedbackType;
	}

	/**
	 * @param strFeedbackType the _strFeedbackType to set
	 */
	public void setFeedbackType( String strFeedbackType )
	{
		this._strFeedbackType = strFeedbackType;
	}

	/**
	 * @return the _strLuteceUserName
	 */
	public String getLuteceUserName( )
	{
		return _strLuteceUserName;
	}

	/**
	 * @param strLuteceUserName the _strLuteceUserName to set
	 */
	public void setLuteceUserName( String strLuteceUserName )
	{
		this._strLuteceUserName = strLuteceUserName;
	}

	/**
	 * @return the _strEmail
	 */
	public String getEmail( )
	{
		return _strEmail;
	}

	/**
	 * @param strEmail the _strEmail to set
	 */
	public void setEmail( String strEmail )
	{
		this._strEmail = strEmail;
	}   

 }
