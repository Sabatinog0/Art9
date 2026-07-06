<%@ include file="taglibs.jspf" %>
<aside class="admin-sidebar">
    <a href="${pageContext.request.contextPath}/admin" class="${pageContext.request.servletPath == '/admin' ? 'active' : ''}">Dashboard</a>
    <a href="${pageContext.request.contextPath}/admin/prodotti" class="${fn:startsWith(pageContext.request.servletPath, '/admin/prodotti') ? 'active' : ''}">Prodotti</a>
    <a href="${pageContext.request.contextPath}/admin/categorie" class="${fn:startsWith(pageContext.request.servletPath, '/admin/categorie') ? 'active' : ''}">Categorie</a>
    <a href="${pageContext.request.contextPath}/admin/ordini" class="${fn:startsWith(pageContext.request.servletPath, '/admin/ordini') ? 'active' : ''}">Ordini</a>
    <a href="${pageContext.request.contextPath}/admin/recensioni" class="${fn:startsWith(pageContext.request.servletPath, '/admin/recensioni') ? 'active' : ''}">Recensioni</a>
</aside>
