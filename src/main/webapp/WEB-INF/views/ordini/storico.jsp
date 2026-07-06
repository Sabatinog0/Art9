<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="section-header"><h2>I tuoi ordini</h2></div>

<c:choose>
    <c:when test="${empty ordini}">
        <div class="empty-state">
            <p>Non hai ancora effettuato ordini.</p>
            <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-primary">Vai al catalogo</a>
        </div>
    </c:when>
    <c:otherwise>
        <c:forEach var="o" items="${ordini}">
            <article class="order-card">
                <div class="order-card-head">
                    <h3>Ordine #${o.idOrdine}</h3>
                    <span class="badge badge-${o.stato.cssClass()}">${o.stato.etichetta()}</span>
                </div>
                <p>
                    <fmt:formatDate value="${o.dataOrdineAsDate}" pattern="dd/MM/yyyy HH:mm"/> &mdash;
                    ${fn:length(o.righe)} articoli &mdash;
                    <strong><fmt:formatNumber value="${o.totale}" type="currency" currencySymbol="€"/></strong>
                </p>
                <a href="${pageContext.request.contextPath}/ordini/dettaglio?id=${o.idOrdine}" class="btn btn-outline btn-sm">Vedi dettagli e fattura</a>
            </article>
        </c:forEach>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
