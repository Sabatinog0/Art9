<%@ page isErrorPage="true" %>
<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="empty-state">
    <h1>403</h1>
    <p>Non hai i permessi necessari per accedere a questa pagina.</p>
    <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-primary">Torna al catalogo</a>
</div>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
