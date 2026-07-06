<%@ page isErrorPage="true" %>
<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="empty-state">
    <h1>Ops!</h1>
    <p>Si e' verificato un errore imprevisto durante l'elaborazione della richiesta. Riprova tra qualche istante.</p>
    <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-primary">Torna al catalogo</a>
</div>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
