<%@ page isErrorPage="true" %>
<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="empty-state">
    <h1>404</h1>
    <p>La pagina o il prodotto che cerchi non esiste piu' o non e' mai esistito.</p>
    <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-primary">Torna al catalogo</a>
</div>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
