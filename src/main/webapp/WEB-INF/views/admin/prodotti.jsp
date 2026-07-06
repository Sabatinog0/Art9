<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="admin-layout">
    <%@ include file="/WEB-INF/fragments/admin-sidebar.jsp" %>

    <div class="admin-content">
        <div class="section-header">
            <h2>Prodotti (${fn:length(prodotti)})</h2>
            <a href="${pageContext.request.contextPath}/admin/prodotti/nuovo" class="btn btn-primary btn-sm">Aggiungi prodotto</a>
        </div>

        <div style="overflow-x:auto;">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Nome</th>
                        <th>Categoria</th>
                        <th>Prezzo</th>
                        <th>Disponibilita'</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${prodotti}">
                        <tr>
                            <td>${fn:escapeXml(p.nome)}</td>
                            <td>${fn:escapeXml(p.nomeCategoria)}</td>
                            <td><fmt:formatNumber value="${p.prezzo}" type="currency" currencySymbol="€"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${p.quantitaDisponibile > 0}">${p.quantitaDisponibile} pezzi</c:when>
                                    <c:when test="${p.inPreordine}"><span class="badge badge-preordine">Preordine</span></c:when>
                                    <c:otherwise><span class="badge badge-esaurito">Esaurito</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="azioni">
                                <a href="${pageContext.request.contextPath}/admin/prodotti/modifica?id=${p.idProdotto}" class="btn btn-outline btn-sm">Modifica</a>
                                <form action="${pageContext.request.contextPath}/admin/prodotti/elimina" method="post" data-conferma="Eliminare definitivamente &quot;${fn:escapeXml(p.nome)}&quot;? L'operazione non e' reversibile.">
                                    <input type="hidden" name="id" value="${p.idProdotto}">
                                    <button type="submit" class="btn btn-danger btn-sm">Elimina</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
