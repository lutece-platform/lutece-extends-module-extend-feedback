<%@page import="fr.paris.lutece.plugins.extend.web.ResourceExtenderJspBean"%>
<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:useBean id="extendFeedback" scope="session" class="fr.paris.lutece.plugins.extend.modules.feedback.web.FeedbackJspBean" />
<% 
extendFeedback.init( request, ResourceExtenderJspBean.RIGHT_MANAGE_RESOURCE_EXTENDER );
	response.sendRedirect( extendFeedback.doProcessWorkflowAction(request));
%>