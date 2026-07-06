<%@ include file="/WEB-INF/fragments/header.jsp" %>
<%@ include file="/WEB-INF/fragments/menu.jsp" %>

<div class="section-header"><h2>I tuoi preferiti</h2></div>

<c:choose>
    <c:when test="${empty preferiti}">
        <div class="empty-state">
            <p>Non hai ancora salvato nessun prodotto nei preferiti.</p>
            <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-primary">Sfoglia il catalogo</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="product-grid">
            <c:forEach var="pref" items="${preferiti}">
                <c:set var="p" value="${pref.prodotto}"/>
                <article class="product-card">
                    <form action="${pageContext.request.contextPath}/preferiti/rimuovi" method="post">
                        <input type="hidden" name="idProdotto" value="${p.idProdotto}">
                        <input type="hidden" name="ritorno" value="/preferiti">
                        <button type="submit" class="wishlist-btn attivo" aria-label="Rimuovi dai preferiti">&#9829;</button>
                    </form>
                    <a href="${pageContext.request.contextPath}/prodotto?id=${p.idProdotto}" class="product-card-img">
                        <img src="${pageContext.request.contextPath}/${p.immagine}" alt="${fn:escapeXml(p.nome)}" loading="lazy">
                    </a>
                    <div class="product-card-body">
                        <span class="product-card-categoria">${fn:escapeXml(p.nomeCategoria)}</span>
                        <a href="${pageContext.request.contextPath}/prodotto?id=${p.idProdotto}" class="product-card-nome">${fn:escapeXml(p.nome)}</a>
                        <div class="product-card-footer">
                            <span class="product-card-prezzo"><fmt:formatNumber value="${p.prezzo}" type="currency" currencySymbol="€"/></span>
                            <c:if test="${p.quantitaDisponibile > 0}">
                                <form class="add-to-cart-form" action="${pageContext.request.contextPath}/carrello/aggiungi" method="post">
                                    <input type="hidden" name="idProdotto" value="${p.idProdotto}">
                                    <input type="hidden" name="quantita" value="1">
                                    <button type="submit" class="btn btn-primary btn-sm">Aggiungi</button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </article>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/fragments/footer.jsp" %>
