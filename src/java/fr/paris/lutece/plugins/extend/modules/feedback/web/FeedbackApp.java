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
package fr.paris.lutece.plugins.extend.modules.feedback.web;

import fr.paris.lutece.plugins.extend.modules.feedback.service.ExtendFeedbackService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.FeedbackCaptchaService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.IExtendFeedbackService;
import fr.paris.lutece.plugins.extend.modules.feedback.service.IFeedbackCaptchaService;
import fr.paris.lutece.plugins.extend.modules.feedback.util.constants.FeedbackConstants;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;


import javax.servlet.http.HttpServletRequest;



/**
 *
 * FeedbackApp
 *
 */
public class FeedbackApp implements XPageApplication
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7290027035637324504L;
	// TEMPLATES
    private IFeedbackCaptchaService _feedbackCaptchaService = SpringContextService.getBean( FeedbackCaptchaService.BEAN_SERVICE );
    private IExtendFeedbackService _extendFeedbackService = SpringContextService.getBean( ExtendFeedbackService.BEAN_SERVICE );
    
    /**
     * {@inheritDoc}
     */
    @Override
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
        throws UserNotSignedException, SiteMessageException
    {
        // Test the captcha
        _feedbackCaptchaService.validateCaptcha( request );

        if ( _extendFeedbackService.doSubmitFeedback( request ) )
        {
            SiteMessageService.setMessage( request, FeedbackConstants.MESSAGE_MESSAGE_SENT,
                SiteMessage.TYPE_CONFIRMATION );
        }

        SiteMessageService.setMessage( request, Messages.MANDATORY_FIELDS, SiteMessage.TYPE_STOP );

        return null;
    }
}
