<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="admin-layout">
    <%@ include file="/WEB-INF/fragments/admin-sidebar.jsp" %>

    <div class="admin-content">
        <div class="section-header"><h2>Ordini (${totaleOrdini})</h2></div>

        <form class="filter-bar" action="${pageContext.request.contextPath}/admin/ordini" method="get">
            <div class="form-group">
                <label for="dal">Dalla data</label>
                <input type="date" id="dal" name="dal" value="${dal}">
            </div>
            <div class="form-group">
                <label for="al">Alla data</label>
                <input type="date" id="al" name="al" value="${al}">
            </div>
            <div class="form-group">
                <label for="cliente">Cliente (nome, cognome o email)</label>
                <input type="text" id="cliente" name="cliente" value="${fn:escapeXml(cliente)}" placeholder="Es. Mario Rossi">
            </div>
            <button type="submit" class="btn btn-primary">Filtra</button>
            <a href="${pageContext.request.contextPath}/admin/ordini" class="btn btn-outline">Reimposta</a>
        </form>

        <div style="overflow-x:auto;">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Cliente</th>
                        <th>Data</th>
                        <th>Totale</th>
                        <th>Stato</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="o" items="${ordini}">
                        <tr>
                            <td>${o.idOrdine}</td>
                            <td>${fn:escapeXml(o.nomeClienteCompleto)}<br><small>${fn:escapeXml(o.emailCliente)}</small></td>
                            <td><fmt:formatDate value="${o.dataOrdineAsDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td><fmt:formatNumber value="${o.totale}" type="currency" currencySymbol="€"/></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/ordini/stato" method="post" style="display:flex; gap:6px;">
                                    <input type="hidden" name="id" value="${o.idOrdine}">
                                    <select name="stato" onchange="this.form.submit()">
                                        <c:forEach var="stato" items="${statiDisponibili}">
                                            <option value="${stato}" ${stato == o.stato ? 'selected' : ''}>${stato.etichetta()}</option>
                                        </c:forEach>
                                    </select>
                                </form>
                            </td>
                            <td class="azioni">
                                <a href="${pageContext.request.contextPath}/ordini/dettaglio?id=${o.idOrdine}" class="btn btn-outline btn-sm">Dettagli</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <c:if test="${totalePagine > 1}">
            <nav class="pagination">
                <c:forEach begin="1" end="${totalePagine}" var="numeroPagina">
                    <a href="${pageContext.request.contextPath}/admin/ordini?pagina=${numeroPagina}&dal=${dal}&al=${al}&cliente=${cliente}"
                       class="${numeroPagina == paginaCorrente ? 'attiva' : ''}">${numeroPagina}</a>
                </c:forEach>
            </nav>
        </c:if>
    </div>
</div>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
