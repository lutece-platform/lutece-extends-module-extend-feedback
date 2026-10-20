/*
 * Copyright (c) 2002-2026, City of Paris
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.extend.modules.feedback.business.config.FeedbackExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.feedback.util.constants.FeedbackConstants;
import fr.paris.lutece.portal.service.captcha.ICaptchaService;
import freemarker.core.TemplateHTMLOutputModel;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.util.TypeLiteral;
import jakarta.servlet.http.HttpServletRequest;

/**
 * The captcha HTML is put in the model of the feedback form as markup (LUT-33153), so that the auto-escaped template
 * writes it as HTML.
 */
public class FeedbackCaptchaServiceTest
{
    private static final String CAPTCHA = "<div class=\"captcha\"><img src=\"jcaptcha\" alt=\"captcha\"></div>";

    /**
     * A captcha service returning a fixed HTML code.
     */
    private static final class FixedCaptchaService implements ICaptchaService
    {
        @Override
        public boolean validate( HttpServletRequest request )
        {
            return true;
        }

        @Override
        public String getHtmlCode( )
        {
            return CAPTCHA;
        }

        @Override
        public List<String> getCaptchaEngineNameList( )
        {
            return List.of( );
        }

        @Override
        public String getDefaultCaptchaEngineName( )
        {
            return null;
        }

        @Override
        public void setDefaultCaptchaEngineName( String strDefaultCaptchaEngine )
        {
            // nothing
        }
    }

    /**
     * An instance resolving to one captcha service.
     */
    private static final class SingleInstance implements Instance<ICaptchaService>
    {
        private final ICaptchaService _service = new FixedCaptchaService( );

        @Override
        public ICaptchaService get( )
        {
            return _service;
        }

        @Override
        public Iterator<ICaptchaService> iterator( )
        {
            return List.of( _service ).iterator( );
        }

        @Override
        public Instance<ICaptchaService> select( Annotation... qualifiers )
        {
            return this;
        }

        @Override
        public <U extends ICaptchaService> Instance<U> select( Class<U> subtype, Annotation... qualifiers )
        {
            throw new UnsupportedOperationException( );
        }

        @Override
        public <U extends ICaptchaService> Instance<U> select( TypeLiteral<U> subtype, Annotation... qualifiers )
        {
            throw new UnsupportedOperationException( );
        }

        @Override
        public boolean isUnsatisfied( )
        {
            return false;
        }

        @Override
        public boolean isAmbiguous( )
        {
            return false;
        }

        @Override
        public void destroy( ICaptchaService instance )
        {
            // nothing
        }

        @Override
        public Handle<ICaptchaService> getHandle( )
        {
            throw new UnsupportedOperationException( );
        }

        @Override
        public Iterable<? extends Handle<ICaptchaService>> handles( )
        {
            throw new UnsupportedOperationException( );
        }
    }

    /**
     * Builds the service with its injected fields.
     *
     * @return the service
     * @throws Exception
     *             if a field cannot be set
     */
    private static FeedbackCaptchaService service( ) throws Exception
    {
        FeedbackCaptchaService service = new FeedbackCaptchaService( );
        set( service, "_captchaService", new SingleInstance( ) );
        set( service, "_bUseCaptcha", true );

        return service;
    }

    private static void set( Object target, String strField, Object value ) throws Exception
    {
        Field field = FeedbackCaptchaService.class.getDeclaredField( strField );
        field.setAccessible( true );
        field.set( target, value );
    }

    /**
     * With a captcha configured, its HTML code is put as markup, unchanged.
     *
     * @throws Exception
     *             if the service cannot be built
     */
    @Test
    public void testCaptchaIsMarkup( ) throws Exception
    {
        FeedbackExtenderConfig config = new FeedbackExtenderConfig( );
        config.setCaptcha( true );
        Map<String, Object> model = new HashMap<>( );

        service( ).fillModel( model, config );

        assertEquals( Boolean.TRUE, model.get( FeedbackConstants.MARK_IS_ACTIVE_CAPTCHA ) );
        TemplateHTMLOutputModel markup = assertInstanceOf( TemplateHTMLOutputModel.class, model.get( FeedbackConstants.MARK_CAPTCHA ) );
        assertEquals( CAPTCHA, markup.getOutputFormat( ).getMarkupString( markup ) );
    }

    /**
     * Without a captcha configured, nothing is put.
     *
     * @throws Exception
     *             if the service cannot be built
     */
    @Test
    public void testNoCaptcha( ) throws Exception
    {
        FeedbackExtenderConfig config = new FeedbackExtenderConfig( );
        config.setCaptcha( false );
        Map<String, Object> model = new HashMap<>( );

        service( ).fillModel( model, config );

        assertEquals( Boolean.FALSE, model.get( FeedbackConstants.MARK_IS_ACTIVE_CAPTCHA ) );
        assertFalse( model.containsKey( FeedbackConstants.MARK_CAPTCHA ) );
    }
}
