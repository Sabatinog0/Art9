<%@ include file="taglibs.jspf" %>
<nav class="category-nav" aria-label="Categorie prodotto">
    <div class="container category-nav-inner">
        <button type="button" class="category-nav-toggle" aria-expanded="false" aria-controls="category-nav-list">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="3" y1="12" x2="21" y2="12"/><line x1="3" y1="6" x2="21" y2="6"/><line x1="3" y1="18" x2="21" y2="18"/></svg>
            Categorie
        </button>
        <ul id="category-nav-list" class="category-nav-list">
            <li><a href="${pageContext.request.contextPath}/catalogo" class="${empty param.categoria ? 'active' : ''}">Tutti i prodotti</a></li>
            <c:forEach var="cat" items="${categorieMenu}">
                <li><a href="${pageContext.request.contextPath}/catalogo?categoria=${cat.slug}"
                       class="${param.categoria == cat.slug ? 'active' : ''}">${fn:escapeXml(cat.nome)}</a></li>
            </c:forEach>
        </ul>
    </div>
</nav>
<main class="container main-content">
