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
package fr.paris.lutece.plugins.extend.modules.feedback.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * The "no mailing list" item of the configuration form is put in a ReferenceList by FeedbackResourceExtenderComponent and
 * printed by the template with auto-escaping (LUT-33153): the message holds the plain text, escaped once. Written as
 * &amp;lt;No mailing list&amp;gt; in the bundle, it was escaped a second time and the form showed the entities.
 */
public class FeedbackNoMailingListLabelTest
{
    private static final String BUNDLE_PATH = "src/java/fr/paris/lutece/plugins/extend/modules/feedback/resources/";

    /**
     * The message of the default bundle is plain text, escaped once where the option is written.
     *
     * @throws Exception
     *             if the bundle cannot be read or the option cannot be rendered
     */
    @Test
    public void testLabelIsPlainTextEscapedOnce( ) throws Exception
    {
        assertLabelIsPlainTextEscapedOnce( "feedback_messages.properties", "No mailing list" );
    }

    /**
     * The message of the French bundle is plain text, escaped once where the option is written.
     *
     * @throws Exception
     *             if the bundle cannot be read or the option cannot be rendered
     */
    @Test
    public void testFrenchLabelIsPlainTextEscapedOnce( ) throws Exception
    {
        assertLabelIsPlainTextEscapedOnce( "feedback_messages_fr.properties", "Aucune liste de diffusion" );
    }

    /**
     * Reads the "no mailing list" message of a bundle and checks that it holds the plain text, written once escaped in the option.
     *
     * @param strBundle
     *            the file name of the bundle
     * @param strText
     *            the text expected between the angle brackets
     * @throws Exception
     *             if the bundle cannot be read or the option cannot be rendered
     */
    private static void assertLabelIsPlainTextEscapedOnce( String strBundle, String strText ) throws Exception
    {
        Properties properties = new Properties( );
        try ( Reader reader = Files.newBufferedReader( Path.of( BUNDLE_PATH + strBundle ), StandardCharsets.ISO_8859_1 ) )
        {
            properties.load( reader );
        }
        String strLabel = properties.getProperty( "feedback_config.labelNoMailingList" );
        assertEquals( "<" + strText + ">", strLabel );

        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        StringWriter writer = new StringWriter( );
        new Template( "option.html", new StringReader( "<option value=\"-1\">${name}</option>" ), cfg ).process( Map.of( "name", strLabel ), writer );
        assertEquals( "<option value=\"-1\">&lt;" + strText + "&gt;</option>", writer.toString( ) );
    }
}
