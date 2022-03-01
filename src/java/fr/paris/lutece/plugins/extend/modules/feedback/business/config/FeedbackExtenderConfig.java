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

import fr.paris.lutece.plugins.extend.business.extender.config.ExtenderConfig;


/**
 *
 * FeedbackConfig
 *
 */
public class FeedbackExtenderConfig extends ExtenderConfig
{
    private String _strMessage;
    private int _nIdMailingList = -1;
    private boolean _bCaptcha;
    private boolean _bShowFeedbackTypeList;
    private int _nIdWorkflow;
    private boolean _bAuthenticatedMode;
    
    /**
     * @return the strMessage
     */
    public String getMessage(  )
    {
        return _strMessage;
    }

    /**
     * @param strMessage the strMessage to set
     */
    public void setMessage( String strMessage )
    {
        _strMessage = strMessage;
    }

    /**
    * @return the nIdMailingList
    */
    public int getIdMailingList(  )
    {
        return _nIdMailingList;
    }

    /**
     * @param nIdMailingList the nIdMailingList to set
     */
    public void setIdMailingList( int nIdMailingList )
    {
        _nIdMailingList = nIdMailingList;
    }

	/**
	 * @return the _bCaptcha
	 */
	public boolean isCaptcha( )
	{
		return _bCaptcha;
	}

	/**
	 * @param _bCaptcha the _bCaptcha to set
	 */
	public void setCaptcha( boolean bCaptcha )
	{
		this._bCaptcha = bCaptcha;
	}

	/**
	 * @return the _bShowFeedbackTypeList
	 */
	public boolean isShowFeedbackTypeList( )
	{
		return _bShowFeedbackTypeList;
	}

	/**
	 * @param bShowFeedbackTypeList the _bCaptcha to set
	 */
	public void setShowFeedbackTypeList( boolean bShowFeedbackTypeList )
	{
		this._bShowFeedbackTypeList = bShowFeedbackTypeList;
	}

	/**
	 * @return the _nIdWorkflow
	 */
	public int getIdWorkflow( )
	{
		return _nIdWorkflow;
	}

	/**
	 * @param nIdWorkflow the _nIdWorkflow to set
	 */
	public void setIdWorkflow( int nIdWorkflow )
	{
		this._nIdWorkflow = nIdWorkflow;
	}

	/**
	 * @return the _bAuthenticatedMode
	 */
	public boolean isAuthenticatedMode( )
	{
		return _bAuthenticatedMode;
	}

	/**
	 * @param bAuthenticatedMode the _bAuthenticatedMode to set
	 */
	public void setAuthenticatedMode( boolean bAuthenticatedMode )
	{
		this._bAuthenticatedMode = bAuthenticatedMode;
	}

	
	
}
