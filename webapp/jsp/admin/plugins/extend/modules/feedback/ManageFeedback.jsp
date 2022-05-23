<jsp:useBean id="manageFeedback" scope="session" class="fr.paris.lutece.plugins.extend.modules.feedback.web.FeedbackJspBean" />
<% String strContent = manageFeedback.processController ( request , response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>
