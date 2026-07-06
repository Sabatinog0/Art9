<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="section-header"><h2>Il tuo carrello</h2></div>

<c:choose>
    <c:when test="${empty carrello.items}">
        <div class="cart-empty">
            <p>Il tuo carrello e' vuoto.</p>
            <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-primary">Continua lo shopping</a>
        </div>
    </c:when>
    <c:otherwise>
        <table class="cart-table" id="cart-table">
            <thead>
                <tr>
                    <th>Prodotto</th>
                    <th>Prezzo</th>
                    <th>Quantita'</th>
                    <th>Subtotale</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${carrello.items}">
                    <tr>
                        <td>
                            <div class="cart-item-info">
                                <img src="${pageContext.request.contextPath}/${item.immagine}" alt="${fn:escapeXml(item.nome)}">
                                <a href="${pageContext.request.contextPath}/prodotto?id=${item.idProdotto}" class="cart-item-nome">${fn:escapeXml(item.nome)}</a>
                            </div>
                        </td>
                        <td><fmt:formatNumber value="${item.prezzoUnitario}" type="currency" currencySymbol="€"/></td>
                        <td>
                            <input type="number" class="qty-input" data-id-prodotto="${item.idProdotto}"
                                   value="${item.quantita}" min="1" max="${item.quantitaDisponibile > 0 ? item.quantitaDisponibile : 99}" style="width:80px;">
                        </td>
                        <td class="riga-subtotale"><fmt:formatNumber value="${item.subtotale}" type="currency" currencySymbol="€"/></td>
                        <td><a href="#" class="remove-link" data-id-prodotto="${item.idProdotto}">Rimuovi</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="cart-summary">
            <div class="cart-summary-row">
                <span>Articoli (<span id="cart-numero-articoli">${carrello.numeroArticoli}</span>)</span>
            </div>
            <div class="cart-summary-row totale">
                <span>Totale</span>
                <span id="cart-totale"><fmt:formatNumber value="${carrello.totale}" type="currency" currencySymbol="€"/></span>
            </div>
            <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary btn-block">Procedi al checkout</a>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
