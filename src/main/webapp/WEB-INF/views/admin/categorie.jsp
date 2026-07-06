<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="admin-layout">
    <%@ include file="/WEB-INF/fragments/admin-sidebar.jsp" %>

    <div class="admin-content">
        <div class="section-header"><h2>Categorie</h2></div>

        <div style="overflow-x:auto; margin-bottom:28px;">
            <table class="data-table">
                <thead>
                    <tr><th>Nome</th><th>Descrizione</th><th>Azioni</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="cat" items="${categorie}">
                        <tr>
                            <td>${fn:escapeXml(cat.nome)}</td>
                            <td>${fn:escapeXml(cat.descrizione)}</td>
                            <td class="azioni">
                                <form action="${pageContext.request.contextPath}/admin/categorie/elimina" method="post" data-conferma="Eliminare la categoria &quot;${fn:escapeXml(cat.nome)}&quot;?">
                                    <input type="hidden" name="id" value="${cat.idCategoria}">
                                    <button type="submit" class="btn btn-danger btn-sm">Elimina</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="card" style="max-width:520px;">
            <h3>Nuova categoria</h3>
            <form class="js-validate" action="${pageContext.request.contextPath}/admin/categorie" method="post" novalidate>
                <div class="form-group">
                    <label for="nome">Nome</label>
                    <input type="text" id="nome" name="nome" required>
                    <span class="errore-inline ${not empty errori.nome ? 'visibile' : ''}">${errori.nome}</span>
                </div>
                <div class="form-group">
                    <label for="descrizione">Descrizione</label>
                    <textarea id="descrizione" name="descrizione"></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Aggiungi categoria</button>
            </form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
