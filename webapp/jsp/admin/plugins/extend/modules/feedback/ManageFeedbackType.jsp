<jsp:useBean id="manageFeedbackType" scope="session" class="fr.paris.lutece.plugins.extend.modules.feedback.web.FeedbackTypeJspBean" />
<% String strContent = manageFeedbackType.processController ( request , response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>
