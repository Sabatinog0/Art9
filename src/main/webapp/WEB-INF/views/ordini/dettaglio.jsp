<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="section-header"><h2>Ordine #${ordine.idOrdine}</h2></div>

<div class="invoice-box">
    <div class="invoice-header">
        <div>
            <p class="mb-0"><strong>Data ordine:</strong> <fmt:formatDate value="${ordine.dataOrdineAsDate}" pattern="dd/MM/yyyy HH:mm"/></p>
            <p class="mb-0"><strong>Stato:</strong> <span class="badge badge-${ordine.stato.cssClass()}">${ordine.stato.etichetta()}</span></p>
            <c:if test="${not empty ordine.nomeClienteCompleto}">
                <p class="mb-0"><strong>Cliente:</strong> ${fn:escapeXml(ordine.nomeClienteCompleto)} (${fn:escapeXml(ordine.emailCliente)})</p>
            </c:if>
        </div>
        <div>
            <p class="mb-0"><strong>Spedizione a:</strong></p>
            <p class="mb-0">${fn:escapeXml(ordine.indirizzoSpedizione)}</p>
        </div>
    </div>

    <table class="invoice-table">
        <thead>
            <tr>
                <th>Prodotto</th>
                <th>Quantita'</th>
                <th>Prezzo unit.</th>
                <th>IVA</th>
                <th>Subtotale</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="riga" items="${ordine.righe}">
                <tr>
                    <td>
                        <c:choose>
                            <c:when test="${riga.prodottoAncoraDisponibile}">
                                <a href="${pageContext.request.contextPath}/prodotto?id=${riga.idProdotto}">${fn:escapeXml(riga.nomeProdotto)}</a>
                            </c:when>
                            <c:otherwise>${fn:escapeXml(riga.nomeProdotto)} <span class="badge badge-esaurito">Non piu' in catalogo</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td>${riga.quantita}</td>
                    <td><fmt:formatNumber value="${riga.prezzoUnitario}" type="currency" currencySymbol="€"/></td>
                    <td><fmt:formatNumber value="${riga.ivaPercentuale}" maxFractionDigits="0"/>%</td>
                    <td><fmt:formatNumber value="${riga.subtotale}" type="currency" currencySymbol="€"/></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <div class="invoice-totals">
        <div class="cart-summary-row"><span>Imponibile</span><span><fmt:formatNumber value="${ordine.imponibileTotale}" type="currency" currencySymbol="€"/></span></div>
        <div class="cart-summary-row"><span>IVA</span><span><fmt:formatNumber value="${ordine.ivaTotale}" type="currency" currencySymbol="€"/></span></div>
        <div class="cart-summary-row totale"><span>Totale</span><span><fmt:formatNumber value="${ordine.totale}" type="currency" currencySymbol="€"/></span></div>
    </div>
</div>

<div style="margin-top:24px;">
    <a href="${pageContext.request.contextPath}/ordini" class="btn btn-outline">Torna allo storico ordini</a>
</div>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
