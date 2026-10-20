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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.template.HtmlMarkup;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Renders the module-extend-feedback templates that hold HTML built by another component, with HTML auto-escaping
 * (LUT-33153): the captcha, the workflow history, the workflow task form and the task informations, marked with
 * HtmlMarkup, keep their tags; a fragment left as a String prints as text; the data around them is escaped; the front
 * office message is rich text written with richText(); the resource link goes through safeUrl(). The core macros the
 * templates call are replaced by stand-ins that write their arguments (escaped) and their nested content: only the
 * FreeMarker engine is needed.
 */
public class FeedbackTemplatesRenderTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String TEMPLATE_FEEDBACK = "skin/plugins/extend/modules/feedback/feedback.html";
    private static final String TEMPLATE_DETAIL = "admin/plugins/extend/modules/feedback/feedback_detail_view.html";
    private static final String TEMPLATE_TASKS_FORM = "admin/plugins/extend/modules/feedback/tasks_form_workflow.html";
    private static final String TEMPLATE_HISTORY = "admin/plugins/extend/modules/feedback/feedback_workflow_history.html";

    /**
     * Stand-ins of the core macros and functions the templates call. The macros taking params write them, which must be
     * a map; safeUrl keeps a relative or http(s) URL and returns the fallback for any other scheme, as the core function
     * does; richText returns its content as markup (the core one sanitizes it first).
     */
    private static final String MACROS = "macros.ftl";
    private static final String MACROS_SOURCE = "<#macro attrs params><#list params as key, param> ${key}=\"${param}\"</#list></#macro>"
            + "<#macro cForm action='' rest...><form action=\"${action}\"><#nested></form></#macro><#macro cFieldset rest...><#nested></#macro>"
            + "<#macro cField label='' rest...><#if label?has_content><label>${label}</label></#if><#nested></#macro>"
            + "<#macro cSelect rest...><select><#nested></select></#macro>"
            + "<#macro cOption label='' value='' rest...><option value=\"${value}\">${label}</option></#macro>"
            + "<#macro cTextArea name='' rest...><textarea name=\"${name}\"></textarea></#macro>"
            + "<#macro cInput name='' value='' rest...><input name=\"${name}\" value=\"${value}\"></#macro>"
            + "<#macro cBtn label='' params={} rest...><button<@attrs params />>${label}</button></#macro>"
            + "<#macro pageContainer rest...><#nested></#macro><#macro pageColumn rest...><#nested></#macro>"
            + "<#macro pageHeader title='' rest...><h1>${title}</h1><#nested></#macro>"
            + "<#macro aButton href='' title='' rest...><a href=\"${href}\">${title}</a></#macro>"
            + "<#macro messages rest...></#macro><#macro row rest...><#nested></#macro><#macro columns rest...><#nested></#macro>"
            + "<#macro box rest...><#nested></#macro><#macro boxHeader title='' rest...><h3>${title}</h3></#macro><#macro boxBody rest...><#nested></#macro>"
            + "<#macro table rest...><table><#nested></table></#macro><#macro tr rest...><tr><#nested></tr></#macro>"
            + "<#macro th rest...><th><#nested></th></#macro><#macro td rest...><td><#nested></td></#macro>"
            + "<#macro tform action='' enctype='' params={} rest...><form action=\"${action}\"<#if enctype?has_content> enctype=\"${enctype}\"</#if><@attrs params />><#nested></form></#macro>"
            + "<#macro input name='' value='' rest...><input name=\"${name}\" value=\"${value}\"></#macro>"
            + "<#macro button name='' value='' title='' rest...><button name=\"${name}\" value=\"${value}\">${title}<#nested></button></#macro>"
            + "<#macro img url='' title='' params={} rest...><img src=\"${url}\" title=\"${title}\"<@attrs params />></#macro>"
            + "<#macro formGroup rest...><div class=\"form-group\"><#nested></div></#macro>"
            + "<#macro empty title='' rest...><div class=\"empty\">${title}</div></#macro>"
            + "<#macro ul rest...><ul><#nested></ul></#macro><#macro li params={} rest...><li<@attrs params />><#nested></li></#macro>"
            + "<#macro p rest...><p><#nested></p></#macro><#macro icon rest...><i></i></#macro>"
            + "<#macro alert params={} rest...><div class=\"alert\"<@attrs params />><#nested></div></#macro>"
            + "<#function safeUrl url fallback='#'><#if url?matches( '(?i)^\\\\s*(https?:|/|[^:/?#]*([/?#]|$)).*', 's' )><#return url /></#if><#return fallback /></#function>"
            + "<#function richText content><#return content?no_esc /></#function>";

    private static final String HOSTILE = "Bug <b>x</b> \"q\"";
    private static final String HOSTILE_ESCAPED = "Bug &lt;b&gt;x&lt;/b&gt; &quot;q&quot;";
    private static final String CAPTCHA = "<div class=\"captcha\"><img src=\"jcaptcha\" alt=\"captcha\"><input type=\"text\" name=\"j_captcha_response\"></div>";
    private static final String HISTORY = "<ul class=\"timeline-form\"><li>Accepted &lt;b&gt;x&lt;/b&gt;</li></ul>";
    private static final String TASKS_FORM = "<div class=\"mb-3\"><label for=\"comment\">Comment</label><textarea name=\"comment\" id=\"comment\"></textarea></div>";
    private static final String TASK_INFORMATION = "Comment : <strong>accepted</strong>";
    private static final String MESSAGE = "<p>Give us your <strong>opinion</strong></p>";

    /**
     * Renders a template of the module with the core configuration of the output format.
     *
     * @param strTemplate
     *            the template path
     * @param model
     *            the model
     * @return the HTML
     * @throws IOException
     *             if the template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( String strTemplate, Map<String, Object> model ) throws IOException, TemplateException
    {
        StringTemplateLoader macros = new StringTemplateLoader( );
        macros.putTemplate( MACROS, MACROS_SOURCE );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                macros, new FileTemplateLoader( new File( TEMPLATES_ROOT ) )
        } ) );
        cfg.addAutoInclude( MACROS );
        StringWriter writer = new StringWriter( );
        cfg.getTemplate( strTemplate ).process( model, writer );

        return writer.toString( );
    }

    /**
     * The model of the front office form.
     *
     * @param captcha
     *            the captcha, or null when the captcha is off
     * @return the model
     */
    private static Map<String, Object> feedbackModel( Object captcha )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "listFeedbackType", List.of( Map.of( "label", HOSTILE, "value", "bug", "default", true ) ) );
        model.put( "message", MESSAGE );
        model.put( "is_active_captcha", captcha != null );
        if ( captcha != null )
        {
            model.put( "captcha", captcha );
        }
        model.put( "idExtendableResource", "42\"><script>" );
        model.put( "extendableResourceType", "PAGE" );
        model.put( "feedbackToken", "tok" );

        return model;
    }

    /**
     * The front office form writes the captcha marked by FeedbackCaptchaService and the rich text message as HTML, and
     * escapes the data.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testFeedbackFormKeepsTheCaptchaAndTheMessage( ) throws Exception
    {
        String strHtml = render( TEMPLATE_FEEDBACK, feedbackModel( HtmlMarkup.of( CAPTCHA ) ) );

        assertTrue( strHtml.contains( CAPTCHA ), strHtml );
        assertTrue( strHtml.contains( "<div class=\"lead\" id=\"feedback_message_intro\">" + MESSAGE + "</div>" ), strHtml );
        assertTrue( strHtml.contains( ">" + HOSTILE_ESCAPED + "</option>" ), strHtml );
        assertTrue( strHtml.contains( "value=\"42&quot;&gt;&lt;script&gt;\"" ), strHtml );
        assertTrue( strHtml.contains( "<button name=\"send\">" ), strHtml );
        assertFalse( strHtml.contains( "<script>" ), strHtml );
    }

    /**
     * A captcha left as a String is escaped: the reason FeedbackCaptchaService marks it.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testAStringCaptchaIsEscaped( ) throws Exception
    {
        String strHtml = render( TEMPLATE_FEEDBACK, feedbackModel( CAPTCHA ) );

        assertFalse( strHtml.contains( CAPTCHA ), strHtml );
        assertTrue( strHtml.contains( "&lt;div class=&quot;captcha&quot;&gt;" ), strHtml );
    }

    /**
     * The model of the feedback detail page.
     *
     * @param history
     *            the workflow history
     * @param strResourceUrl
     *            the address of the resource
     * @return the model
     */
    private static Map<String, Object> detailModel( Object history, String strResourceUrl )
    {
        Map<String, Object> feedback = new HashMap<>( );
        feedback.put( "status", false );
        feedback.put( "feedbackType", "bug" );
        feedback.put( "comment", "Broken <b>link</b>" );
        feedback.put( "idResource", 42 );
        feedback.put( "updateStatusDate", new Date( ) );
        feedback.put( "email", "a@b.fr" );
        feedback.put( "luteceUserName", "" );
        feedback.put( "listWorkflowActions", List.of( ) );

        Map<String, Object> model = new HashMap<>( );
        model.put( "feedback", feedback );
        model.put( "feedbackTypeLabels", Map.of( "bug", HOSTILE ) );
        model.put( "historyWorkflow", history );
        model.put( "resourceUrl", strResourceUrl );
        model.put( "resourceName", "Home <i>page</i>" );
        model.put( "feedbackResourceTypeLabel", "Page" );

        return model;
    }

    /**
     * The detail page writes the workflow history marked by FeedbackJspBean as HTML, escapes the feedback, and links the
     * resource through safeUrl().
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testDetailKeepsTheWorkflowHistory( ) throws Exception
    {
        String strHtml = render( TEMPLATE_DETAIL, detailModel( HtmlMarkup.of( HISTORY ), "jsp/site/Portal.jsp?page_id=2&a=b" ) );

        assertTrue( strHtml.contains( HISTORY ), strHtml );
        assertTrue( strHtml.contains( "<td>Broken &lt;b&gt;link&lt;/b&gt;</td>" ), strHtml );
        assertTrue( strHtml.contains( "<td>" + HOSTILE_ESCAPED + "</td>" ), strHtml );
        assertTrue( strHtml.contains( "<td>Home &lt;i&gt;page&lt;/i&gt;</td>" ), strHtml );
        assertTrue( strHtml.contains( "<a href=\"jsp/site/Portal.jsp?page_id=2&amp;a=b\">" ), strHtml );
    }

    /**
     * A history left as a String is escaped, and a resource address with another scheme is replaced by safeUrl().
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testAStringHistoryIsEscapedAndAJavaScriptUrlReplaced( ) throws Exception
    {
        String strHtml = render( TEMPLATE_DETAIL, detailModel( HISTORY, "javascript:alert(1)" ) );

        assertFalse( strHtml.contains( HISTORY ), strHtml );
        assertTrue( strHtml.contains( "&lt;ul class=&quot;timeline-form&quot;&gt;" ), strHtml );
        assertFalse( strHtml.contains( "javascript:" ), strHtml );
        assertTrue( strHtml.contains( "<a href=\"#\">" ), strHtml );
    }

    /**
     * The tasks form page writes the task form marked by FeedbackJspBean as HTML, in a multipart form.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testTasksFormKeepsTheTaskForm( ) throws Exception
    {
        String strHtml = render( TEMPLATE_TASKS_FORM, new HashMap<>( Map.of( "tasks_form", HtmlMarkup.of( TASKS_FORM ) ) ) );

        assertTrue( strHtml.contains( TASKS_FORM ), strHtml );
        assertTrue( strHtml.contains( "enctype=\"multipart/form-data\"" ), strHtml );

        String strEscaped = render( TEMPLATE_TASKS_FORM, new HashMap<>( Map.of( "tasks_form", TASKS_FORM ) ) );
        assertFalse( strEscaped.contains( TASKS_FORM ), strEscaped );
    }

    /**
     * The history template, rendered by plugin-workflow, writes the task informations it marks as HTML and escapes the
     * action description and the user name.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testHistoryKeepsTheTaskInformations( ) throws Exception
    {
        Map<String, Object> history = new HashMap<>( );
        history.put( "resource_history", Map.of( "action", Map.of( "description", HOSTILE ), "creationDate", "20/10/2026" ) );
        history.put( "task_information_list", HtmlMarkup.ofAll( List.of( TASK_INFORMATION ) ) );
        history.put( "user_history", Map.of( "firstName", "Jane <i>", "lastName", "Doe", "userId", 7 ) );
        Map<String, Object> model = new HashMap<>( );
        model.put( "history_information_list", List.of( history ) );
        model.put( "adminAvatar", true );

        String strHtml = render( TEMPLATE_HISTORY, model );

        assertTrue( strHtml.contains( "<p>" + TASK_INFORMATION + "</p>" ), strHtml );
        assertTrue( strHtml.contains( HOSTILE_ESCAPED ), strHtml );
        assertTrue( strHtml.contains( "Jane &lt;i&gt; Doe" ), strHtml );
        assertTrue( strHtml.contains( "<li>" ), strHtml );
        assertFalse( strHtml.contains( "tabindex" ), strHtml );
        assertFalse( strHtml.contains( "arai-hidden" ), strHtml );
    }

    /**
     * Without history, the template shows its warning, not focusable (no tabindex).
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testEmptyHistoryShowsTheWarning( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "history_information_list", List.of( ) );
        model.put( "adminAvatar", false );

        String strHtml = render( TEMPLATE_HISTORY, model );

        assertTrue( strHtml.contains( "<div class=\"alert\">" ), strHtml );
    }
}
