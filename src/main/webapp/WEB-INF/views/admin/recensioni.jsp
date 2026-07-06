<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="admin-layout">
    <%@ include file="/WEB-INF/fragments/admin-sidebar.jsp" %>

    <div class="admin-content">
        <div class="section-header"><h2>Recensioni (${fn:length(recensioni)})</h2></div>

        <div style="overflow-x:auto;">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Prodotto</th>
                        <th>Autore</th>
                        <th>Voto</th>
                        <th>Testo</th>
                        <th>Data</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="r" items="${recensioni}">
                        <tr>
                            <td><a href="${pageContext.request.contextPath}/prodotto?id=${r.idProdotto}">${fn:escapeXml(r.nomeProdotto)}</a></td>
                            <td>${fn:escapeXml(r.nomeUtente)}</td>
                            <td>${r.voto}/5</td>
                            <td style="max-width:320px;">${fn:escapeXml(r.testo)}</td>
                            <td><fmt:formatDate value="${r.dataRecensioneAsDate}" pattern="dd/MM/yyyy"/></td>
                            <td class="azioni">
                                <form action="${pageContext.request.contextPath}/admin/recensioni/elimina" method="post" data-conferma="Eliminare questa recensione?">
                                    <input type="hidden" name="id" value="${r.idRecensione}">
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
