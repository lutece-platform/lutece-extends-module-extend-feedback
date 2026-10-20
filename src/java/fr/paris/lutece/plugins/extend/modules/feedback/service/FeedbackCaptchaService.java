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
package fr.paris.lutece.plugins.extend.modules.feedback.service;

import fr.paris.lutece.plugins.extend.modules.feedback.business.config.FeedbackExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.feedback.util.constants.FeedbackConstants;
import fr.paris.lutece.portal.service.captcha.ICaptchaService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.template.HtmlMarkup;
import fr.paris.lutece.portal.service.util.BeanUtils;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.eclipse.microprofile.config.inject.ConfigProperty;



/**
 *
 * FeedbackCaptchaService
 *
 */
@ApplicationScoped
@Named( "extend-feedback.feedbackCaptchaService" )
public class FeedbackCaptchaService implements IFeedbackCaptchaService
{
    @Inject
    @Named( BeanUtils.BEAN_CAPTCHA_SERVICE )
    private Instance<ICaptchaService> _captchaService;

    @Inject
    @ConfigProperty( name = FeedbackConstants.PROPERTY_USE_CAPTCHA, defaultValue = "true" )
    private boolean _bUseCaptcha;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillModel( Map<String, Object> model, FeedbackExtenderConfig config )
    {
        model.put( FeedbackConstants.MARK_IS_ACTIVE_CAPTCHA, isCaptchaEnabled(  ) && config.isCaptcha( ) );

        if ( isCaptchaEnabled(  ) && config.isCaptcha( ) )
        {
            // HTML of the captcha service, written as is by the feedback form
            model.put( FeedbackConstants.MARK_CAPTCHA, HtmlMarkup.of( _captchaService.get( ).getHtmlCode( ) ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateCaptcha( HttpServletRequest request, FeedbackExtenderConfig config )
        throws SiteMessageException
    {
        if ( isCaptchaEnabled(  ) && config.isCaptcha( ) )
        {
            if ( !_captchaService.get( ).validate( request ) )
            {
                SiteMessageService.setMessage( request, FeedbackConstants.MESSAGE_ERROR_BAD_JCAPTCHA,
                    SiteMessage.TYPE_STOP );
            }
        }
    }

    /**
     * Checks if is captcha enabled.
     *
     * @return true, if is captcha enabled
     */
    private boolean isCaptchaEnabled(  )
    {
        return _captchaService.isResolvable( ) && _bUseCaptcha;
    }
}
