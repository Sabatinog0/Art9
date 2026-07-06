<%@ include file="taglibs.jspf" %>
<nav class="account-nav">
    <a href="${pageContext.request.contextPath}/account" class="${pageContext.request.servletPath == '/account' ? 'active' : ''}">Dati personali</a>
    <a href="${pageContext.request.contextPath}/account/indirizzi" class="${pageContext.request.servletPath == '/account/indirizzi' ? 'active' : ''}">Indirizzi</a>
    <a href="${pageContext.request.contextPath}/account/pagamenti" class="${pageContext.request.servletPath == '/account/pagamenti' ? 'active' : ''}">Metodi di pagamento</a>
    <a href="${pageContext.request.contextPath}/ordini" class="${pageContext.request.servletPath == '/ordini' ? 'active' : ''}">Storico ordini</a>
    <a href="${pageContext.request.contextPath}/preferiti" class="${pageContext.request.servletPath == '/preferiti' ? 'active' : ''}">Preferiti</a>
</nav>
