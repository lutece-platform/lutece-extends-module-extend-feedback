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
package fr.paris.lutece.plugins.extend.modules.feedback.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

/**
 * The changeset that decodes, once, the entities the removed input filter of the core (sanitizeFilterMode, LUT-34109) wrote into
 * the plain text values of module-extend-feedback; the rich text message of the configuration is not decoded. The upgrade script
 * is read and its REPLACE calls applied as the database would.
 */
public class FeedbackDecodeEntitiesUpgradeTest
{
    private static final String UPGRADE_SCRIPT = "src/sql/plugins/extend/modules/feedback/upgrade/update_db_extend_feedback-3.0.0-3.0.1.sql";
    private static final String CHANGESET = "-- changeset extend-feedback:update_db_extend_feedback-3.0.0-3.0.1.sql\n";

    /** The chain of lutece-core (update_db_lutece_core-8.0.1-8.0.2-rev20): &amp;amp; last, so that a value is decoded once. */
    private static final List<String> CORE_CHAIN = List.of( "&#39;", "'", "&#x27;", "'", "&#34;", "\"", "&quot;", "\"", "&#96;", "`", "&#61;", "=",
            "&#64;", "@", "&#43;", "+", "&lt;", "<", "&gt;", ">", "&amp;", "&" );

    private static final Pattern UPDATE = Pattern.compile( "UPDATE (\\w+) SET (\\w+) =(.*?)\\s+WHERE(.*?);[ \\t]*(?:\\r?\\n|$)", Pattern.DOTALL );
    private static final Pattern REPLACED = Pattern.compile( "'((?:[^']|'')*)',\\s*'((?:[^']|'')*)'" );

    /**
     * Reads the REPLACE calls of the decoding changeset, by table.column, in the order the database applies them (the innermost first).
     *
     * @return the replacements (searched, replacement, ...) by table.column
     * @throws IOException
     *             if the script cannot be read
     */
    private static Map<String, List<String>> changeset( ) throws IOException
    {
        String strScript = Files.readString( Path.of( UPGRADE_SCRIPT ), StandardCharsets.UTF_8 );
        int nStart = strScript.indexOf( CHANGESET );
        assertTrue( nStart >= 0, CHANGESET );
        int nEnd = strScript.indexOf( "-- changeset ", nStart + CHANGESET.length( ) );
        String strSection = strScript.substring( nStart, nEnd < 0 ? strScript.length( ) : nEnd );

        Map<String, List<String>> mapUpdates = new LinkedHashMap<>( );
        Matcher update = UPDATE.matcher( strSection );
        while ( update.find( ) )
        {
            List<String> listReplacements = new ArrayList<>( );
            Matcher replaced = REPLACED.matcher( update.group( 3 ) );
            while ( replaced.find( ) )
            {
                listReplacements.add( replaced.group( 1 ).replace( "''", "'" ) );
                listReplacements.add( replaced.group( 2 ).replace( "''", "'" ) );
            }
            assertTrue( update.group( 4 ).contains( update.group( 2 ) + " LIKE '%&%;%'" ), update.group( 0 ) );
            mapUpdates.put( update.group( 1 ) + "." + update.group( 2 ), listReplacements );
        }
        return mapUpdates;
    }

    /**
     * Applies the changeset to a stored value of a column, as REPLACE does.
     *
     * @param strColumn
     *            the table.column
     * @param strValue
     *            the stored value
     * @return the value after the upgrade
     * @throws IOException
     *             if the script cannot be read
     */
    private static String decode( String strColumn, String strValue ) throws IOException
    {
        List<String> listReplacements = changeset( ).get( strColumn );
        String strResult = strValue;
        for ( int i = 0; i < listReplacements.size( ); i += 2 )
        {
            strResult = strResult.replace( listReplacements.get( i ), listReplacements.get( i + 1 ) );
        }
        return strResult;
    }

    /**
     * The changeset decodes the plain text columns, each with the chain of the core.
     *
     * @throws Exception
     *             if the script cannot be read
     */
    @Test
    public void testChangesetDecodesThePlainTextColumnsWithTheChainOfTheCore( ) throws Exception
    {
        Map<String, List<String>> mapUpdates = changeset( );
        assertEquals( List.of( "extend_feedback.comment", "extend_feedback.feedback_type", "extend_feedback_type.label", "extend_feedback_type.value_type" ), new ArrayList<>( mapUpdates.keySet( ) ) );
        for ( List<String> listReplacements : mapUpdates.values( ) )
        {
            assertEquals( CORE_CHAIN, listReplacements );
        }
    }

    /**
     * A value is decoded once: what the filter wrote comes back as typed, an entity typed by the user stays an entity.
     *
     * @throws Exception
     *             if the script cannot be read
     */
    @Test
    public void testValuesAreDecodedOnce( ) throws Exception
    {
        for ( String strColumn : changeset( ).keySet( ) )
        {
            assertEquals( "L'été \"A\" & B `t` = x@y + 1 <c>", decode( strColumn, "L&#39;été &#34;A&#34; &amp; B &#96;t&#96; &#61; x@y + 1 &lt;c&gt;" ), strColumn );
            assertEquals( "L'été \"A\"", decode( strColumn, "L&#x27;été &quot;A&quot;" ), strColumn );
            assertEquals( "A &lt; B", decode( strColumn, "A &amp;lt; B" ), strColumn );
            assertEquals( "A &amp; B", decode( strColumn, "A &amp;amp; B" ), strColumn );
            assertEquals( "Sans entité", decode( strColumn, "Sans entité" ), strColumn );
        }
    }

    /**
     * The message of the configuration, typed with the rich text editor, is HTML: it is not decoded.
     *
     * @throws Exception
     *             if the script cannot be read
     */
    @Test
    public void testRichTextMessageIsLeftAsItIs( ) throws Exception
    {
        assertTrue( changeset( ).keySet( ).stream( ).noneMatch( strColumn -> strColumn.startsWith( "extend_feedback_config." ) ) );
    }
}
