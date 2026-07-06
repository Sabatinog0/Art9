<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="admin-layout">
    <%@ include file="/WEB-INF/fragments/admin-sidebar.jsp" %>

    <div class="admin-content">
        <div class="section-header"><h2>Dashboard</h2></div>

        <div class="admin-stats">
            <div class="stat-card">
                <div class="valore">${totaleProdotti}</div>
                <div class="etichetta">Prodotti a catalogo</div>
            </div>
            <div class="stat-card">
                <div class="valore">${totaleOrdini}</div>
                <div class="etichetta">Ordini totali</div>
            </div>
            <div class="stat-card">
                <div class="valore">${totaleClienti}</div>
                <div class="etichetta">Clienti registrati</div>
            </div>
            <div class="stat-card">
                <div class="valore"><fmt:formatNumber value="${fatturatoTotale}" type="currency" currencySymbol="€"/></div>
                <div class="etichetta">Fatturato totale</div>
            </div>
        </div>

        <div class="card">
            <h3>Azioni rapide</h3>
            <div style="display:flex; gap:12px; flex-wrap:wrap;">
                <a href="${pageContext.request.contextPath}/admin/prodotti/nuovo" class="btn btn-primary">Aggiungi prodotto</a>
                <a href="${pageContext.request.contextPath}/admin/ordini" class="btn btn-outline">Vedi ordini</a>
                <a href="${pageContext.request.contextPath}/admin/recensioni" class="btn btn-outline">Modera recensioni</a>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
